package domino.analyzer.utils
import domino.analyzer.models.Domino
import domino.analyzer.models.bazar.{BazarAnalysisResult, BazarAnalysisResultsAliases}
import domino.analyzer.models.graph.DominoGraphInfo

import scala.annotation.tailrec
import scala.collection.immutable.IndexedSeq

/**
  * Created by nazarko on 18.04.17.
  */

/**
  * Bazar utils
  */
object BazarUtils {

  /**
    * Generate set of dominos (bazar) according to domino game rules
 *
    * @param size size of bazar
    * @return bazar
    */
  def generateBazar(size: Int): Set[Domino] = {
    require(2 <= size && size <= 28, s"Domino size: $size is not in range [2, 28]")

    /**
      * Bazar generator, implemented in tail-recursive way
 *
      * @param size size of bazar
      * @param acc bazar set accumulator
      * @return set of dominos (in normalized form) without dubs
      */
    @tailrec
    def innerGenBazar(size: Int, acc: Set[Domino]): Set[Domino] = size match {
      case 0 => acc
      case currSize =>
        val domino = DominoUtils.generateRandomDomino
        if (acc.contains(domino)) {
          innerGenBazar(size, acc)
        } else {
          innerGenBazar(size - 1, acc + domino)
        }
    }

    innerGenBazar(size, Set.empty[Domino])
  }

  /**
    * Get bazar analisys result (@see BazarAnalysisResult) according to type of Eulers Graph or ability to fold all dominos without remnant
    *
    * @param bazar bazar
    * @return BazarType
    */
  def analyseBazar(bazar: List[Domino]): BazarAnalysisResult = {
    val graph: Option[DominoGraphInfo] = GraphUtils.buildDominoConnectionsTable(bazar)
    graph.map(GraphUtils.analyzeGraph).getOrElse(BazarAnalysisResultsAliases.Unacceptable)
  }

}
