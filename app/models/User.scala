package models

import java.time.ZonedDateTime

import play.api.libs.json.Json

case class User(
  override val id: Option[Long],
  firstName: String,
  lastName: String,
  age: Int,
  active: Boolean,
  createdDate: Option[ZonedDateTime] = Some(ZonedDateTime.now),
  updatedDate: Option[ZonedDateTime] = Some(ZonedDateTime.now)
) extends IdModelLong[User]

object User {
  implicit val userFormat = Json.format[User]
}