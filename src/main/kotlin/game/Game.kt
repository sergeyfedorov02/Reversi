package game

import player.*
import player.evaluator.EvaluatePosition
import player.evaluator.EvaluatorSettings
import java.io.File
import java.math.RoundingMode
import java.text.DecimalFormat

fun main(args: Array<String>) {
    val settings = EvaluatorSettings()
    val check = settings.readFromFile("input/evaluateCoefficients.txt")

    if (!check) {
        println("Файл, из которого вы хотите взять коэффициенты, не существует!")
    }

    val evaluate = EvaluatePosition(settings)

    fun getPlayer(playName: String): Player {
        return when (playName) {
            "smart2" -> SmartBot(evaluate, 2)
            "smart3" -> SmartBot(evaluate, 3)
            "smart4" -> SmartBot(evaluate, 4)
            "smart5" -> SmartBot(evaluate, 5)
            "smart6" -> SmartBot(evaluate, 6)
            "human" -> HumanPlayer()
            "advanced" -> AdvancedBot(evaluate)
            else -> PrimitiveBot()
        }
    }

    var whitePlayer: Player = PrimitiveBot()
    var blackPlayer: Player = PrimitiveBot()

    if (args.isNotEmpty()) {
        whitePlayer = getPlayer(args[0])
    }

    if (args.size > 1) {
        blackPlayer = getPlayer(args[1])
    }

    val time = System.currentTimeMillis()
    val board = Board()
    var gameStatus = board.getGameStatus()
    val showBoard = ShowBoard()

    //счетчик ходов
    var counter = 0

    // Первоначальной расположение фигур
    println()
    println(" ".repeat(7) + "\u001B[31m!!!Игра началась!!!\u001B[0m")
    showBoard.printBoard(board)

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

    //Запись статистики
    val currentTime = (System.currentTimeMillis() - time) * 0.001

    val df = DecimalFormat("#.###")
    df.roundingMode = RoundingMode.CEILING

    val s = df.format(currentTime).replace(',', '.')

    val gameResult = showBoard.getWinner(board)
    val newStatistics = "${s}s, ${whitePlayer.name()}, ${blackPlayer.name()}, $gameResult"
    File("statistics.txt").appendText(newStatistics + "\n")

    println()
    println("В партии было сделано $counter ходов")
}
