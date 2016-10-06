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

router.route("/static/*").handler(StaticHandler.create())

router.post("/some").handler { routingContext ->
  // println routingContext.properties
  //recuperando los datos del formulario
  def a= routingContext.request().getParam("email_1")
  def b= routingContext.request().getParam("email_2")
  def c= routingContext.request().getParam("asunto")
  def d= routingContext.request().getParam("title")
  def e= routingContext.request().getParam("contenido")

  println a
  println b
  println c
  println d
  println e

  //json for insert into Mongo
  def email=[
  receiver:a,
  sender:b,
  submit:c,
  title:d,
  content:e
  ]

  //catch mongo reader
  def f

  mongoClient.save("email_storage", email, { id ->
    f=id.result()
      })

  //response
  routingContext.response()
  .setStatusCode(201)
  .putHeader("content-type", "text/html; charset=utf-8")
  .end("ok!"+e)
}

//router by show new things
  router.route("/show").handler({ routingContext ->
    List reader=[]
    def query = [:]
    mongoClient.find("email_storage", query, { res ->
      if (res.succeeded()) {
        res.result().each { json ->
          //println(groovy.json.JsonOutput.toJson(json))
          reader.add(groovy.json.JsonOutput.toJson(json))
        }
         def response = routingContext.response()
        response.putHeader("content-type", "text/plain")
        response.end("Holi World from Vert.x-Web!"+reader)

     } else {
        res.cause().printStackTrace()
      }
    })
  })

//router for show only one json file--------------------------------------------------------------------------
router.post("/one").handler { routingContext ->
  // println routingContext.properties
  //recuperando los datos del formulario
  def a= routingContext.request().getParam("idEmail")
  println a

  def query = ["_id":a]

    mongoClient.find("email_storage", query, { res ->
      if (res.succeeded()) {
        res.result().each { json ->
          def b =groovy.json.JsonOutput.toJson(json)
          //response
          routingContext.response()
          .setStatusCode(201)
          .putHeader("content-type", "text/html; charset=utf-8")
          .end("ok!"+b)

        }
     } else {
        res.cause().printStackTrace()
      }
    })

}

//router for remove a json file on Mongo----------------------------------------------------------------------
router.post("/remove").handler { routingContext ->
  // println routingContext.properties
  //recuperando los datos del formulario
  def r= routingContext.request().getParam("idEmailRemove")
  println r

  def query = ["_id":r]

    mongoClient.remove("email_storage", query, { res ->
      if (res.succeeded()) {
        //res.result().each { json ->
         // def b =groovy.json.JsonOutput.toJson(json)
          //response
          routingContext.response()
          .setStatusCode(201)
          .putHeader("content-type", "text/html; charset=utf-8")
          .end("Eliminado!")

        //}
     } else {
        res.cause().printStackTrace()
      }
    })

}


//router by show new things
  router.route("/show").handler({ routingContext ->
    List reader=[]
    def query = [:]
    mongoClient.find("email_storage", query, { res ->
      if (res.succeeded()) {
        res.result().each { json ->
          //println(groovy.json.JsonOutput.toJson(json))
          reader.add(groovy.json.JsonOutput.toJson(json))
        }
         def response = routingContext.response()
        response.putHeader("content-type", "text/plain")
        response.end("Holi World from Vert.x-Web!"+reader)

     } else {
        res.cause().printStackTrace()
      }
    })
  })




//port
server.requestHandler(router.&accept).listen(8080)

