package filters

import play.api.http.HeaderNames
import play.api.mvc.{Filter, RequestHeader, Result}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

/**
 * A Filter that implements Cross-Origin Resource Sharing (CORS).
 *
 * This class should be available in Play 3.0
 *
 * It can be configured to...
 *
 * - filter paths by a whitelist of path prefixes
 * - allow only requests with origins from a whitelist (by default all origins are allowed)
 * - allow only HTTP methods from a whitelist for preflight requests (by default all methods are allowed)
 * - allow only HTTP headers from a whitelist for preflight requests (by default all headers are allowed)
 * - set custom HTTP headers to be exposed in the response (by default no headers are exposed)
 * - disable/enable support for credentials (by default credentials support is enabled)
 * - set how long (in seconds) the results of a preflight request can be cached in a preflight result cache (by
 * default 3600 seconds, 1 hour)
 */

object CorsFilter extends Filter {

  def apply(nextFilter: (RequestHeader) => Future[Result])(requestHeader: RequestHeader): Future[Result] = {

    nextFilter(requestHeader).map { result =>
      result.withHeaders(HeaderNames.ACCESS_CONTROL_ALLOW_ORIGIN -> "*",
        HeaderNames.ALLOW -> "*",
        HeaderNames.ACCESS_CONTROL_ALLOW_METHODS -> "POST, GET, PUT, DELETE, OPTIONS",
        HeaderNames.ACCESS_CONTROL_ALLOW_HEADERS ->
            "Origin, X-Requested-With, Content-Type, Accept, Authorization, Referer, User-Agent"
      )
    }
  }
}