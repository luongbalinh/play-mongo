package controllers

import javax.inject.Inject

import models._
import play.api.Logger
import play.api.data.validation.ValidationError
import play.api.libs.json.Json.toJson
import play.api.libs.json._
import play.api.mvc._
import services.UserService

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class UserController @Inject()(userService: UserService) extends Controller {

  private val logger = Logger(this.getClass)

  def findUser(id: Long) = Action.async {
    userService.findUser(id) map {
      case Some(user) =>
        Ok(toJson(user))
      case None =>
        logger.info(s"Cannot find a user with id=$id")
        NotFound(s"Cannot find a user with id=$id")
    }
  }

  def findAllUsers = Action.async {
    userService.findAllUsers() map { users =>
      logger.info(s"Found ${users.size} users.")
      Ok(toJson(users))
    }
  }

  def insertUser() = Action.async(parse.json) { request =>
    request.body.validate[User] fold(invalid = handleValidationErrors, valid = saveUser)
  }

  def removeUser(id: Long) = Action.async {
    logger.info(s"Removing user with id $id")
    userService.removeUser(id) map { _ => Ok(s"Successfully deleted user with id=$id") }
  }

  def updateUser(id: Long) = Action.async(parse.json) { request =>
    userService.updateUser(id, request.body) map { user => Ok(toJson(user)) }
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
        Ok(toJson(savedUser))
    }
  }
}
