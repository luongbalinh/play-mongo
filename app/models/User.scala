package models

import java.time.ZonedDateTime

import akka.util.ByteString
import play.api.libs.json.Json
import redis.ByteStringFormatter

case class User(
  override val id: Option[Long],
  firstName: String,
  lastName: String,
  age: Int,
  active: Boolean,
  createdDate: Option[ZonedDateTime] = Some(ZonedDateTime.now),
  updatedDate: Option[ZonedDateTime] = Some(ZonedDateTime.now)
) extends IdModelLong[User] {
  override def withNewId(newId: Long): User = this.copy(id = Some(newId))
}

object User {

  import helpers.ZonedDateTimeReadWrite._

  implicit val jsonFormat = Json.format[User]

  implicit val byteStringFormat = new ByteStringFormatter[User] {
    def serialize(data: User): ByteString =
      ByteString(Json.toJson(data).toString)

    def deserialize(bs: ByteString): User =
      Json.fromJson[User](Json.parse(bs.utf8String)).get
  }
}
