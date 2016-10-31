import io.vertx.core.json.Json
//mongo
import io.vertx.groovy.core.Vertx
import io.vertx.groovy.ext.mongo.MongoClient
//Mongo DB
def config = Vertx.currentContext().config()
def mongoClient = MongoClient.createShared(vertx, config.mongo)

//Event Bus Born
def eb = vertx.eventBus()

//Handlers for Event bus

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

eb.consumer("com.makingdevs.emailer.show.total", { message ->
    println("I have received a message: ${message.body()}")
})

eb.consumer("com.makingdevs.emailer.remove", { message ->
    println("I have received a message: ${message.body()}")
})

eb.consumer("com.makingdevs.emailer.show.email", { message ->
    println("I have received a message: ${message.body()}")
})

eb.consumer("com.makingdevs.emailer.update", { message ->
    println("I have received a message: ${message.body()}")
})

eb.consumer("com.makingdevs.emailer.count", { message ->
    println("I have received a message: ${message.body()}")
})

eb.consumer("com.makingdevs.emailer.show.set", { message ->
    println("I have received a message: ${message.body()}")
})

