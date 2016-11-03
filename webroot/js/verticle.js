var eb = new EventBus('http://localhost:8080/eventbus');
eb.onopen = function() {
  eb.registerHandler('chat.to.client', function(error, message) {
    console.log('received a message: '+message.body);
  });
}
