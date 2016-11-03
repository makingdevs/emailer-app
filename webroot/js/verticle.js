var eb = new EventBus('http://localhost:8080/eventbus');
eb.onopen = function() {
  eb.registerHandler('com.makingdevs.email.success', function(error, message) {
    var response=JSON.stringify(message.body);
    console.log("Se envio esto: "+response);
    console.log('We send message this subject: '+response.to);
  });
}
