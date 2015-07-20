package models

import play.api.libs.json.{JsValue, Json, Writes}

case class Error(code: String, msg: String) {

  def withDetails(detail: JsValue): JsValue = {
    Json.obj(
      "code" -> code,
      "msg" -> msg,
      "details" -> detail
    )
  }

  def withDetails(detail: String): JsValue = {
    withDetails(Json.obj("error" -> detail))
  }
}

object Error {

  implicit val errorWrites = new Writes[Error] {
    override def writes(o: Error): JsValue = {
      Json.obj(
        "code" -> o.code,
        "msg" -> o.msg
      )
    }
  }

}
