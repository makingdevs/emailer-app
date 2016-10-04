import io.vertx.groovy.ext.web.handler.StaticHandler

def server = vertx.createHttpServer()

router.route("/static/*").handler(StaticHandler.create())


server.listen(8080)
