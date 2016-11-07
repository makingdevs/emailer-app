import io.vertx.groovy.ext.unit.TestSuite
import io.vertx.groovy.core.Vertx

def config = Vertx.currentContext().config()
  //configuracion externalizada
  opt =[
  "config":config
  ]


def options = [
reporters:[
[
to:"console"
]
]
]

def suite = TestSuite.create("the_test_suite")

suite.before({ context ->
  vertx.deployVerticle("webserver.groovy",opt)
})

suite.run(options)
