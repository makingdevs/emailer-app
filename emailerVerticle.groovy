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
eb.consumer("com.makingdevs.emailer.new", { message ->
    println("I have received a message: ${message.body()}")
    mongoClient.save("email_storage", message.body(), { id ->
      println "añadiendo email, checar en mongo directamente"
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

