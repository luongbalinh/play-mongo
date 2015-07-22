package dao.mongo.impl

import javax.inject.{Inject, Singleton}

import dao.mongo.impl.CounterDAOMongo._
import dao.traits.CounterDAO
import reactivemongo.api.DB
import reactivemongo.api.DB
import reactivemongo.bson.BSONDocument
import reactivemongo.bson._
import reactivemongo.core.commands.{FindAndModify, Update}

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class CounterDAOMongo @Inject() (db: DB)(implicit val ctx: ExecutionContext) extends CounterDAO {
  def getNextId(collectionName: String): Future[Long] = {
    db.command(increaseCounterCommand(collectionName)) flatMap extractId
  }

  private def increaseCounterCommand(collectionName: String): FindAndModify = {
    val selector = BSONDocument(collection -> collectionName)
    val modifier = BSONDocument("$inc" -> BSONDocument(counter -> 1))
    FindAndModify(countersCollection, selector, Update(modifier, false))
  }

  private def extractId(res: Option[BSONDocument]): Future[Long] = Future {

    res.get.get(counter).get match {
      case BSONLong(v) => println(s"I am here 1$v"); v
      case BSONInteger(v) => println(s"I am here $v"); v.toLong
      case _ => throw new RuntimeException("unsupported document, value is not a valid number")
    }
  }
}

object CounterDAOMongo {
  val countersCollection = "counters"
  val collection = "collection"
  val counter = "c"
}
