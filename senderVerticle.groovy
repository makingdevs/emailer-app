import io.vertx.ext.mail.StartTLSOptions
import io.vertx.groovy.core.Vertx
import io.vertx.groovy.ext.mail.MailClient

//configuracion del email
def config = Vertx.currentContext().config()
def mailClient = MailClient.createShared(vertx, config.mail)

def eb = vertx.eventBus()

eb.consumer("com.makingdevs.emailer.send.email", { message ->
    //formando el email
    def mail=[:]
    mail.from="emailer@app.com"
    mail.to=message.body().to
    mail.subject=message.body().subject
    mail.html=message.body().content
    println "Esto es el mail a mandar"+mail
    //Mandar email
    mailClient.sendMail(mail, { result ->
      if (result.succeeded()) {
        println("Mail enviado, checa tu correo")
      } else {
        result.cause().printStackTrace()
      }
    })
})

