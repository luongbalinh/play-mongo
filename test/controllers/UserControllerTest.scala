package controllers

import java.time.ZonedDateTime

import dao.impl.{CounterDaoMongo, UserDaoMongo}
import dao.{FakeMongo, UserDao}
import models.User
import modules.{CommonModule, TestDaoModule}
import org.scalatest.{BeforeAndAfterAll, MustMatchers}
import org.scalatestplus.play.{OneServerPerSuite, PlaySpec}
import play.api.inject.bind
import play.api.inject.guice.GuiceApplicationBuilder
import play.api.libs.json.JsSuccess
import play.api.libs.json.Json._
import play.api.mvc.Result
import play.api.test.Helpers._
import play.api.test._
import play.modules.reactivemongo.{ReactiveMongoApi, ReactiveMongoModule}
import reactivemongo.api.DB
import utils.AwaitHelper._
import utils.UserJsonTestFactory._
import utils.UserTestFactory._

import scala.concurrent.Future


class UserControllerTest extends PlaySpec with OneServerPerSuite with MustMatchers with BeforeAndAfterAll {

  "Users controller" must {
    "insert a valid user" in {
      val request = FakeRequest.apply(POST, "/users").withJsonBody(userJson01)

      val response = route(request)
      response.isDefined mustEqual true

      val result: Future[Result] = response.get
      status(result) mustEqual OK
      val userJson = contentAsJson(result)
      (userJson \ "id").as[Long] mustBe 1l
      (userJson \ "firstName").as[String] mustBe "first1"
      (userJson \ "lastName").as[String] mustBe "last1"
      (userJson \ "age").as[Long] mustBe 20l
      ((userJson \ "createdDate").validate[ZonedDateTime] match {
        case JsSuccess(_, _) => true
        case _ => false
      }) mustBe true

      ((userJson \ "updatedDate").validate[ZonedDateTime] match {
        case JsSuccess(_, _) => true
        case _ => false
      }) mustBe true
    }

    "fail inserting a non valid json" in {
      val request = FakeRequest.apply(POST, "/users").withJsonBody(
        obj(
          "firstName" -> 98,
          "lastName" -> "London",
          "age" -> 27))
      val response = route(request)
      response.isDefined mustEqual true
      val result = awaitResult(response.get)
      result.header.status mustEqual BAD_REQUEST
    }

    "update a valid json" in {
      addUser(user00)
      val request = FakeRequest.apply(PUT, "/users/1").withJsonBody(obj(
        "firstName" -> "Jack",
        "age" -> 27))
      val response = route(request)
      response.isDefined mustEqual true
      val result = awaitResult(response.get)
      result.header.status mustEqual OK
      val updatedUserRequest = FakeRequest.apply(GET, "/users/1")
      val updatedUserResponse = route(updatedUserRequest)
      (contentAsJson(updatedUserResponse.get) \ "firstName").get.validate[String].get mustEqual "Jack"
      (contentAsJson(updatedUserResponse.get) \ "age").get.validate[Int].get mustEqual 27

    }
  }

  private lazy val appConfig: Map[String, Any] = Map(
    "mongodb.uri" -> "mongodb://localhost:38128/users-db"
  )
  //  private val mockProvider: MockProvider = new MockProvider()

  private lazy val fakeMongo = new FakeMongo(38128)

  private lazy val fakeReactiveMongoApi = fakeMongo.createMockedReactiveMongoApi("users-db")
  private var db: DB = _
  private var userDao: UserDao = _

  implicit override lazy val app = new GuiceApplicationBuilder()
    .configure(appConfig)
    .disable(classOf[ReactiveMongoModule], classOf[CommonModule])
    .bindings(bind(classOf[ReactiveMongoApi]).toInstance(fakeReactiveMongoApi))
    .bindings(TestDaoModule())
    .build()


  override def beforeAll(): Unit = {
    fakeMongo.start()
    initDb()
    initDAOs()
  }

  override def afterAll(): Unit = {
    fakeReactiveMongoApi.connection.actorSystem.shutdown()
    fakeReactiveMongoApi.connection.actorSystem.awaitTermination()
    fakeMongo.stop()
  }

  def initDb() = {
    db = fakeReactiveMongoApi.db
  }

  def initDAOs(): Unit = {
    userDao = new UserDaoMongo(fakeReactiveMongoApi, new CounterDaoMongo(fakeReactiveMongoApi))
  }

  private def addUser(user: User) = awaitResult(userDao.insertUser(user))
}