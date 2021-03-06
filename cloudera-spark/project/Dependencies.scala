import sbt._
import Keys._

object Dependencies {
  lazy val scalaTest = "org.scalatest" %% "scalatest" % "3.0.1"
  lazy val sparkCatalyst = "org.apache.spark" % "spark-catalyst_2.11" % "2.3.0.cloudera2"
  lazy val sparkCore = "org.apache.spark" % "spark-core_2.11" % "2.3.0.cloudera2"
  lazy val sparkGraphx = "org.apache.spark" % "spark-graphx_2.11" % "2.3.0.cloudera2"
  lazy val sparkHive = "org.apache.spark" % "spark-hive_2.11" % "2.3.0.cloudera2"
  lazy val sparkLauncher = "org.apache.spark" % "spark-launcher_2.11" % "2.3.0.cloudera2"
  lazy val sparkMllib = "org.apache.spark" % "spark-mllib_2.11" % "2.3.0.cloudera2"
  lazy val sparkNetworkCommon = "org.apache.spark" % "spark-network-common_2.11" % "2.3.0.cloudera2"
  lazy val sparkNetworkShuffle = "org.apache.spark" % "spark-network-shuffle_2.11" % "2.3.0.cloudera2"
  lazy val sparkNetworkYarn = "org.apache.spark" % "spark-network-yarn_2.11" % "2.3.0.cloudera2"
  lazy val sparkRepl = "org.apache.spark" % "spark-repl_2.11" % "2.3.0.cloudera2"
  lazy val sparkSql = "org.apache.spark" % "spark-sql_2.11" % "2.3.0.cloudera2"
  lazy val sparkStreamingFlumeSink = "org.apache.spark" % "spark-streaming-flume-sink_2.11" % "2.3.0.cloudera2"
  lazy val sparkStreamingFlume = "org.apache.spark" % "spark-streaming-flume_2.11" % "2.3.0.cloudera2"
  lazy val sparkStreamingKafka82 = "org.apache.spark" % "spark-streaming-kafka-0-8_2.11" % "2.3.0.cloudera2"
  lazy val sparkStreamingKafka10 = "org.apache.spark" % "spark-streaming-kafka-0-10_2.11" % "2.3.0.cloudera2"
  lazy val kafka = "org.apache.kafka" % "kafka_2.11" % "0.11.0-kafka-3.0.0"
  lazy val sparkStreaming = "org.apache.spark" % "spark-streaming_2.11" % "2.3.0.cloudera2"
  lazy val sparkUnsafe = "org.apache.spark" % "spark-unsafe_2.11" % "2.3.0.cloudera2"
  lazy val sparkYarn = "org.apache.spark" % "spark-yarn_2.11" % "2.3.0.cloudera2"
  lazy val avro = "org.apache.avro" % "avro" % "1.7.6-cdh5.14.2"
  lazy val typesafeConfig = "com.typesafe" % "config" % "1.3.1"
  lazy val scalaConfig = "com.github.andr83" %% "scalaconfig" % "0.3"
  lazy val hadoopClient = "org.apache.hadoop" % "hadoop-client" % "2.6.0-cdh5.14.2"
  lazy val hadoopCommon = "org.apache.hadoop" % "hadoop-common" % "2.6.0-cdh5.14.2"
  lazy val hadoopMain = "org.apache.hadoop" % "hadoop-main" % "2.6.0-cdh5.14.2"
  lazy val parquet = "com.twitter" % "parquet" % "1.5.0-cdh5.14.2"
}
