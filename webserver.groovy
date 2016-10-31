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

router.route("/static/*").handler(
	 StaticHandler.create().setCachingEnabled(false)
)

router.post("/newEmail").handler { routingContext ->
  //call verticle

    def params = routingContext.request().params()
    def email = [
      subject:params.subjectEmail,
      content:params.contentEmail,
      dateCreated:new Date().time,
      lastUpdate:new Date().time,
      version:1
    ]

    println "Datos: "+email

  vertx.eventBus().publish("com.makingdevs.emailer.new", email)
  /*
		mongoClient.save("email_storage", email, { id ->
      routingContext.response()
      .setStatusCode(201)
      .putHeader("content-type", "text/html; charset=utf-8")
      .end("ok! Email Agregado")
    })
  */
}






/*
router.route("/show").handler({ routingContext ->
  vertx.eventBus().publish("com.makingdevs.emailer.show.total", [ msg: "Mostrando todo", timestamp: new Date().time])
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
  vertx.eventBus().publish("com.makingdevs.emailer.remove", [ msg: "Quitando email", timestamp: new Date().time])
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
  vertx.eventBus().publish("com.makingdevs.emailer.show.email", [ msg: "Mostrando un  email", timestamp: new Date().time])
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

  vertx.eventBus().publish("com.makingdevs.emailer.update", [ msg: "Actualizando  un  email", timestamp: new Date().time])

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

  vertx.eventBus().publish("com.makingdevs.emailer.count", [ msg: "Contando Emails", timestamp: new Date().time])
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
  vertx.eventBus().publish("com.makingdevs.emailer.show.set", [ msg: "Mostrando un set de email", timestamp: new Date().time])

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
vertx.deployVerticle("senderVerticle.groovy")
