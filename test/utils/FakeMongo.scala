package utils

import de.flapdoodle.embed.mongo.config.{MongodConfigBuilder, Net}
import de.flapdoodle.embed.mongo.distribution.Version
import de.flapdoodle.embed.mongo.{MongodExecutable, MongodProcess, MongodStarter}
import de.flapdoodle.embed.process.runtime.Network
import org.mockito.Mockito._
import play.modules.reactivemongo.ReactiveMongoApi
import reactivemongo.api.MongoConnection.ParsedURI
import reactivemongo.api.{MongoConnection, MongoConnectionOptions, MongoDriver}

import scala.concurrent.ExecutionContext.Implicits.global

class FakeMongo(port: Int) {

  private var mongodExe: MongodExecutable = _
  private var mongodProcess: MongodProcess = _

  def start(): Unit = {
    mongodExe = prepareExe()
    mongodProcess = mongodExe.start()
  }

  def stop(): Unit = {
    mongodProcess.stop()
    mongodExe.stop()
  }

  def createConnection(): MongoConnection = {
    val driver = new MongoDriver
    driver.connection(ParsedURI(
      hosts = List(("localhost", port)),
      options = MongoConnectionOptions(),
      ignoredOptions = List.empty[String],
      db = None,
      authenticate = None
    ))
  }

  def createMockedReactiveMongoApi(dbName: String): ReactiveMongoApi = {
    val connection = createConnection()
    val db = connection(dbName)
    val api = mock(classOf[ReactiveMongoApi])
    doReturn(db).when(api).db
    doReturn(connection).when(api).connection

    api
  }

  private def prepareExe(): MongodExecutable = MongodStarter.getDefaultInstance.prepare(
    new MongodConfigBuilder()
      .version(Version.Main.V3_1)
      .net(new Net(port, Network.localhostIsIPv6()))
      .build()
  )
}
