package services

import javax.inject.{Inject, Singleton}

import akka.actor.ActorSystem
import models.User
import play.api.Configuration
import redis.RedisClient

import scala.concurrent.{Future, ExecutionContext}

@Singleton
class CacheServiceRedis @Inject()(config: Configuration)(implicit ec:
ExecutionContext, implicit val system: ActorSystem)
  extends CacheService {

  val redisConfig: RedisConfig = readRedisConfig
  private val redisClient = RedisClient(redisConfig.host, redisConfig.port, None, Some(redisConfig.db))

  override def getUser(token: String): Future[Option[User]] =
  redisClient.get[User](token)

  private def readRedisConfig: RedisConfig = {
    val host = config.getString("cache.redis.url").getOrElse("127.0.0.1")
    val port = config.getInt("cache.redis.port").getOrElse(6379)
    val db = config.getInt("cache.redis.db").getOrElse(13)

    RedisConfig(host, port, db)
  }
}

case class RedisConfig(host: String, port: Int, db: Int)
