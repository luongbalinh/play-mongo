package modules

import com.google.inject.AbstractModule
import dao.impl.{CounterDaoMongo, UserDaoMongo}
import dao.{CounterDao, UserDao}
import play.api.{Configuration, Environment}
import services.{UserService, UserServiceImpl}

case class CommonModule(environment: Environment, configuration: Configuration) extends AbstractModule {

  def configure() {
    bindDAOs()
    bindServices()
  }

  private def bindDAOs(): Unit = {
    bind(classOf[CounterDao]).to(classOf[CounterDaoMongo])
    bind(classOf[UserDao]).to(classOf[UserDaoMongo])
  }

  def bindServices(): Unit = {
    bind(classOf[UserService]).to(classOf[UserServiceImpl])
  }
}
