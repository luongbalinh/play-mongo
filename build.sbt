import com.typesafe.sbt.packager.archetypes.ServerLoader

name := "play-mongo"

version := "1.1.0"

scalaVersion := "2.11.8"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

resolvers ++= Seq(
  "Typesafe repository" at "http://repo.typesafe.com/typesafe/releases/",
  "sonatype-releases" at "https://oss.sonatype.org/content/repositories/releases/",
  "Sonatype Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots/"
)

libraryDependencies ++= Seq(
  filters,
  ws,
  "org.reactivemongo" %% "play2-reactivemongo" % "0.12.0",
  "com.github.etaty" %% "rediscala" % "1.7.0",
  "net.codingwell" %% "scala-guice" % "4.1.0",
  "org.scalatest" %% "scalatest" % "3.0.1" % Test,
  "org.scalatestplus.play" %% "scalatestplus-play" % "2.0.0-M1" % Test,
  "de.flapdoodle.embed" % "de.flapdoodle.embed.mongo" % "2.0.0-SNAPSHOT" % Test,
  "com.github.kstyrc" % "embedded-redis" % "0.6" % Test,
  "org.mockito" % "mockito-core" % "1.10.19" % Test
)

routesGenerator := InjectedRoutesGenerator

parallelExecution in Test := false
fork in Test := false

packageName in Rpm := "attribute-service"
version in Rpm := version.value
maintainer in Rpm := "LUONG Ba Linh <linhluongba@gmail.com>"
packageSummary in Rpm := "Play Service"
packageDescription in Rpm := "This is a Play project with MongoDb and Redis"

rpmVendor := "luongbalinh"
rpmRelease := "1"
rpmLicense := Some("Proprietary License")
rpmChangelogFile := Some("Changelog.md")
rpmUrl := Some("http://www.lekhiet.org")
rpmGroup := Some("Group Name")

exportJars := true
defaultLinuxInstallLocation := "/opt"
serverLoading in Rpm := ServerLoader.SystemV
daemonUser in Linux := packageName.value
daemonGroup in Linux := (daemonUser in Linux).value

mappings in Universal ++= Seq(
  file("conf/application.conf") -> "conf/application.conf",
  file("conf/logback.xml") -> "conf/logback.xml"
)

javaOptions in Universal ++= Seq(
  "-J-Xmx1024m",
  "-J-Xms256m",

  s"-Dpidfile.path=/dev/null",
  s"-Dhttp.port=9000",
  s"-Dconfig.file=/opt/${packageName.value}/conf/application.conf",
  s"-Dlogger.file=/opt/${packageName.value}/conf/logback.xml"
)
scalacOptions ++= Seq(
  "-encoding", "UTF-8", "-optimise",
  "-deprecation", "-unchecked", "-feature", "-Xlint",
  "-Ywarn-infer-any"
)