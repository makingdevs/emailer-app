import io.vertx.groovy.ext.web.handler.StaticHandler
import io.vertx.groovy.ext.web.Router
import io.vertx.groovy.ext.web.handler.BodyHandler
import io.vertx.core.json.Json
import io.vertx.groovy.core.Vertx
//for use mail
import io.vertx.ext.mail.StartTLSOptions
import io.vertx.groovy.ext.mail.MailClient

def config = Vertx.currentContext().config()
if(!config.mail || !config.mongo)
  throw new RuntimeException("Cannot run withouit config, check https://github.com/makingdevs/emailer-app/wiki/Emailer-App")

def mailClient = MailClient.createShared(vertx, config.mail)
//ndef mongoClient = MongoClient.createShared(vertx, config.mongo)

//configuracion externalizada
options =[
  "config":config
]

//routers
def server = vertx.createHttpServer()
def router = Router.router(vertx)
router.route().handler(BodyHandler.create())

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
        .putHeader("content-type", "text/html; charset=utf-8")
        .end("Email Agregado Exitosamente.")
      }
      else {
        routingContext.response()
        .setStatusCode(400)
        .putHeader("content-type", "text/html; charset=utf-8")
        .end("Problema para agregar email.")
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
      else {
        routingContext.response()
        .setStatusCode(400)
        .putHeader("content-type", "text/html; charset=utf-8")
        .end("Problema para mostrar todos los documentos.")
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
        .putHeader("content-type", "text/html; charset=utf-8")
        .end("Email Eliminado Exitosamente ${reply.result().body()}")
      }
      else {
        routingContext.response()
        .setStatusCode(400)
        .putHeader("content-type", "text/html; charset=utf-8")
        .end("Problema para eliminar  email.")
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
        .end(Json.encodePrettily(reply.result().body()))
      }
      else {
        routingContext.response()
        .setStatusCode(400)
        .putHeader("content-type", "text/html; charset=utf-8")
        .end("Problemas para contar emails.")
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
        .putHeader("content-type", "text/html; charset=utf-8")
        .end(reply.result().body())
      }
      else {
        routingContext.response()
        .setStatusCode(400)
        .putHeader("content-type", "text/html; charset=utf-8")
        .end("Problema para encontrar el ID")
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
    else {
      routingContext.response()
      .setStatusCode(400)
      .putHeader("content-type", "text/html; charset=utf-8")
      .end("Problema para mostrar el set de emails.")
    }
  })
}

//Actualizar un email
router.post("/update").handler { routingContext ->
  //Obtener datos del update
  def paramsUpdate=routingContext.request().params()
  //mensaje que actualizará los datos, seguir el orden
  def message=[
  paramsUpdate.email_id,//id del email a actualizar
  paramsUpdate.subjectEmail,//subject
  paramsUpdate.contentEmail,//contenido
  paramsUpdate.versionEmail.toInteger()+1,//Version
  new Date().time//fecha nueva en que esta siendo actualizado
  ]

  vertx.eventBus().send("com.makingdevs.emailer.update", message, { reply ->
    if (reply.succeeded()) {
      routingContext.response()
      .setStatusCode(201)
      .putHeader("content-type", "application/json; charset=utf-8")
      .end("Update [ok]")
    }
    else {
      routingContext.response()
      .setStatusCode(400)
      .putHeader("content-type", "text/html; charset=utf-8")
      .end("Problema para actualizar")
    }
  })
}

//Mandar el preview a un email
router.post("/send").handler { routingContext ->

	  def idTemplate= routingContext.request().getParam("email_id")
	  def emailToSend= routingContext.request().getParam("emailPreview")

    def message=[
        idTemplate,//id del email a enviar
        emailToSend//Email receiver
    ]

    vertx.eventBus().send("com.makingdevs.emailer.send", message, { reply ->
      if (reply.succeeded()) {
        routingContext.response()
        .setStatusCode(201)
        .putHeader("content-type", "application/json; charset=utf-8")
      .end("Sended. [ok]")
    }
    else {
      routingContext.response()
      .setStatusCode(400)
      .putHeader("content-type", "text/html; charset=utf-8")
      .end("Problema para enviar")
    }
    })

    /*
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
     */
}

/*
//router por post
router.post("/serviceEmail").handler { routingContext ->

  if(routingContext.getBody().length()){
    def jsonResponse=routingContext.getBodyAsJson()
    //buscar el id del emailer
    def query = ["_id":jsonResponse["id"]]
    mongoClient.find("email_storage", query, { res ->
      if(res.result){
        //recuperando json
        res.result().each { json ->
          if(jsonResponse["params"]){
            //recuperando y armando el correo
            def jsonEmail =groovy.json.JsonOutput.toJson(json)//regresando el json del template
            //Match con Engine-------
            //Obtener el contenido
            def params=jsonResponse["params"]
            //Inicializar el engine
            def engine=new groovy.text.SimpleTemplateEngine()
            //hacer el match, e imprimir el resultado
            def contentEmail=engine.createTemplate(json["content"]).make(params)
            //Armando el correo a enviar con los datos de
            def message = [:]
            message.from = "emailer@app.com"
            message.to = jsonResponse["to"]//de la peticion
            message.subject = jsonResponse["subject"]//de la peticion
            message.html = contentEmail.toString()//del emailer en db
            //Evaluando si hay cc y cco
            if(jsonResponse["cc"]) message.cc = jsonResponse["cc"]//de la peticion
            if(jsonResponse["cco"]) message.bcc = jsonResponse["cco"]//de la peticion
              //Enviando el correo
              mailClient.sendMail(message, { result ->
                if (result.succeeded()) {
                  routingContext.response()
                  .setStatusCode(201)
                  .putHeader("content-type", "text/html; charset=utf-8")
                  .end("Emailer Founded [done] \nEmailer generated [done] \nEmailer sending [in progress...] \nPlease wait a seconds")
                } else {
                  result.cause().printStackTrace()
                }
              })
        }
        else{
        //response
        routingContext.response()
        .setStatusCode(400)
        .putHeader("Content-Type", "text/html; charset=utf-8")
        .end("I can't make the email, please send me params for fill the Emailer template.")
        }
        }//result.each
      }
      else{
        //response
        routingContext.response()
        .setStatusCode(400)
        .putHeader("Content-Type", "text/html; charset=utf-8")
        .end("I can't find the ID, please give me a true ID")
      }
    })
  }else{
    //response
    routingContext.response()
    .setStatusCode(400)
    .putHeader("Content-Type", "text/html; charset=utf-8")
    .end("I can't do my job, please send me something please.")
  }
}*/

server.requestHandler(router.&accept).listen(8080)

//deploy verticles
vertx.deployVerticle("emailerVerticle.groovy", options)
vertx.deployVerticle("senderVerticle.groovy", options)
