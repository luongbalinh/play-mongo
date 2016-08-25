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

  def find(id: Long) = Action.async {
    userService.find(id) map {
      case Some(user) =>
        Ok(toJson(user))
      case None =>
        logger.info(s"Cannot find a user with id=$id")
        NotFound(s"Cannot find a user with id=$id")
    }
  }

  def findAll = Action.async {
    userService.findAll() map { users =>
      logger.info(s"Found ${users.size} users.")
      Ok(toJson(users))
    }
  }

  def insert() = Action.async(parse.json) { request =>
    request.body.validate[User] fold(invalid = handleValidationErrors, valid = save)
  }

  def remove(id: Long) = Action.async {
    logger.info(s"Removing user with id $id")
    userService.remove(id) map { _ => Ok(s"Successfully deleted user with id=$id") }
  }

  def update(id: Long) = Action.async(parse.json) { request =>
    userService.update(id, request.body) map { user => Ok(toJson(user)) }
  }

  private def handleValidationErrors: Seq[(JsPath, Seq[ValidationError])] => Future[Result] = { errors =>
    Future {
      logger.error(s"Failed to create a user due to invalid json format: ${JsError.toJson(errors)}")
      BadRequest(s"Invalid json format: ${JsError.toJson(errors)}")
    }
  }

  private def save: User => Future[Result] = { user =>
    userService.insert(user) map {
      case savedUser =>
        logger.info(s"Successfully created user=$savedUser")
        Ok(toJson(savedUser))
    }
  }
}
