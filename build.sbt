name := "play-mongo"

version := "1.0"

scalaVersion := "2.11.7"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

pipelineStages in Assets := Seq()

pipelineStages := Seq(uglify, digest, gzip)

DigestKeys.algorithms += "sha1"

UglifyKeys.uglifyOps := { js =>
  Seq((js.sortBy(_._2), "concat.min.js"))
}

resolvers ++= Seq(
  "Sonatype Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots/"
)

libraryDependencies ++= Seq(
  filters,
  ws,
  "javax.inject" % "javax.inject" % "1",
  "com.google.inject" % "guice" % "4.0",
  "org.reactivemongo" %% "play2-reactivemongo" % "0.11.2.play24",
  "org.reactivemongo" % "reactivemongo-extensions-json_2.11" % "0.11.7.play24",
  "com.github.etaty" %% "rediscala" % "1.6.0",
  "org.webjars" % "bootstrap" % "3.3.4",
  "org.webjars" % "angularjs" % "1.3.15",
  "org.webjars" % "angular-ui-bootstrap" % "0.13.0",
  "org.scalatest" % "scalatest_2.11" % "2.2.4" % Test,
  "org.scalatestplus" %% "play" % "1.4.0-M4" % Test,
  "de.flapdoodle.embed" % "de.flapdoodle.embed.mongo" % "1.50.0" % Test,
  "com.github.kstyrc" % "embedded-redis" % "0.6" % Test,
  "org.mockito" % "mockito-core" % "1.10.19" % Test
)

// Speed up compilation

// disable documentation generation
sources in(Compile, doc) := Seq.empty
// avoid to publish the documentation artifact
publishArtifact in(Compile, packageDoc) := false
parallelExecution in Test := false
fork in Test := false

scalacOptions ++= Seq(
  "-encoding", "UTF-8", "-optimise",
  "-deprecation", "-unchecked", "-feature", "-Xlint",
  "-Ywarn-infer-any")

routesGenerator := InjectedRoutesGenerator