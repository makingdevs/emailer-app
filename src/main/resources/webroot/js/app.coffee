class @.App
  constructor: ->
    @manager= new UrlManager()
    Verticle.init()

class @.UrlManager

  constructor: ->
    @emailerManager = new EmailerManager()
    @start()

  start: ->
    @routes =
      '/': @emailerManager.index
      '/newEmailer': @emailerManager.new
      '/readEmailers': @emailerManager.readEmailers
      '/setEmails/:id': @emailerManager.readSetEmailers
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

class @.Verticle
  baseUrl = "http://localhost:8000"
  @init: ->
    eb = new EventBus('http://localhost:8000/eventbus')
    eb.onopen = ->
      eb.registerHandler 'com.makingdevs.email.success', (error, message) ->
        Materialize.toast 'Email Enviado\n ' + message.body, 4000

class @.Paginator
  @paginate: ->
    $.get('http://localhost:8000/countTotal').done((response)->
      counter = response
      count = counter.count
      pages = if count % 10 == 0 then parseInt(count / 10) else parseInt(count / 10 + 1)
      html = '<ul class=\'paginator\'>'
      skip = 0
      i = 1
      while i <= pages
        html = html.concat('<li class=\'paginate\'><a href=\'#/setEmails/' + skip + '\'>' + i + '</a></li>')
        skip = skip + 10
        i++
      html = html.concat('</ul>')
      $('#paginas').html html
      $('ul.paginator').addClass 'pagination'
      $('li.paginate').addClass 'waves-effect'
      $('#numberPage').html "Emailers: #{count}"
    ).fail ->
      console.log "Error al consultar conteo"

