package ru.pavlenov

import akka.actor.{ExtendedActorSystem, ExtensionIdProvider, ExtensionId}
import org.mashupbots.socko.webserver.WebServerConfig

/**
 * ⓭ + 07
 * Какой сам? by Pavlenov Semen 26.07.14.
 */
object ServerConfig extends ExtensionId[WebServerConfig] with ExtensionIdProvider {
    override def lookup() = ServerConfig
    override def createExtension(system: ExtendedActorSystem) =
      new WebServerConfig(system.settings.config, "akka-config-example")
}