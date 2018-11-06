import org.apache.spark.sql.{DataFrame, SparkSession}
import org.scalatest.{FlatSpec, Matchers}
class DataSkewSpec extends FlatSpec with Matchers {

  lazy val spark = {
    SparkSession
      .builder()
      .appName("Spark-Tests")
      .master("local")
      .getOrCreate()
  }


  def readCSVSample(ss: SparkSession, resourse: String): DataFrame = {
    val filename = getClass.getResource("/" + resourse).getFile
    val df = ss.read.format("csv").option("header", "true").load(filename)
    df
  }


  val sampleDepsCSV  = "Spark+task+attachment+1+-+deps.csv.gz"
  val sampleUsersCSV = "Spark+task+attachment+2+-+users.csv.gz"


  lazy val deps  = readCSVSample(spark, sampleDepsCSV)
  lazy val users = readCSVSample(spark, sampleUsersCSV)

  "DataAnalyse" should "show schema" in {

    // show data samples
    deps.printSchema()
    users.printSchema()
  }
  it should "show samples" in {

    deps.show(10)
    users.show(10)

  }

  it should "show execution plan" in {

    // expression for optimisation
    import spark.implicits._
    val joinExp = $"users.department_id" === $"deps.id" && $"users.date_of_birth" === $"deps.assigned_date" && ($"users.company_id" === $"deps.company_id" || $"users.company_id" === $"deps.factory_id")
    val countExpr = users.as('users).join(deps.as('deps), joinExp, "inner")

    countExpr.explain()

  }


}
