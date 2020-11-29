package game

class ShowBoard() {

    private fun whoWasWin(board: Board): Pair<String, Pair<Int, Int>> {

        val whites = board.getWhite().size
        val blacks = board.getBlack().size

        return when {
            whites > blacks -> Pair("Белыми", Pair(whites, blacks))
            blacks > whites -> Pair("Черными", Pair(whites, blacks))
            else -> Pair("Ничья", Pair(whites, blacks))
        }
    }

    //Функция, которая рисует текущую доску + состояние игры
    fun printBoard(board: Board) {

        //Если игра еще не закончилась
        if (board.getGameStatus() != GameStatus.GameOver) {
            println()

            val status = if (board.getGameStatus() == GameStatus.WhiteTurn)
                "Ход белых"
            else "Ход черных"

            println(" ".repeat(3) + "\u001B[35mТекущий статус игры: $status\u001B[0m")
        } else {

            val whoWin = whoWasWin(board)
            val whichTeamWin = whoWin.first
            val points = Pair(whoWin.second.first, whoWin.second.second)

            println()
            println(" ".repeat(4) + "\u001B[31m!!!Игра закончилась!!!\u001B[0m")

            if (whichTeamWin != "Ничья") {
                println("\u001B[31mПобедил игрок с $whichTeamWin фишками\u001B[0m")
            } else {
                println(" ".repeat(6) + "\u001B[31mУстановилась $whichTeamWin\u001B[0m")
            }

            println()
            print("Белых фишек: ${points.first}  Черных фишек: ${points.second}")
            println()
        }

        println()

        //Функция для отображения букв на доске
        fun printLetters() {

            val listOfLetters = listOf("A", "B", "C", "D", "E", "F", "G", "H")
            for (i in 0 until 8) {
                val element = listOfLetters[i]
                print(" ".repeat(3) + element)
            }
        }

        //выводим буквы на доску
        printLetters()

        //Выводим текущее положение фишек на доске
        val whites = board.getWhite()
        val blacks = board.getBlack()
        val validMoves = board.getValidMoves(board.isWhiteTurn())

        for (i in 0 until 8) {
            println()

            //Выводим цифры на доску
            val number = i + 1
            print("$number ")

            for (j in 0 until 8) {
                when (Cells.fromCell(i, j)) {
                    in whites -> print("\u001B[34m Б  \u001B[0m")
                    in blacks -> print("\u001B[33m Ч  \u001B[0m")
                    in validMoves -> print(" +  ")
                    else -> print(" -  ")
                }
            }
        }
        println()
    }


}