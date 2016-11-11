import io.vertx.groovy.ext.unit.TestSuite
import io.vertx.groovy.core.Vertx
import io.vertx.groovy.core.http.HttpClient

def options = [ reporters: [[ to:"console" ]]]

//Agregar la configuración externa
def config = Vertx.currentContext().config()

//configuracion externalizada
config_ext =["config":config]

//Create TestSuite
def suite = TestSuite.create("the_test_suite")

//Implement TestCase for senderVerticle consumers
suite.before({ context ->
  def async = context.async()
  vertx.deployVerticle("senderVerticle.groovy", config_ext){ ar ->
    context.assertTrue(ar.succeeded())
    vertx.deployVerticle("helperVerticle.groovy")
    async.complete()
  }
}).test("buildEmail_TestCase", { context ->
  def async = context.async()
  def emailContent='''Hola ${name}, te quiero decir:${msg}, saludos ${ami}'''
  def testService=[
    content:emailContent,
    params:[
      name:"MakingDevs",
      msg:"buenos dias",
      ami:"karlosins"
    ]
  ]
  def resultService='''Hola MakingDevs, te quiero decir:buenos dias, saludos karlosins'''
  vertx.eventBus().send("com.makingdevs.emailer.buildEmail", testService) { response ->
    context.assertEquals(resultService, response.result.body())
    async.complete()
  }
}).test("sendEmailService_TestCase",{ context ->
  def async = context.async()
  def testSender=[
    id:"0000-id-prueba",
    from:"emailer@app.com",
    to:"carlo@makingdevs.com",
    cco:"carlogilmar@gmail.com",
    subject:"Unit Test",
    html:"Esta es una prueba unitaria del servicio Sender"
  ]
  def testResponse="Hemos enviado lo siguiente:\n ID:0000-id-prueba.\n DESTINATARIO: carlo@makingdevs.com\n SUBJECT: Unit Test \n CCO: carlogilmar@gmail.com"

  vertx.eventBus().send("com.makingdevs.emailer.sender", testSender) { response ->
    context.assertEquals(testResponse, response.result.body())
    async.complete()
  }
}).test("integration_TestCase",{ context ->
  def async = context.async()
  def testSender=[
    id:"0000-id-verticleIntegracion",
    from:"emailer@app.com",
    to:"carlo@makingdevs.com",
    cco:"carlogilmar@gmail.com",
    subject:"Unit Test for Integration Verticle Vertx Run",
    content:'''Esta es una prueba unitaria del servicio ${sender}, con el mensaje:${msg}''',
    params:[
      sender:"MakingDevs Emailer",
      msg:" Saludo2 "
    ]
  ]
  vertx.eventBus().send("com.makingdevs.emailer.serviceEmail", testSender) { response ->
    context.assertEquals("Solicitud Enviada", response.result.body())
    async.complete()
  }

}).test("verificationCheck_TestCase",{ context ->
  def async=context.async()
  println "Verification test"
  def testCheck=[
    id:"1111",
    params:[
      uno:"1"
    ]
  ]
  vertx.eventBus().send("com.makingdevs.emailer.check", testCheck){ response ->
    def errorTest=[
      to:"Empty",
      subject:"Empty"
    ]
    context.assertEquals(errorTest, response.result.body())
    async.complete()
  }

}).after({ context ->
  println "Terminando las pruebas"
})

def completion = suite.run(options)

