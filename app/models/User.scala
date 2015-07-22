package models

import java.time.ZonedDateTime

case class User(override val id: Option[Long] = None,
                firstName: String,
                lastName: String,
                age: Int,
                active: Boolean,
                createdDate: Option[ZonedDateTime] = None,
                updatedDate: Option[ZonedDateTime] = None
                 ) extends IdModel[User] {
  override def withNewId(id: Long): User = this.copy(id = Some(id))
}

object User {

  import play.api.libs.functional.syntax._
  import play.api.libs.json._

  implicit val userReads: Reads[User] = (
    (JsPath \ "id").readNullable[Long] and
      (JsPath \ "firstName").read[String] and
      (JsPath \ "lastName").read[String] and
      (JsPath \ "age").read[Int] and
      (JsPath \ "active").read[Boolean] and
      (JsPath \ "createdDate").readNullable[ZonedDateTime] and
      (JsPath \ "updatedDate").readNullable[ZonedDateTime]
    )(User.apply _)

  implicit val userWrites: Writes[User] = (
    (JsPath \ "id").writeNullable[Long] and
      (JsPath \ "firstName").write[String] and
      (JsPath \ "lastName").write[String] and
      (JsPath \ "age").write[Int] and
      (JsPath \ "active").write[Boolean] and
      (JsPath \ "createdDate").writeNullable[ZonedDateTime] and
      (JsPath \ "updatedDate").writeNullable[ZonedDateTime]
    )(unlift(User.unapply))
}