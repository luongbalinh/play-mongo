package services

import models.User
import play.api.libs.json.JsValue

import scala.concurrent.Future
import scala.util.Try

/**
 * Service for users. This adds another layer above DAO such that application does not know details of DAO.
 *
 * @author luongbalinh (Software Engineer - RedMart)
 *         <linhluongba@gmail.com>
 * @since 3/7/15
 */
trait UserService {

  def create(c: User): Future[Try[User]]

  def read(id: Long): Future[Option[User]]

  def update(id: Long, updates: JsValue): Future[Try[Unit]]

  def delete(id: Long): Future[Try[Unit]]

  def findAll(): Future[List[User]]
}
