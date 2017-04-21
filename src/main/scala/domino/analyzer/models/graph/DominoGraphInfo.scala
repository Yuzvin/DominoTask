package domino.analyzer.models.graph

import domino.analyzer.utils.GraphUtils.{DominoConnectionsTable, MirroredDominosSides}

/**
  * Created by nazarko on 21.04.17.
  */

/**
  * Graph info, which contains domino connections table and set of mirrored dominos side
  *
  * @param connections connections table of each side with opposite side of domino
  * @param mirrors list of sides, which have mirrored dominos in bazar
  */
case class DominoGraphInfo(connections: DominoConnectionsTable, mirrors: MirroredDominosSides)
