package game

import player.*
import player.evaluator.EvaluatePosition

fun main(args: Array<String>) {
    val board = Board()
    var gameStatus = board.getGameStatus()
    val showBoard = ShowBoard()

    //счетчик ходов
    var counter = 0

    //Здесь выбираем тех, кто будет играть
    fun getPlayer(isWhite: Boolean): Player {
        if (isWhite) {
            return SmartBot(EvaluatePosition(), 5)
        }
        return AdvancedBot(EvaluatePosition()) // SmartBot(4)
    }

    // Первоначальной расположение фигур
    println()
    println(" ".repeat(7) + "\u001B[31m!!!Игра началась!!!\u001B[0m")
    showBoard.printBoard(board)

    val whitePlayer = getPlayer(true)
    val blackPlayer = getPlayer(false)

    //Реализация самой игры
    while (gameStatus != GameStatus.GameOver) {

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
        showBoard.printBoard(board)
        counter++
    }

    println()
    println("В партии было сделано $counter ходов")
}
