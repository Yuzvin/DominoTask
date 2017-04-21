package domino.analyzer

import domino.analyzer.models.Domino
import domino.analyzer.models.bazar._
import domino.analyzer.utils.{BazarUtils, GraphUtils}
import org.scalatest.{Matchers, WordSpec}

/**
  * Created by WriterMix on 19.04.2017.
  */
class BazarAnalyzerTest extends WordSpec with Matchers {

  "Bazar analyzer" should {
    "detect Cycled bazar" in {
      val bazar = List(Domino(2,2), Domino(2,3), Domino(3,4), Domino(4,5), Domino(2,5))
      val bazarAnalysisResult: BazarAnalysisResult = BazarUtils.analyseBazar(bazar)
      bazarAnalysisResult should equal(BazarAnalysisResultsAliases.Cycled)
    }
    "detect Linear bazar" in {
      val bazar = List(Domino(2,2), Domino(2,3), Domino(3,4), Domino(4,5), Domino(5,5))
      val bazarAnalysisResult: BazarAnalysisResult = BazarUtils.analyseBazar(bazar)
      bazarAnalysisResult should equal(BazarAnalysisResultsAliases.Linear)
    }
    "detect Acceptable bazar" in {
      val bazar = List(Domino(2,2), Domino(2,3), Domino(2,4), Domino(2,5), Domino(2,6), Domino(3,4), Domino(4,5), Domino(5,5))
      val bazarAnalysisResult: BazarAnalysisResult = BazarUtils.analyseBazar(bazar)
      bazarAnalysisResult should equal (BazarAnalysisResultsAliases.Acceptable)
    }
    "detect full bazar as Cycled" in {
      val size = 28
      val bazar = BazarUtils.generateBazar(size)
      val graph = GraphUtils.buildDominoConnectionsTable(bazar.toList)
      GraphUtils.analyzeGraph(graph.get) should equal(BazarAnalysisResultsAliases.Cycled)
    }
    "detect Unacceptable bazar" in {
      val bazar = List(Domino(1,2), Domino(2,2), Domino(2,3), Domino(3,1), Domino(4,5))
      val bazarAnalysisResult: BazarAnalysisResult = BazarUtils.analyseBazar(bazar)
      bazarAnalysisResult should equal (BazarAnalysisResultsAliases.Unacceptable)
    }
    "detect Unacceptable bazar - 2" in {
      val bazar = List(Domino(1,2), Domino(2,3), Domino(2,4), Domino(4,6))
      val bazarAnalysisResult: BazarAnalysisResult = BazarUtils.analyseBazar(bazar)
      bazarAnalysisResult should equal (BazarAnalysisResultsAliases.Unacceptable)
    }
    "detect bazar with one double as Acceptable" in {
      val bazar = List(Domino(1,2), Domino(2,3), Domino(2,4), Domino(4,6), Domino(2, 2))
      val bazarAnalysisResult: BazarAnalysisResult = BazarUtils.analyseBazar(bazar)
      bazarAnalysisResult should equal (BazarAnalysisResultsAliases.Acceptable)
    }
  }

}
