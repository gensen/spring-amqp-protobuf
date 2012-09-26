name := "spring-amqp-protobuf"

organization := "com.gs"

credentials += Credentials(Path.userHome / ".ivy2" / ".credentials")

publishTo <<= (version) { version: String =>
  val nexus = "http://nexus.generalsensing.com/content/repositories/"
  if (version.trim.endsWith("SNAPSHOT")) Some("snapshots" at nexus + "snapshots/")
  else                                   Some("releases"  at nexus + "releases/")
}

scalaVersion := "2.9.1"

libraryDependencies ++= Seq(
  "com.google.protobuf"      % "protobuf-java" % "2.4.1",
  "org.springframework.amqp" % "spring-amqp"   % "1.1.0.RELEASE",
  "org.scalatest"            %% "scalatest"    % "1.7.2"          % "test"
)

releaseSettings
