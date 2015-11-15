package services

import dao.FakeMongo
import dao.impl.{CounterDaoMongo, UserDaoMongo}
import models.User
import org.scalatest._
import play.api.libs.json.Json._
import play.api.test.FakeApplication
import play.api.{Application, GlobalSettings}
import play.modules.reactivemongo.ReactiveMongoApi
import reactivemongo.api.DB
import reactivemongo.api.collections.bson.BSONCollection
import utils.AwaitHelper._
import utils.UserTestFactory._

import scala.concurrent.ExecutionContext.Implicits.global

class UserServiceIntegrationTest extends FlatSpec
with ShouldMatchers with GivenWhenThen with BeforeAndAfterEach with BeforeAndAfterAll {
  "UserService" should "find a user given its Id" in {
    Given("a user")
    addUser(user01)

    When("tries to find that user")
    val result: Option[User] = awaitResult(userService.findUser(1l))

    Then("the result is that user")
    result.isDefined shouldBe true
    result.get.firstName shouldBe user01.firstName
  }

  it should "find a user with non-existing Id" in {
    Given("a user")
    addUser(user01)

    When("tries to find user with non-existing Id")
    val result: Option[User] = awaitResult(userService.findUser(-1L))

    Then("the result is None")
    result.isDefined shouldBe false
  }

  it should "get all users" in {
    Given("two users")
    addUser(user01)
    addUser(user02)

    When("tries to get all users")
    val result: List[User] = awaitResult(userService.findAllUsers())

    Then("the result contains two users with ids 1 and 2")
    result.size shouldBe 2
    result.head.id shouldEqual Some(1l)
    result(1).id shouldEqual Some(2l)
  }

  it should "insert a new user" in {
    Given("a new user")

    When("UserService tries to insert that new user")
    val result = awaitResult(userDao.insertUser(user01))
    db.collection[BSONCollection](CounterDaoMongo.CollectionName).drop()

    Then("the user is inserted successfully with a id=1")
    result.get.id.get shouldBe 1l
  }

  it should "delete an existing user" in {
    Given("a user")
    addUser(user01)

    When("tries to delete that user")
    val result: Boolean = awaitResult(userService.removeUser(1l))

    Then("the user is deleted successfully")
    result shouldBe true
    awaitResult(userService.findUser(1l)).isDefined shouldBe false
  }

  it should "return false when deleting a non-existing user" in {
    Given("a user")
    addUser(user01)

    When("tries to delete a non-existing user")
    val result: Boolean = awaitResult(userService.removeUser(-1l))

    Then("the return value is false")
    result shouldBe false
  }

  it should "update an existing user" in {
    Given("a user")
    addUser(user01)

    When("tries to update that user")
    val result: User = awaitResult(userService.updateUser(1l, obj("firstName" -> "updatedName")))

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

  private var fakeMongo: FakeMongo = _
  private var reactiveMongoApi: ReactiveMongoApi = _
  private var db: DB = _

  private var counterDao: CounterDaoMongo = _
  private var userDao: UserDaoMongo = _

  private var userService: UserServiceImpl = _

  override def beforeAll() = {
    initDb()
    initDAOs()
    initServices()
  }

  override def afterAll() = {
    reactiveMongoApi.connection.actorSystem.shutdown()
    reactiveMongoApi.connection.actorSystem.awaitTermination()
    fakeMongo.stop()
  }

  override def afterEach(): Unit = {
    db.collection[BSONCollection](UserDaoMongo.CollectionName).drop()
    db.collection[BSONCollection](CounterDaoMongo.CollectionName).drop()
  }

  private def initDb() = {
    fakeMongo = new FakeMongo(38128)
    fakeMongo.start()
    reactiveMongoApi = fakeMongo.createMockedReactiveMongoApi("users-db")
    db = reactiveMongoApi.db
  }

  private def initDAOs() = {
    counterDao = new CounterDaoMongo(reactiveMongoApi)
    userDao = new UserDaoMongo(reactiveMongoApi, counterDao)
  }

  private def initServices() = {
    userService = new UserServiceImpl(userDao)
  }

  private def addUser(user: User) = awaitResult(userDao.insertUser(user))
}