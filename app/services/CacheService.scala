package services

import models.User

import scala.concurrent.Future

trait CacheService {
  def getUser(ppRequestToken: String): Future[Option[User]]
}

