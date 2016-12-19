package daos.impl

import daos.exceptions.{MongoUpdateDeleteException, MongoObjectNotFoundException}
import play.api.libs.json.Json._
import play.api.libs.json.{JsObject, JsValue}
import play.modules.reactivemongo.json._
import reactivemongo.api.commands.WriteResult
import reactivemongo.play.json.collection.JSONCollection
import scala.concurrent.{ExecutionContext, Future}

object MongoUpdateHelper {
  def update(coll: JSONCollection)(id: Long, value: JsValue)(implicit ctx: ExecutionContext): Future[Unit] =
    coll.update(obj("id" -> id), obj("$set" -> value)) map
      throwUpdateRemoveException(s"Failed to update object with id = $id")

  def updateSubDocument(coll: JSONCollection)(id: Long, field: String, value: JsValue)(implicit ctx: ExecutionContext): Future[Unit] =
    coll.update(obj("id" -> id), set(buildSubDocumentUpdate(field, value))) map
      throwUpdateRemoveException(s"Failed to update object with id = $id")

  def updateField(coll: JSONCollection)(id: Long, value: JsValue)(implicit ctx: ExecutionContext): Future[Unit] =
    coll.update(obj("id" -> id), set(value)) map
      throwUpdateRemoveException(s"Failed to update object with id = $id")

  def remove(coll: JSONCollection)(id: Long)(implicit ctx: ExecutionContext): Future[Unit] =
    coll.remove(obj("id" -> id)) map throwUpdateRemoveException(s"Failed to remove object with id = $id")

  def throwUpdateRemoveException(message: String): WriteResult => Unit = { result =>
    if (result.ok) {
      if (result.n > 0) () else throw MongoObjectNotFoundException(s"No object matches the query.")
    } else {
      throw MongoUpdateDeleteException(s"$message. ${result.writeErrors.mkString}")
    }
  }

  private def set(update: JsValue): JsObject = obj("$set" -> update)

  private def buildSubDocumentUpdate(field: String, js: JsValue): JsObject = buildUpdate(js)(field)

  private def buildUpdate(js: JsValue)(fieldName: String): JsObject =
    js.asInstanceOf[JsObject].fieldSet.foldLeft(obj()) { (obj, field) =>
      val (key, value) = field
      obj + (s"$fieldName.$key" -> value)
    }
}
