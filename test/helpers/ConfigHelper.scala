package helpers

import injection.{Module, TestDaoModule}
import play.api.Application
import play.api.inject.guice.GuiceApplicationBuilder
import play.modules.reactivemongo.ReactiveMongoModule

object ConfigHelper {
  private lazy val appConfig: Map[String, Any] = Map(
    "mongodb.servers" -> List(testMongoHosts),
    "mongodb.port" -> testMongoPort,
    "mongodb.db" -> testUserMongoDbName
  )

  val app: Application = new GuiceApplicationBuilder()
    .configure(appConfig)
    .disable(classOf[ReactiveMongoModule], classOf[Module])
    .bindings(TestDaoModule())
    .build()
}
