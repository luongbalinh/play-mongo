package utils

import play.api.Logger
import redis.embedded.RedisServer

class FakeRedis {
  private val logger= Logger(this.getClass)

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
