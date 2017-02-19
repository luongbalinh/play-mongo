package controllers

import javax.inject.Singleton

import play.api.mvc._
import com.newrelic.api.agent.{NewRelic, Trace}

@Trace
@Singleton
class HealthController extends Controller {
  def isHealthy = Action {
    NewRelic.addCustomParameter("test_attribute", 4)
    Ok("The application is healthy.")
  }
}

