package utils

import play.api.libs.json.{JsSuccess, JsValue}

object UserJsonTestFactory {
  val userJsonList: List[JsValue] = JsonFileHelper.fileToJson("users.json").validate[List[JsValue]] match {
    case JsSuccess(result, _) => result
    case _ => List()
  }

  val userJson00 = userJsonList.head
  val userJson01 = userJsonList(1)
  val userJson02 = userJsonList(1)
}
