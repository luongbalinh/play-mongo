package dao.mongo.impl

import javax.inject.{Inject,Singleton}

import dao.UserDAO
import dao.mongo.BasicCrudDAO
import models.User
import reactivemongo.api.DB
import scala.concurrent.ExecutionContext
import dao.mongo.impl.UserDAOMongo._

@Singleton
class UserDAOMongo @Inject()(db: DB)(implicit val eCtx: ExecutionContext)
    extends BasicCrudDAO[User](collectionName, db)(eCtx, User.userReads, User.userWrites) with UserDAO {
}

object UserDAOMongo {
  val collectionName = "users"
}
