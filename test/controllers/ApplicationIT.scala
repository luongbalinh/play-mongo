package controllers

import org.specs2.mutable._
import play.api.test.Helpers._
import play.api.test._

/**
 * You can mock out a whole application including requests, plugins etc.
 * For more information, consult the wiki.
 */
class ApplicationIT extends Specification {

  "Application" should {

    "send 404 on a bad request" in {
      running(FakeApplication()) {
        route(FakeRequest(GET, "/boum")) must beNone
      }
    }

    "render the index page" in {
      running(FakeApplication()) {
        val home = route(FakeRequest(GET, "/")).get
        status(home) must equalTo(OK)
        contentType(home) must beSome.which(_ == "text/html")
      }
    }

    "be healthy" in {
      running(FakeApplication()) {
        val health = route(FakeRequest(GET, "/health")).get
        status(health) must equalTo(OK)
        contentAsString(health) must equalTo("The application is healthy.")
      }
    }

  }
}