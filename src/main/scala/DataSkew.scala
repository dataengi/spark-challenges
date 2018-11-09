import com.typesafe.config.ConfigFactory
import org.apache.spark.sql.functions.broadcast
import utils.SparkActions

object DataSkew extends SparkActions {
  private val config = ConfigFactory.load()

  private lazy val appName = config.getString("app.name")
  private lazy val spark   = initSpark(appName)


  def main(args: Array[String]): Unit = {

    if (args.size < 2) {
      println ("Error: path to <users-dataset> and <deps-dataset> are required!")
      System.exit(1)
    }

    val usersCSV = args(0)
    val depsCSV  = args(1)


    // load data
    val depsIn  = spark.read.format("csv").option("header", "true").load(depsCSV)
    val usersIn = spark.read.format("csv").option("header", "true").load(usersCSV)


    // cluster tweak
    val executors   = activeExecutors(spark)
    val multiFactor = config.getInt("executor.multiFactor")


    // prepare data
    import spark.implicits._
    val deps  = depsIn.sort('id, 'assigned_date, 'company_id, 'factory_id)
    val users = usersIn.repartition(executors.size*multiFactor)


    // process data
    val joinExp   = $"users.department_id" === $"deps.id" && $"users.date_of_birth" === $"deps.assigned_date" && ($"users.company_id" === $"deps.company_id" || $"users.company_id" === $"deps.factory_id")
    val countExpr = users.as('users).join(broadcast(deps).as('deps), joinExp, "inner")

    countExpr.explain()

    println (s"\n\nresult: = ${countExpr.count()}\n\n")

  }

}
