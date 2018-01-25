package com.robo.crypto.portfolio.view

import akka.http.scaladsl.{ConnectionContext, Http}
import akka.http.scaladsl.model._
import akka.http.scaladsl.model.HttpMethods._
import akka.http.scaladsl.model.headers.RawHeader
import java.time.Instant
import javax.crypto.spec.SecretKeySpec
import javax.crypto.Mac
import javax.net.ssl.SSLContext

import akka.actor.ActorSystem
import akka.http.scaladsl.unmarshalling.Unmarshal
import akka.stream.ActorMaterializer

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{Failure, Success}

trait ExchangeService {
  def ping: Future[Unit]
  def getAccount: Future[Either[String, ExchangeAccount]]
}

final case class ExchangeConfig(host: String, apiKey: String, secretKey: String)

object Hex {
  def valueOf(buf: Array[Byte]): String = {
    val sb = new StringBuilder(buf.length * 2)
    for (b <- buf) {
      sb.append("%02X".format(b & 0xff))
    }
    sb.toString()
  }
}

class BinanceService(conf: ExchangeConfig) extends ExchangeService {
  private val ApiKeyHeader = "X-MBX-APIKEY"

  private implicit val system = ActorSystem()
  private implicit val materializer = ActorMaterializer()
  //private val httpsContext = ConnectionContext.https(SSLContext.getDefault)

  def ping: Future[Unit] = {
    val uri = conf.host + "/api/v1/ping"
    val request = HttpRequest(GET, uri)

    //Http().setDefaultClientHttpsContext(httpsContext)
    val responseFuture: Future[HttpResponse] = Http().singleRequest(request)

    responseFuture.map(_ => {
      ()
    })
  }

  def getAccount: Future[Either[String, ExchangeAccount]] = {
    //GET /api/v3/account (HMAC SHA256)
    val uri = conf.host + "/api/v3/account"
    val query = s"timestamp=${getTimestamp}"
    val signature = sign(query, conf.secretKey)
    val signedQuery = s"${query}&signature=${signature}"

    val request = HttpRequest(GET, Uri(uri).withRawQueryString(signedQuery))
      .withHeaders(RawHeader(ApiKeyHeader, conf.apiKey))
    val futureResponse: Future[HttpResponse] = Http().singleRequest(request)

    // TODO: check for error code
    for {
      response <- futureResponse
      str <- Unmarshal(response.entity).to[String]
    } yield {
      Right(ExchangeAccount(str, Seq.empty))
    }
  }

  def getTimestamp: Long = {
    Instant.now().toEpochMilli
  }

  def sign(str: String, secretKey: String): String = {
    val secret = new SecretKeySpec(secretKey.getBytes("UTF-8"), "HmacSHA256")
    val mac = Mac.getInstance("HmacSHA256")
    mac.init(secret)
    val hmac: Array[Byte] = mac.doFinal(str.getBytes("UTF-8"))
    Hex.valueOf(hmac)
  }
}
