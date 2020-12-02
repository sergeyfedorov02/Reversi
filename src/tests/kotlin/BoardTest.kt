package tests.kotlin

import game.Board
import game.Cells
import game.GameStatus
import game.main
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import player.SmartBot
import player.evaluator.EvaluatePosition
import player.evaluator.EvaluatorSettings

internal class BoardTest {

    //Тест для заполнения статистики
    /*@Test
    fun mainTest() {
        val smartBot2 = "smart2"
        val smartBot3 = "smart3"
        val smartBot4 = "smart4"
        val smartBot5 = "smart5"
        val smartBot6 = "smart6"
        val primitiveBot = "primitive"
        val advancedBot = "advanced"

        val listOfPlayers = listOf(smartBot2, smartBot3, smartBot4, smartBot5, smartBot6, primitiveBot, advancedBot)
        var whitePlayer: String
        var blackPlayer: String

        for (i in 1..17) {
            when (i) {
                1 -> {
                    whitePlayer = listOfPlayers[0]
                    blackPlayer = listOfPlayers[1]
                }

                2 -> {
                    whitePlayer = listOfPlayers[1]
                    blackPlayer = listOfPlayers[0]
                }

                3 -> {
                    whitePlayer = listOfPlayers[1]
                    blackPlayer = listOfPlayers[2]
                }

                4 -> {
                    whitePlayer = listOfPlayers[2]
                    blackPlayer = listOfPlayers[1]
                }

                5 -> {
                    whitePlayer = listOfPlayers[3]
                    blackPlayer = listOfPlayers[2]
                }

                6 -> {
                    whitePlayer = listOfPlayers[2]
                    blackPlayer = listOfPlayers[3]
                }

                7 -> {
                    whitePlayer = listOfPlayers[1]
                    blackPlayer = listOfPlayers[5]
                }

                8 -> {
                    whitePlayer = listOfPlayers[5]
                    blackPlayer = listOfPlayers[1]
                }

                9 -> {
                    whitePlayer = listOfPlayers[6]
                    blackPlayer = listOfPlayers[1]
                }

                10 -> {
                    whitePlayer = listOfPlayers[1]
                    blackPlayer = listOfPlayers[6]
                }

                11 -> {
                    whitePlayer = listOfPlayers[6]
                    blackPlayer = listOfPlayers[5]
                }

                12 -> {
                    whitePlayer = listOfPlayers[5]
                    blackPlayer = listOfPlayers[6]
                }

                13 -> {
                    whitePlayer = listOfPlayers[5]
                    blackPlayer = listOfPlayers[5]
                }

                14 -> {
                    whitePlayer = listOfPlayers[6]
                    blackPlayer = listOfPlayers[6]
                }

                15 -> {
                    whitePlayer = listOfPlayers[3]
                    blackPlayer = listOfPlayers[4]
                }

                16 -> {
                    whitePlayer = listOfPlayers[4]
                    blackPlayer = listOfPlayers[3]
                }

                else -> {
                    whitePlayer = listOfPlayers[2]
                    blackPlayer = listOfPlayers[2]
                }
            }
            for (j in 1..1) {
                main(arrayOf(whitePlayer, blackPlayer))
            }
        }
    }*/

    @Test
    fun smartBotTest() {
        val board = Board()
        board.setPosition(
            setOf(
                Cells.A2,
                Cells.C1,
                Cells.C2,
                Cells.C4,
                Cells.C5,
                Cells.D2,
                Cells.D4,
                Cells.D5,
                Cells.E1,
                Cells.E2,
                Cells.E5,
                Cells.F2,
                Cells.G3
            ), setOf(
                Cells.A3,
                Cells.A6,
                Cells.B3,
                Cells.B4,
                Cells.B5,
                Cells.C3,
                Cells.D3,
                Cells.E3,
                Cells.E4,
                Cells.F3,
                Cells.F4,
                Cells.G4
            ), true
        )

        val settings = EvaluatorSettings()
        val player = SmartBot(EvaluatePosition(settings), 5)
        val botMove = player.selectMove(board)

        assertEquals(Cells.A4, botMove)
    }

    @Test
    fun checkStartPosition() {
        val newBoard = Board()
        assertEquals(GameStatus.BlackTurn, newBoard.getGameStatus())

        assertEquals(setOf(Cells.D4, Cells.E5), newBoard.getWhite())
        assertEquals(setOf(Cells.D5, Cells.E4), newBoard.getBlack())
    }

    @Test
    fun makeCopyAndMove() {
        val board = Board()
        assertEquals(GameStatus.BlackTurn, board.getGameStatus())

        val newBoard = board.makeCopyAndMove(Cells.F5)
        assertEquals(GameStatus.WhiteTurn, newBoard.getGameStatus())

        assertEquals(setOf(Cells.D4), newBoard.getWhite())
        assertEquals(setOf(Cells.D5, Cells.E5, Cells.F5, Cells.E4), newBoard.getBlack())
    }

    @Test
    fun sameTurnAfterMove() {
        val board = Board()
        board.setPosition(
            setOf(Cells.G8, Cells.G7), setOf(
                Cells.F8
            ), false
        )
        board.move(Cells.H8)

        assertEquals(GameStatus.BlackTurn, board.getGameStatus())
    }

    @Test
    fun gameOver() {
        val board = Board()
        board.setPosition(setOf(Cells.G8), setOf(Cells.F8), false)
        board.move(Cells.H8)

        assertEquals(GameStatus.GameOver, board.getGameStatus())
    }

    @Test
    fun isValidMoves() {
        val newBoard = Board()
        assertEquals(GameStatus.BlackTurn, newBoard.getGameStatus())

        assertTrue(newBoard.isValidMove(Cells.C4))
        assertFalse(newBoard.isValidMove(Cells.A1))
        assertFalse(newBoard.isValidMove(Cells.D4))
        assertFalse(newBoard.isValidMove(Cells.D5))

        //проверим функцию getLastSquare
        val newBoard2 = Board()
        newBoard2.setPosition(
            setOf(Cells.A7, Cells.A8, Cells.H2, Cells.A2, Cells.A3),
            setOf(Cells.B7, Cells.B8, Cells.H1, Cells.H3, Cells.A1), false
        )

        //Все следующие фишки - это фишки соперника(нет нашей фишки после ряда фишек соперника)
        assertFalse(newBoard2.isValidMove(Cells.A6))

        //Следующая фишка - это наша фишка
        assertFalse(newBoard2.isValidMove(Cells.B6))
        assertFalse(newBoard2.isValidMove(Cells.C7))
        assertFalse(newBoard2.isValidMove(Cells.C8))
        assertFalse(newBoard2.isValidMove(Cells.H4))

        //После ряда фишек соперника есть наша фишка
        assertTrue(newBoard2.isValidMove(Cells.A4))
    }

    @Test
    fun getValidMoves() {
        val newBoard = Board()
        assertEquals(GameStatus.BlackTurn, newBoard.getGameStatus())

        //Валидные ходы для начальной позиции
        val validMoves = newBoard.getValidMoves(newBoard.isWhiteTurn())
        assertEquals(setOf(Cells.E6, Cells.F5, Cells.C4, Cells.D3), validMoves)

        //Если черные из стартовой позиции походили на D3
        newBoard.setPosition(
            setOf(Cells.E5),
            setOf(Cells.D5, Cells.D4, Cells.D3, Cells.E4),
            true
        )

        val validMoves2 = newBoard.getValidMoves(newBoard.isWhiteTurn())
        assertEquals(setOf(Cells.C3, Cells.C5, Cells.E3), validMoves2)
    }

    @Test
    fun checkSetPosition() {
        val newBoard = Board()
        newBoard.setPosition(
            setOf(Cells.E5),
            setOf(Cells.D5, Cells.D4, Cells.D3, Cells.E4),
            true
        )

        assertEquals(GameStatus.WhiteTurn, newBoard.getGameStatus())

        assertEquals(setOf(Cells.E5), newBoard.getWhite())
        assertEquals(setOf(Cells.D5, Cells.D4, Cells.D3, Cells.E4), newBoard.getBlack())

        newBoard.reset()
        assertEquals(setOf(Cells.D4, Cells.E5), newBoard.getWhite())
        assertEquals(setOf(Cells.D5, Cells.E4), newBoard.getBlack())

    }
}