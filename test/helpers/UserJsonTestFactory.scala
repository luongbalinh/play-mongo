package helpers

import play.api.libs.json.{JsSuccess, JsValue}
import helpers.JsonFileHelper._

object UserJsonTestFactory {
  val userJsonList: List[JsValue] = fileToJson("test/resources/users.json").validate[List[JsValue]] match {
    case JsSuccess(result, _) => result
    case _ => List()
  }

  val userJson00 = userJsonList.head
  val userJson01 = userJsonList(1)
  val userJson02 = userJsonList(2)
}
