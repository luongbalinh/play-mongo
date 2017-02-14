package services

import akka.actor.ActorSystem
import helpers.FakeRedis
import org.scalatest._
import play.api.Configuration

import scala.concurrent.ExecutionContext.Implicits.global

class CacheServiceRedisIntegrationTest extends FlatSpec with GivenWhenThen with BeforeAndAfterEach {
  var cacheServiceRedis: CacheServiceRedis = _

  override def beforeEach(): Unit = {
    startRedisServer()
    initRedisClient()
  }

  override def afterEach(): Unit =
    fakeRedis.stop()

  val host = "localhost"
  val port = 20000
  val db = 0
  var fakeRedis: FakeRedis = _

  private def startRedisServer(): Unit = {
    fakeRedis = new FakeRedis()
    fakeRedis.connect(port)
    fakeRedis.start()
  }

  private def initRedisClient(): Unit = {
    implicit val akkaSystem = ActorSystem()
    cacheServiceRedis = new CacheServiceRedis(Configuration.from(Map(
      "cache.redis.port" -> port))
    )
  }
}
