package helpers

import de.flapdoodle.embed.mongo.config.{MongodConfigBuilder, Net}
import de.flapdoodle.embed.mongo.distribution.Version
import de.flapdoodle.embed.mongo.{MongodExecutable, MongodProcess, MongodStarter}
import de.flapdoodle.embed.process.runtime.Network
import helpers.AwaitHelper._
import reactivemongo.api.MongoConnection.ParsedURI
import reactivemongo.api._

import scala.concurrent.ExecutionContext
import scala.concurrent.ExecutionContext.Implicits.global

trait EmbeddedMongo {
  private var mongodExe: MongodExecutable = _
  private var mongodProcess: MongodProcess = _
  var db: DefaultDB = _

  def startDB(dbName: String)(implicit ctx: ExecutionContext): Unit = {
    start()
    db = createDb(dbName)
  }

  def stopDB(): Unit = {
    val connection = db.connection
    connection.close()
    awaitResult(connection.actorSystem.terminate())
    awaitResult(connection.actorSystem.whenTerminated)
    stop()
  }

  def start(): Unit = {
    mongodExe = prepareExe()
    mongodProcess = mongodExe.start()
  }

  def stop(): Unit = {
    mongodProcess.stop()
    mongodExe.stop()
  }


  def createDb(dbName: String): DefaultDB = {
    val connection = createConnection()
    connection(dbName)
  }

  private def createConnection(): MongoConnection = {
    val driver = new MongoDriver
    driver.connection(ParsedURI(
      hosts = List((testMongoHosts, testMongoPort)),
      options = MongoConnectionOptions(),
      ignoredOptions = List.empty[String],
      db = None,
      authenticate = None
    ))
  }

  private def prepareExe(): MongodExecutable = MongodStarter.getDefaultInstance.prepare(
    new MongodConfigBuilder()
      .version(Version.Main.PRODUCTION)
      .net(new Net(testMongoHosts, testMongoPort, Network.localhostIsIPv6()))
      .build())

}
