/**
 * ⓭ + 15
 * Какой сам? by Pavlenov Semen 24.07.14.
 * ${TITLE}
 * ${URL}
 *
 * ${GIVEN}
 * ${RETURN}
 */

import org.mashupbots.socko.routes._
import org.mashupbots.socko.infrastructure.Logger
import org.mashupbots.socko.webserver.WebServer
import org.mashupbots.socko.webserver.WebServerConfig

import akka.actor._

/**
 * This example shows how to setup a simple route and create a simple processor actor.
 *  - Run this class as a Scala Application
 *  - Open your browser and navigate to `http://localhost:8888/`
 *
 * Socko uses Netty to handle incoming requests and Akka to process them
 *  - Incoming requests are converted into Socko events using threads from the Netty thread pool
 *  - Your `routes` are then called to dispatch the event for processing
 *  - Inside our route definition, we instance a new `HelloHandler` actor and pass the event to it
 *  - The `HelloHandler` actor is executed in Akka default thread pool. This frees up the Netty thread pool to
 *    undertake more networking activities.
 */
object HelloApp extends Logger {
  //
  // STEP #1 - Define Actors and Start Akka
  // See `HelloHandler`
  //
  val actorSystem = ActorSystem("HelloExampleActorSystem")

  //
  // STEP #2 - Define Routes
  // Dispatch all HTTP GET events to a newly instanced `HelloHandler` actor for processing.
  // `HelloHandler` will `stop()` itself after processing each request.
  //
  val routes = Routes({
    case GET(request) =>
      actorSystem.actorOf(Props[HelloHandler]) ! request
  })

  //
  // STEP #3 - Start and Stop Socko Web Server
  //
  def main(args: Array[String]) {

    val myWebServerConfig1 = WebServerConfig(
      serverName = "CodedConfigApp",
      hostname = "localhost",
      port = 3001)

    log.info("Loading configuration from application.conf")
    val myWebServerConfig2 = MyWebServerConfig(actorSystem)
    log.info("Config is: " + myWebServerConfig2.toString)

    val webServer = new WebServer(myWebServerConfig2, routes, actorSystem)
    webServer.start()

    Runtime.getRuntime.addShutdownHook(new Thread {
      override def run() { webServer.stop() }
    })

    println("Open your browser and navigate to http://localhost:8888")
//    println(actorSystem.settings.config)
  }
}

object MyWebServerConfig extends ExtensionId[WebServerConfig] with ExtensionIdProvider {
  override def lookup() = MyWebServerConfig
  override def createExtension(system: ExtendedActorSystem) =
    new WebServerConfig(system.settings.config, "akka-config-example")
}