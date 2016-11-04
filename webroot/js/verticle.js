var eb = new EventBus('http://localhost:8080/eventbus');
eb.onopen = function() {
  eb.registerHandler('com.makingdevs.email.success', function(error, message) {

    new PNotify({
      title: 'Se ha enviado el correo exitosamente.',
      text: 'Mensaje: '+message.body,
      type: 'success'
    });
  });
}
