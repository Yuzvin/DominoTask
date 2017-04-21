package domino.analyzer

import domino.analyzer.models.Domino
import domino.analyzer.models.bazar._
import domino.analyzer.utils.{BazarUtils, GraphUtils}
import org.scalatest.{Matchers, WordSpec}

import scala.util.Try

/**
  * Created by WriterMix on 19.04.2017.
  */
class BazarGeneratorTest extends WordSpec with Matchers {

  "Bazar generator" should {
    "generate bazar with correct size" in {
      val bazarSize = 7
      val bazar: Set[Domino] = BazarUtils.generateBazar(7)
      bazar.size should equal(7)
    }
    "don't generate bazar with incorrect size" in {
      val bazarSize = 77
      val bazarTry = Try(BazarUtils.generateBazar(bazarSize))
      bazarTry.isSuccess should equal(false)
    }


  }

}
