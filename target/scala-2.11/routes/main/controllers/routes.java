
// @GENERATOR:play-routes-compiler
// @SOURCE:/Users/luongbalinh/Documents/repos/templates/play-mongo/conf/routes
// @DATE:Sun Dec 18 19:59:19 SGT 2016

package controllers;

import router.RoutesPrefix;

public class routes {
  
  public static final controllers.ReverseHealthController HealthController = new controllers.ReverseHealthController(RoutesPrefix.byNamePrefix());
  public static final controllers.ReverseUserController UserController = new controllers.ReverseUserController(RoutesPrefix.byNamePrefix());

  public static class javascript {
    
    public static final controllers.javascript.ReverseHealthController HealthController = new controllers.javascript.ReverseHealthController(RoutesPrefix.byNamePrefix());
    public static final controllers.javascript.ReverseUserController UserController = new controllers.javascript.ReverseUserController(RoutesPrefix.byNamePrefix());
  }

}
