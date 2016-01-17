package utils

import redis.embedded.RedisServer

class FakeRedis {
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
