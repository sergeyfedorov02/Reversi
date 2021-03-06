package player

import game.Board
import game.Cells
import game.GameStatus
import player.evaluator.Evaluator

class MiniMax(private val evaluate: Evaluator) {

    fun solve(board: Board, depth: Int, whatColorIsMine: Boolean): Cells {
        var bestResult = -Double.MAX_VALUE  // Минимальное отрицательное значение типа double

        val validMoves = board.getValidMoves(whatColorIsMine)

        var bestMove = validMoves.first()

        for (move in validMoves) {

            //Запоминаем чей сейчас ход
            val currentStatus = board.getGameStatus()

            //Делаем ход на копии доски
            val newBoard = board.makeCopyAndMove(move)

            val gameStatus = newBoard.getGameStatus()

            val newMax = currentStatus == gameStatus //наш ход продолжается -> делаем максимизацию

            //Рекурсивно ищем вес данного хода при помощи miniMaxAlphaBetta
            val childResult =
                miniMaxAlphaBetta(
                    newBoard,
                    whatColorIsMine,
                    depth - 1,
                    newMax,
                    -Double.MAX_VALUE,
                    Double.MAX_VALUE
                )

            if (childResult > bestResult) {
                bestResult = childResult
                bestMove = move
            }

        }

        return bestMove
    }

    private fun whoWin(board: Board, whatColorIsMine: Boolean, first: Int, second: Int): Double {

        return when {
            first > second -> Double.MAX_VALUE
            first < second -> -Double.MAX_VALUE
            else -> evaluate.evaluatePosition(board, whatColorIsMine)
        }

    }

    private fun miniMaxAlphaBetta(
        board: Board,
        whatColorIsMine: Boolean,
        depth: Int,
        max: Boolean,
        alpha: Double,
        betta: Double
    ): Double {

        var newAlpha = alpha
        var newBetta = betta

        //Если игра закончилась, то найдем вес парти
        // если мы проиграли -> минимальный вес
        // если выиграли -> максимальный вес
        if (board.getGameStatus() == GameStatus.GameOver) {
            val white = board.getWhite().size
            val black = board.getBlack().size

            return if (whatColorIsMine) {
                whoWin(board, whatColorIsMine, white, black)
            } else {
                whoWin(board, whatColorIsMine, black, white)
            }

        }

        //все рассчеты для заданной глубины проведены -> возвращаем вес позиции
        if (depth == 0) {
            return evaluate.evaluatePosition(board, whatColorIsMine)
        }

        val enemyColor = !whatColorIsMine

        var result: Double

        if (max) {
            result = -Double.MAX_VALUE

            val listOfMyValidMoves = board.getValidMoves(whatColorIsMine)

            //Максимизация (для моего хода)
            for (move in listOfMyValidMoves) {

                //Запоминаем чей сейчас ход
                val currentStatus = board.getGameStatus()

                //Делаем ход на копии доски
                val newBoard = board.makeCopyAndMove(move)

                val gameStatus = newBoard.getGameStatus()

                //Проверяем, была ли передача хода
                val newMax = currentStatus == gameStatus

                //рекурсивно вызываем miniMaxAlphaBetta
                val currentResult =
                    miniMaxAlphaBetta(newBoard, whatColorIsMine, depth - 1, newMax, newAlpha, newBetta)

                if (currentResult > result) {
                    result = currentResult
                }

                //обновление alpha
                if (result > newAlpha) {
                    newAlpha = result
                }

                //betta отсечение
                if (newBetta <= newAlpha)
                    break
            }
        } else {
            //Минимизация (для хода соперника)
            result = Double.MAX_VALUE

            val listOfEnemyValidMoves = board.getValidMoves(enemyColor)

            for (move in listOfEnemyValidMoves) {

                //Запоминаем чей сейчас ход
                val currentStatus = board.getGameStatus()

                //Делаем ход на копии доски
                val newBoard = board.makeCopyAndMove(move)

                val gameStatus = newBoard.getGameStatus()

                //Проверяем, была ли передача хода
                val newMin = currentStatus == gameStatus

                //рекурсивно вызываем miniMaxAlphaBetta
                val currentResult =
                    miniMaxAlphaBetta(newBoard, whatColorIsMine, depth - 1, !newMin, newAlpha, newBetta)

                if (currentResult < result) {
                    result = currentResult
                }

                //обновление betta
                if (result < newBetta) {
                    newBetta = result
                }

                //alpha отсечение
                if (newBetta <= newAlpha)
                    break
            }
        }

        return result
    }

}