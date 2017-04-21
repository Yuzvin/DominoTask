package domino.analyzer

import domino.analyzer.models.Domino
import domino.analyzer.models.bazar.BazarAnalysisResultsAliases
import domino.analyzer.models.graph.DominoGraphInfo
import domino.analyzer.utils.GraphUtils
import org.scalatest.{Matchers, WordSpec}

/**
  * Created by WriterMix on 19.04.2017.
  */
class GraphAnalyzerTest extends WordSpec with Matchers {

  "GraphAnalyzer" should {
    "detect cycled graph" in {
      val graphInfo = DominoGraphInfo(
        mirrors = Set(2),
        connections = Map(
          1 -> List(2, 3),
          2 -> List(1, 3),
          3 -> List(2, 1)
        )
      )
      val bazarType = GraphUtils.analyzeGraph(graphInfo)
      bazarType should equal(BazarAnalysisResultsAliases.Cycled)
    }
    "detect linear graph" in {
      val graphInfo = DominoGraphInfo(
        mirrors = Set(2),
        connections = Map(
          1 -> List(2, 3),
          2 -> List(1, 3),
          3 -> List(2, 1, 4),
          4 -> List(3)
        )
      )
      val bazarType = GraphUtils.analyzeGraph(graphInfo)
      bazarType should equal(BazarAnalysisResultsAliases.Linear)
    }
    "detect unacceptable graph" in {
      val graphInfo = DominoGraphInfo(
        mirrors = Set(2),
        connections = Map(
          1 -> List(2,3),
          2 -> List(1,3,4,5,6),
          3 -> List(2,1,4),
          4 -> List(3),
          5 -> List(2),
          6 -> List(2)
        )
      )
      val bazarType = GraphUtils.analyzeGraph(graphInfo)
      bazarType should equal(BazarAnalysisResultsAliases.Unacceptable)
    }
    "detect acceptable graph" in {

      val graphInfo = DominoGraphInfo(
        mirrors = Set(2),
        connections = Map(
          1 -> List(2,6),
          2 -> List(1,3,4,5,6),
          3 -> List(2,4),
          4 -> List(3,2),
          5 -> List(2),
          6 -> List(2,1)
        )
      )

      val bazarType = GraphUtils.analyzeGraph(graphInfo)
      bazarType should equal(BazarAnalysisResultsAliases.Acceptable)
    }
  }

}
