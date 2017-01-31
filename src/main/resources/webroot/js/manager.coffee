class @.ViewResolver
  @mergeViewWithModel = (templateName, model) ->
    source = $(templateName).html()
    template = Handlebars.compile source
    template model

class @.Validator

  @validateNewForm: ->
    $('#submitEmailer').click ->
      if $('#subjectEmail').val() == ""
        Materialize.toast 'Agrega un título al emailer', 4000
      else if $('#contentEmail').val() == ""
        Materialize.toast 'Agrega contenido al emailer', 4000
      else
        EmailerManager.add()

  @validateUpdateForm: ->
    $('#submitUpdate').click ->
      if $('#subjectEmail').val() == ""
        Materialize.toast 'Agrega un título al emailer', 4000
      else if $('#contentEmail').val() == ""
        Materialize.toast 'Agrega contenido al emailer', 4000
      else
        EmailerManager.update()

class @.EmailerManager
  baseUrl = "http://localhost:8000"

  @add: ->
    console.log
    $.ajax
     data: $('#newForm').serialize()
     type: 'post'
     url: baseUrl + '/newEmail'
     success: ->
       UrlManager.setRoute()
       Materialize.toast 'Emailer Agregado', 4000
     error: ->
       console.log "Error al agregar"

  @update: ->
    $.ajax
     data: $('#updateForm').serialize()
     type: 'post'
     url: baseUrl + '/update'
     success: ->
       UrlManager.setRoute()
       Materialize.toast 'Emailer Actualizado', 4000
     error: ->
       console.log "Error al actualizar correo"

  index: ->
    html = ViewResolver.mergeViewWithModel "#start-emailer"
    $("#index-banner").html(html)

  new: ->
    html = ViewResolver.mergeViewWithModel "#new-emailer"
    $("#index-banner").html(html)
    Validator.validateNewForm()

  readEmailers: ->
    $.ajax
      data: 'setValue=1'
      url: "#{baseUrl}/showSet"
      type: 'post'
      success: (response) ->
       context =
         emails: response
       html = ViewResolver.mergeViewWithModel "#read-emailer", context
       $("#index-banner").html(html)
       $('#modalDelete').modal()
      error: ->
        alert 'error al procesar'

  previewEmailer: (id)->
    $.ajax
       data: 'idEmail=' + id
       url: "#{baseUrl}/showEmail"
       type: 'post'
       success: (response) ->
         json = $.parseJSON(response)
         html = ViewResolver.mergeViewWithModel "#preview-emailer", json
         $("#index-banner").html(html)
         $('#iframeID').contents().find("body").html(json.content)
         $("#dateCreated").html("Fecha de Creación: "+json.dateCreated)
         $("#lastUpdate").html("Última Actualización "+json.lastUpdate)
       error: ->
         console.log "Error al ir por el emailer"

  editEmailer: (id)->
    $.ajax
       data: 'idEmail=' + id
       url: "#{baseUrl}/showEmail"
       type: 'post'
       success: (response) ->
         # tinyMCE.remove()
         json = $.parseJSON(response)
         html = ViewResolver.mergeViewWithModel "#update-emailer", json
         $("#index-banner").html(html)
         Validator.validateUpdateForm()


  delete: (id)->
    $.ajax
       data: 'idEmail=' + id
       url:"#{baseUrl}/remove"
       type: 'post'
       success: (response) ->
         UrlManager.setRoute()
         Materialize.toast 'Eliminando emailer '+id, 4000

