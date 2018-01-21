package com.robo.crypto.portfolio.view

import akka.http.scaladsl.Http
import akka.http.scaladsl.model._
import akka.http.scaladsl.model.HttpMethods._
import akka.http.scaladsl.model.headers.RawHeader
import java.time.Instant
import javax.crypto.spec.SecretKeySpec
import javax.crypto.Mac

import akka.actor.ActorSystem
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
  val ApiKeyHeader = "X-MBX-APIKEY"

  implicit val system = ActorSystem()
  implicit val materializer = ActorMaterializer()

  def ping: Future[Unit] = {
    val uri = conf.host + "/api/v1/ping"
    println(uri)
    val request = HttpRequest(GET, uri)
    val responseFuture: Future[HttpResponse] = Http().singleRequest(request)

    responseFuture.map(_ => {
      println("ping finished")
      ()
    })
  }

  def getAccount: Future[Either[String, ExchangeAccount]] = {
    //GET /api/v3/account (HMAC SHA256)
    val uri = conf.host + "/api/v3/account"
    val query = s"timestamp=${getTimestamp}"
    val signedQuery = sign(query, conf.secretKey)
    val entity = HttpEntity(signedQuery)

    val request = HttpRequest(GET, uri)
      .withHeaders(RawHeader(ApiKeyHeader, conf.apiKey))
      .withEntity(entity)
    val responseFuture: Future[HttpResponse] = Http().singleRequest(request)

    responseFuture.map(response => {
      println(response.entity.toString)
      Right(ExchangeAccount(response.entity.toString, Seq.empty))
    })
//    responseFuture.recover {
//      case thrown: Throwable => Left(thrown.getMessage)
//    }
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
