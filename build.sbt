name := "spring-amqp-protobuf"

organization := "com.gs"

credentials += Credentials(Path.userHome / ".ivy2" / ".credentials")

bintrayOrganization := Some("generalsensing")

licenses += ("MIT", url("http://opensource.org/licenses/MIT"))

scalaVersion := "2.11.12"

libraryDependencies ++= Seq(
  "com.google.protobuf"      % "protobuf-java" % "2.4.1",
  "org.springframework.amqp" % "spring-amqp"   % "1.1.0.RELEASE",
  "org.specs2" %% "specs2" % "2.3.12" % "test"
)
