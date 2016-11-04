import io.vertx.ext.mail.StartTLSOptions
import io.vertx.groovy.core.Vertx
import io.vertx.groovy.ext.mail.MailClient
import io.vertx.core.json.Json

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

    def response="Hemos enviado lo siguiente:\n TEMPLATE ID:"+message.body().id+".\n DESTINATARIO: "+mail.to+"\n SUBJECT: "+mail.subject
    //Mandar email
    mailClient.sendMail(mail, { result ->
      if (result.succeeded()) {
        vertx.eventBus().send("com.makingdevs.email.success", response)
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

  def response="Hemos enviado lo siguiente:\n TEMPLATE ID:"+message.body().id+".\n DESTINATARIO: "+message.body().to+"\n SUBJECT: "+message.body().subject

  if(message.body().cc) {
    mail.cc=message.body().cc
    response=response+" \n CC: "+mail.cc
  }

  if(message.body().cco){
    mail.bcc=message.body().cco
    response=response+" \n CCO: "+mail.bcc
  }

  mail.from="Emailer@app.com"
  mail.to=message.body().to
  mail.subject=message.body().subject
  mail.html=contentEmail.toString()

  mailClient.sendMail(mail, { result ->
    if (result.succeeded()) {
      vertx.eventBus().send("com.makingdevs.email.success", response)
    } else {
      result.cause().printStackTrace()
    }
  })
})
