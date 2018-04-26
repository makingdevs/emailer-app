class @.App
  constructor: ->
    @manager= new UrlManager()
    Verticle.init()
    @helper()

  helper: ()->
    Handlebars.registerHelper 'currentIndex', (index, skip) ->
      parseInt(index) + 1 + parseInt(skip)

class @.UrlManager

  constructor: ->
    @emailerController = new EmailerController()
    @start()

  start: ->
    @routes =
      '/': @emailerController.index
      '/newEmailer': @emailerController.new
      '/readEmailers': @emailerController.readEmailers
      '/setEmails/:id': @emailerController.readSetEmailers
      '/previewEmailer/:id': @emailerController.previewEmailer
      '/editEmailer/:id': @emailerController.editEmailer
      '/deleteEmailer/:id':@emailerController.delete
    @urlMappings()

  urlMappings: ->
    router = Router(@routes)
    router.init()

  @setRoute: ->
    router = Router(@routes)
    router.setRoute '/readEmailers'

class @.Verticle
  baseUrl = "http://af850b0d5496411e88d850abaf866a56-9749683.us-east-1.elb.amazonaws.com"
  @init: ->
    eb = new EventBus('http://af850b0d5496411e88d850abaf866a56-9749683.us-east-1.elb.amazonaws.com/eventbus')
    eb.onopen = ->
      eb.registerHandler 'com.makingdevs.email.success', (error, message) ->
        Materialize.toast 'Email Enviado\n ' + message.body, 4000


