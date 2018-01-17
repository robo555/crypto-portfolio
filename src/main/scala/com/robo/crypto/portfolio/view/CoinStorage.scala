package com.robo.crypto.portfolio.view

import java.time.ZonedDateTime
import java.time.temporal.ChronoUnit

import scala.concurrent.Future
import scala.concurrent.duration._

// this is where we define all side-effecting interactions of our system

// A Coin Storage can be a Wallet, or an account at an Exchange.
trait CoinStorage {
  def balances: Seq[CoinBalance]
}

case class Wallet(accountId: String, balances: Seq[CoinBalance])
    extends CoinStorage
case class ExchangeAccount(accountId: String, balances: Seq[CoinBalance])
    extends CoinStorage

trait CoinStorageRepository[F[_]] {
  def getAll: F[Seq[CoinStorage]]
}

//class CachedCoinStorage[F[_]](storage: CoinStorage[F],
//                              lastUpdated: ZonedDateTime = ZonedDateTime.now(),
//                              expiry: Duration = 15.seconds)
//    extends CoinStorage[F] {
//  //var cachedBalances: F[Set[CoinBalance]]
//  // var lastUpdated: ZonedDateTime
//
//  def getBalances: F[Set[CoinBalance]] = {
//    storage.getBalances
//  }
//
//  private def hasExpired: Boolean = {
//    timediff(lastUpdated, ZonedDateTime.now()) > expiry
//  }
//
//  private def timediff(from: ZonedDateTime,
//                       to: ZonedDateTime): FiniteDuration = {
//    ChronoUnit.SECONDS.between(from, to).seconds
//  }
//}
