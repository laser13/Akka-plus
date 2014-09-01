package ru.pavlenov.handler

/**
 * ⓭ + 29
 * Какой сам? by Pavlenov Semen 26.07.14.
 * ${TITLE}
 * ${URL}
 *
 * ${GIVEN}
 * ${RETURN}
 */
import java.text.SimpleDateFormat
import java.util.{GregorianCalendar, TimeZone}

import akka.actor.Actor
import akka.event.Logging
import org.mashupbots.socko.events.HttpRequestEvent

/**
 * Returns the current time in the response
 */
class TimeHandler extends Actor {
  val log = Logging(context.system, this)

  /**
   * Returns the time in the specified timezone.
   *
   * This actor only receives 1 time of message: `TimeRequest`.
   *
   * The message contains the `HttpRequestProcessingEvent` that contains request data and will be used to
   * write the response.
   */
  def receive = {
    case msg: TimeRequest =>

      val tz = if (msg.timezone.isDefined) {
        val tzid = TimeZone.getAvailableIDs.find(s =>
          s.toLowerCase.contains(msg.timezone.get.replace("%20", "_").toLowerCase))
        if (tzid.isDefined) {
          TimeZone.getTimeZone(tzid.get)
        } else {
          TimeZone.getDefault
        }
      } else {
        TimeZone.getDefault
      }

      val dateFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
      dateFormatter.setTimeZone(tz)
      val time = new GregorianCalendar()
      val ts = dateFormatter.format(time.getTime)

      msg.event.response.write("The time is " + ts + ".\nThe timezone is " +
        dateFormatter.getTimeZone.getDisplayName)
      context.stop(self)
    case _ => {
      log.info("received unknown message of type: ")
      context.stop(self)
    }
  }
}

/**
 * Request Message
 *
 * @param event HTTP Request event
 * @param timzone The requested timezone
 */
case class TimeRequest(event: HttpRequestEvent, timezone: Option[String])
