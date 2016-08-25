package utils

import java.time.ZonedDateTime

import play.api.Logger
import play.api.libs.json.{JsArray, JsValue, Json, Reads}
import reactivemongo.api.DB
import reactivemongo.api.collections.bson.BSONCollection
import reactivemongo.bson.BSONDocument
import utils.AwaitHelper._

import scala.concurrent.ExecutionContext
import scala.reflect.io.File
import scala.util.{Failure, Success, Try}

object JsonFileHelper {
  private val logger = Logger(this.getClass)

  def insertResource(db: DB, collName: String, fileName: String)(implicit ctx: ExecutionContext): Unit = {
    import play.modules.reactivemongo.json.toBSON

    val coll = db[BSONCollection](collName)
    val jsArray: JsArray = fileToJson(fileName).asInstanceOf[JsArray]
    val bsonArray: Stream[BSONDocument] = jsArray.value.toStream.map(js => toBSON(js).get.asInstanceOf[BSONDocument])

    awaitResult(coll.bulkInsert(bsonArray, ordered = true))
  }

  def clearResource(db: DB, collName: String)(implicit ctx: ExecutionContext): Unit =
    db.collection[BSONCollection](collName).drop()

  def fileToJson(fileName: String): JsValue =
    Json.parse(getFileAsString(fileName))

  def fileToClass[T](fileName: String)(implicit reads: Reads[T]): T =
    fileToJson(fileName).validate[T].get

  def getFileAsString(fileName: String): String =
    File(Thread.currentThread().getContextClassLoader.getResource(fileName).getPath).slurp()

  def getFile(fileName: String): java.io.File =
    new java.io.File(Thread.currentThread().getContextClassLoader.getResource(fileName).getPath)

  def retry[T](n: Int)(fn: => T): Try[T] = Try(fn) match {
    case Success(t) => Success(t)
    case _ if n > 0 => retry(n - 1) {
      logger.info("execution failed, retrying in 3 seconds")
      val threeSeconds = 3000
      Thread.sleep(threeSeconds)
      fn
    }
    case Failure(e) => Failure(e)
  }

  implicit val zonedDateTimeOrdering = new Ordering[ZonedDateTime] {
    override def compare(x: ZonedDateTime, y: ZonedDateTime): Int =
      (x.toInstant.toEpochMilli - y.toInstant.toEpochMilli).toInt
  }
}
