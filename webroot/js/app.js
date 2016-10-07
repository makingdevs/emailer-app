$(function() {
  console.log("Realizando la primera funcion");

  var source = $("#entry-template").html()
  var template = Handlebars.compile(source)
  var mail = $.getJSON("http://localhost:8080/show")

  console.log("Get Json ok");
  console.log(mail);
  console.log(mail.responseJSON);

  //var wrapper={emails:mail.responseJSON}
  //var html = template(wrapper);
  //$("div.row:first").prepend(html);
});
