package com.robo.crypto.portfolio.view

import javax.net.ssl.{SSLContext, SSLParameters}

import akka.http.scaladsl.HttpsConnectionContext
import akka.stream.TLSClientAuth
import com.typesafe.sslconfig.akka.AkkaSSLConfig
import org.scalatest.{FlatSpec, Matchers}
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.concurrent.PatienceConfiguration.{Interval, Timeout}
import org.scalatest.time.{Milliseconds, Span}

/**
  * Created by rcheng on 18/01/18.
  */
class BinanceServiceSpec extends FlatSpec with ScalaFutures with Matchers {
  private val DefaultTimeoutMilliseconds = 200L
  private val defaultTimeout = Timeout(
    Span(DefaultTimeoutMilliseconds, Milliseconds))

  trait TestData {
    val conf = ExchangeConfig(
      "https://api.binance.com",
      "VEOrf4gqEk0dk6vWCshE3Mz9dN6tOrvFPovoGDRMKO547W8tR0wriuAQJjrkGDzp",
      "FAPabJePs7peRtasYEAwdRo6EBktDHfBtJw5UyiBdzkKpDQ8st63WKhcHG0szYBw"
    )
    val binance: ExchangeService = new BinanceService(conf)
  }

  def https(
             sslContext:          SSLContext,
             sslConfig:           Option[AkkaSSLConfig]         = None,
             enabledCipherSuites: Option[Seq[String]] = None,
             enabledProtocols:    Option[Seq[String]] = None,
             clientAuth:          Option[TLSClientAuth]         = None,
             sslParameters:       Option[SSLParameters]         = None) =
    new HttpsConnectionContext(sslContext)

  "ping" should "return successfully" in new TestData {
    whenReady(binance.ping, defaultTimeout, Interval(Span(200, Milliseconds))) {
      _ =>
        ()
    }
  }
}
