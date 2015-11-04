package actors

import javax.inject._

import akka.actor._
import play.api.libs.concurrent.InjectedActorSupport

object ParentActor {

  case class GetChild(key: String)

}

class ParentActor @Inject()(childFactory: ConfiguredChildActor.Factory) extends Actor with InjectedActorSupport {

  import ParentActor._

  def receive = {
    case GetChild(key: String) =>
      val child: ActorRef = injectedChild(childFactory(key), key)
      sender() ! child
  }
}
