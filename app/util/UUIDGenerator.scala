package util

import java.util.UUID

import javax.inject.Singleton

/**
 * A type declaring the interface that will be injectable.
 */
abstract class UUIDGenerator() {
  def generate: UUID
}

/**
 * A simple implementation of UUIDGenerator that we will inject.
 */
@Singleton
class SimpleUUIDGenerator extends UUIDGenerator {
  def generate: UUID = UUID.randomUUID()
}