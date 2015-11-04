package dao

import scala.concurrent.Future

trait CounterDao {
  def nextId(collection: String): Future[Long]

  def resetId(collection: String): Future[Long]
}
