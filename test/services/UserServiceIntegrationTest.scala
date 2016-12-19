package services

import daos.exceptions.MongoObjectNotFoundException
import daos.impl.{CounterMongo, UserMongo, _}
import helpers.AwaitHelper._
import helpers.UserTestFactory._
import helpers.{EmbeddedMongo, _}
import models.User
import org.scalatest._
import play.api.libs.json.Json._
import reactivemongo.api.collections.bson.BSONCollection

import scala.concurrent.ExecutionContext.Implicits.global

class UserServiceIntegrationTest extends FlatSpec
  with Matchers with GivenWhenThen with BeforeAndAfterEach with BeforeAndAfterAll with EmbeddedMongo {
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
    awaitResult(userDAO.insert(user01))

    Then("the user is inserted successfully with a id=1")
    awaitResult(userService.find(1L)).isDefined should be(true)

  }

  it should "delete an existing user" in {
    Given("a user")
    addUser(user01)

    When("tries to delete that user")
    awaitResult(userService.remove(1L))

    Then("the user is deleted successfully")
    awaitResult(userService.find(1L)).isDefined shouldBe false
  }

  it should "return exception when deleting a non-existing user" in {
    Given("a user")
    addUser(user01)

    When("tries to delete a non-existing user")

    Then("return exception")
    intercept[MongoObjectNotFoundException] {
      awaitResult(userService.remove(-1L))
    }
  }

  it should "update an existing user" in {
    Given("a user")
    addUser(user01)

    When("tries to update that user")
    awaitResult(userService.update(1L, obj("firstName" -> "updatedName")))

    Then("the user is updated successfully")
    val result: User = awaitResult(userService.find(1L)).get
    result.firstName should be("updatedName")
  }

  implicit val config = ConfigHelper.app.configuration

  private var counterDAO: CounterMongo = _
  private var userDAO: UserMongo = _

  private var userService: UserServiceImpl = _

  override def beforeAll() = {
    startDB(testUserMongoDbName)
    initDAOs()
    initServices()
  }

  override def afterAll() =
    stopDB()

  override def beforeEach(): Unit = {
    awaitResult(db.collection[BSONCollection](userCollectionName).drop(failIfNotFound = false))
    awaitResult(db.collection[BSONCollection](counterCollectionName).drop(failIfNotFound = false))
  }

  private def initDAOs() = {
    counterDAO = new CounterMongo
    userDAO = new UserMongo(counterDAO)
  }

  private def initServices() = {
    userService = new UserServiceImpl(userDAO)
  }

  private def addUser(user: User) = awaitResult(userDAO.insert(user))
}