package actors

import javax.inject._

import akka.actor._
import com.google.inject.assistedinject.Assisted
import play.api.Configuration

object ConfiguredChildActor {

  case object GetConfig

  trait Factory {
    def apply(key: String): Actor
  }

}

class ConfiguredChildActor @Inject()(configuration: Configuration, @Assisted key: String) extends Actor {

  import ConfiguredChildActor._

  val config = configuration.getString(key).getOrElse("none")

  def receive = {
    case GetConfig =>
      sender() ! config
  }
}