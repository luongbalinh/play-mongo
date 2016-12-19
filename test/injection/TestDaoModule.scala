package injection

import daos.impl.{CounterMongo, UserMongo}
import daos.{CounterDAO, UserDAO}
import net.codingwell.scalaguice.ScalaModule
import services.{UserService, UserServiceImpl}

case class TestDaoModule() extends ScalaModule {


  override def configure(): Unit = {
    bindDAOs()
    bindServices()
  }

  private def bindDAOs() = {
    bind[CounterDAO].to[CounterMongo]
    bind[UserDAO].to[UserMongo]
  }

  def bindServices(): Unit = {
    bind[UserService].to[UserServiceImpl]
  }
}
