package com.robo.crypto.portfolio.view

import java.time.ZonedDateTime

import scala.concurrent.Future
import scala.concurrent.duration._

// this is where we define all side-effecting interactions of our system

// A Coin Storage can be a Wallet, or an account at an Exchange.
trait CoinStorage[F[_]] {
  def getBalances: F[Set[CoinBalance]]
}

class CachedCoinStorage[F[_]](storage: CoinStorage[F],
                              lastUpdated: ZonedDateTime = ZonedDateTime.now(),
                              expiry: Duration = 15.seconds)
    extends CoinStorage[F] {
  //var cachedBalances: F[Set[CoinBalance]]
  // var lastUpdated: ZonedDateTime

  def getBalances: F[Set[CoinBalance]] = {
    storage.getBalances
  }

  private def hasExpired: Boolean = {
    ???
  }
}

class Wallet[F[_]](address: String) extends CoinStorage[F] {
  override def getBalances: F[Set[CoinBalance]] = ???
}

class ExchangeAccount[F[_]] extends CoinStorage[F] {
  def getBalances: F[Set[CoinBalance]] = ???
}
