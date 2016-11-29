var eb = new EventBus(window.APP.url + '/eventbus');
console.log("Verticle Javascript Vertx");
eb.onopen = function() {
  eb.registerHandler('com.makingdevs.email.success', function(error, message) {
   console.log("Verticle funcionando");
    new PNotify({
      title: 'Se ha enviado el correo exitosamente.',
      text: 'Mensaje: '+message.body,
      type: 'success'
    });
  });
}
