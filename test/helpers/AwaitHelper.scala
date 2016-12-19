package helpers

import scala.concurrent.duration._
import scala.concurrent.{Await, Future}
import scala.language.postfixOps

object AwaitHelper {
  private val longTime = Long.MaxValue nanos

  def awaitResult[T](f: Future[T]): T = {
    Await.result(f, longTime)
  }
}
