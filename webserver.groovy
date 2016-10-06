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
//    println("Inserted id: ${id.result()}")
    f=id.result()
    //println "Id here:"+f
    /*
    mongoClient.find("email_storage",[
    _id:"$f"
    ],{ res ->
      println "Lectura: ${res.result()[0].sender}"
    })

    mongoClient.find("products", [
    itemId:"12346"
    ], { res ->
    println("Name is ${res.result()[0].name}")

    mongoClient.remove("products", [
    itemId:"12345"
    ], { rs ->
    if (rs.succeeded()) {
    println("Product removed ")
    }
    })

    })
     */
  })

  //response
  routingContext.response()
  .setStatusCode(201)
  .putHeader("content-type", "text/html; charset=utf-8")
  .end("ok!"+e)
}

//router by show new things
router.route("/show").handler({ routingContext ->
    def response = routingContext.response()
      response.putHeader("content-type", "text/plain")
        response.end("Holi World from Vert.x-Web!")
})

//port
server.requestHandler(router.&accept).listen(8000)

