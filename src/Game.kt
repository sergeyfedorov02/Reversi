fun main(args : Array<String>) {
    val board = Board()
    var gameStatus = board.getGameStatus()

    //счетчик ходов
    var counter = 0

    fun whoWasWin(): Pair<String, Pair<Int, Int>> {

        val whites = board.getWhite().size
        val blacks = board.getBlack().size

        return when{
            whites > blacks -> Pair("Белыми", Pair(whites, blacks))
            blacks > whites -> Pair("Черными", Pair(whites, blacks))
            else -> Pair("Ничья", Pair(whites, blacks))
        }
    }

    //Функция, которая рисует текущую доску + состояние игры
    fun showBoard() {

        //Если игра еще не закончилась
        if (gameStatus != GameStatus.GameOver) {
            println()

            val status = if (gameStatus == GameStatus.WhiteTurn )
                "Ход белых"
            else "Ход черных"

            println(" ".repeat(3) + "\u001B[35mТекущий статус игры: $status\u001B[0m")
        } else {

            val whoWin = whoWasWin()
            val whichTeamWin = whoWin.first
            val points = Pair(whoWin.second.first,whoWin.second.second)

            println()
            println(" ".repeat(4) + "\u001B[31m!!!Игра закончилась!!!\u001B[0m")

            if (whichTeamWin != "Ничья") {
                println("\u001B[31mПобедил игрок с $whichTeamWin фишками\u001B[0m")
            } else {
                println(" ".repeat(8) + "\u001B[31mУстановилась $whichTeamWin\u001B[0m")
            }

            println()
            print("Белых фишек: ${points.first}  Черных фишек: ${points.second}")
            println()
        }

        println()

        //Функция для отображения букв на доске
        fun printLetters(){
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

        for (i in 0 until 8) {
            println()

            //Выводим цифры на доску
            val number = i + 1
            print("$number ")

            for (j in 0 until 8) {
                when(Cells.fromCell(i, j)) {
                    in whites -> print("\u001B[34m Б  \u001B[0m")
                    in blacks -> print("\u001B[33m Ч  \u001B[0m")
                    else -> print(" -  ")
                }
            }
        }
        println()
    }

    //Здесь выбираем тех, кто будет играть
    fun getPlayer(isWhite: Boolean) : Player {
        if (isWhite) {
            return AdvancedBot()
        }
        return PrimitiveBot()
    }

    // Первоначальной расположение фигур
    println()
    println(" ".repeat(7) + "\u001B[31m!!!Игра началась!!!\u001B[0m")
    showBoard()

    val whitePlayer = getPlayer(true)
    val blackPlayer = getPlayer( false)

    //Реализация самой игры
    while(gameStatus != GameStatus.GameOver) {

        println()
        print("Выберите клетку куда будете ходить: ")

        val player = if (gameStatus == GameStatus.WhiteTurn)
            whitePlayer
        else blackPlayer

        //Выбираем клетку для хода
        val currentCell = player.selectMove(board)

        //Совершаем ход
        val currentMove = board.move(currentCell)

        //Вбита клетка, куда нельзя походить , но валидные ходы присутствуют
        if (!currentMove) {

            println("Вы не можете походить в клетку \u001B[32m${currentCell}\u001B[0m, выберите другую")
            continue
        }

        println("Выбран ход $currentCell")
        gameStatus = board.getGameStatus()
        showBoard()
        counter++
    }

    println()
    println("В партии было сделано $counter ходов")
}
