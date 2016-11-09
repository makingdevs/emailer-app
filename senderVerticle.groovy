import io.vertx.ext.mail.StartTLSOptions
import io.vertx.groovy.core.Vertx
import io.vertx.groovy.ext.mail.MailClient
import io.vertx.core.json.Json

//configuracion del email
def config = Vertx.currentContext().config()
def mailClient = MailClient.createShared(vertx, config.mail)

def eb = vertx.eventBus()

//Consumer para mandar email de prueba
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

//Consumer para mandar email desde servicio
eb.consumer("com.makingdevs.emailer.send.service", { message ->
  //Engine, match with params
  //Inicializar el engine
  def engine=new groovy.text.SimpleTemplateEngine()
  //hacer el match, e imprimir el resultado
  def contentEmail=engine.createTemplate(message.body().content).make(message.body().params)
  message.reply(contentEmail.toString())

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

//Consumer para construir el contenido del correo
//Recibe: [:] Contenido, y params para hacer el match
//Salida: [String]=Contenido
eb.consumer("com.makingdevs.emailer.buildEmail", { message ->
  def params=message.body().params
  def content=message.body().content
  def engine=new groovy.text.SimpleTemplateEngine()
  def contentEmail=engine.createTemplate(content).make(params)
  message.reply(contentEmail.toString())
})

//Consumer para mandar el correo y la notificación de que se mandó
//Recibe: [:] Email con parámetros
//Salida: Reply de confirmado
eb.consumer("com.makingdevs.emailer.sender", { message ->
  def mail=message.body()
  //Armando el correo y la respuesta
  def response="Hemos enviado lo siguiente:\n ID:"+message.body().id+".\n DESTINATARIO: "+message.body().to+"\n SUBJECT: "+message.body().subject

  if(message.body().cc) {
    mail.cc=message.body().cc
    response=response+" \n CC: "+mail.cc
  }
  if(message.body().cco){
    mail.bcc=message.body().cco
    response=response+" \n CCO: "+mail.bcc
  }

  mailClient.sendMail(mail, { result ->
    if (result.succeeded()) {
      message.reply(response)
      vertx.eventBus().send("com.makingdevs.email.success", response)
    } else {
      result.cause().printStackTrace()
    }
  })
})


