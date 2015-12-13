Play 2.4.4 (Scala) Reactivemongo Template
===========

**Play (Scala) - ReactiveMongo - Redis - Guice**

A full-stack web application using Play 2.4.4 for endpoint backend, Reactivemongo for reactive MongoDB driver, Redis for Cache, Guice
 for dependency injection, ScalaTest and Mockito for unit testing and integration testing, FlapDoodle embedded Mongo driver, embedded Redis driver, Frisby for endpoint testing, and 
 AngularJs, Bookstrap and Coffee script for frontend.


* **PlayFramework** - a high velocity web framework for Java and Scala. This project currently uses 2.4.4 with the
Scala API. Play 2.4.x recommends to get rid of global settings, i.e. (Global.scala), and use dependency injection as the best practices for both writing code and testing.
  * [PlayFramework Docs](http://www.playframework.com/documentation/)

* **PlayReactiveMongo**  a non-blocking MongoDB driver. It also provides some useful additions for handling JSON.
  * [Play-ReactiveMongo github](https://github.com/ReactiveMongo/Play-ReactiveMongo)
  
* **RediScala**  Non-blocking, Reactive Redis driver for Scala.
  * [RedisScala](https://github.com/etaty/rediscala)
    
* **Guice** - a framework for Dependency injection.
  * [Play-Guice](http://www.typesafe.com/activator/template/play-guice)
  
* **ScalaTest** - a good testing framework for Play. It provides helpers and application stubs to make testing easier.
  * [ScalaTest](http://www.scalatest.org/)

* **FlapDoodle embedded Mongo**  an embedded reactive mongo
  * [RedisScala](https://github.com/etaty/rediscala)
    
* **RediScala**  Non-blocking, Reactive Redis driver for Scala.
  * [RedisScala](https://github.com/etaty/rediscala)
  
* **Frisby** - a framework for testing endpoints.
  * [Frisby](http://frisbyjs.com/)
    
* **AngularJS** - a client side javascript framework for creating complex MVC applications in Javascript, fronted with 
Twitter bootstrap CSS framework, because well, im not a web designer.
  * [AngularJS](http://angularjs.org/)

* **Bootstrap** - Bootstrap components written in pure AngularJS
  *  [http://angular-ui.github.io/bootstrap/](http://angular-ui.github.io/bootstrap/)

* **CoffeeScript** - CoffeeScript is an attempt to expose the good parts of JavaScript in a simple way.
  *  [http://coffeescript.org/](http://coffeescript.org/)



Getting Started
----------

Prerequisites:
*  [Activator(SBT)](https://www.typesafe.com/get-started) - activator wraps around SBT to provide additional features 
that make creating, building, and deploying SBT projects easier. 
*  [MongoDB] (https://www.mongodb.org/). [Robomongo](http://robomongo.org/) is a nice MongoDB GUI tool.

Once the prerequisites have been installed, start Mongodb:


    > mongod


Start the Play project:

    ../play-mongo >  activator -Dconfig.file=conf/application.test.conf "~run 9000" 

You can select a specific configuration for running the application using *-Dconfig.file* option. *~* will make SBT keep looking at changes of the source code and automatically re-compile the project as soon as the changes have been saved. This should fetch all the dependencies and start a Web Server listening on *localhost:9000*. Open a web browser at 
*localhost:9000*, you should be able to see a web interface that allows you to view all users, create a new user, 
and update an existing user. 

Note that, in this example, a collection named *users* will be created automatically under database *users-db* when 
the first new user is created from the web interface.

## Testing SBT project in Intellij

You can import the project to Intellij by
 
    File -> Open -> {path to repo} -> build.sbt

TDD in Intellij with Play could be painful. In 14.1.2 version (and below), you may want to delegate the build process
 to an external SBT compile server.
First, you need to disable the `make` process for your tests.
       
    Run -> Edit Configurations -> Defaults -> ScalaTest -> Before Launch -> remove `make`

Then, start a compile server
    
    activator ~ test:compile
    
or

    sbt ~ test:compile

Again, *~* will signal SBT to automatically re-run all the tests as soon as the test changes have been saved.