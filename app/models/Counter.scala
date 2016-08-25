package models

import play.api.libs.json.Json

case class Counter(collection: String, counter: Long = 0L)

object Counter {
  implicit val counterFormat = Json.format[Counter]
}
