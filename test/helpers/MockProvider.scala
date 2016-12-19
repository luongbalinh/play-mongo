package helpers

import org.mockito.Mockito._

import scala.collection.mutable

class MockProvider {

  private val mocks: mutable.Map[Class[_], AnyRef] = mutable.Map()

  def getMock[M <: AnyRef](clazz: Class[M], autoMockIfNotFound: Boolean = true): M = {
    if (!mocks.contains(clazz) && autoMockIfNotFound) {
      registerMock(clazz, mock(clazz))
    }
    mocks.get(clazz).get.asInstanceOf[M]
  }

  def registerMock[M <: AnyRef](clazz: Class[M], mock: M): Unit = {
    mocks.put(clazz, mock)
  }
}
