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
    //Mandar email
    mailClient.sendMail(mail, { result ->
      if (result.succeeded()) {
        vertx.eventBus().send("com.makingdevs.email.success", "Se ha enviado el correo exitosamente")
        println("Preview enviado, checa tu correo")
      } else {
        result.cause().printStackTrace()
      }
    })
})

eb.consumer("com.makingdevs.emailer.send.service", { message ->
  //Engine, match with params
  //Inicializar el engine
  def engine=new groovy.text.SimpleTemplateEngine()
  //hacer el match, e imprimir el resultado
  def contentEmail=engine.createTemplate(message.body().content).make(message.body().params)
  //Armar el email
  def mail=[:]

  if(message.body().cc) mail.cc=message.body().cc
  if(message.body().cco) mail.bcc=message.body().cco

  mail.from="Emailer@app.com"
  mail.to=message.body().to
  mail.subject=message.body().subject
  mail.html=contentEmail.toString()
  mailClient.sendMail(mail, { result ->
    if (result.succeeded()) {
      println("Mail enviado, checa tu correo")
  vertx.eventBus().send("com.makingdevs.email.success", [to:mail.from, subject:mail.to, id:"niohnio"])
    } else {
      result.cause().printStackTrace()
    }
  })
})
