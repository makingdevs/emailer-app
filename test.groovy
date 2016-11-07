import io.vertx.groovy.ext.unit.TestSuite
import io.vertx.groovy.core.Vertx
import io.vertx.groovy.core.http.HttpClient

/************************************
Please run as webserver...
vertx run test.groovy -conf conf.json
************************************/

//Adaptar la configuración externalizada
def config = Vertx.currentContext().config()
  opt =[
  "config":config
  ]

//Configuración para la TestSuite
def options = [
    reporters:
    [
       [ to:"console" ]
    ]
]

//Create TestSuite
def suite = TestSuite.create("the_test_suite")

//Deploy webserver
suite.before({ context ->
  vertx.deployVerticle("webserver.groovy",opt)
})

suite.test("testCaseCountable", { context ->
    def msg="hola"
    context.assertEquals("hola", msg)
})


//--------------------------------- Start TestCase
suite.test("countable_test", { context ->
  // Send a request and get a response
  def client = vertx.createHttpClient()
  def async = context.async()
  client.getNow(8080, "localhost", "/", { resp ->
    resp.bodyHandler({ body ->
      context.assertEquals("No resources found", body.toString("UTF-8"))
    })
    client.close()
    async.complete()
  })
})



//Run Suite
suite.run(options)
