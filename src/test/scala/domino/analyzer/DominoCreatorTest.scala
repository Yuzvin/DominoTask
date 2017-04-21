package domino.analyzer

import domino.analyzer.models.Domino
import org.scalatest.{Matchers, WordSpec}

/**
  * Created by WriterMix on 19.04.2017.
  */
class DominoCreatorTest extends WordSpec with Matchers {
  "Domino creator" should {
    "create domino with normalizing" in {
      val domino = Domino(5,1)
      domino should equal(Domino(1,5))
    }
  }
}
