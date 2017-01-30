class @.App
  constructor: ->
    console.log '1.- Inicializando App Class'
    @manager= new UrlManager()

class @.ViewResolver
  @mergeViewWithModel = (templateName, model) ->
    source = $(templateName).html()
    template = Handlebars.compile source
    template model

class @.UrlManager

  constructor: ->
    @start()
    @emailer = new EmailerManager()

  start: ->
    console.log "Start on"
    @routes =
      '/': @infoEmailer
      '/newEmailer': @newEmailer
      '/readEmailers': @readEmailers
      '/previewEmailer/:id': @previewEmailer
      '/editEmailer/:id': @editEmailer
      '/deleteEmailer/:id':@removeEmailer
    @urlMappings()

  urlMappings: ->
    router = Router(@routes)
    router.init()

  infoEmailer: ->
    html = ViewResolver.mergeViewWithModel "#start-emailer", context
    $("#index-banner").html(html)

  newEmailer: ->
    context =
       title: 'Raiz'
       body: 'This is my first post!'
    html = ViewResolver.mergeViewWithModel "#new-emailer", context
    $("#index-banner").html(html)

  readEmailers: ->
    $.ajax
      data: 'setValue=1'
      url: 'http://localhost:8000/showSet'
      type: 'post'
      success: (response) ->
       context =
         emails: response
       html = ViewResolver.mergeViewWithModel "#read-emailer", context
       $("#index-banner").html(html)
       $("#deleteButton").on('click', @write)
      error: ->
       alert 'error al procesar'

  previewEmailer: (id) ->
     console.log "Preview Emailer"
     $.ajax
       data: 'idEmail=' + id
       url: 'http://localhost:8000/showEmail'
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

  editEmailer:(id) ->
     $.ajax
       data: 'idEmail=' + id
       url: 'http://localhost:8000/showEmail'
       type: 'post'
       success: (response) ->
        # tinyMCE.remove()
         json = $.parseJSON(response)
         console.log json
         html = ViewResolver.mergeViewWithModel "#new-emailer", json
         $("#index-banner").html(html)

  removeEmailer:(id) ->
     console.log "Removiendo correo"
     $.ajax
       data: 'idEmail=' + id
       url:'http://localhost:8000/remove'
       type: 'post'
       success: (response) ->
         Materialize.toast '<b>Eliminando emailer<b>'+id, 4000

