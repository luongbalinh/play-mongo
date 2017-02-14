package services

import models.User

import scala.concurrent.Future

trait CacheService {
  def getUser(key: String): Future[Option[User]]
}

