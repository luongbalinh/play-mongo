package services

import javax.inject.{Inject, Singleton}

import akka.actor.ActorSystem
import models.User
import play.api.Configuration
import redis.RedisClient
import services.CacheServiceRedis._

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class CacheServiceRedis @Inject()(config: Configuration)(
  implicit ec: ExecutionContext,
  implicit val system: ActorSystem
) extends CacheService {
  private val redisConfig = config.getConfig("redis").getOrElse(defaultRedisConfig)
  private val host = redisConfig.getString("host").getOrElse(defaultRedisHost)
  private val port = redisConfig.getInt("port").getOrElse(defaultRedisPort)
  private val db = redisConfig.getString("db").getOrElse(defaultRedisDb)
  private val redisClient = RedisClient(host = host, port = port, name = db)

  override def getUser(token: String): Future[Option[User]] =
    redisClient.get[User](token)
}

object CacheServiceRedis {
  val defaultRedisHost = "localhost"
  val defaultRedisPort = 6379
  val defaultRedisDb = "users"
  val defaultRedisConfig = Configuration.from(
    Map(
      "host" -> defaultRedisHost,
      "port" -> defaultRedisPort,
      "db" -> defaultRedisDb))
}