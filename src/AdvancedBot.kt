import java.util.concurrent.ThreadLocalRandom

/**
 * Данный бот использует алгоритм оценки позиций для выбора своего хода
 * Просчитывает все возможные ходы на данном этапе и выбирает наилучший
 */

class AdvancedBot : Player {
    override fun selectMove(board: Board): Cells {

        var validMoves = board.getValidMoves(board.isWhiteTurn())

        if (validMoves.isEmpty())
            throw IllegalStateException("Невозможная ситуация")

        if (validMoves.size == 1)
            return validMoves.first()

        //Лист в котором будут храниться клетки валидных ходов с их оценкой
        val listOfEvaluatePosition = mutableListOf<Pair<Cells, Double>>()

        while (validMoves.isNotEmpty()) {
            val cell = validMoves.first()
            val evaluatePos = EvaluatePosition()

            listOfEvaluatePosition.add(Pair(cell, evaluatePos.evaluatePosition(board,cell)))

            validMoves = validMoves.drop(1).toSet()
        }

        //Если получится несколько клеток с одинаковым и наибольшим значением -> из них выберем рандомную
        //Отсортируем список по убыванию
        var resultList = listOfEvaluatePosition.sortedBy { it.second }.reversed()

        //Получим максимальное значение и отфильтруем список, чтобы остались только одинаковые и равные наибольшему
        val maxValue = resultList.first().second
        resultList = listOfEvaluatePosition.filter { it.second == maxValue }

        if (resultList.size == 1)
            return resultList.first().first

        //Выберем из них рандомное
        val index = ThreadLocalRandom.current().nextInt(0, resultList.size - 1)
        return resultList.toList()[index].first

    }

}