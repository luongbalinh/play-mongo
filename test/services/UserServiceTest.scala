package services

import dao.UserDao
import models.User
import org.mockito.Mockito.{doReturn, mock, times, verify}
import org.scalatest.{BeforeAndAfterEach, FlatSpec, GivenWhenThen, ShouldMatchers}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.Success

class UserServiceTest extends FlatSpec with ShouldMatchers with GivenWhenThen with BeforeAndAfterEach {

  it should "allow to get a user" in {
    Given("a user")
    val mockUser: User = mock(classOf[User])
    doReturn(Future(Some(mockUser))).when(mockUserDAO).find(1L)

    When("UserService tries to read a user")
    userService find 1L

    Then("UserDao read() should be called once")
    verify(mockUserDAO, times(1)) find 1L
  }

  it should "allow to delete a user" in {
    Given("a user in database")
    doReturn(Future(Success(Unit))).when(mockUserDAO).remove(1L)

    When("UserService tries to delete the user")
    userService remove 1L

    Then("UserDao delete() should be called once")
    verify(mockUserDAO, times(1)) remove 1L
  }

  it should "allow to get all users" in {
    Given("a list of users")
    doReturn(Future(List())).when(mockUserDAO).findAll()

    When("tries to get all users")
    userService findAll()

    Then("UserDao findAll() should be called once")
    verify(mockUserDAO, times(1)) findAll()
  }

  val mockUserDAO: UserDao = mock(classOf[UserDao])
  val userService: UserService = new UserServiceImpl(mockUserDAO)
  val userId: Long = 1L
}