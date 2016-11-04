import io.vertx.core.json.Json
import io.vertx.groovy.core.Vertx
import io.vertx.groovy.ext.mongo.MongoClient

//Configuration of Mongo
def config = Vertx.currentContext().config()
def mongoClient = MongoClient.createShared(vertx, config.mongo)

//Event Bus Born
def eb = vertx.eventBus()

//Add new Email
eb.consumer("com.makingdevs.emailer.new", { message ->
    mongoClient.save("email_storage", message.body(), { id ->
      if (id.succeeded()) {
        message.reply("[ok]")
      } else {
        res.cause().printStackTrace()
      }
    })
})

//Show all emails
eb.consumer("com.makingdevs.emailer.show.total", { message ->
    def query=[:]
    mongoClient.find("email_storage", query, { res ->
      if (res.succeeded()) {
        message.reply(res.result())
      } else {
        res.cause().printStackTrace()
      }
    })
})

//Remove Email
eb.consumer("com.makingdevs.emailer.remove", { message ->
  mongoClient.remove("email_storage", message.body(), { res ->
    if (res.succeeded()) {
      message.reply("[ok]")
    } else {
      res.cause().printStackTrace()
    }
  })
})

//Count total of emails
eb.consumer("com.makingdevs.emailer.count", { message ->
  def query=[:]
  mongoClient.count("email_storage",query,{res ->
    if(res.succeeded()){
      message.reply(res.result())
    }
  })
})

//Show one Email
eb.consumer("com.makingdevs.emailer.show.one", { message ->
  mongoClient.find("email_storage", message.body(), { res ->
    if (res.succeeded()) {
      res.result().each { json ->
        def jsonEmail =groovy.json.JsonOutput.toJson(json)
        message.reply(jsonEmail)
      }
    } else {
      res.cause().printStackTrace()
    }
  })
})

//Show set of emails
eb.consumer("com.makingdevs.emailer.show.set", { message ->
  def query=[:]
  mongoClient.findWithOptions("email_storage", query, message.body(), { res ->
    if (res.succeeded()) {
      message.reply(res.result().reverse())
    } else {
      res.cause().printStackTrace()
    }
  })
})

//Update an email
eb.consumer("com.makingdevs.emailer.update", { message ->
  //Armando variables con los datos del mensaje
  def update=[
    $set:[
       subject:message.body().subject,
       content:message.body().content,
       version:message.body().version,
       lastUpdate:message.body().update,
    ]
  ]
  def query=["_id":message.body().id]


  //Haciendo el update
  mongoClient.update("email_storage", query, update, { res ->
    if (res.succeeded()) {
      message.reply("[ok]")
    } else {
      res.cause().printStackTrace()
    }
  })
})

//Send Email Preview
eb.consumer("com.makingdevs.emailer.send", { message ->
  def query=["_id": message.body().id]
  def receiver=message.body().email
  //Buscando email
  mongoClient.find("email_storage", query, { res ->
    if (res.succeeded()) {
      res.result().each { json ->
        def jsonEmail =groovy.json.JsonOutput.toJson(json)
        vertx.eventBus().publish("com.makingdevs.emailer.send.email",
        [id:json["_id"],to:receiver, subject:json["subject"], content:json["content"]])
      }
    } else {
      res.cause().printStackTrace()
    }
  })
})

//Service
eb.consumer("com.makingdevs.emailer.service", { message ->
  def query=["_id":message.body().id]
  def email=[
//  content:json["content"],
  id:message.body().id,
  subject:message.body().subject,
  to: message.body().to,
  params: message.body().params
  ]

  //Validaciones
  if(message.body().cc) email.cc=message.body().cc
  if(message.body().cco) email.cco=message.body().cco

  //Buscar el id
  mongoClient.find("email_storage", query, { res ->
    if (res.succeeded()) {
      res.result().each { json ->
        def jsonEmail =groovy.json.JsonOutput.toJson(json)
        //Agregar el content
        email.content=json["content"]
        //Enviar los datos del email encontrado, más los datos del servicio
        vertx.eventBus().publish("com.makingdevs.emailer.send.service",email)
      }
    } else {
      res.cause().printStackTrace()
    }
  })
})


