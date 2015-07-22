package services

import org.specs2.mock._
import org.specs2.mutable._

/**
 * Unit tests for the service itself. We would expect that the majority of unit tests would be on components like
 * this.
 */
class UserServiceTest extends Specification with Mockito{

  "UserService" should {

    "allow to create a new user" in {
      val user = mock[User]

    }


    "allow to find a user by id" in {

    }

    "allow to update a user by id" in {

    }

    "allow to delete a user by id" in {

    }
  }
}