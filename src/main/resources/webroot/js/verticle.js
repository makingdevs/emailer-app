var eb = new EventBus(window.APP.url + '/eventbus');
eb.onopen = function() {
  eb.registerHandler('com.makingdevs.email.success', function(error, message) {
    new PNotify({
      title: 'Mensaje de Emailer App v2',
      text: 'Mensaje: '+message.body,
    });
  });
}
