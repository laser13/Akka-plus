/**
 * ⓭ + 16
 * Какой сам? by Pavlenov Semen 24.07.14.
 * ${TITLE}
 * ${URL}
 *
 * ${GIVEN}
 * ${RETURN}
 */

import org.mashupbots.socko.events.HttpRequestEvent
import akka.actor.Actor
import java.util.Date

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
