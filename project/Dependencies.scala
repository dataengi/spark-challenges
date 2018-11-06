import sbt._

object Version {
  val typesafeConfig = "1.3.3"
  val spark          = "2.2.0"
  val playJson       = "2.6.9"
  // Test deps ver
  val scalcheck = "1.14.0"
  val scalactic = "3.0.1"
  val specs2    = "4.3.0"
}

object Dependencies {
  val dependencies = Seq(
    "com.typesafe" % "config" % Version.typesafeConfig
  )
  val spark = Seq(
    "org.apache.spark" %% "spark-core"      % Version.spark % Provided,
    "org.apache.spark" %% "spark-streaming" % Version.spark % Provided,
    "org.apache.spark" %% "spark-sql"       % Version.spark % Provided,
  )
  val playJson = "com.typesafe.play" %% "play-json" % Version.playJson
  
  val testDeps = Seq(
    "org.scalacheck" % "scalacheck_2.11" % Version.scalcheck % "test",
    "org.scalatest"  %% "scalatest"      % Version.scalactic % Test,
    "org.specs2"     %% "specs2-core"    % Version.specs2    % "test"
  )
}
