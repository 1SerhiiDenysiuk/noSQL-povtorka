import org.apache.spark.eventhubs.{ ConnectionStringBuilder, EventHubsConf, EventPosition }
import org.apache.spark.sql.types._
import org.apache.spark.sql.functions._

val appID = "93695c79-f39a-40e6-8642-accc9a3b93d4"
val password = "YyUd-N32v6k5~__7QT30~RsY8Z.0d1ykPI"
val tenantID = "127eebd6-263b-4cf3-8d2c-8614de14c8a1"
val fileSystemName = "bdlabs"
val storageAccountName = "lab8storageacc"
val connectionString = ConnectionStringBuilder("Endpoint=sb://labsbd.servicebus.windows.net/;SharedAccessKeyName=lab8Denysiuk;SharedAccessKey=vf0O4iim+/kwJCXB3sa4uy5y6dQQsy5aweZ1qkpwyDo=")
  .setEventHubName("lab8denysiuk")
  .build
val eventHubsConf = EventHubsConf(connectionString)
  .setStartingPosition(EventPosition.fromEndOfStream)

var streamingInputDF =
  spark.readStream
    .format("eventhubs")
    .options(eventHubsConf.toMap)
    .load()

val filtered = streamingInputDF.select (
  from_unixtime(col("enqueuedTime").cast(LongType)).alias("enqueuedTime")
     , get_json_object(col("body").cast(StringType), "$.sanitation_district").alias("sanitation_district")
     , get_json_object(col("body").cast(StringType), "$.tons").alias("tons")
     , get_json_object(col("body").cast(StringType), "$.date").alias("date")
     , get_json_object(col("body").cast(StringType), "$.day_of_week").alias("day_of_week")
     , get_json_object(col("body").cast(StringType), "$.month").alias("month")
)


filtered.writeStream
  .format("com.databricks.spark.csv")
  .outputMode("append")
  .option("checkpointLocation", "/mnt/labDenysiuk/labsDir")
  .start("/mnt/labDenysiuk/labsDir")