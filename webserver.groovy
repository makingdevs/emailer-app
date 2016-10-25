import io.vertx.groovy.ext.web.handler.StaticHandler
import io.vertx.groovy.ext.web.Router
import io.vertx.groovy.ext.web.handler.BodyHandler
import io.vertx.core.json.Json
import io.vertx.groovy.core.Vertx
import io.vertx.groovy.ext.mongo.MongoClient
//for use mail
import io.vertx.ext.mail.StartTLSOptions
import io.vertx.groovy.ext.mail.MailClient


def config = Vertx.currentContext().config()
if(!config.mail || !config.mongo)
  throw new RuntimeException("Cannot run withouit config, check https://github.com/makingdevs/emailer-app/wiki/Emailer-App")

def mailClient = MailClient.createShared(vertx, config.mail)
def mongoClient = MongoClient.createShared(vertx, config.mongo)

def server = vertx.createHttpServer()
def router = Router.router(vertx)
router.route().handler(BodyHandler.create())

router.route("/static/*").handler(
	 StaticHandler.create().setCachingEnabled(false)
)

router.post("/newEmail").handler { routingContext ->
    def params = routingContext.request().params()
    def email = [
      subject:params.subjectEmail,
      content:params.contentEmail,
      dateCreated:new Date().time,
      lastUpdate:new Date().time,
      version:1
    ]
		mongoClient.save("email_storage", email, { id ->
      routingContext.response()
      .setStatusCode(201)
      .putHeader("content-type", "text/html; charset=utf-8")
      .end("ok! Email Agregado")
    })
}

router.route("/show").handler({ routingContext ->
		def query = [:]
		mongoClient.find("email_storage", query, { res ->
				if (res.succeeded()) {
						routingContext.response()
						.putHeader("content-type", "application/json; charset=utf-8")
						.end(Json.encodePrettily(res.result()))
				} else {
				res.cause().printStackTrace()
				}
				})
	})

router.post("/remove").handler { routingContext ->
	def emailRemove= routingContext.request().getParam("idEmail")
		def query = ["_id":emailRemove]
		mongoClient.remove("email_storage", query, { res ->
				if (res.succeeded()) {
				routingContext.response()
				.setStatusCode(201)
				.putHeader("content-type", "text/html; charset=utf-8")
				.end("Eliminado!")
				} else {
				res.cause().printStackTrace()
				}
				})
}

router.post("/showEmail").handler { routingContext ->
	def idEmail= routingContext.request().getParam("idEmail")
		def query = ["_id":idEmail]
		mongoClient.find("email_storage", query, { res ->
				if (res.succeeded()) {
						res.result().each { json ->
						def jsonEmail =groovy.json.JsonOutput.toJson(json)
						routingContext.response()
						.setStatusCode(201)
						.putHeader("content-type", "text/html; charset=utf-8")
						.end(jsonEmail)
						}
				} else {
				res.cause().printStackTrace()
				}
				})
}

router.post("/update").handler { routingContext ->

    def paramsUpdate=routingContext.request().params()
    def query = ["_id":paramsUpdate.email_id]
    def update = [
		$set:[
          subject:paramsUpdate.subjectEmail,
          content:paramsUpdate.contentEmail,
          version:paramsUpdate.versionEmail.toInteger() +1,
          lastUpdate:new Date().time
	    	]
		]
		mongoClient.update("email_storage", query, update, { res ->
				if (res.succeeded()) {
						routingContext.response()
						.setStatusCode(201)
						.putHeader("content-type", "text/html; charset=utf-8")
						.end("Update!")
				} else {
				res.cause().printStackTrace()
				}
		})
}

router.route("/countTotal").handler({ routingContext ->
  def query = [:]
  mongoClient.count("email_storage",query,{res ->
    if(res.succeeded()){
      routingContext.response()
      .putHeader("content-type", "application/json; charset=utf-8")
      .end(Json.encodePrettily(res.result()))
    }
  })
})

router.post("/showSet").handler { routingContext ->
  def setValue=0
  setValue= routingContext.request().getParam("setValue")
  def query = [:]
  def options=[
      limit:10,
      skip:setValue.toInteger()
      ]
  mongoClient.findWithOptions("email_storage", query, options, { res ->
    if (res.succeeded()) {
      routingContext.response()
      .putHeader("content-type", "application/json; charset=utf-8")
      .end(Json.encodePrettily(res.result().reverse()))
    } else {
      res.cause().printStackTrace()
    }
  })
}

router.post("/send").handler { routingContext ->

	  def idTemplate= routingContext.request().getParam("email_id")
	  def emailToSend= routingContext.request().getParam("emailPreview")
    def query = ["_id":idTemplate]
		mongoClient.find("email_storage", query, { res ->
				if (res.succeeded()) {
						res.result().each { json ->
						def jsonEmail =groovy.json.JsonOutput.toJson(json)//regresando el json del template
            def message = [:]
            message.from = "emailer@app.com"
            message.to = emailToSend
            message.subject = json["subject"]
            //message.cc = "carlogilmar12@gmail.com"
            message.html = json["content"]
            mailClient.sendMail(message, { result ->
              if (result.succeeded()) {
                println(result.result())
                routingContext.response()
                .setStatusCode(201)
                .putHeader("content-type", "text/html; charset=utf-8")
                .end("Enviado")
              } else {
                result.cause().printStackTrace()
              }
            })
          }
				} else {
				res.cause().printStackTrace()
				}
				})
}

//router por post
router.post("/serviceEmail").handler { routingContext ->
  println routingContext.request().delegate.properties
  println routingContext.request().delegate.dump()
  println routingContext?.getBody()
  println routingContext?.getBody()?.length()
  def json = [error:"No body"]
  def statusCode = 400
  if(routingContext?.getBody()?.length()){
    json=routingContext.getBodyAsJson()
    statusCode = 200
  }
  routingContext.response()
  .setStatusCode(statusCode)
  .putHeader("Content-Type", "json/application; charset=utf-8")
  .end(Json.encodePrettily(json))
}

server.requestHandler(router.&accept).listen(8080)

