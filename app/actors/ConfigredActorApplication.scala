package actors

import javax.inject._

import actors.ConfiguredActor._
import akka.actor._
import akka.pattern.ask
import akka.util.Timeout
import play.api.mvc._

import scala.concurrent.ExecutionContext
import scala.concurrent.duration._

@Singleton
class ConfigredActorApplication @Inject()(@Named("configured-actor") configuredActor: ActorRef)
    (implicit ec: ExecutionContext) extends Controller {

  implicit val timeout: Timeout = 5.seconds

  def getConfig = Action.async {
    (configuredActor ? GetConfig).mapTo[String].map { message =>
      Ok(message)
    }
  }
}