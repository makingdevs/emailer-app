# Emailer App

Please run the project. Clone, and run with:

```
vertx run webserver.groovy -conf conf.json
```
Before you need to create your file conf.json
Please go to Github and check.

#For Use the Project

+ Please go to your localhost:8080/static/
+ Add new Email
+ Write some like this:
```
Prueba de mi emailer.

Atentamente: ${name}
```
+ Check your email added

#Functions:
+ You can send you a preview
+ You can update your email
+ You can delete your email

#Service

How to call the service:

```
curl -i -H "Content-Type: application/json" -X POST -d '{"id":"58126387e3bce5edc834f6a1", "subject":"Pruebas del Dia Jueves Emailer", "to":"carlo@makingdevs.com", "params":{ "name":"MakingDevs" }}' localhost:8080/serviceEmail``
```


