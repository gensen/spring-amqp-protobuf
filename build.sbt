name := "spring-amqp-protobuf"

organization := "com.gs"

credentials += Credentials(Path.userHome / ".ivy2" / ".credentials")

bintrayOrganization := Some("gensen")

licenses += ("MIT", url("http://opensource.org/licenses/MIT"))

scalaVersion := "2.12.4"

crossScalaVersions := Seq(scalaVersion.value, "2.11.12")

inConfig(Test)(sbtprotoc.ProtocPlugin.protobufConfigSettings)

PB.targets in Test := Seq(
  PB.gens.java -> (sourceManaged in Test).value
)

libraryDependencies ++= Seq(
  "com.google.protobuf"      % "protobuf-java" % "3.+",
  "org.springframework.amqp" % "spring-amqp"   % "1.3.+",
  "org.specs2" %% "specs2-core" % "4.0.+" % Test
)
