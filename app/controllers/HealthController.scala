package controllers

import javax.inject.Singleton
import play.api.mvc._

@Singleton
class HealthController extends Controller {
  def check = Action {
    Ok("The application is healthy.")
  }
}

