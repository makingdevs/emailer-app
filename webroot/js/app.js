$(function() {
  console.log("Realizando la primera funcion");
  var source = $("#entry-template").html()
  var template = Handlebars.compile(source)
  var mail = { receiver:"gilmar@makingdevs.com", sender:"carlo@makingdevs.com", title:"Saludos", subject:"Joli", content:"Saludos a to2" }
  var html = template(mail);
  $("div.row:first").prepend(html);
});
