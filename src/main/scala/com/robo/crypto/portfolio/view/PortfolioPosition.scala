package com.robo.crypto.portfolio.view

import cats._
import cats.implicits._

final case class PortfolioPosition(equities: Seq[Equity])

case class Coin(symbol: String, name: String)
case class Currency(value: BigDecimal) extends AnyVal
case class Quantity(value: BigDecimal) extends AnyVal
case class Equity(coin: Coin,
                  qty: Quantity,
                  price: Currency,
                  marketValue: Currency)
case class CoinBalance(coin: Coin, qty: Quantity)

// We create a module to contain our main business logic.
// A module is pure and depends only on other modules, algebras and pure functions.

final class PortfolioPositionSync[F[_]](implicit M: Monad[F],
                                        sources: CoinStorageRepository[F]) {

  def initial: PortfolioPosition = {
    PortfolioPosition(Seq.empty)
  }

  // sum the qty of each coins from all coin storage sources
  def update(old: PortfolioPosition): F[PortfolioPosition] = {
    for {
      storages <- sources.getAll
      balances = storages.map(storage => storage.balances)
      grouped = groupBalancesByCoin(balances)
      equities = getEquities(grouped)
    } yield PortfolioPosition(equities)
  }

  def groupBalancesByCoin(
      balances: Seq[Seq[CoinBalance]]): Map[Coin, Quantity] = {
    balances.flatten.groupBy(_.coin).mapValues(sumBalances)
  }

  def sumBalances(list: Seq[CoinBalance]): Quantity = {
    Quantity(list.map(_.qty.value).sum)
  }

  def getEquities(coins: Map[Coin, Quantity]): List[Equity] = {
    coins.map {
      case (coin, qty) => Equity(coin, qty, Currency(0), Currency(0))
    }.toList
  }

}
