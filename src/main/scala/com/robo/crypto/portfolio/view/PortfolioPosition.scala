package com.robo.crypto.portfolio.view

import cats.Monad

final case class PortfolioPosition(equities: Set[Equity])

case class Coin(symbol: String, name: String)

case class Equity()

case class CoinBalance(coin: Coin, qty: BigDecimal)

// sum the qty of each coins from all coin storage sources
final class PortfolioPositionSync[F[_]](implicit M: Monad[F],
                                        sources: Set[CoinStorage[F]]) {
  def initial: PortfolioPosition = {
    PortfolioPosition(Set.empty)
  }

  def update(old: PortfolioPosition): PortfolioPosition = {
    val snap = initial
    ???
  }
}
