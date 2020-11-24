/**
 * Данный бот использует алгоритм оценки позиций для выбора своего хода
 */

class AdvancedBot : Player {
    override fun selectMove(board: Board): Cells {

        //Используем класс оценки позиций
        val evaluatePos = EvaluatePosition()
        return evaluatePos.evaluatePosition(board)
    }

}