class @.ViewResolver
  @mergeViewWithModel = (templateName, model) ->
    source = $(templateName).html()
    template = Handlebars.compile source
    template model

class @.EmailerManager
  baseUrl = "http://localhost:8000"

  index: ->
    html = ViewResolver.mergeViewWithModel "#start-emailer"
    $("#index-banner").html(html)

  new: ->
    html = ViewResolver.mergeViewWithModel "#new-emailer"
    $("#index-banner").html(html)

  read: ->
    $.ajax
      data: 'setValue=1'
      url: '#{baseUrl}/showSet'
      type: 'post'
      success: (response) ->
       context =
         emails: response
       html = ViewResolver.mergeViewWithModel "#read-emailer", context
       $("#index-banner").html(html)
       $("#deleteButton").on('click', @write)
      error: ->
       alert 'error al procesar'

  preview: (id)->
    $.ajax
       data: 'idEmail=' + id
       url: '#{baseUrl}/showEmail'
       type: 'post'
       success: (response) ->
         json = $.parseJSON(response)
         console.log json
         html = ViewResolver.mergeViewWithModel "#preview-emailer", json
         $("#index-banner").html(html)
         $('#iframeID').contents().find("body").html(json.content)
         $("#dateCreated").html("Fecha de Creación: "+json.dateCreated)
         $("#lastUpdate").html("Última Actualización "+json.lastUpdate)
       error: ->
         console.log "Error al ir por el emailer"

  edit: (id)->
    $.ajax
       data: 'idEmail=' + id
       url: '#{baseUrl}/showEmail'
       type: 'post'
       success: (response) ->
        # tinyMCE.remove()
         json = $.parseJSON(response)
         console.log json
         html = ViewResolver.mergeViewWithModel "#new-emailer", json
         $("#index-banner").html(html)

  delete: (id)->
     $.ajax
       data: 'idEmail=' + id
       url:'#{baseUrl}/remove'
       type: 'post'
       success: (response) ->
         Materialize.toast '<b>Eliminando emailer<b>'+id, 4000

