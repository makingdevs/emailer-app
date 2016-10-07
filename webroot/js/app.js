$(function() {
  console.log("Realizando la primera funcion");
  var source = $("#entry-template").html()
  var template = Handlebars.compile(source)
  var mail = [
             { receiver:"gilmar@makingdevs.com", sender:"carlo@makingdevs.com", title:"Saludos", subject:"Joli", content:"Saludos a to2" },
             { receiver:"1gilmar@makingdevs.com", sender:"1carlo@makingdevs.com", title:"1Saludos", subject:"1Joli", content:"1Saludos a to2" }];

  var wrapper={emails:mail}

  var html = template(wrapper);

  $("div.row:first").prepend(html);
});
