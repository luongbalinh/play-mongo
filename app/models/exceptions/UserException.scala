package models.exceptions

class InvalidJsonException(message: String = null, cause: Throwable = null) extends RuntimeException(message, cause)

class SaveUserException(message: String = null, cause: Throwable = null) extends RuntimeException(message, cause)

class DeleteUserException(message: String = null, cause: Throwable = null) extends RuntimeException(message, cause)

class UpdateUserException(message: String = null, cause: Throwable = null) extends RuntimeException(message, cause)