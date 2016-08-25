package dao.exception

sealed abstract class DaoException(message: String, cause: Throwable) extends RuntimeException(message, cause)

case class UserDaoException(message: String, cause: Throwable = null) extends DaoException(message, cause)
