class @.EmailerController
  getUrl = window.location;
  baseUrl = "#{getUrl.protocol}//#{getUrl.host}"

  @add: ->
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

  @send: ->
    $.ajax
     data: $('#previewForm').serialize()
     type: 'post'
     url: baseUrl + '/send'
     success: ->
       Materialize.toast 'Solicitud de envio de correo al Emailer API fue enviada correctamente.', 3000
       Verticle.init()
     error: ->
       console.log "Error al enviar preview"


  index: ->
    html = ViewResolver.mergeViewWithModel "#start-emailer"
    $("#index-banner").html(html)

  new: ->
    html = ViewResolver.mergeViewWithModel "#new-emailer"
    $("#index-banner").html(html)
    Validator.validateNewForm()
    TinyMce.addingTextEditor()

  readEmailers: ->
    $.ajax
      data: 'setValue=0'
      url: "#{baseUrl}/showSet"
      type: 'post'
      success: (response) ->
       context =
         emails: response
         skip: 0
       html = ViewResolver.mergeViewWithModel "#read-emailer", context
       $("#index-banner").html(html)
       $('#modalDelete').modal()
       Paginator.paginate()
      error: ->
        alert 'error al procesar'

   readSetEmailers: (id)->
    $.ajax
      data: 'setValue='+id
      url: "#{baseUrl}/showSet"
      type: 'post'
      success: (response) ->
       context =
         emails: response
         skip: parseInt(id)
       html = ViewResolver.mergeViewWithModel "#read-emailer", context
       $("#index-banner").html(html)
       $('#modalDelete').modal()
       Paginator.paginate()
      error: ->
        alert 'error al procesar set de emailers'

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
         $("#dateCreated").html("Fecha de Creación: "+moment(json.dateCreated).format('MMMM Do YYYY'))
         $("#lastUpdate").html("Última Actualización: "+moment(json.lastUpdate).format('MMMM Do YYYY, h:mm:ss'))
         $('#modalDelete').modal()
         $('#modalPreview').modal()
         Validator.validateSendPreview()
       error: ->
         console.log "Error al ir por el emailer"

  editEmailer: (id)->
    $.ajax
       data: 'idEmail=' + id
       url: "#{baseUrl}/showEmail"
       type: 'post'
       success: (response) ->
         json = $.parseJSON(response)
         html = ViewResolver.mergeViewWithModel "#update-emailer", json
         $("#index-banner").html(html)
         Validator.validateUpdateForm()
         TinyMce.addingTextEditor()

  delete: (id)->
    $.ajax
       data: 'idEmail=' + id
       url:"#{baseUrl}/remove"
       type: 'post'
       success: (response) ->
         Materialize.toast 'Eliminando emailer '+id, 4000
         UrlManager.setRoute()

