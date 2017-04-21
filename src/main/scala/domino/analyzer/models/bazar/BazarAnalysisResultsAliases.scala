package domino.analyzer.models.bazar

/**
  * Created by nazarko on 21.04.17.
  */

/**
  * Short bazar analysis result aliases
  */
object BazarAnalysisResultsAliases {
  val Cycled       = BazarAnalysisResult(isAcceptable = true, isLinear = true, isCycled = true)
  val Linear       = BazarAnalysisResult(isAcceptable = true, isLinear = true, isCycled = false)
  val Acceptable   = BazarAnalysisResult(isAcceptable = true, isLinear = false, isCycled = false)
  val Unacceptable = BazarAnalysisResult(isAcceptable = false, isLinear = false, isCycled = false)

}
