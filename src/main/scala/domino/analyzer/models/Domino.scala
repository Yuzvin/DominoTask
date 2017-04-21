package domino.analyzer.models

/**
  * Created by nazarko on 18.04.17.
  */

/**
  * Domino playing chip in normalized form: first side is less than second side
  * @param side1 first side of chip
  * @param side2 second side of chip
  */
case class Domino(side1: Int, side2: Int) {
  require(0 <= side1 && side1 <= 6, s"Wrong domino first side: $side1")
  require(0 <= side2 && side2 <= 6, s"Wrong domino second side: $side2")

  val isMirrored = side1 == side2
}

object Domino {
  /**
    * Creating domino chip in normalized form
    * @param side1
    * @param side2
    * @return
    */
  def apply(side1: Int, side2: Int): Domino = if(side1 <= side2) new Domino(side1, side2) else new Domino(side2, side1)
}
