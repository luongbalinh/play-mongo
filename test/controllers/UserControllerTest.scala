package controllers

import java.time.ZonedDateTime

import dao.FakeMongo
import modules.{CommonModule, TestDaoModule}
import org.scalatest.{BeforeAndAfterAll, MustMatchers}
import org.scalatestplus.play.{OneServerPerSuite, PlaySpec}
import play.api.inject.bind
import play.api.inject.guice.GuiceApplicationBuilder
import play.api.libs.json.JsSuccess
import play.api.mvc.Result
import play.api.test.Helpers._
import play.api.test._
import play.modules.reactivemongo.{ReactiveMongoApi, ReactiveMongoModule}
import utils.UserJsonTestFactory._

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
        case JsSuccess(_,_) => true
        case _ => false
      }) mustBe true

      ((userJson \ "updatedDate").validate[ZonedDateTime] match {
        case JsSuccess(_,_) => true
        case _ => false
      }) mustBe true

      (userJson \ "createdDate") mustEqual (userJson \ "updatedDate")
    }

    //    "fail inserting a non valid json" in {
    //      running(FakeApplication()) {
    //        val request = FakeRequest.apply(POST, "/user").withJsonBody(Json.obj(
    //          "firstName" -> 98,
    //          "lastName" -> "London",
    //          "age" -> 27))
    //        val response = route(request)
    //        response.isDefined mustEqual true
    //        val result = awaitResult(response.get)
    //        contentAsString(response.get) mustEqual "invalid json"
    //        result.header.status mustEqual BAD_REQUEST
    //      }
    //    }
    //
    //    "update a valid json" in {
    //      running(FakeApplication()) {
    //        val request = FakeRequest.apply(PUT, "/user/Jack/London").withJsonBody(Json.obj(
    //          "firstName" -> "Jack",
    //          "lastName" -> "London",
    //          "age" -> 27,
    //          "active" -> true))
    //        val response = route(request)
    //        response.isDefined mustEqual true
    //        val result = awaitResult(response.get)
    //        result.header.status must equalTo(CREATED)
    //      }
    //    }
    //
    //    "fail updating a non valid json" in {
    //      running(FakeApplication()) {
    //        val request = FakeRequest.apply(PUT, "/user/Jack/London").withJsonBody(Json.obj(
    //          "firstName" -> "Jack",
    //          "lastName" -> "London",
    //          "age" -> 27))
    //        val response = route(request)
    //        response.isDefined mustEqual true
    //        val result = Await.result(response.get, timeout)
    //        contentAsString(response.get) mustEqual "invalid json"
    //        result.header.status mustEqual BAD_REQUEST
    //      }
    //    }
  }

  private lazy val appConfig: Map[String, Any] = Map(
    "mongodb.uri" -> "mongodb://localhost:38128/users-db"
  )
  //  private val mockProvider: MockProvider = new MockProvider()

  private lazy val fakeMongo = new FakeMongo(38128)

  private lazy val fakeReactiveMongoApi = fakeMongo.createMockedReactiveMongoApi("users-db")

  implicit override lazy val app = new GuiceApplicationBuilder()
    .configure(appConfig)
    .disable(classOf[ReactiveMongoModule], classOf[CommonModule])
    .bindings(bind(classOf[ReactiveMongoApi]).toInstance(fakeReactiveMongoApi))
    .bindings(TestDaoModule())
    .build()

  override def beforeAll(): Unit = {
    fakeMongo.start()
  }

  override def afterAll(): Unit = {
    fakeReactiveMongoApi.connection.actorSystem.shutdown()
    fakeReactiveMongoApi.connection.actorSystem.awaitTermination()
    fakeMongo.stop()
  }
}