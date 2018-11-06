name := "sparkchallenges"

version := "0.1"

scalaVersion := "2.11.12"

libraryDependencies ++= Dependencies.dependencies
libraryDependencies ++= Dependencies.spark
libraryDependencies ++= Dependencies.testDeps

parallelExecution in Test := false
fork in Test := true