
// @GENERATOR:play-routes-compiler
// @SOURCE:/Users/luongbalinh/Documents/repos/templates/play-mongo/conf/routes
// @DATE:Sun Dec 18 19:59:19 SGT 2016


package router {
  object RoutesPrefix {
    private var _prefix: String = "/"
    def setPrefix(p: String): Unit = {
      _prefix = p
    }
    def prefix: String = _prefix
    val byNamePrefix: Function0[String] = { () => prefix }
  }
}
