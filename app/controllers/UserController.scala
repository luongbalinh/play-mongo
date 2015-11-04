package controllers

import javax.inject.Inject

import models._
import org.slf4j.LoggerFactory
import play.api.data.validation.ValidationError
import play.api.libs.json._
import play.api.mvc._
import services.UserService

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class UserController @Inject()(userService: UserService) extends Controller {

  private val logger = LoggerFactory.getLogger(classOf[UserController])

  def findUser(id: Long) = Action.async { request =>
    userService.findUser(id) map {
      case Some(user) =>
        logger.info(s"Found a user with id=$id")
        Ok(Json.toJson(user))
      case None =>
        logger.info(s"Cannot find a user with id=$id")
        NotFound(s"Cannot find a user with id=$id")
    }
  }

  def findAllUsers = Action.async { request =>
    userService.findAllUsers() map { users => {
      logger.info(s"Found ${users.size} users.")
      Ok(Json.toJson(users))
    }
    }
  }

  def insertUser() = Action.async(parse.json) { request =>
    request.body.validate[User] fold(invalid = handleValidationErrors, valid = saveUser)
  }

  def removeUser(id: Long) = Action.async { request =>
    logger.info(s"Removing user with id $id")
    userService.removeUser(id) map {
      case true => Ok(s"Successfully deleted user with id=$id")
      case false => NotFound(s"Failed to delete user with id=$id")
    }
  }

  def updateUser(id: Long) = Action.async(parse.json) { request =>
    val update = request.body

    userService.updateUser(id, update) map { user =>
      Ok(Json.toJson(user))
    } recover {
      case e: Exception =>
        logger.error(s"Failed to update user with id=$id; update Json=$update", e)
        BadRequest(s"Failed to update user id = $id ${e.getMessage}")
    }
  }

  private def handleValidationErrors: Seq[(JsPath, Seq[ValidationError])] => Future[Result] = { errors =>
    Future {
      logger.error(s"Failed to create a user due to invalid json format: ${JsError.toJson(errors)}")
      BadRequest(s"Invalid json format: ${JsError.toJson(errors)}")
    }
  }

  private def saveUser: User => Future[Result] = { user =>
    userService.insertUser(user) map {
      case savedUser =>
        logger.info(s"Successfully created user=$savedUser")
        Ok(Json.toJson(savedUser))
    } recover {
      case e: Exception =>
        logger.error(s"Failed to insert user $user", e)
        BadRequest(s"Failed to insert user: $user")
    }
  }
}