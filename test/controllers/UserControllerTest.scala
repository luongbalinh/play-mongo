package controllers

import java.time.ZonedDateTime

import akka.stream.Materializer
import daos.UserDAO
import daos.impl._
import helpers.AwaitHelper._
import helpers.UserJsonTestFactory._
import helpers.UserTestFactory._
import helpers.ZonedDateTimeReadWrite._
import helpers.{EmbeddedMongo, _}
import models.User
import org.scalatest.{BeforeAndAfterAll, BeforeAndAfterEach, MustMatchers}
import org.scalatestplus.play.{OneServerPerSuite, PlaySpec}
import play.api.libs.json.JsSuccess
import play.api.libs.json.Json._
import play.api.test.Helpers._
import play.api.test._
import reactivemongo.api.collections.bson.BSONCollection
import services.{UserService, UserServiceImpl}

import scala.concurrent.ExecutionContext.Implicits.global


class UserControllerTest extends PlaySpec with OneServerPerSuite with MustMatchers with BeforeAndAfterAll with BeforeAndAfterEach with EmbeddedMongo {
  implicit lazy val materializer: Materializer = app.materializer

  "Users controller" must {
    "insert a valid user" in {
      val insertRequest = FakeRequest(POST, "/users").withJsonBody(userJson01)
      val insertResponse = call(userController.insert(), insertRequest)
      status(insertResponse) mustBe OK

      val getRequest = FakeRequest(GET, "/users/1")
      val getResponse = call(new UserController(userService).find(1), getRequest)
      status(getResponse) mustBe OK
      val user = contentAsJson(getResponse)
      (user \ "id").as[Long] mustBe 1L
      (user \ "firstName").as[String] mustBe "first1"
      (user \ "lastName").as[String] mustBe "last1"
      (user \ "age").as[Long] mustBe 20L
      ((user \ "createdDate").validate[ZonedDateTime] match {
        case JsSuccess(_, _) => true
        case _ => false
      }) mustBe true

      ((user \ "updatedDate").validate[ZonedDateTime] match {
        case JsSuccess(_, _) => true
        case _ => false
      }) mustBe true
    }


    "fail inserting a non valid json" in {
      val request = FakeRequest(POST, "/users").withJsonBody(obj("firstName" -> 98, "lastName" -> "London", "age" -> 27))
      val response = call(userController.insert(), request)
      status(response) mustBe BAD_REQUEST
    }

    "update a valid json" in {
      addUser(user00)

      val updateRequest = FakeRequest(PUT, "/users/1").withJsonBody(obj("firstName" -> "Jack", "age" -> 27))
      val updateResponse = call(userController.update(1), updateRequest)

      status(updateResponse) mustBe OK
      val getRequest = FakeRequest(GET, "/users/1")
      val getResponse = call(userController.find(1), getRequest)
      (contentAsJson(getResponse) \ "firstName").get.validate[String].get mustBe "Jack"
      (contentAsJson(getResponse) \ "age").get.validate[Int].get mustBe 27

    }
  }

  implicit val config = ConfigHelper.app.configuration

  private var userDAO: UserDAO = _
  private var userService: UserService = _
  private var userController: UserController = _


  override def beforeAll(): Unit = {
    startDB(testUserMongoDbName)
    initDAOs()
    initServices()
    initControllers()
  }

  override def afterAll(): Unit = {
    stopDB()
  }

  override def beforeEach(): Unit = {
    awaitResult(db.collection[BSONCollection](userCollectionName).drop(failIfNotFound = false))
    awaitResult(db.collection[BSONCollection](counterCollectionName).drop(failIfNotFound = false))
  }

  def initDAOs(): Unit = {
    userDAO = new UserMongo(new CounterMongo())
  }

  def initServices(): Unit = {
    userService = new UserServiceImpl(userDAO)
  }

  def initControllers(): Unit = {
    userController = new UserController(userService)
  }

  private def addUser(user: User) = awaitResult(userDAO.insert(user))
}