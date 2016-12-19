package daos

import scala.concurrent.Future

trait CounterDAO {
  def nextId(collection: String): Future[Long]

  def resetId(collection: String): Future[Long]
}
