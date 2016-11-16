import io.vertx.groovy.ext.web.handler.StaticHandler
import io.vertx.groovy.ext.web.Router
import io.vertx.groovy.ext.web.handler.BodyHandler
import io.vertx.core.json.Json
import io.vertx.groovy.core.Vertx
import io.vertx.groovy.ext.web.handler.sockjs.SockJSHandler
import io.vertx.groovy.ext.web.handler.StaticHandler

def config = Vertx.currentContext().config()
if(!config.mail || !config.mongo)
  throw new RuntimeException("Cannot run withouit config, check https://github.com/makingdevs/emailer-app/wiki/Emailer-App")

  //configuracion externalizada
 options =[
  "config":config
  ]

  //Configuración para hacer el worker
  def senderOptions=options
  senderOptions.worker=true

  //Config for permissons
  def opts = [
  outboundPermitteds:[
      [
       address:"com.makingdevs.email.success"
      ]
    ]
  ]


  //routers
  def server = vertx.createHttpServer()
  def router = Router.router(vertx)
  router.route().handler(BodyHandler.create())

 // Create the event bus bridge and add it to the router.
  def ebHandler = SockJSHandler.create(vertx).bridge(opts)
  router.route("/eventbus/*").handler(ebHandler)


  //Route to Index
  router.route("/static/*").handler(
    StaticHandler.create().setCachingEnabled(false)
  )

  //Add new Email
  router.post("/newEmail").handler { routingContext ->
    def params = routingContext.request().params()
    def email = [
    subject:params.subjectEmail,
  content:params.contentEmail,
  dateCreated:new Date().time,
  lastUpdate:new Date().time,
  version:1
  ]
  vertx.eventBus().send("com.makingdevs.emailer.new", email, { reply ->
    if (reply.succeeded()) {
      routingContext.response()
      .setStatusCode(201)
      .putHeader("content-type", "application/json; charset=utf-8")
      .end(Json.encodePrettily([msg:"Email Agregado Correctamente."]))
    }
  })
}

//Show all emails
router.route("/show").handler({ routingContext ->
  vertx.eventBus().send("com.makingdevs.emailer.show.total", "Show me", { reply ->
    if (reply.succeeded()) {
      routingContext.response()
      .setStatusCode(201)
      .putHeader("content-type", "application/json; charset=utf-8")
      .end(Json.encodePrettily(reply.result().body()))
    }

  })
})

//Remove an email
router.post("/remove").handler { routingContext ->
  def emailRemove= routingContext.request().getParam("idEmail")
  def query = ["_id":emailRemove]
  vertx.eventBus().send("com.makingdevs.emailer.remove", query, { reply ->
    if (reply.succeeded()) {
      routingContext.response()
      .setStatusCode(201)
      .putHeader("content-type", "application/json; charset=utf-8")
      .end(Json.encodePrettily([msg:"Email Eliminado Correctamente ${reply.result().body()}"]))
    }
  })
}

//Count all of emails
router.route("/countTotal").handler({ routingContext ->
  vertx.eventBus().send("com.makingdevs.emailer.count", "Dame el conteo", { reply ->
    if (reply.succeeded()) {
      routingContext.response()
      .setStatusCode(201)
      .putHeader("content-type", "application/json; charset=utf-8")
      .end(Json.encodePrettily([count:reply.result().body()]))
    }
  })
})

//Show one Email
router.post("/showEmail").handler { routingContext ->
  def emailId= routingContext.request().getParam("idEmail")
  def query = ["_id":emailId]
  vertx.eventBus().send("com.makingdevs.emailer.show.one", query, { reply ->
    if (reply.succeeded()) {
      routingContext.response()
      .setStatusCode(201)
      .putHeader("content-type", "application/json; charset=utf-8")
      .end(Json.encodePrettily(reply.result().body()))
    }
  })
}

//Show set of emails
router.post("/showSet").handler { routingContext ->

  def setValue=0
  setValue= routingContext.request().getParam("setValue")
  def options=[
  limit:10,
  skip:setValue.toInteger()
  ]
  //comunicate with verticle
  vertx.eventBus().send("com.makingdevs.emailer.show.set", options, { reply ->
    if (reply.succeeded()) {
      routingContext.response()
      .setStatusCode(201)
      .putHeader("content-type", "application/json; charset=utf-8")
      .end(Json.encodePrettily(reply.result().body()))
    }
  })
}

//Actualizar un email
router.post("/update").handler { routingContext ->
  //Obtener datos del update
  def paramsUpdate=routingContext.request().params()
  //mensaje que actualizará los datos, seguir el orden
  def message=[
  id:paramsUpdate.email_id,//id del email a actualizar
  subject:paramsUpdate.subjectEmail,//subject
  content:paramsUpdate.contentEmail,//contenido
  version:paramsUpdate.versionEmail.toInteger()+1,//Version
  update:new Date().time//fecha nueva en que esta siendo actualizado
  ]

  vertx.eventBus().send("com.makingdevs.emailer.update", message, { reply ->
    if (reply.succeeded()) {
      routingContext.response()
      .setStatusCode(201)
      .putHeader("content-type", "application/json; charset=utf-8")
      .end(Json.encodePrettily([msg:"Solicitud Enviada Correctamente."]))
    }
  })
}

//Mandar el preview a un email
router.post("/send").handler { routingContext ->

  def idTemplate= routingContext.request().getParam("email_id")
  def emailToSend= routingContext.request().getParam("emailPreview")
  def message=[
  id:idTemplate,
  email:emailToSend
  ]
  vertx.eventBus().send("com.makingdevs.emailer.send", message)

  routingContext.response()
  .setStatusCode(201)
  .putHeader("content-type", "application/json; charset=utf-8")
  .end(Json.encodePrettily([msg:"Solicitud Enviada Correctamente."]))
}

//Route para el servicio Web
router.post("/serviceEmail").handler { routingContext ->

  if(routingContext.getBody().length()){
    def jsonResponse=routingContext.getBodyAsJson()
  vertx.eventBus().send("com.makingdevs.emailer.check", jsonResponse){ reply ->
    if(reply.result.body() == "ok" ){
      vertx.eventBus().send("com.makingdevs.emailer.service", jsonResponse)
      routingContext.response()
      .setStatusCode(201)
      .putHeader("content-type", "application/json; charset=utf-8")
      .end(Json.encodePrettily([
        message:"Solicitud enviada correctamente.",
      ]))
    }else{
      routingContext.response()
      .setStatusCode(400)
      .putHeader("content-type", "application/json; charset=utf-8")
      .end(Json.encodePrettily([
        message:"I can't do my job. You have the follow errors",
        errors : reply.result().body()
      ]))
    }
  }
  }else{
    //response
    routingContext.response()
    .setStatusCode(400)
    .putHeader("content-type", "application/json; charset=utf-8")
    .end(Json.encodePrettily([
      message:"I can't do my job, please send me something please."
    ]))
  }
}

server.requestHandler(router.&accept).listen(8080)

//deploy verticles
vertx.deployVerticle("emailerVerticle.groovy", options)
vertx.deployVerticle("senderVerticle.groovy", senderOptions)
vertx.deployVerticle("helperVerticle.groovy")
