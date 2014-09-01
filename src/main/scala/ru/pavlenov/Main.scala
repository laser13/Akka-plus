package ru.pavlenov

/**
 * ⓭ + 15
 * Какой сам? by Pavlenov Semen 24.07.14.
 */

import akka.actor._
import org.mashupbots.socko.events.HttpResponseStatus
import org.mashupbots.socko.infrastructure.Logger
import org.mashupbots.socko.routes._
import org.mashupbots.socko.webserver.{WebServer, WebServerConfig}
import ru.pavlenov.handler.{TimeRequest, TimeHandler}

/**
 * This example shows how to setup a simple route and create a simple processor actor.
 *  - Run this class as a Scala Application
 *  - Open your browser and navigate to `http://localhost:8888/`
 *
 * Socko uses Netty to handle incoming requests and Akka to process them
 *  - Incoming requests are converted into Socko events using threads from the Netty thread pool
 *  - Your `routes` are then called to dispatch the event for processing
 *  - Inside our route definition, we instance a new `ru.pavlenov.handler.HelloHandler` actor and pass the event to it
 *  - The `ru.pavlenov.handler.HelloHandler` actor is executed in Akka default thread pool. This frees up the Netty thread pool to
 *    undertake more networking activities.
 */
object Main extends Logger {
  //
  // STEP #1 - Define Actors and Start Akka
  // See `ru.pavlenov.handler.HelloHandler`
  //
  val actorSystem = ActorSystem("HelloExampleActorSystem")

  //
  // STEP #2 - Define Routes
  // Dispatch all HTTP GET events to a newly instanced `ru.pavlenov.handler.HelloHandler` actor for processing.
  // `ru.pavlenov.handler.HelloHandler` will `stop()` itself after processing each request.
  //
  val routes = Routes({
//    case GET(request) =>
//      actorSystem.actorOf(Props[HelloHandler]) ! request
    case HttpRequest(request) => request match {
      // *** HOW TO EXTRACT QUERYSTRING VARIABLES AND USE CONCATENATION ***
      // If the timezone is specified on the query string, (like "/time?tz=sydney"), pass the
      // timezone to the TimeHandler
      case (GET(Path("/time")) & TimezoneQueryString(timezone)) =>
        actorSystem.actorOf(Props[TimeHandler]) ! TimeRequest(request, Some(timezone))

      // *** HOW TO MATCH AND EXTRACT A PATH SEGMENT ***
      // If the timezone is specified on the path (like "/time/sydney"), pass the
      // timezone to the TimeHandler
      case GET(PathSegments("time" :: timezone :: Nil)) =>
        actorSystem.actorOf(Props[TimeHandler]) ! TimeRequest(request, Some(timezone))

      // *** HOW TO MATCH AN EXACT PATH ***
      // No timezone specified, make TimeHandler return the time in the default timezone
      case GET(Path("/time")) =>
        actorSystem.actorOf(Props[TimeHandler]) ! TimeRequest(request, None)

      // If favicon.ico, just return a 404 because we don't have that file
      case Path("/favicon.ico") =>
        request.response.write(HttpResponseStatus.NOT_FOUND)
//      case _ =>
//        request.response.write(HttpResponseStatus.NOT_FOUND)
    }

  })

  object TimezoneQueryString extends QueryStringField("tz")

  //
  // STEP #3 - Start and Stop Socko Web Server
  //
  def main(args: Array[String]) {

//    val myWebServerConfig1 = WebServerConfig(
//      serverName = "CodedConfigApp",
//      hostname = "localhost",
//      port = 3001)

//    log.info("Loading configuration from application.conf")
//    val serverConfig = ServerConfig(actorSystem)
//    log.info("Config is: " + serverConfig.toString)
//
//    val webServer = new WebServer(serverConfig, routes, actorSystem)
//    webServer.start()
//    println(serverConfig.hostname, serverConfig.port)

//    Runtime.getRuntime.addShutdownHook(new Thread {
//      override def run() { webServer.stop() }
//    })

  }
}

