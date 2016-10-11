//------------------------------------------------------------------------funcion que recupera todos los registros y pinta la tabla
var findAll = function(){
  $.ajax({
      url:"http://localhost:8080/show",
      type:'GET',
      dataType: 'json',
      success:
        function(response){
          var source = $("#entry-template").html();
          var template = Handlebars.compile(source);
          var wrapper={emails:response};
          var html = template(wrapper);
          $("div.row:first").prepend(html);
      }
    });
}

//-------------------------------------------------------New Email
var newEmail = function(){
  console.log("new email");
}

//-------------------------------------------------------Show Email
var showOne = function(id){
  $.ajax({
    data: "idEmail="+id,
    url: 'http://localhost:8080/one',
    type: 'post',
    success: function (response) {
          //parseando JSON
          var json=$.parseJSON(response);
          //inputs 1by1
          $('input[name="email_id"]').val(json._id);
          $('input[name="email_1"]').val(json.receiver);
          $('input[name="email_2"]').val(json.sender);
          $('input[name="asunto"]').val(json.submit);
          $('input[name="title"]').val(json.title);
          $('textarea').val(json.content);
          //change the form action
          $("form").attr("action","/update")
      }
  });

}

//------------------------------------------------Delete Email
var deleteEmail = function(id){
  $.ajax({
    data: "idEmailRemove="+id,
    url: 'http://localhost:8080/remove',
    type: 'post',
    success: function (response) {
    }
  });


}

//------------------------------------------------------------------------routes DirectorJS
var routes = {
  '/': findAll,
  '/new': newEmail,
  '/showOne/:mailId': showOne,
  '/delete/:mailId': deleteEmail
};

var router = Router(routes);
router.init();

