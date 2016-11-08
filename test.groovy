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
    async.complete()
  }
}).test("sendEmail_TestCase", { context ->
  def async = context.async()
  //Mandar un mensaje al verticle send.service
  //Preparar los datos
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


  vertx.eventBus().send("com.makingdevs.emailer.send.service", testService) { response ->
    context.assertEquals(resultService, response.result.body())
    async.complete()
  }
}).after({ context ->
  println "Terminando las pruebas"
})

def completion = suite.run(options)

