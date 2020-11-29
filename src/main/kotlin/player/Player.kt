package player

import game.Board
import game.Cells

interface Player {
    fun selectMove(board: Board): Cells
}
