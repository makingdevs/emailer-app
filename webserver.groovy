import io.vertx.groovy.ext.web.handler.StaticHandler
import io.vertx.groovy.ext.web.Router
import io.vertx.groovy.ext.web.handler.BodyHandler
import io.vertx.core.json.Json
import io.vertx.groovy.core.Vertx
import io.vertx.groovy.ext.mongo.MongoClient

//mongo config
def config = Vertx.currentContext().config()
def uri = config.mongo_uri

	if (uri == null) {
		uri = "mongodb://localhost:27017"
	}

def db = config.mongo_db
if (db == null) {
	db = "emailerDevelop"
}

def mongoconfig = [
	connection_string:uri,
	db_name:db
]

def mongoClient = MongoClient.createShared(vertx, mongoconfig)



//vertx routes
def server = vertx.createHttpServer()
def router = Router.router(vertx)

router.route().handler(BodyHandler.create())

//route by defect->index
router.route("/static/*").handler(
	 StaticHandler.create().setCachingEnabled(false)
)

//route by insert new Email to MongoDb
router.post("/newEmail").handler { routingContext ->
		def receiverEmail= routingContext.request().getParam("receiverEmail")
		def senderEmail= routingContext.request().getParam("senderEmail")
		def submitInput= routingContext.request().getParam("subjectEmail")
		def contentText= routingContext.request().getParam("contentEmail")

		def email=[
		receiver:receiverEmail,
		sender:senderEmail,
		submit:submitInput,
		content:contentText
		]

		mongoClient.save("email_storage", email, {id -> })
		//response
		routingContext.response()
		.setStatusCode(201)
		.putHeader("content-type", "text/html; charset=utf-8")
		.end("ok! Email Agregado")
}

//route for show all items
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

//route for remove one Email
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

//route for show one email
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

//route for receive a petition and update an email
router.post("/update").handler { routingContext ->


	//recuperando los datos del formulario
	  def emailToUpdate= routingContext.request().getParam("email_id")
		def receiverEmail= routingContext.request().getParam("receiverEmail")
		def senderEmail= routingContext.request().getParam("senderEmail")
		def submitInput= routingContext.request().getParam("subjectEmail")
		def contentText= routingContext.request().getParam("contentEmail")

		def query = ["_id":emailToUpdate]
		def update = [
		$set:[
		receiver:receiverEmail,
		sender:senderEmail,
		submit:submitInput,
		content:contentText
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

//router by send the total of documents
router.route("/countTotal").handler({ routingContext ->
  def query = [:]
  //conteo de numeros
  mongoClient.count("email_storage",query,{res ->
    if(res.succeeded()){
      routingContext.response()
      .putHeader("content-type", "application/json; charset=utf-8")
      .end(Json.encodePrettily(res.result()))
    }
  })
})

//router for show only a set of values
router.post("/showSet").handler { routingContext ->
  def setValue=0
  setValue= routingContext.request().getParam("setValue")
  println "El valor del set value es"+setValue
  def query = [:]
  def options=[
  limit:10,
  skip:setValue.toInteger()
  ]
  mongoClient.findWithOptions("email_storage", query, options, { res ->
    if (res.succeeded()) {
      routingContext.response()
      .putHeader("content-type", "application/json; charset=utf-8")
      .end(Json.encodePrettily(res.result()))
    } else {
      res.cause().printStackTrace()
    }
  })
}
server.requestHandler(router.&accept).listen(8080)

