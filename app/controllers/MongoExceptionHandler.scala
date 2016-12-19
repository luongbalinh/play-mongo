package controllers


import daos.exceptions.{MongoInsertException, MongoObjectNotFoundException, MongoUpdateDeleteException}
import play.api.data.validation.ValidationError
import play.api.libs.json.{JsError, JsPath}
import play.api.mvc.Result
import play.api.mvc.Results._

import scala.concurrent.{ExecutionContext, Future}

trait MongoExceptionHandler {
  def handleValidationErrors(implicit ctx: ExecutionContext): Seq[(JsPath, Seq[ValidationError])] => Future[Result] = { errors =>
    Future {
      BadRequest(s"Invalid json format: ${JsError.toJson(errors)}")
    }
  }

  def handleUpdateDeleteFailure: PartialFunction[Throwable, Result] = {
    case e: MongoObjectNotFoundException => NotFound(e.getMessage)
    case e: MongoUpdateDeleteException => BadRequest(e.getMessage)
    case e: Exception => InternalServerError(e.getMessage)
  }

  def handleInsertFailure: PartialFunction[Throwable, Result] = {
    case e: MongoInsertException => BadRequest(e.getMessage)
    case e: Exception => InternalServerError(e.getMessage)
  }
}


