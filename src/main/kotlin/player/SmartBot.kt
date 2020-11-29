package player

import game.Board
import game.Cells
import player.evaluator.Evaluator

class SmartBot(private val evaluate: Evaluator, private val depth: Int = 6) : Player {
    override fun selectMove(board: Board): Cells {

        val validMoves = board.getValidMoves(board.isWhiteTurn())

        if (validMoves.isEmpty())
            throw IllegalStateException("Невозможная ситуация")

        if (validMoves.size == 1)
            return validMoves.first()

        val miniMax = MiniMax(evaluate)

        return miniMax.solve(board, depth, board.isWhiteTurn())
    }

}