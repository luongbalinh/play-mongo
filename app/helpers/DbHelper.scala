package helpers

import play.api.Configuration
import reactivemongo.api.MongoDriver
import reactivemongo.play.json.collection.JSONCollection

import scala.collection.JavaConversions._
import scala.concurrent.{ExecutionContext, Future}

object DbHelper {
  def getCollection(collectionName: String)(implicit config: Configuration, ctx: ExecutionContext): Future[JSONCollection] = {
    val mongoConfig = config.getConfig("mongodb").getOrElse(defaultMongoConfig)
    val hosts = mongoConfig.getStringList("servers").map(_.toList).getOrElse(defaultMongoHosts)
    val dbName = mongoConfig.getString("db").getOrElse("users-db")
    val driver = new MongoDriver
    val connection = driver.connection(hosts)

    connection.database(dbName) map { db =>
      db.collection[JSONCollection](collectionName)
    }
  }

  val defaultMongoHosts = List("localhost:27017")
  val defaultMongoDb = "users-db"
  val defaultMongoConfig = Configuration.from(
    Map(
      "servers" -> defaultMongoHosts,
      "db" -> defaultMongoDb))
}

