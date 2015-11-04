package actors

import javax.inject._

import akka.actor._
import play.api.Configuration

object ConfiguredActor {

  case object GetConfig

}

class ConfiguredActor @Inject()(configuration: Configuration) extends Actor {

  import ConfiguredActor._

  val config = configuration.getString("my.config").getOrElse("none")

  def receive = {
    case GetConfig =>
      sender() ! config
  }
}