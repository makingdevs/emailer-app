//------------------------------------------------------------------------funcion que recupera todos los registros y pinta la tabla
var findAll = function(){
  var a=  $.ajax({
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

//-------------------------------------------------------------------------------------------New Email
var newEmail = function(){ console.log("new email"); }

//-------------------------------------------------------------------------------------------Show Email
var showOne = function(id){
  console.log("show email"+ id);
  $('input[name="email_1"]').val(id);

//----------------------------------------------------ajax
  var a = $.ajax({
    data: "idEmail="+id,
    url: 'http://localhost:8080/one',
    type: 'post',
    beforeSend: function () {
      console.log("Procesando, aguanta");
    },
    success: function (response) {
      console.log("Ya estuvo:D"+response);
    }
  });

}

var deleteEmail = function(id){ console.log("delete email " + id); }

//------------------------------------------------------------------------routes DirectorJS
var routes = {
  '/': findAll,
  '/new': newEmail,
  '/showOne/:mailId': showOne,
  '/delete/:mailId': deleteEmail
};

var router = Router(routes);
router.init();

