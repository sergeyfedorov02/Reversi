package main.kotlin

class MiniMax {

    fun solve(board: Board, depth: Int, whatColorIsMine: Boolean): Cells {
        var bestResult = Double.MIN_VALUE

        val validMoves = board.getValidMoves(whatColorIsMine)

        var bestMove = validMoves.first()

        for (move in validMoves) {

            //Делаем ход на копии доски
            val newBoard = board.makeCopyAndMove(move)

            //Рекурсивно ищем вес данного хода при помощи miniMaxAlphaBetta
            val childResult =
                miniMaxAlphaBetta(
                    newBoard,
                    whatColorIsMine,
                    depth - 1,
                    false,
                    Double.MIN_VALUE,
                    Double.MAX_VALUE
                )

            if (childResult > bestResult) {
                bestResult = childResult
                bestMove = move
            }

        }

        return bestMove
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

        val evaluatePos = EvaluatePosition()

        //Если игра закончилась или же мы провели все рассчеты для заданной глубины
        if (depth == 0 || board.getGameStatus() == GameStatus.GameOver) {
            return evaluatePos.evaluatePosition(board, whatColorIsMine)
        }

        val enemyColor = !whatColorIsMine

        //Если нет валидных ходов -> теряем ход
        if ((max && board.getValidMoves(whatColorIsMine).isEmpty() || (!max && board.getValidMoves(enemyColor).isEmpty()))) {
            return miniMaxAlphaBetta(board, whatColorIsMine, depth - 1, !max, newAlpha, newBetta)
        }

        var result: Double

        if (max) {
            result = Double.MIN_VALUE

            val listOfMyValidMoves = board.getValidMoves(whatColorIsMine)

            //Максимизация (для моего хода)
            for (move in listOfMyValidMoves) {

                //Делаем ход на копии доски
                val newBoard = board.makeCopyAndMove(move)

                //рекурсивно вызываем miniMaxAlphaBetta
                val currentResult =
                    miniMaxAlphaBetta(newBoard, whatColorIsMine, depth - 1, false, newAlpha, newBetta)

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

                //Делаем ход на копии доски
                val newBoard = board.makeCopyAndMove(move)

                //рекурсивно вызываем miniMaxAlphaBetta
                val currentResult =
                    miniMaxAlphaBetta(newBoard, whatColorIsMine, depth - 1, true, newAlpha, newBetta)

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