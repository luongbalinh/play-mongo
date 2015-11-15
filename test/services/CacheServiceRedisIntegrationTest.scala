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
    configRedis()
    startRedisServer()
    initRedisClient()
  }

  override def afterEach(): Unit = {
    fakeRedis.stop()
  }

  var host: String = _
  var port: Int = _
  var db: Int = _
  var fakeRedis: FakeRedis = _

  private def configRedis(): Unit = {
    host = "localhost"
    port = 20000
    db = 0
  }

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
