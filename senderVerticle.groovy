
def eb = vertx.eventBus()

eb.consumer("com.makingdevs.emailer.send.email", { message ->
    println "Hola soy el consumer del SenderVerticle ${message.body()}"
})

