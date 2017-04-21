package domino.analyzer

import domino.analyzer.models.Domino
import domino.analyzer.utils.{BazarUtils, GraphUtils}
import org.scalatest.{FreeSpec, Matchers, WordSpec}

/**
  * Created by nazarko on 19.04.17.
  */
class GraphBuilderTest extends WordSpec with Matchers {

  "GraphBuilder" should {
    "build graph from bazar" in {
      val bazar: List[Domino] = List(Domino(1,2), Domino(2,2), Domino(2,3))
      val graph = GraphUtils.buildDominoConnectionsTable(bazar)
      graph.isDefined should equal(true)
    }
    "not build graph from bazar with unlinked dominos" in {
      val bazar: List[Domino] = List(Domino(1,2), Domino(2,2), Domino(2,3), Domino(3,4), Domino(5,6))
      val graph = GraphUtils.buildDominoConnectionsTable(bazar)

      graph.isDefined should equal(false)
    }
    "not build graph from only mirrored dominos" in {
      val bazar = List(Domino(1,1), Domino(2,2), Domino(3,3), Domino(4,4))
      val graph = GraphUtils.buildDominoConnectionsTable(bazar)

      graph.isDefined should equal(false)
    }
    "not build graph with incorrect bazar" in {
      val bazar = List(Domino(1,2), Domino(2,3), Domino(2,4), Domino(4,6))
      val graph = GraphUtils.buildDominoConnectionsTable(bazar)
      graph.isDefined should equal(false)
    }

    "build graph with correct bazar" in {
      val bazar = List(Domino(1,2), Domino(2,3), Domino(2,2), Domino(2,4), Domino(4,6))
      val graph = GraphUtils.buildDominoConnectionsTable(bazar)
      graph.isDefined should equal(true)
    }
    "not build graph with bazar with unlinked dominos" in {
      val bazar: List[Domino] = List(Domino(1,2), Domino(2,2), Domino(2,3), Domino(3,4), Domino(5,6))
      val graph = GraphUtils.buildDominoConnectionsTable(bazar)

      graph.isDefined should equal(false)
    }
  }

}
