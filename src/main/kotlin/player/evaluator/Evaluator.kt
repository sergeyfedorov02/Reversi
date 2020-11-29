package player.evaluator

import game.Board

interface Evaluator {
    fun evaluatePosition(board: Board, whatColorIsMine: Boolean): Double

}