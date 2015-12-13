package services

import akka.actor.ActorSystem
import org.scalatest._
import play.api.Configuration
import utils.FakeRedis

import scala.concurrent.ExecutionContext.Implicits.global

class CacheServiceRedisIntegrationTest
  extends FlatSpec with ShouldMatchers with GivenWhenThen with BeforeAndAfterEach {

  var cacheServiceRedis: CacheServiceRedis = _

  "Cache Service Redis" should "allow to set and get user" in {

  }

  override def beforeEach(): Unit = {
    startRedisServer()
    initRedisClient()
  }

  override def afterEach(): Unit = {
    fakeRedis.stop()
  }

  val host: String = "localhost"
  val port: Int = 20000
  val db: Int = 0
  var fakeRedis: FakeRedis = _

  private def startRedisServer(): Unit = {
    fakeRedis = new FakeRedis()
    fakeRedis.connect(port)
    fakeRedis.start()
  }

  private def initRedisClient(): Unit = {
    implicit val akkaSystem = ActorSystem()
    cacheServiceRedis = new CacheServiceRedis(Configuration.from(Map(
      "cache.redis.port" -> port,
      "cache.redis.mpdb" -> db))
    )
  }
}
