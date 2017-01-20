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
    console.log '2.- Manager Class on'
    @start()

  start: ->
    console.log "Start on"
    @routes =
      '/': @infoEmailer
      '/newEmailer': @newEmailer
      '/readEmailers': @readEmailers
      '/previewEmailer/:id': @previewEmailer
    @urlMappings()

  urlMappings: ->
    console.log "url mappings on"
    router = Router(@routes)
    router.init()

  infoEmailer: ->
    context =
       title: 'Raiz'
       body: 'This is my first post!'
    html = ViewResolver.mergeViewWithModel "#start-emailer", context
    $("#index-banner").html(html)
    console.log "prueba con handlebars"

  newEmailer: ->
    context =
       title: 'Raiz'
       body: 'This is my first post!'
    html = ViewResolver.mergeViewWithModel "#new-emailer", context
    $("#index-banner").html(html)

  readEmailers: ->
    context =
       title: 'Raiz'
       body: 'This is my first post!'
    html = ViewResolver.mergeViewWithModel "#read-emailer", context
    $("#index-banner").html(html)

  previewEmailer: (id) ->
    context =
       id: id
    html = ViewResolver.mergeViewWithModel "#preview-emailer", context
    $("#index-banner").html(html)

