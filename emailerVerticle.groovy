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


