package helpers

import models.User
import play.api.libs.json.JsSuccess
import helpers.AwaitHelper._

object UserTestFactory {
  val users: List[User] = JsonFileHelper.fileToJson("test/resources/users.json").validate[List[User]] match {
    case JsSuccess(result, _) => result
    case _ => List()
  }
  val user00 = users.head
  val user01 = users(1)
  val user02 = users(2)
}
