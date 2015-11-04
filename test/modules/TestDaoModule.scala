package modules

import com.google.inject.AbstractModule
import dao.impl.{CounterDaoMongo, UserDaoMongo}
import dao.{CounterDao, UserDao}
import services.{UserService, UserServiceImpl}

case class TestDaoModule() extends AbstractModule {


  override def configure(): Unit = {
    bindDAOs()
    bindServices()
  }

  private def bindDAOs() = {
    bind(classOf[CounterDao]).to(classOf[CounterDaoMongo])
    bind(classOf[UserDao]).to(classOf[UserDaoMongo])
  }

  def bindServices(): Unit = {
    bind(classOf[UserService]).to(classOf[UserServiceImpl])
  }
}
