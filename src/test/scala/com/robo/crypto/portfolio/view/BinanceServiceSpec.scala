package com.robo.crypto.portfolio.view

import org.scalatest.{FlatSpec, Matchers}
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.concurrent.PatienceConfiguration.Timeout
import org.scalatest.time.{Milliseconds, Span}

class BinanceServiceSpec extends FlatSpec with ScalaFutures with Matchers {
  private val DefaultTimeoutMilliseconds = 10000L
  private val DefaultTimeout = Timeout(
    Span(DefaultTimeoutMilliseconds, Milliseconds))

  trait TestData {
    val conf = ExchangeConfig(
      "https://api.binance.com",
      "VEOrf4gqEk0dk6vWCshE3Mz9dN6tOrvFPovoGDRMKO547W8tR0wriuAQJjrkGDzp",
      "FAPabJePs7peRtasYEAwdRo6EBktDHfBtJw5UyiBdzkKpDQ8st63WKhcHG0szYBw"
    )
    val binance: ExchangeService = new BinanceService(conf)
  }

  "ping" should "return successfully" in new TestData {
    whenReady(binance.ping, DefaultTimeout) { _ =>
      ()
    }
  }

  "getAccount" should "return successfully using correct account key" in new TestData {
    whenReady(binance.getAccount, DefaultTimeout) { response =>
      response match {
        case Right(acct) => println(acct.accountId)
        case Left(error) => assert(false, error)
      }
    }
  }
}
