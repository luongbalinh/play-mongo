package dao.mongo

import reactivemongo.api.DB
import reactivemongo.bson._
import reactivemongo.core.commands.{FindAndModify, Update}

import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success, Try}

import dao.mongo.IdGenerator._

/**
 * This class generates the next index used as id for a new document of collection <collectionName>.
 *
 * The increased index is updated back to the corresponding document (of collection <countersCollectionName>) of that
 * refers to collection <collectionName>.
 *
 * @author luongbalinh (Software Engineer - RedMart) 
 *         <linhluongba@gmail.com>
 * @since 3/7/15
 */

class IdGenerator(collectionName: String, db: DB)(implicit val ctx: ExecutionContext) {

  def getNextId: Future[Try[Long]] = {
    db.command(findAndModifyCounter()) flatMap extractId
  }

  private def findAndModifyCounter(): FindAndModify = {
    val query = BSONDocument(collectionField -> collectionName)
    val modification = BSONDocument(
      "$inc" -> BSONDocument(currentCountField -> 1)
    )
    FindAndModify(
      countersCollectionName,
      query,
      Update(modification, fetchNewObject = false)
    )
  }

  private def extractId(res: Option[BSONDocument]): Future[Try[Long]] = Future {
    val c = res.get.get(currentCountField).get
    tryToCastAsLong(c) match {
      case Success(v) => Try(v)
      case Failure(e) => Try(c.asInstanceOf[BSONDouble].value.toLong)
    }
  }

  private def tryToCastAsLong(v: BSONValue): Try[Long] =
    Try(v.asInstanceOf[BSONInteger].value)
}

object IdGenerator {
  val countersCollectionName = "counters"

  // the number of current documents of a collection
  val collectionField = "collection"
  val currentCountField = "c"
}
