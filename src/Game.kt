fun main(args : Array<String>) {
    val b = Board()
    val x = b.getBoard()


    //Функция, которая рисует текущую доску + состояние игры
    fun showBoard() {
        println()
        val gameStatus = b.getGameStatus()
        println(" ".repeat(3) + "Текущий статус игры: " + gameStatus)
        println()

        //Выводим буквы на доску
        val listOfLetters = listOf("A", "B", "C", "D", "E", "F", "G", "H")
        for (i in 0 until 8) {
            val element = listOfLetters[i]
            print(" ".repeat(3) + element)
        }

        //Выводим текущее положение фишек на доске
        for (i in 0 until 8) {
            println()

            //Выводим цифры на доску
            val number = i + 1
            print("$number ")

            for (j in 0 until 8) {
                when(x[i][j]) {
                    Board.CellStatus.White ->  print("\u001B[34m Б \u001B[0m")
                    Board.CellStatus.Black -> print("\u001B[33m Ч \u001B[0m")
                    else -> print(" - ")
                }
                print(" ")
            }
        }
        println()
    }

    showBoard()


    val list = b.getValidMoves()
    println()
    list.forEach {
        println("Клетка с координатами: " + it.h + " " + it.v)
    }

    println()
    println(b.getGameStatus())

}
