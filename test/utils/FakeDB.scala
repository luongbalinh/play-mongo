package utils

import play.modules.reactivemongo.ReactiveMongoApi

import scala.concurrent.ExecutionContext

trait FakeDB {

  val embeddedPort = 38128
  val fakeMongo: FakeMongo = new FakeMongo(embeddedPort)

  var api: ReactiveMongoApi = _

  def startDB(dbName: String)(implicit ctx: ExecutionContext): Unit = {
    fakeMongo.start()
    api = fakeMongo.createMockedReactiveMongoApi(dbName)
  }

  def stopDB(): Unit = {
    api.db.connection.close()
    api.connection.actorSystem.shutdown()
    api.connection.actorSystem.awaitTermination()
    fakeMongo.stop()
  }
}
