package domino.analyzer.utils

import domino.analyzer.models.Domino

import scala.util.Random

/**
  * Created by nazarko on 18.04.17.
  */

/**
  * Domino utils
  */
object DominoUtils {

  val random = new Random()

  /**
    * Generate random domino
    * @return
    */
  def generateRandomDomino: Domino = Domino(random.nextInt(7), random.nextInt(7))

}
