package dao.exception

sealed abstract class DaoException(message: String,cause: Throwable = null) extends RuntimeException(message, cause)
case class UserDaoException(message: String,cause: Throwable = null) extends DaoException(message, cause)

sealed abstract class ServiceException(message: String,cause: Throwable = null) extends RuntimeException(message, cause)
case class UserServiceException(message: String,cause: Throwable = null) extends ServiceException(message, cause)

