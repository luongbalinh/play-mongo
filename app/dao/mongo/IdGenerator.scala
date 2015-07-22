package dao.mongo

import com.google.inject.Singleton
import dao.traits.CounterDAO

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class IdGenerator(collectionName: String, counterDAO: CounterDAO)(implicit val ctx: ExecutionContext) {
  def getNextId: Future[Long] = counterDAO.getNextId(collectionName)
}
