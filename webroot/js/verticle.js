var eb = new EventBus('http://localhost:8080/eventbus');
eb.onopen = function() {
  eb.registerHandler('com.makingdevs.email.success', function(error, message) {
    var response=JSON.stringify(message.body);
    new PNotify({
      title: 'Se ha enviado el correo exitosamente.',
      text: 'Hemos enviado los siguientes datos:'+message.body,
      type: 'success'
    });
  });
}
