package ru.pavlenov.handler

/**
 * ⓭ + 16
 * Какой сам? by Pavlenov Semen 24.07.14.
 * ${TITLE}
 * ${URL}
 *
 * ${GIVEN}
 * ${RETURN}
 */

import java.util.Date

import akka.actor.Actor
import org.mashupbots.socko.events.HttpRequestEvent

/**
 * Hello processor writes a greeting and stops.
 */
class HelloHandler extends Actor {
  def receive = {
    case event: HttpRequestEvent =>
      event.response.write("Hello from Socko (" + new Date().toString + ")")
      context.stop(self)
  }
}
