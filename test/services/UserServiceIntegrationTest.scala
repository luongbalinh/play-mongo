package services

import dao.exception.UserDaoException
import dao.impl.{CounterDaoMongo, UserDaoMongo}
import models.User
import org.scalatest._
import play.api.libs.json.Json._
import play.api.test.FakeApplication
import play.api.{Application, GlobalSettings}
import reactivemongo.api.collections.bson.BSONCollection
import utils.AwaitHelper._
import utils.FakeDB
import utils.UserTestFactory._

import scala.concurrent.ExecutionContext.Implicits.global

class UserServiceIntegrationTest extends FlatSpec
with ShouldMatchers with GivenWhenThen with BeforeAndAfterEach with BeforeAndAfterAll with FakeDB {
  "UserService" should "find a user given its Id" in {
    Given("a user")
    addUser(user01)

    When("tries to find that user")
    val result: Option[User] = awaitResult(userService.find(1L))

    Then("the result is that user")
    result.isDefined shouldBe true
    result.get.firstName shouldBe user01.firstName
  }

  it should "find a user with non-existing Id" in {
    Given("a user")
    addUser(user01)

    When("tries to find user with non-existing Id")
    val result: Option[User] = awaitResult(userService.find(-1L))

    Then("the result is None")
    result.isDefined shouldBe false
  }

  it should "get all users" in {
    Given("two users")
    addUser(user01)
    addUser(user02)

    When("tries to get all users")
    val result: List[User] = awaitResult(userService.findAll())

    Then("the result contains two users with ids 1 and 2")
    result.size shouldBe 2
    result.head.id shouldEqual Some(1L)
    result(1).id shouldEqual Some(2L)
  }

  it should "insert a new user" in {
    Given("a new user")

    When("UserService tries to insert that new user")
    val result = awaitResult(userDao.insert(user01))
    api.db.collection[BSONCollection](CounterDaoMongo.CollectionName).drop()

    Then("the user is inserted successfully with a id=1")
    result.id.get shouldBe 1L
  }

  it should "delete an existing user" in {
    Given("a user")
    addUser(user01)

    When("tries to delete that user")
    val result = awaitResult(userService.remove(1L))

    Then("the user is deleted successfully")
    awaitResult(userService.find(1L)).isDefined shouldBe false
  }

  it should "return exception when deleting a non-existing user" in {
    Given("a user")
    addUser(user01)

    When("tries to delete a non-existing user")

    Then("return exception")
    intercept[UserDaoException] {
      awaitResult(userService.remove(-1L))
    }
  }

  it should "update an existing user" in {
    Given("a user")
    addUser(user01)

    When("tries to update that user")
    val result: User = awaitResult(userService.update(1L, obj("firstName" -> "updatedName")))

    Then("the user is updated successfully")
    result.firstName shouldBe "updatedName"
  }

  val appConfig: Map[String, Any] = Map(

  )

  val theFakeApp = FakeApplication(
    additionalConfiguration = appConfig,
    withGlobal = Some(new GlobalSettings {
      override def onStart(app: Application): Unit = {
      }
    })
  )

  implicit lazy val config = theFakeApp.configuration

  private var counterDao: CounterDaoMongo = _
  private var userDao: UserDaoMongo = _

  private var userService: UserServiceImpl = _

  override def beforeAll() = {
    startDB("users-db")
    initDAOs()
    initServices()
  }

  override def afterAll() = {
    stopDB()
  }

  override def afterEach(): Unit = {
    api.db.collection[BSONCollection](UserDaoMongo.CollectionName).drop()
    api.db.collection[BSONCollection](CounterDaoMongo.CollectionName).drop()
  }

  private def initDAOs() = {
    counterDao = new CounterDaoMongo(api)
    userDao = new UserDaoMongo(api, counterDao)
  }

  private def initServices() = {
    userService = new UserServiceImpl(userDao)
  }

  private def addUser(user: User) = awaitResult(userDao.insert(user))
}