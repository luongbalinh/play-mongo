package controllers

import play.api.Logger
import play.api.http.HttpErrorHandler
import play.api.mvc.Results._
import play.api.mvc._

import scala.concurrent._

class ErrorHandler extends HttpErrorHandler {
  private val logger = Logger(this.getClass)

  def onClientError(request: RequestHeader, statusCode: Int, message: String): Future[Result] =
    if (statusCode == play.api.http.Status.NOT_FOUND)
      Future.successful(Status(statusCode)(s"No handler found for ${request.uri}"))
    else
      Future.successful(Status(statusCode)(s"A client error occurred: request=${request.toString()}, message = $message"))

  def onServerError(request: RequestHeader, exception: Throwable): Future[Result] = {
    logger.error(s"Failed to execute request ${request.toString()}", exception)
    Future.successful(InternalServerError("A server error occurred: " + exception.getMessage))
  }
}
