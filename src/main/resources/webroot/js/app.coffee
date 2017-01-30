class @.App
  constructor: ->
    console.log '1.- Inicializando App Class'
    @manager= new UrlManager()


class @.UrlManager

  constructor: ->
    @emailerManager = new EmailerManager()
    @start()

  start: ->
    @routes =
      '/': @emailerManager.index
      '/newEmailer': @emailerManager.new
      '/readEmailers': @emailerManager.read
      '/previewEmailer/:id': @emailerManager.preview
      '/editEmailer/:id': @emailerManager.edit
      '/deleteEmailer/:id':@emailerManager.delete
    @urlMappings()

  urlMappings: ->
    router = Router(@routes)
    router.init()

