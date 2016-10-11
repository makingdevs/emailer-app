import io.vertx.groovy.ext.web.handler.StaticHandler
import io.vertx.groovy.ext.web.Router
import io.vertx.groovy.ext.web.handler.BodyHandler
import io.vertx.core.json.Json
import io.vertx.groovy.core.Vertx
import io.vertx.groovy.ext.mongo.MongoClient

//mongodb config
def config = Vertx.currentContext().config()
def uri = config.mongo_uri

if (uri == null) {
  uri = "mongodb://localhost:27017"
}

def db = config.mongo_db
if (db == null) {
  db = "emailer1"
}

def mongoconfig = [
connection_string:uri,
db_name:db
]

def mongoClient = MongoClient.createShared(vertx, mongoconfig)

//create Server and Router
def server = vertx.createHttpServer()
def router = Router.router(vertx)

router.route().handler(BodyHandler.create())

//router por defecto, al index---------------------------------------------------------------------------------
router.route("/static/*").handler(
  StaticHandler.create().setCachingEnabled(false)
)

//router para tomar el formulario principal y salvarlo en mongoDB----------------------------------------------
router.post("/some").handler { routingContext ->
  // println routingContext.properties
  //recuperando los datos del formulario
  def receiverEmail= routingContext.request().getParam("email_1")
  def senderEmail= routingContext.request().getParam("email_2")
  def submitInput= routingContext.request().getParam("asunto")
  def titleInput= routingContext.request().getParam("title")
  def contentText= routingContext.request().getParam("contenido")

  def email=[
  receiver:receiverEmail,
  sender:senderEmail,
  submit:submitInput,
  title:titleInput,
  content:contentText
  ]

  mongoClient.save("email_storage", email, {id -> })
  //response
  routingContext.response()
  .setStatusCode(201)
  .putHeader("content-type", "text/html; charset=utf-8")
  .end("ok! Email Agregado")
}

//router by show all things
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

//router for show only one json file--------------------------------------------------------------------------
router.post("/one").handler { routingContext ->
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

//router for remove a json file on Mongo----------------------------------------------------------------------
router.post("/remove").handler { routingContext ->
  def emailRemove= routingContext.request().getParam("idEmailRemove")
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

//router for update a json file on MongoDB
router.post("/update").handler { routingContext ->

  //recuperando los datos del formulario
  def emailToUpdate= routingContext.request().getParam("email_id")
  def receiverEmail= routingContext.request().getParam("email_1")
  def senderEmail= routingContext.request().getParam("email_2")
  def submitInput= routingContext.request().getParam("asunto")
  def titleInput= routingContext.request().getParam("title")
  def contentText= routingContext.request().getParam("contenido")


 def query = ["_id":emailToUpdate]
  def update = [
    $set:[
      receiver:receiverEmail,
      sender:senderEmail,
      submit:submitInput,
      title:titleInput,
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

//port
server.requestHandler(router.&accept).listen(8080)

