package domino.analyzer.models.bazar

/**
  * Created by nazarko on 21.04.17.
  */

/**
  * Bazar analysis result
  * Bazar is unacceptable for domino game when it has dominos which couldn't be linked according to domino game rules
  * @param isAcceptable Bazar is acceptable for domino game when bazar isn't Cycled of Linear but all dominos are connected in right way according to domino game rules
  * @param isLinear Bazar is linear when all dominos could be foldable in linear way
  * @param isCycled Bazar is cycled when all dominos could be foldable in circular way
  */
case class BazarAnalysisResult(isAcceptable: Boolean, isLinear: Boolean, isCycled: Boolean)
