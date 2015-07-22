name := "play-mongo"

version := "0.2-SNAPSHOT"

scalaVersion := "2.11.6"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

pipelineStages in Assets := Seq()

pipelineStages := Seq(uglify, digest, gzip)

DigestKeys.algorithms += "sha1"

UglifyKeys.uglifyOps := { js =>
  Seq((js.sortBy(_._2), "concat.min.js"))
}

resolvers += "Sonatype Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots/"

libraryDependencies ++= Seq(
  ws,
  specs2 % Test,
  "javax.inject" % "javax.inject" % "1",
  "com.google.inject" % "guice" % "4.0",
  "org.reactivemongo" %% "play2-reactivemongo" % "0.11.1.play24",
  "org.webjars" % "bootstrap" % "3.3.4",
  "org.webjars" % "angularjs" % "1.3.15",
  "org.webjars" % "angular-ui-bootstrap" % "0.13.0",
  "org.mockito" % "mockito-core" % "1.10.19" % "test")

// Speed up compilation

// disable documentation generation
sources in(Compile, doc) := Seq.empty
// avoid to publish the documentation artifact
publishArtifact in(Compile, packageDoc) := false
parallelExecution in Test := true
fork in Test := false

routesGenerator := InjectedRoutesGenerator