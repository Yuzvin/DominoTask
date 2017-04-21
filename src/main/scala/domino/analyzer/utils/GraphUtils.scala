package domino.analyzer.utils

import domino.analyzer.models.Domino
import domino.analyzer.models.bazar._
import domino.analyzer.models.graph.DominoGraphInfo

import scala.annotation.tailrec

/**
  * Created by nazarko on 19.04.17.
  */
/**
  * Graph utils
  */
object GraphUtils {

  /**
    * Key - count of dotes on single domino side
    * Value - list of other sides of domino
    */
  type DominoConnectionsTable = Map[Int, List[Int]]

  /**
    * Set of sides of dominos, which are mirrored in current bazar
    */
  type MirroredDominosSides = Set[Int]

  /**
    * Statistic for builded graph for it's verticals
    *
    * @param oddVerticalCount Odd vertical count
    * @param evenVerticalCount Event vertical count
    * @param mirroredEdgesCount Max count of linked edges for single mirrored domino in all grap
    */
  case class VerticalsStats(oddVerticalCount: Int, evenVerticalCount: Int, mirroredEdgesCount: Int)

  /**
    * Build domino graph representation as connections table according to domino game rules.
    * Non-mirrored domino could be added when:
    * a) count of all dominos in table with the same side as in current is odd
    * b) count of all dominos in table with the same side is even AND there is mirrored domino for this side in table of mirrored dominos
    * c) connection table is empty
    * Mirrored domino could be added when:
    * a) connection table is empty
    * b) count of all dominos with the same side non empty
    * @param bazar bazar
    * @return optional graph info, which contains all domino connections and mirrored sides of dominos. If graph couldn't be builded according to domino game rules - method returns None
    */
  def buildDominoConnectionsTable(bazar: List[Domino]): Option[DominoGraphInfo] = {

    /**
      * Tail-recursive method for building connections table
      * @param bazarCurr bazar
      * @param unLinked unlinked dominos
      * @param previousUnLinkedCount prevoius unlinked count (for preverting infinity loop when unlinked dominos still be unlinked after second pass)
      * @param currTable connection table
      * @param mirrors mirrored dominos sides
      * @return optional graph info
      */
    @tailrec
    def buildConnectionTableInner(bazarCurr: List[Domino],
                                  unLinked: List[Domino],
                                  previousUnLinkedCount: Int,
                                  currTable: DominoConnectionsTable,
                                  mirrors: MirroredDominosSides): Option[DominoGraphInfo] = bazarCurr match {
      case Nil if currTable.isEmpty                      => None
      case Nil if unLinked.isEmpty                       => Some(DominoGraphInfo(currTable, mirrors))
      case Nil if unLinked.size == previousUnLinkedCount => None
      case Nil                                           => buildConnectionTableInner(unLinked, Nil, unLinked.size, currTable, mirrors)
      case domino :: tail =>
        val Domino(side1, side2) = domino
        if (domino.isMirrored) {
          if (couldMirroredDominoBeAddedToConnectionTable(currTable, side1))
            buildConnectionTableInner(tail, unLinked, previousUnLinkedCount, currTable, mirrors + side1)
          else
            buildConnectionTableInner(tail, domino :: unLinked, previousUnLinkedCount, currTable, mirrors)
        } else {
          val sizeOfSide1Vertical = currTable.getOrElse(side1, Nil).size
          val sizeOfSide2Vertical = currTable.getOrElse(side2, Nil).size
          val isOddSizeForSide1   = isOdd(sizeOfSide1Vertical)
          val isOddSizeForSide2   = isOdd(sizeOfSide2Vertical)

          if (couldNonMirroredDominoBeAddedToConnectionsTable(currTable, mirrors, side1, isOddSizeForSide1) ||
              couldNonMirroredDominoBeAddedToConnectionsTable(currTable, mirrors, side2, isOddSizeForSide2))
            buildConnectionTableInner(tail,
                                      unLinked,
                                      previousUnLinkedCount,
                                      appendValuesToConnectionsTable(currTable, side1, side2),
                                      mirrors)
          else
            buildConnectionTableInner(tail, domino :: unLinked, previousUnLinkedCount, currTable, mirrors)

        }

    }

    buildConnectionTableInner(bazar, Nil, bazar.tail.size, Map.empty[Int, List[Int]], Set.empty[Int])

  }

  /**
    * Mirrored domino could be added to connection table when conn. table is empty OR count of already added dominos to table with this side non empty
    * @param currTable current connections table
    * @param side1 side of domino
    * @return is condition satisfied
    */
  private def couldMirroredDominoBeAddedToConnectionTable(currTable: DominoConnectionsTable, side1: Int): Boolean = {
    currTable.isEmpty || currTable.get(side1).nonEmpty
  }

  /**
    * Non-mirrored domino could be added when:
    * a) count of all dominos in table with the same side as in current is odd
    * b) count of all dominos in table with the same side is even AND there is mirrored domino for this side in table of mirrored dominos
    * c) connection table is empty
    * @param currTable current connections table
    * @param mirrors mirrored sides
    * @param side1 current side
    * @param isOddSizeForSide1 count of already added dominos with side
    * @return
    */
  private def couldNonMirroredDominoBeAddedToConnectionsTable(currTable: DominoConnectionsTable,
                                                              mirrors: MirroredDominosSides,
                                                              side1: Int,
                                                              isOddSizeForSide1: Boolean): Boolean = {
    isOddSizeForSide1 || !isOddSizeForSide1 && mirrors.contains(side1) || currTable.isEmpty
  }

  /**
    * Append side1 and side2 to connection table map, where key is opposite side
    * @param currTable current connections table
    * @param side1 side1
    * @param side2 opposite side
    * @return updated connections table
    */
  private def appendValuesToConnectionsTable(currTable: DominoConnectionsTable,
                                             side1: Int,
                                             side2: Int): Map[Int, List[Int]] = {
    currTable
      .updated(side1, side2 :: currTable.getOrElse(side1, Nil))
      .updated(side2, side1 :: currTable.getOrElse(side2, Nil))
  }

  private def isOdd(num: Int): Boolean = num % 2 != 0

  /**
    * Analyze Euler graph (https://en.wikipedia.org/wiki/Eulerian_path).
    * Graph is Cycled when:
    * a) all verticals are connected
    * b) all verticals have even degree
    * Graph is Linear when:
    * a) all verticals are connected
    * b) two vertivals have an odd degree and other verticals have even degree
    * In our case, if graph isn't Cycled or Linear, we try to analyze it according Domino game rules for possible dominos folding
    *
    * @param graph
    * @return
    */
  def analyzeGraph(graph: DominoGraphInfo): BazarAnalysisResult = {
    val verticalStats = getStatisticByGraphVerticals(graph)
    detectBazarType(verticalStats)
  }

  /**
    * Get statistic by all vertical from graph
    *
    * @param graph general graph
    * @return Count of even verticals, odd verticals and max edges connections for mirrored domino in general graph
    */
  private def getStatisticByGraphVerticals(graph: DominoGraphInfo): VerticalsStats = {
    graph.connections.foldLeft(VerticalsStats(0, 0, 0)) {
      case (vs @ VerticalsStats(odd, even, maxMirroredEdges), (vertical, edges)) =>
        val areEdgesSizeEven       = edges.size % 2 == 0
        val currMirroredEdges      = if (graph.mirrors.contains(vertical)) edges.size else 0
        val maxMirroredEdgesGlobal = if (currMirroredEdges > maxMirroredEdges) currMirroredEdges else maxMirroredEdges

        if (areEdgesSizeEven)
          vs.copy(evenVerticalCount = even + 1, mirroredEdgesCount = maxMirroredEdgesGlobal)
        else
          vs.copy(oddVerticalCount = odd + 1, mirroredEdgesCount = maxMirroredEdgesGlobal)
    }
  }

  /**
    * Analyze Euler graph.
    * If there are only 2 odd nodes - bazar is Cycled and bazar has single domino with/without mirrored
    * If there are 0 odd nodes - bazar is also cycled
    * If there are 2 odd nodes and max count of mirrored edges for domino is >= 0 and <= 2 - bazar is Linear
    * If max count of mirrored edges for domino is more than 4 and odd nodes count is more than 2 - bazar is Unacceptable according to domino game rules
    * If previous conditions aren't satisfied - bazar is Acceptable according to domino game rules
    *
    * @param verticalStats
    * @return BazarAnalysisResult, which contains info about solved analysis task: is bazar cycled, is bazar linear, is bazar acceptable according to domino game rules
    */
  private def detectBazarType(verticalStats: VerticalsStats): BazarAnalysisResult = {
    verticalStats match {
      case VerticalsStats(2, 0, _) => BazarAnalysisResultsAliases.Cycled
      case VerticalsStats(0, _, _) => BazarAnalysisResultsAliases.Cycled
      case VerticalsStats(2, _, mirroredEdges) if mirroredEdges >= 0 && mirroredEdges <= 2 =>
        BazarAnalysisResultsAliases.Linear
      case VerticalsStats(oddCount, _, mirroredEdges) if mirroredEdges > 4 && oddCount > 2 =>
        BazarAnalysisResultsAliases.Unacceptable
      case otherStats => BazarAnalysisResultsAliases.Acceptable
    }
  }

}
