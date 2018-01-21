lazy val root = (project in file(".")).settings(
  inThisBuild(
    List(
      organization := "com.robo",
      scalaVersion := "2.12.4"
    )),
  name := "Crypto Portfolio",
  libraryDependencies ++= {
    lazy val AkkaHttpVersion = "10.0.11"
    lazy val AkkaVersion = "2.5.9"
    lazy val ScalaTestVersion = "3.0.4"
    Seq(
      "com.typesafe.akka" %% "akka-http" % AkkaHttpVersion,
      "com.typesafe.akka" %% "akka-http-spray-json" % AkkaHttpVersion,
      "com.typesafe.akka" %% "akka-http-xml" % AkkaHttpVersion,
      "com.typesafe.akka" %% "akka-stream" % AkkaVersion,
      "org.typelevel" %% "cats-core" % "1.0.1",
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
