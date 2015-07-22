package dao.traits

import _root_.util.futureutils.WaterfallFuture
import dao.mongo.IdGenerator
import dao.traits.BasicCrudDAO.{RemoveFailed, SavingFailed}
import models.IdModel
import play.api.libs.json._
import play.modules.reactivemongo.json.collection.JSONCollection
import reactivemongo.api._
import reactivemongo.api.commands.WriteResult

import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success, Try}

abstract class BasicCrudDAO[T <: IdModel[T]] private[dao](collectionName: String, db: DB, counterDAO: CounterDAO)
                                                         (implicit override val ctx: ExecutionContext, implicit val
                                                         reader: Reads[T], implicit val writer: Writes[T]) extends
IdGenerator(collectionName, counterDAO) with CrudDAO[T] {

  val collection = db[JSONCollection](collectionName)

  override def create(t: T): Future[Try[Long]] = t.id match {

    case Some(id) => persist(t);
    case None => getNextId andThen saveEntityWithNewId(t) complete(tryId => Future(tryId))
  }

  override def read(id: Long): Future[Option[T]] = {
    collection.find(Map("id" -> id)).one[T]
  }

  override def update(id: Long, updates: JsValue): Future[Try[Unit]] = {
    collection.update(Map("id" -> id), Map("$set" -> updates)) flatMap asTryUnit
  }

  override def delete(id: Long): Future[Try[Unit]] = {
    collection.remove(Map("id" -> id)) flatMap asTryUnit
  }

  override def findAll(): Future[List[T]] = {
    implicit val myWrites: OWrites[Unit] = new OWrites[Unit] {
      def writes(a: Unit) = Json.obj()
    }
    collection.find(()).cursor[T].collect[List]()
  }

  private def saveEntityWithNewId(t: T)(): PartialFunction[Try[Long], Future[Try[Long]]] = {
    case Success(id) => persist(t.withNewId(id))
    case Failure(e) => Future {
      Failure(e)
    }
  }

  private def persist(t: T) = collection.save(t) map {
    res => if (res.ok) {
      Success(t.id.get)
    } else {
      Failure(SavingFailed(res.errmsg.get))
    }
  }

  private def asTryUnit(writeResult: WriteResult): Future[Try[Unit]] = Future {
    if (writeResult.ok) {
      Success(())
    } else {
      Failure(RemoveFailed(writeResult.errmsg.get))
    }
  }
}

object BasicCrudDAO {

  case class SavingFailed(msg: String = "") extends RuntimeException(msg)

  case class RemoveFailed(msg: String = "") extends RuntimeException(msg)

}


