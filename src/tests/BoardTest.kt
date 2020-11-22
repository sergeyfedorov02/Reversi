package tests

import Board
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

internal class BoardTest {

    @Test
    fun checkStartPosition() {
        val newBoard = Board()
        assertEquals(Board.GameStatus.BlackTurn, newBoard.getGameStatus())

        assertEquals( setOf(Cells.D4, Cells.E5), newBoard.getWhite())
        assertEquals( setOf(Cells.D5, Cells.E4), newBoard.getBlack())
    }

    @Test
    fun isValidMoves() {
        val newBoard = Board()
        assertEquals(Board.GameStatus.BlackTurn, newBoard.getGameStatus())

        assertTrue(newBoard.isValidMove(Cells.C4))
        assertFalse(newBoard.isValidMove(Cells.A1))
        assertFalse(newBoard.isValidMove(Cells.D4))
        assertFalse(newBoard.isValidMove(Cells.D5))
    }

    @Test
    fun getValidMoves() {
        val newBoard = Board()
        assertEquals(Board.GameStatus.BlackTurn, newBoard.getGameStatus())

        //Валидные ходы для начальной позиции
        val validMoves = newBoard.getValidMoves()
        assertEquals( setOf(Cells.E6, Cells.F5, Cells.C4, Cells.D3), validMoves)

        //Если черные из стартовой позиции походили на D3
        newBoard.setPosition(setOf(Cells.E5),
            setOf(Cells.D5, Cells.D4, Cells.D3, Cells.E4),
            true)

        val validMoves2 = newBoard.getValidMoves()
        assertEquals( setOf(Cells.C3, Cells.C5, Cells.E3), validMoves2)
    }

    @Test
    fun checkSetPosition() {
        val newBoard = Board()
        newBoard.setPosition(setOf(Cells.E5),
           setOf(Cells.D5, Cells.D4, Cells.D3, Cells.E4),
            true)

        assertEquals(Board.GameStatus.WhiteTurn, newBoard.getGameStatus())

        assertEquals( setOf(Cells.E5), newBoard.getWhite())
        assertEquals( setOf(Cells.D5, Cells.D4, Cells.D3, Cells.E4), newBoard.getBlack())

        newBoard.reset()
        assertEquals( setOf(Cells.D4, Cells.E5), newBoard.getWhite())
        assertEquals( setOf(Cells.D5, Cells.E4), newBoard.getBlack())

    }
}