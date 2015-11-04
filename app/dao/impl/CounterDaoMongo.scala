package dao.impl

import javax.inject.Inject

import dao.CounterDao
import play.modules.reactivemongo.ReactiveMongoApi
import reactivemongo.api.collections.bson.BSONCollection
import reactivemongo.bson._

import scala.concurrent.{ExecutionContext, Future}
import play.api.libs.concurrent.Execution.Implicits._

class CounterDaoMongo @Inject()(reactiveMongoApi: ReactiveMongoApi) extends CounterDao {

  private val counterCollection = reactiveMongoApi.db.collection[BSONCollection](CounterDaoMongo.CollectionName)

  override def nextId(collection: String): Future[Long] = {
    val query = BSONDocument("collection" -> collection)
    val update = BSONDocument("$inc" -> BSONDocument("counter" -> 1))
    findAndModify(query, update)
  }

  override def resetId(collection: String) : Future[Long] = {
    val query = BSONDocument("collection" -> collection)
    val update = BSONDocument("$set" -> BSONDocument("counter" -> 0))
    findAndModify(query, update)
  }

  private def findAndModify(query: BSONDocument, update: BSONDocument): Future[Long] = {
    val modify = counterCollection.BatchCommands.FindAndModifyCommand.Update(
      update,
      fetchNewObject = true,
      upsert = true)

    counterCollection.findAndModify(query, modify) map { res =>
      res.result.get.get("counter") match {
        case Some(BSONLong(id)) => id
        case Some(BSONDouble(id)) => id.toLong
        case Some(BSONInteger(id)) => id.toLong
        case _ => 1L
      }
    }
  }
}

object CounterDaoMongo {
  val CollectionName = "counters"
}