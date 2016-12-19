package helpers

import injection.{Module, TestDaoModule}
import play.api.inject.guice.GuiceApplicationBuilder
import play.modules.reactivemongo.ReactiveMongoModule

object ConfigHelper {
  private lazy val appConfig: Map[String, Any] = Map(
    "mongodb.servers" -> testMongoHosts,
    "mongodb.db" -> testUserMongoDbName
  )

  val app = new GuiceApplicationBuilder()
    .configure(appConfig)
    .disable(classOf[ReactiveMongoModule], classOf[Module])
    .bindings(TestDaoModule())
    .build()
}
