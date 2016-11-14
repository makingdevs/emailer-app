var eb = new EventBus(window.APP.url + '/eventbus');
eb.onopen = function() {
  eb.registerHandler('com.makingdevs.email.success', function(error, message) {

    new PNotify({
      title: 'Se ha enviado el correo exitosamente.',
      text: 'Mensaje: '+message.body,
      type: 'success'
    });
  });
}
