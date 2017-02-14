package injection

import daos.impl._
import daos.{CounterDAO, UserDAO}
import net.codingwell.scalaguice.ScalaModule
import play.api.{Configuration, Environment}
import services.{UserService, UserServiceImpl}

case class Module(environment: Environment, configuration: Configuration) extends ScalaModule {

  def configure() {
    bindDAOs()
    bindServices()
  }

  private def bindDAOs(): Unit = {
    bind[CounterDAO].to[CounterMongo]
    bind[UserDAO].to[UserMongo]
  }

  def bindServices(): Unit = {
    bind[UserService].to[UserServiceImpl]
  }
}
