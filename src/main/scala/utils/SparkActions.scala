package utils
import org.apache.spark.sql.SparkSession

trait SparkActions {

  def initSpark (appName: String): SparkSession = {
    SparkSession
      .builder()
      .appName(appName)
      .getOrCreate()
  }

  def activeExecutors(spark: SparkSession): Seq[String] = {
    val allExecutors = spark.sparkContext.getExecutorMemoryStatus.map(_._1)
    val driverHost: String = spark.sparkContext.getConf.get("spark.driver.host")
    allExecutors.filter(! _.split(":")(0).equals(driverHost)).toList
  }


}
