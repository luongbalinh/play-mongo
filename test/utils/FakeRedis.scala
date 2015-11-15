package utils

import org.slf4j.{Logger, LoggerFactory}
import redis.embedded.RedisServer

class FakeRedis {
  private val logger: Logger = LoggerFactory.getLogger(classOf[FakeRedis])

  var redisServer: RedisServer = _

  def start(): Unit = {
    redisServer.start()
  }

  def stop(): Unit = {
    redisServer.stop()
  }

  def connect(port: Int): Unit = {
    redisServer = new RedisServer(port)
  }
}
