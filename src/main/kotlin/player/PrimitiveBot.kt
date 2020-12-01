package player

import game.Board
import game.Cells
import java.lang.IllegalStateException
import java.util.concurrent.ThreadLocalRandom

/**
 * Данный бот максимально примитивный, он просто выбирает рандомную клетку для хода из списка возможных ходов
 */

class PrimitiveBot : Player {

    override fun selectMove(board: Board): Cells {
        val validMoves = board.getValidMoves(board.isWhiteTurn())

        if (validMoves.isEmpty())
            throw IllegalStateException("Невозможная ситуация")

        if (validMoves.size == 1)
            return validMoves.first()

        val index = ThreadLocalRandom.current().nextInt(0, validMoves.size - 1)
        return validMoves.toList()[index]
    }

    override fun name(): String = "primitive"
}