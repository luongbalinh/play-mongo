package daos.impl

import javax.inject.{Inject, Singleton}

import daos.CounterDAO
import daos.impl.CounterMongo._
import helpers.DbHelper._
import models.Counter
import play.api.Configuration
import play.api.libs.json.JsObject
import play.api.libs.json.Json._
import play.modules.reactivemongo.json._
import reactivemongo.bson.Macros

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class CounterMongo @Inject()(implicit config: Configuration, exec: ExecutionContext) extends CounterDAO {

  private val collectionFuture = getCollection(counterCollectionName)

  override def nextId(collectionName: String): Future[Long] =
    findAndModify(obj("collection" -> collectionName), obj("$inc" -> obj("counter" -> 1)))

  override def resetId(collectionName: String): Future[Long] =
    findAndModify(obj("collection" -> collectionName), obj("$set" -> obj("counter" -> minCounter)))

  private def findAndModify(query: JsObject, update: JsObject): Future[Long] = collectionFuture flatMap { collection =>
    implicit val reader = Macros.reader[Counter]
    collection.findAndUpdate(query, update, fetchNewObject = true, upsert = true) map (_.result[Counter]) map {
      case Some(x) => x.counter
      case None => (minCounter + 1).toLong
    }
  }
}

object CounterMongo {
  val minCounter = 0
}
