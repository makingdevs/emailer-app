import io.vertx.groovy.ext.asyncsql.MySQLClient

// To create a MySQL client:

def mySQLClientConfig = [
  host:"http://localhost:3306/vertx_test"
	]
def mySQLClient = MySQLClient.createShared(vertx, mySQLClientConfig)

