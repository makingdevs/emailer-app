import io.vertx.groovy.ext.unit.TestSuite
import io.vertx.groovy.core.Vertx
import io.vertx.groovy.core.http.HttpClient

def options = [
reporters:
[
[ to:"console" ]
]
]

//Agregar la configuración externa
def config = Vertx.currentContext().config()
//configuracion externalizada
config_ext =[
"config":config
]

//Create TestSuite
def suite = TestSuite.create("the_test_suite")

suite.before({ context ->
  def async = context.async()
  vertx.deployVerticle("emailerVerticle.groovy", config_ext){ ar ->
    context.assertTrue(ar.succeeded())
    async.complete()
  }
}).test("my_test_case_1", { context ->
  def async = context.async()
  vertx.eventBus().send("com.makingdevs.emailer.count", "22") { response ->
    context.assertEquals(22, response.result.body().toInteger())
    async.complete()
  }
}).after({ context ->
  println "After"
})

def completion = suite.run(options)

