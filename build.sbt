lazy val root = (project in file(".")).settings(
  inThisBuild(
    List(
      organization := "com.robo",
      scalaVersion := "2.12.4"
    )),
  name := "Crypto Portfolio",
  libraryDependencies ++= {
    lazy val akkaHttpVersion = "10.0.11"
    lazy val akkaVersion = "2.5.9"
    Seq(
      "com.typesafe.akka" %% "akka-http" % akkaHttpVersion,
      "com.typesafe.akka" %% "akka-http-spray-json" % akkaHttpVersion,
      "com.typesafe.akka" %% "akka-http-xml" % akkaHttpVersion,
      "com.typesafe.akka" %% "akka-stream" % akkaVersion,
      "org.typelevel" %% "cats-core" % "1.0.1",
      "com.typesafe.akka" %% "akka-http-testkit" % akkaHttpVersion % Test,
      "com.typesafe.akka" %% "akka-testkit" % akkaVersion % Test,
      "com.typesafe.akka" %% "akka-stream-testkit" % akkaVersion % Test,
      "org.scalatest" %% "scalatest" % "3.0.1" % Test
    )
  }
)

scalacOptions ++= Seq(
  "-language:higherKinds",
  "-Ypartial-unification"
)

scalafmtOnCompile := true
