package actors

import javax.inject._

import actors.HelloActor.SayHello
import akka.actor._
import akka.util.Timeout
import play.api.mvc._

@Singleton
class ActorApplication @Inject()(system: ActorSystem) extends Controller {

  val helloActor = system.actorOf(HelloActor.props, "hello-actor")

  import akka.pattern.ask
  import play.api.libs.concurrent.Execution.Implicits.defaultContext

  import scala.concurrent.duration._

  // the ask pattern must have a timeout
  implicit val timeout: Timeout = 5.seconds

  def sayHello(name: String) = Action.async {
    (helloActor ? SayHello(name)).mapTo[String].map { message =>
      Ok(message)
    }
  }
}
