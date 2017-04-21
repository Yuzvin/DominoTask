package domino.analyzer

import domino.analyzer.models.bazar.BazarAnalysisResult
import domino.analyzer.utils.BazarUtils

import scala.util.Try

/**
  * Created by WriterMix on 21.04.2017.
  */
object Main extends App {
  Try {
    require(args.length > 0, "Argument list is empty")
    args.toList match {
      case "-N" :: count :: tail => Right(BazarUtils.generateBazar(count.toInt))
      case _                     => Left("Arguments are incorrect, size of bazar entered incorrectly")
    }
  }.recover{
    case e: Throwable =>
      Left(e.getMessage)
  }.get match {
    case Right(bazar) =>
      println(s"Generated bazar: $bazar")
      val BazarAnalysisResult(isAcceptable, isLinear, isCycled) = BazarUtils.analyseBazar(bazar.toList)
      println(s"Bazar analisys result: \n" +
        s"Is bazar acceptable: $isAcceptable \n" +
        s"Is bazar linear: $isLinear \n" +
        s"Is bazar cycled: $isCycled")
    case Left(errorMessage) =>
      println(s"$errorMessage")
  }
}
