import io.vertx.ext.mail.StartTLSOptions
import io.vertx.groovy.core.Vertx
import io.vertx.groovy.ext.mail.MailClient
import io.vertx.core.json.Json

//configuracion del email
def config = Vertx.currentContext().config()
def mailClient = MailClient.createShared(vertx, config.mail)

def eb = vertx.eventBus()

//Verify Params at service call
eb.consumer("com.makingdevs.emailer.check", { message ->
  def serviceParams=message.body()
  def errores=[:]

  if(!serviceParams.id) errores.id="Empty"
  if(!serviceParams.to) errores.to="Empty"
  if(!serviceParams.params) errores.params="Empty"
  if(!serviceParams.subject) errores.subject="Empty"

  if(!errores){
    message.reply("ok")
  }
  else{
    message.reply(errores)
  }
})


//Consumer para encargarse de armar y mandar el correo
//Recibe: [:] con valores del email encontrado
//Salida: Llama a los otros dos verticles para realizar el servicio
eb.consumer("com.makingdevs.emailer.serviceEmail", { message ->
  def dataEmail=message.body()
  def contentData=[
  content:message.body().content,
  params:message.body().params
  ]
  eb.send("com.makingdevs.emailer.buildEmail", contentData){ response ->
      dataEmail.html=response.result.body()
      //Mandando
      eb.send("com.makingdevs.emailer.sender", dataEmail)
      message.reply("Solicitud Enviada")
  }
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


