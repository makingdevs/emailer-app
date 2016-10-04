import io.vertx.groovy.ext.web.handler.StaticHandler
import io.vertx.groovy.ext.web.Router
import io.vertx.groovy.ext.web.handler.BodyHandler
import io.vertx.core.json.Json

def server = vertx.createHttpServer()
def router = Router.router(vertx)
router.route().handler(BodyHandler.create())

router.route("/static/*").handler(StaticHandler.create())

router.post("/some").handler { routingContext ->
  println routingContext.properties
  routingContext.response()
    .setStatusCode(201)
    .putHeader("content-type", "application/json; charset=utf-8")
    .end(Json.encodePrettily(routingContext.bodyAsJson))
}


server.requestHandler(router.&accept).listen(8080)
