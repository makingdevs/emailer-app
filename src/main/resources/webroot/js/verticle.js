var eb = new EventBus('http://emailerv2.modulusuno.com/eventbus');
eb.onopen = function() {
  eb.registerHandler('com.makingdevs.email.success', function(error, message) {

    new PNotify({
      title: 'Se ha enviado el correo exitosamente.',
      text: 'Mensaje: '+message.body,
      type: 'success'
    });
  });
}
