
// @GENERATOR:play-routes-compiler
// @SOURCE:/Users/luongbalinh/Documents/repos/templates/play-mongo/conf/routes
// @DATE:Sun Dec 18 19:59:19 SGT 2016

package router

import play.core.routing._
import play.core.routing.HandlerInvokerFactory._
import play.core.j._

import play.api.mvc._

import _root_.controllers.Assets.Asset

class Routes(
  override val errorHandler: play.api.http.HttpErrorHandler, 
  // @LINE:1
  HealthController_1: controllers.HealthController,
  // @LINE:3
  UserController_0: controllers.UserController,
  val prefix: String
) extends GeneratedRouter {

   @javax.inject.Inject()
   def this(errorHandler: play.api.http.HttpErrorHandler,
    // @LINE:1
    HealthController_1: controllers.HealthController,
    // @LINE:3
    UserController_0: controllers.UserController
  ) = this(errorHandler, HealthController_1, UserController_0, "/")

  import ReverseRouteContext.empty

  def withPrefix(prefix: String): Routes = {
    router.RoutesPrefix.setPrefix(prefix)
    new Routes(errorHandler, HealthController_1, UserController_0, prefix)
  }

  private[this] val defaultPrefix: String = {
    if (this.prefix.endsWith("/")) "" else "/"
  }

  def documentation = List(
    ("""GET""", this.prefix + (if(this.prefix.endsWith("/")) "" else "/") + """health""", """controllers.HealthController.check"""),
    ("""GET""", this.prefix + (if(this.prefix.endsWith("/")) "" else "/") + """users/""" + "$" + """id<[^/]+>""", """controllers.UserController.find(id:Long)"""),
    ("""GET""", this.prefix + (if(this.prefix.endsWith("/")) "" else "/") + """users""", """controllers.UserController.findAll"""),
    ("""POST""", this.prefix + (if(this.prefix.endsWith("/")) "" else "/") + """users""", """controllers.UserController.insert"""),
    ("""DELETE""", this.prefix + (if(this.prefix.endsWith("/")) "" else "/") + """users/""" + "$" + """id<[^/]+>""", """controllers.UserController.remove(id:Long)"""),
    ("""PUT""", this.prefix + (if(this.prefix.endsWith("/")) "" else "/") + """users/""" + "$" + """id<[^/]+>""", """controllers.UserController.update(id:Long)"""),
    Nil
  ).foldLeft(List.empty[(String,String,String)]) { (s,e) => e.asInstanceOf[Any] match {
    case r @ (_,_,_) => s :+ r.asInstanceOf[(String,String,String)]
    case l => s ++ l.asInstanceOf[List[(String,String,String)]]
  }}


  // @LINE:1
  private[this] lazy val controllers_HealthController_check0_route = Route("GET",
    PathPattern(List(StaticPart(this.prefix), StaticPart(this.defaultPrefix), StaticPart("health")))
  )
  private[this] lazy val controllers_HealthController_check0_invoker = createInvoker(
    HealthController_1.check,
    HandlerDef(this.getClass.getClassLoader,
      "router",
      "controllers.HealthController",
      "check",
      Nil,
      "GET",
      """""",
      this.prefix + """health"""
    )
  )

  // @LINE:3
  private[this] lazy val controllers_UserController_find1_route = Route("GET",
    PathPattern(List(StaticPart(this.prefix), StaticPart(this.defaultPrefix), StaticPart("users/"), DynamicPart("id", """[^/]+""",true)))
  )
  private[this] lazy val controllers_UserController_find1_invoker = createInvoker(
    UserController_0.find(fakeValue[Long]),
    HandlerDef(this.getClass.getClassLoader,
      "router",
      "controllers.UserController",
      "find",
      Seq(classOf[Long]),
      "GET",
      """""",
      this.prefix + """users/""" + "$" + """id<[^/]+>"""
    )
  )

  // @LINE:4
  private[this] lazy val controllers_UserController_findAll2_route = Route("GET",
    PathPattern(List(StaticPart(this.prefix), StaticPart(this.defaultPrefix), StaticPart("users")))
  )
  private[this] lazy val controllers_UserController_findAll2_invoker = createInvoker(
    UserController_0.findAll,
    HandlerDef(this.getClass.getClassLoader,
      "router",
      "controllers.UserController",
      "findAll",
      Nil,
      "GET",
      """""",
      this.prefix + """users"""
    )
  )

  // @LINE:5
  private[this] lazy val controllers_UserController_insert3_route = Route("POST",
    PathPattern(List(StaticPart(this.prefix), StaticPart(this.defaultPrefix), StaticPart("users")))
  )
  private[this] lazy val controllers_UserController_insert3_invoker = createInvoker(
    UserController_0.insert,
    HandlerDef(this.getClass.getClassLoader,
      "router",
      "controllers.UserController",
      "insert",
      Nil,
      "POST",
      """""",
      this.prefix + """users"""
    )
  )

  // @LINE:6
  private[this] lazy val controllers_UserController_remove4_route = Route("DELETE",
    PathPattern(List(StaticPart(this.prefix), StaticPart(this.defaultPrefix), StaticPart("users/"), DynamicPart("id", """[^/]+""",true)))
  )
  private[this] lazy val controllers_UserController_remove4_invoker = createInvoker(
    UserController_0.remove(fakeValue[Long]),
    HandlerDef(this.getClass.getClassLoader,
      "router",
      "controllers.UserController",
      "remove",
      Seq(classOf[Long]),
      "DELETE",
      """""",
      this.prefix + """users/""" + "$" + """id<[^/]+>"""
    )
  )

  // @LINE:7
  private[this] lazy val controllers_UserController_update5_route = Route("PUT",
    PathPattern(List(StaticPart(this.prefix), StaticPart(this.defaultPrefix), StaticPart("users/"), DynamicPart("id", """[^/]+""",true)))
  )
  private[this] lazy val controllers_UserController_update5_invoker = createInvoker(
    UserController_0.update(fakeValue[Long]),
    HandlerDef(this.getClass.getClassLoader,
      "router",
      "controllers.UserController",
      "update",
      Seq(classOf[Long]),
      "PUT",
      """""",
      this.prefix + """users/""" + "$" + """id<[^/]+>"""
    )
  )


  def routes: PartialFunction[RequestHeader, Handler] = {
  
    // @LINE:1
    case controllers_HealthController_check0_route(params) =>
      call { 
        controllers_HealthController_check0_invoker.call(HealthController_1.check)
      }
  
    // @LINE:3
    case controllers_UserController_find1_route(params) =>
      call(params.fromPath[Long]("id", None)) { (id) =>
        controllers_UserController_find1_invoker.call(UserController_0.find(id))
      }
  
    // @LINE:4
    case controllers_UserController_findAll2_route(params) =>
      call { 
        controllers_UserController_findAll2_invoker.call(UserController_0.findAll)
      }
  
    // @LINE:5
    case controllers_UserController_insert3_route(params) =>
      call { 
        controllers_UserController_insert3_invoker.call(UserController_0.insert)
      }
  
    // @LINE:6
    case controllers_UserController_remove4_route(params) =>
      call(params.fromPath[Long]("id", None)) { (id) =>
        controllers_UserController_remove4_invoker.call(UserController_0.remove(id))
      }
  
    // @LINE:7
    case controllers_UserController_update5_route(params) =>
      call(params.fromPath[Long]("id", None)) { (id) =>
        controllers_UserController_update5_invoker.call(UserController_0.update(id))
      }
  }
}
