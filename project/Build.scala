import sbt._
import Keys._
import play.Project._

object ApplicationBuild extends Build {

  val appName         = "ts-togglz-ui"
  val appVersion      = "1.0-SNAPSHOT"

  val appDependencies = Seq(
    // ReactiveMongo dependencies
    "org.reactivemongo" %% "reactivemongo" % "0.10.0",
    // ReactiveMongo Play plugin dependencies
    "org.reactivemongo" %% "play2-reactivemongo" % "0.10.2",
    "com.typesafe" %% "scalalogging-slf4j" % "1.1.0",

    "com.typesafe.akka" %% "akka-testkit" % "2.2.1",
    "org.scalamock" %% "scalamock-scalatest-support" % "3.1.RC1" % "test",
    "org.scalacheck" %% "scalacheck" % "1.11.3" % "test"
  )


  val main = play.Project(appName, appVersion, appDependencies).settings(
    // Add your own project settings here      
  )

}
