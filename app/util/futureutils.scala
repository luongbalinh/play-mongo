package util

import scala.concurrent.{ExecutionContext, Future, Promise}
import scala.util.Try

object futureutils {

  implicit class WaterfallFuture[T](f: Future[T])(implicit val ctx: ExecutionContext) {

    def complete[S](next: Try[T] => Future[S]): Future[S] = {
      val p = Promise[S]()

      f onComplete { v =>
        next(v) onComplete p.complete
      }
      p.future
    }
  }

}
