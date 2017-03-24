package controllers

import javax.inject.Singleton

import play.api.mvc._
import com.newrelic.api.agent.{NewRelic, Trace}

@Singleton
class HealthController extends Controller {
  @Trace(dispatcher = true)
  def isHealthy = Action {
    NewRelic.addCustomParameter("test_attribute", 4)
    NewRelic.setTransactionName(null, "/store")
    // prevent the method from contributing to the Apdex score
    NewRelic.ignoreApdex()

    // prepend Custom/ to the metric name to that it appears in the custom Dashboard
    NewRelic.incrementCounter("Custom/healthCalledCount")
    Ok("The application is healthy.")
  }
}

