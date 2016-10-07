$(function() {
  console.log("Realizando la primera funcion");
  var a=  $.ajax({
      url:"http://localhost:8080/show",
      type:'POST',
      dataType: 'json',
      success:
        function(response){
  //        console.log(response)
          var source = $("#entry-template").html()
          var template = Handlebars.compile(source)
          var wrapper={emails:response}
          var html = template(wrapper);
          $("div.row:first").prepend(html);
  //        console.log(html)
      }
    })

});
