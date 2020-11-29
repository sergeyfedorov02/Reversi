package player

import game.Board
import game.Cells

/**
 * Данный класс предназначен для реализации игры человека
 */

class HumanPlayer : Player {
    override fun selectMove(board: Board): Cells {
        var yourCell = readLine()

        while (!yourCell!!.matches(Regex("""([a-h]\s*[1-8])"""))) {
            println()
            print("Клетки не существует на шахматной доске, выберите другую клетку: ")
            yourCell = readLine()
        }

        return Cells.valueOf(yourCell.toUpperCase())
    }

}