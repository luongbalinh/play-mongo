package utils

import play.api.libs.json.{JsValue, Json}

import scala.reflect.io.File

object JsonFileHelper {

  def fileToJson(fileName: String): JsValue = {
    Json.parse(getFileAsString(fileName))
  }

  private def getFileAsString(fileName: String): String = {
    File(Thread.currentThread().getContextClassLoader.getResource(fileName).getPath).slurp()
  }
}
