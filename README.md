Play 2.5.10 (Scala) Reactivemongo Template
=========================================

**Play (Scala) - ReactiveMongo - Redis - Guice**

A web backend application using Play 2.5.10 for endpoint backend, Reactivemongo for reactive MongoDB driver, Redis for Cache, Guice
 for dependency injection, ScalaTest and Mockito for unit testing and integration testing, FlapDoodle embedded Mongo driver, embedded Redis driver, Frisby for endpoint testing.


* **PlayFramework** - a high velocity web framework for Java and Scala. This project currently uses 2.5.10 with the
Scala API. Play 2.5.10 recommends to get rid of global settings, i.e. (Global.scala), and use dependency injection as the best practices for both writing code and testing.
  * [PlayFramework Docs](http://www.playframework.com/documentation/)

* **PlayReactiveMongo**  a non-blocking MongoDB driver. It also provides some useful additions for handling JSON.
  * [Play-ReactiveMongo github](https://github.com/ReactiveMongo/Play-ReactiveMongo)
  
* **RediScala**  Non-blocking, Reactive Redis driver for Scala.
  * [RedisScala](https://github.com/etaty/rediscala)
    
* **Guice** - a framework for Dependency injection.
  * [Play-Guice](http://www.typesafe.com/activator/template/play-guice)
  
* **ScalaTest** - a good testing framework for Play. It provides helpers and application stubs to make testing easier.
  * [ScalaTest](http://www.scalatest.org/)

* **Embedded Mongo**  an embedded reactive mongo
  * [Embedded Mongo](https://github.com/flapdoodle-oss/de.flapdoodle.embed.mongo)
    
* **RediScala**  Non-blocking, Reactive Redis driver for Scala.
  * [RedisScala](https://github.com/etaty/rediscala)
  
* **Frisby** - a framework for testing endpoints.
  * [Frisby](http://frisbyjs.com/)

Getting Started
----------

Prerequisites:
*  [Activator(SBT)](https://www.typesafe.com/get-started) - activator wraps around SBT to provide additional features 
that make creating, building, and deploying SBT projects easier. 
*  [MongoDB](https://www.mongodb.org/). [Robomongo](http://robomongo.org/) is a nice MongoDB GUI tool.

## Testing SBT project in Intellij

You can import the project to Intellij by
 
    File -> Open -> {path to repo} -> build.sbt

TDD in Intellij with Play could be painful. In 14.1.2 version (and below), you may want to delegate the build process
 to an external SBT compile server.
First, you need to disable the `make` process for your tests.
       
    Run -> Edit Configurations -> Defaults -> ScalaTest -> Before Launch -> remove `make`

Then, start a compile server
    
    activator ~test:compile
    
or

    sbt ~test:compile

Again, *~* will signal SBT to automatically re-run all the tests as soon as the test changes have been saved.

## Package
Packaging is necessary for deployment. To run/test the project, you can just ignore this process. 
The packaging configurations for RPM and Docker are defined in built.sbt. For Docker packaging, you can run Docker command
againt **Dockerfile** instead of using **sbt-native-packager**.

Note that you can only package the project as RPM or Docker image at a time.
That is why I commented out RPM packaging configuration in built.sbt.
You are free to uncomment it and comment out the Docker packaging configuration part to package the project as RPM image instead.

To package as Docker image

    activator docker:publishLocal
    
To package as RPM image

    activator rpm:packageBin
    
## Run
### Run Mongo
You can either run Mongodb as a service of your OS (you need to install Mongodb)

    mongod
    
or run Mongodb Docker container (You need to download Mongo Docker image)
    
    docker run -p 27017:27017 mongo
    
### Run the project 
#### Run from source code

    activator -Dconfig.file=conf/application.conf "~run 9000" 

You can select a specific configuration for running the application using *-Dconfig.file* option.
*~* will make SBT keep looking at changes of the source code and automatically re-compile the project as soon as
the changes have been saved. This should fetch all the dependencies and start a Web Server listening on *localhost:9000*. 

Note that, all databases and collections will be created automatically (if not existing yet).

#### Run Docker container
To run the project as a Docker container, you must run Mongo as a Docker as well.   
 
    docker run -v <mount_directory>:/var/log -e MONGO_HOST=<mongo_ip_address> -p <host_port>:<container_port> play-mongo:1.1.0
Where **<mongo_ip_address>** is the Ip address of the Mongo Docker container. You can get this value by using the command
    
    docker inspect <mongo_container_id> | grep "IPAddress"

*<container_port>* must be contained in **dockerExposedPorts** list specified in built.sbt.

    In OSX, a Docker container CANNOT connect to the localhost services running on a Docker host.
Consequently, you must run **mongod** in a container rather than running it as a service of the Docker host.
      
#### Run RPM image
You need to install the RPM image, e.g. using **yum**, and start the service
    
    sudo service play-mongo start   
       
## Docker Compose
You can also use Docker compose to run two containers from Mongo image and play-mongo image. 

    docker-compose up
    
The prerequisite is that these images must be available in advance. In addition, you need to update **MONGO_HOST** 
environment variable in docker-compose.yml.
The trick is you run the *docker-compose up* first, and get IP address of the mongo container.
This IP address should be the same for the next time running docker compose.

## Application Performance Monitoring with New Relic
Signup and download the New Relic Java client. Set **license_key** in **newrelic.yml**
where **license_key** value can be obtained from logging in New Relic. Note that **activator run** 
does not work with New Relic although we set javaOptions with **-javaagent:<new_relic_directory>/newrelic/newrelic.jar**. 

    activator clean dist
    cd ./target/universal
    unzip ./*.zip
    cd play-mongo-<version>
    ./bin/play-mongo -J-javaagent:<new_relic_directory>/newrelic/newrelic.jar

      