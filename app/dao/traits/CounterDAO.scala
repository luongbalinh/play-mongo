package dao.traits

import scala.concurrent.Future
import scala.util.Try

trait CounterDAO {
  def getNextId(collection: String): Future[Long]
}
