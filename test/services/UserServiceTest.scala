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
    doReturn(Future(Some(mockUser))).when(mockUserDAO).findUser(1L)

    When("UserService tries to read a user")
    userService findUser 1L

    Then("UserDao read() should be called once")
    verify(mockUserDAO, times(1)) findUser 1L
  }

  it should "allow to delete a user" in {
    Given("a user in database")
    doReturn(Future(Success(Unit))).when(mockUserDAO).removeUser(1L)

    When("UserService tries to delete the user")
    userService removeUser 1L

    Then("UserDao delete() should be called once")
    verify(mockUserDAO, times(1)) removeUser 1L
  }

  it should "allow to get all users" in {
    Given("a list of users")
    doReturn(Future(List())).when(mockUserDAO).findAllUsers()

    When("tries to get all users")
    userService findAllUsers()

    Then("UserDao findAll() should be called once")
    verify(mockUserDAO, times(1)) findAllUsers()
  }

  val mockUserDAO: UserDao = mock(classOf[UserDao])
  val userService: UserService = new UserServiceImpl(mockUserDAO)
  val userId: Long = 1L
}