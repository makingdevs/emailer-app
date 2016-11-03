var eb = new EventBus('http://localhost:8080/eventbus');
eb.onopen = function() {
  eb.registerHandler('com.makingdevs.email.success', function(error, message) {
    var response=JSON.stringify(message.body);
    notie.alert(1, '<small>El correo se ha enviado con los siguientes datos: '+response+'</small>', 3);
  });
}
