package main.kotlin

import main.kotlin.Board
import main.kotlin.Cells

interface Player {
    fun selectMove(board: Board): Cells
}
