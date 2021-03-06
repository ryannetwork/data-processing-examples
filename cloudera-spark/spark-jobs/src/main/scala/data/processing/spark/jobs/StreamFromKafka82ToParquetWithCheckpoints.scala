package data.processing.spark.jobs

import java.util.concurrent.TimeUnit

import com.typesafe.config.Config
import data.processing.avro.AvroDecoder
import kafka.serializer._
import org.apache.spark.streaming._
import org.apache.spark.streaming.kafka.{HasOffsetRanges, KafkaUtils}
import org.apache.spark.sql.{Row, SaveMode, SparkSession}
import org.apache.spark.sql.types.{StructType}

import collection.JavaConverters._
import data.processing.spark.jobs.utils.avro.{SchemaConverters}
import data.processing.spark.jobs.utils.kafka.KafkaAvroDecoder
import org.slf4j.LoggerFactory

/**
  * Created by ipogudin on 14/03/2017.
  */
object StreamFromKafka82ToParquetWithCheckpoints extends ConfigurableJob {

  private val logger = LoggerFactory.getLogger(StreamFromKafka82ToParquetWithCheckpoints.getClass)

  val BASE_KEY = "stream-from-kafka82-to-parquet-with-checkpoints"
  val AVRO_SCHEMA = "avro.schema"
  val TARGET_DIR = "target.dir"
  val CHECKPOINT_DIR = "checkpoint.dir"
  val CHECKPOINT_INTERVAL = "checkpoint.interval"
  val KAFKA_TOPICS = "kafka-consumer82.topics"
  val KAFKA_CONSUMER = "kafka-consumer82"
  val STREAMING_CONTEXT_BATCH_DURATION = "streaming-context.batch-duration"

  def run(spark: SparkSession, config: Config): Unit = {
    val jobConfig = config.getConfig(BASE_KEY)

    val ssc = StreamingContext.getOrCreate(
      jobConfig.getString(CHECKPOINT_DIR),
      () => {
        process(spark, jobConfig)
      })

    ssc.start()
    ssc.awaitTermination()
  }

  def process(spark: SparkSession, config: Config): StreamingContext = {
    val avroSchema = config.getString(AVRO_SCHEMA)
    val targetDir = config.getString(TARGET_DIR)
    val checkpointInterval = config.getDuration(CHECKPOINT_INTERVAL, TimeUnit.MILLISECONDS)
    val topicsSet = config.getStringList(KAFKA_TOPICS).asScala.toSet
    val kafkaParams = config.getConfig(KAFKA_CONSUMER).entrySet().asScala
      .map(e => e.getKey.toString -> e.getValue.unwrapped().toString)
      .toMap
    val avroDecoder = new AvroDecoder(avroSchema)

    val ssc = new StreamingContext(
      spark.sparkContext, Milliseconds(config.getDuration(STREAMING_CONTEXT_BATCH_DURATION, TimeUnit.MILLISECONDS)))
    ssc.checkpoint(config.getString(CHECKPOINT_DIR))

    val directKafkaStream = KafkaUtils.createDirectStream[String, Row, StringDecoder, KafkaAvroDecoder](
      ssc, kafkaParams, topicsSet)
      .transform(rdd => {
        if (logger.isInfoEnabled) {
          val offsetRanges = rdd.asInstanceOf[HasOffsetRanges].offsetRanges
          for (o <- offsetRanges) {
            logger.info(s"Kafka direct stream (topic: ${o.topic}, partition: ${o.partition}, from ${o.fromOffset} until ${o.untilOffset} offset)")
          }
        }
        rdd
      }).checkpoint(Durations.milliseconds(checkpointInterval))

    val st = SchemaConverters.toSqlType(avroDecoder.schema).dataType.asInstanceOf[StructType]

    directKafkaStream.foreachRDD(rdd => {
      val spark = SparkSession.builder.config(rdd.sparkContext.getConf).getOrCreate()
      import spark.implicits._

      val rowRdd = rdd.map(r => r._2)
      if (!rowRdd.isEmpty) { // the job should not create an empty parquet file
        spark.createDataFrame(rowRdd, st).write.mode(SaveMode.Append).parquet(targetDir)
      }
    })

    ssc
  }
}