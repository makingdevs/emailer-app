package com.makingdevs

import io.vertx.ext.mail.StartTLSOptions
import io.vertx.groovy.core.Vertx
import io.vertx.groovy.ext.mail.MailClient
import io.vertx.core.json.Json

//configuracion del email
def config = Vertx.currentContext().config()
def mailClient = MailClient.createShared(vertx, config.mail)

def eb = vertx.eventBus()

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


  println "*"*25
  println "SenderVerticle: Correo recibido, simulación de envío"
  println "Email enviado "+response

//<---------------------------------------------------------------------pruebas
/*
  mailClient.sendMail(mail, { result ->
    if (result.succeeded()) {
      message.reply(response)
      vertx.eventBus().send("com.makingdevs.email.success", response)
    } else {
      response="Error, no se logró mandar el envío correctamente"
      vertx.eventBus().send("com.makingdevs.email.success", response)
      result.cause().printStackTrace()
    }
  })
*/


})


