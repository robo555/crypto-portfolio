lazy val root = (project in file(".")).settings(
  inThisBuild(
    List(
      organization := "com.robo",
      scalaVersion := "2.12.4"
    )),
  name := "Crypto Portfolio",
  libraryDependencies ++= {
    val AkkaHttpVersion = "10.0.11"
    val AkkaVersion = "2.5.9"
    val ScalaTestVersion = "3.0.4"
    val CirceVersion = "0.9.0"
    Seq(
      "com.typesafe.akka" %% "akka-http" % AkkaHttpVersion,
      "com.typesafe.akka" %% "akka-http-spray-json" % AkkaHttpVersion,
      "com.typesafe.akka" %% "akka-http-xml" % AkkaHttpVersion,
      "com.typesafe.akka" %% "akka-stream" % AkkaVersion,
      "org.typelevel" %% "cats-core" % "1.0.1",
      "io.circe" %% "circe-core" % CirceVersion,
      "io.circe" %% "circe-generic" % CirceVersion,
      "io.circe" %% "circe-parser" % CirceVersion,
      "com.typesafe.akka" %% "akka-http-testkit" % AkkaHttpVersion % Test,
      "com.typesafe.akka" %% "akka-testkit" % AkkaVersion % Test,
      "com.typesafe.akka" %% "akka-stream-testkit" % AkkaVersion % Test,
      "org.scalactic" %% "scalactic" % ScalaTestVersion, // use by ScalaTest, but project can use it too
      "org.scalatest" %% "scalatest" % ScalaTestVersion % Test
    )
  }
)

scalacOptions ++= Seq(
  "-feature",
  "-language:higherKinds",
  "-Ypartial-unification"
)

scalafmtOnCompile := true
