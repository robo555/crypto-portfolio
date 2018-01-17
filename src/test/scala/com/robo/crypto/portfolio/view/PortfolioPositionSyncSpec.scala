package com.robo.crypto.portfolio.view

import org.scalatest.{FlatSpec, Matchers}

class PortfolioPositionSyncSpec extends FlatSpec with Matchers {
  type Id[T] = T

  trait TestData {
    val eth = Coin("ETH", "Ethereum")
    val xrb = Coin("XRB", "RaiBlock")
    val ven = Coin("VEN", "VE Chain")
    val bcc = Coin("BCC", "Bit Connect")

    val bal1 = CoinBalance(eth, Quantity(100))
    val wallet = Wallet("id", List(bal1))

    val bal2 = CoinBalance(xrb, Quantity(200))
    val bal3 = CoinBalance(eth, Quantity(300))
    val kucoinAcct = ExchangeAccount("id", List(bal2, bal3))

    val bal4 = CoinBalance(ven, Quantity(400))
    val bal5 = CoinBalance(xrb, Quantity(500))
    val binanceAcct = ExchangeAccount("id", List(bal4, bal5))

    val initial = PortfolioPosition(Seq.empty)

    implicit val repository: CoinStorageRepository[Id] =
      new CoinStorageRepository[Id] {
        override def getAll = List(wallet, kucoinAcct, binanceAcct)
      }
    val sync = new PortfolioPositionSync[Id]()
  }

  "Portfolio Position" should "initialise with empty list of equities" in new TestData {
    val portfolio = sync.initial
    portfolio.equities.isEmpty shouldBe true
  }

  it should "sum the quantity of coins from all sources" in new TestData {
    val updated = sync.update(initial)
    verifyBalance(updated, eth, Quantity(400))
    verifyBalance(updated, xrb, Quantity(700))
  }

  private def verifyBalance(portfolio: PortfolioPosition,
                            coin: Coin,
                            expectedQty: Quantity): Unit = {
    val maybeBal = portfolio.equities.find(equity => equity.coin == coin)
    maybeBal match {
      case Some(bal) => bal.qty shouldBe expectedQty
      case _         => assert(false, s"Coin ${coin} is not in portfolio")
    }
  }
}
