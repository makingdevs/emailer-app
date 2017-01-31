class @.App
  constructor: ->
    @manager= new UrlManager()

class @.UrlManager

  constructor: ->
    @emailerManager = new EmailerManager()
    @start()

  start: ->
    @routes =
      '/': @emailerManager.index
      '/newEmailer': @emailerManager.new
      '/readEmailers': @emailerManager.readEmailers
      '/previewEmailer/:id': @emailerManager.previewEmailer
      '/editEmailer/:id': @emailerManager.editEmailer
      '/deleteEmailer/:id':@emailerManager.delete
    @urlMappings()

  urlMappings: ->
    router = Router(@routes)
    router.init()

  @setRoute: ->
    router = Router(@routes)
    router.setRoute '/readEmailers'

###
class @.Verticle
  baseUrl = "http://localhost:8000"
  @init: ->
    eb = new EventBus(baseURL + '/eventbus')
    eb.onopen = ->
    eb.registerHandler 'com.makingdevs.email.success', (error, message) ->
      Materialize.toast 'Emailer Enviado: '+message.body, 3000
      ###
