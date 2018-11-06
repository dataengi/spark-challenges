import org.apache.spark.sql.functions.broadcast
import utils.SparkActions

object DataSkew extends SparkActions {

  val appName    = "DataSkewExample"
  lazy val spark = initSpark(appName)

  // path on hdfs
  val usersCSV = "/data-sample/Spark+task+attachment+2+-+users.csv.gz"
  val depsCSV  = "/data-sample/Spark+task+attachment+1+-+deps.csv.gz"


  def main(args: Array[String]): Unit = {

    // load data
    val depsIn  = spark.read.format("csv").option("header", "true").load(depsCSV)
    val usersIn = spark.read.format("csv").option("header", "true").load(usersCSV)


    // cluster tweak
    val executors   = activeExecutors(spark)
    val multiFactor = 3


    // prepare data
    import spark.implicits._
    val deps  = depsIn.sort('id, 'assigned_date, 'company_id, 'factory_id)
    val users = usersIn.repartition(executors.size*multiFactor)


    // process data
    val joinExp   = $"users.department_id" === $"deps.id" && $"users.date_of_birth" === $"deps.assigned_date" && ($"users.company_id" === $"deps.company_id" || $"users.company_id" === $"deps.factory_id")
    val countExpr = users.as('users).join(broadcast(deps).as('deps), joinExp, "inner")

    countExpr.explain()
    countExpr.count()

  }

}
