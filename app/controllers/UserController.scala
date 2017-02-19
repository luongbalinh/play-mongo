package controllers

import javax.inject.Inject

import com.newrelic.api.agent.Trace
import models._
import play.api.Logger
import play.api.libs.json.Json.toJson
import play.api.libs.json._
import play.api.mvc._
import services.UserService

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class UserController @Inject()(userService: UserService) extends Controller with MongoExceptionHandler {

  private val logger = Logger(this.getClass)

  @Trace
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
    request.body.validate[User] fold(invalid = handleValidationErrors, valid = handleInsert)
  }

  def remove(id: Long) = Action.async {
    logger.info(s"Removing user with id $id")
    userService.remove(id) map { _ => Ok(s"Successfully deleted user with id=$id") } recover handleUpdateDeleteFailure
  }

  def update(id: Long) = Action.async(parse.json) { request =>
    userService.update(id, request.body) map (_ => Ok) recover handleUpdateDeleteFailure
  }

  private def handleInsert: User => Future[Result] = { user =>
    userService.insert(user) map (id => Ok(Json.obj("id" -> id))) recover handleInsertFailure
  }
}
