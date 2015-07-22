package modules

import com.google.inject.AbstractModule
import dao.mongo.impl.{CounterDAOMongo, UserDAOMongo}
import dao.traits.{CounterDAO, UserDAO}
import play.api.{Configuration, Environment}
import reactivemongo.api.{DB, MongoDriver}
import services.UserService
import services.impl.UserServiceImpl

/**
 * A DI binding module that defines the bindings between abstract dependencies and their real implementations.
 *
 * @author luongbalinh (Software Engineer - RedMart)
 *         <linhluongba@gmail.com>
 * @since 3/7/15
 */

case class DIBindingModule(environment: Environment, configuration: Configuration) extends AbstractModule {

  //  import play.api.libs.concurrent.Execution.Implicits._

  import scala.concurrent.ExecutionContext.Implicits.global

  def configure() {
    bindMongo()
    bindDAOs()
    bindServices()
  }

  private def bindMongo(): Unit = {
    val hosts = configuration.getStringSeq("mongo.hosts").get.asInstanceOf[List[String]]
    val port = configuration.getInt("mongo.port").get
    val dbName = configuration.getString("mongodb.db").get

    val driver = new MongoDriver
    val connection = driver.connection(hosts)
    val db: DB = connection(dbName)
    bind(classOf[DB]).toInstance(db)
  }

  private def bindDAOs(): Unit = {
    bind(classOf[CounterDAO]).to(classOf[CounterDAOMongo])
    bind(classOf[UserDAO]).to(classOf[UserDAOMongo])
  }

  private def bindServices(): Unit = {
    bind(classOf[UserService]).to(classOf[UserServiceImpl])
  }
}
