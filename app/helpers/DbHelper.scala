package helpers

import play.api.Configuration
import reactivemongo.api.MongoDriver
import reactivemongo.play.json.collection.JSONCollection

import scala.collection.JavaConversions._
import scala.concurrent.{ExecutionContext, Future}

object DbHelper {
  def getCollection(collectionName: String)(implicit config: Configuration, ctx: ExecutionContext): Future[JSONCollection] = {
    val mongoConfig = config.getConfig("mongodb").getOrElse(defaultMongoConfig)
    var hosts = mongoConfig.getStringList("servers").map(_.toList).getOrElse(defaultMongoHosts)
    if (hosts.isEmpty) hosts = defaultMongoHosts
    val port = mongoConfig.getInt("port").getOrElse(defaultMongoPort)
    val uris = hosts map (host => s"$host:$port")
    val dbName = mongoConfig.getString("db").getOrElse("users-db")

    new MongoDriver().connection(uris).database(dbName) map (_.collection[JSONCollection](collectionName))
  }

  val defaultMongoHosts = List("localhost")
  val defaultMongoPort = 27017
  val defaultMongoDb = "users-db"
  val defaultMongoConfig: Configuration = Configuration.from(
    Map(
      "servers" -> defaultMongoHosts,
      "db" -> defaultMongoDb))
}

