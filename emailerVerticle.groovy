def eb = vertx.eventBus()


//Handlers for Event bus
eb.consumer("com.makingdevs.emailer.new", { message ->
    println("I have received a message: ${message.body()}")
})

eb.consumer("com.makingdevs.emailer.show.total", { message ->
    println("I have received a message: ${message.body()}")
})

eb.consumer("com.makingdevs.emailer.remove", { message ->
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

