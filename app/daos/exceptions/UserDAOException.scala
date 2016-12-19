package daos.exceptions

sealed abstract class DAOException(message: String, cause: Throwable) extends Exception(message, cause)

case class UserDAOException(message: String, cause: Option[Throwable]) extends DAOException(message, cause.orNull)
