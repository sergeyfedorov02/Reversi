import java.lang.IllegalStateException
import java.util.concurrent.ThreadLocalRandom

class PrimitiveBot : Player
{
    override fun selectMove(board: Board): Cells {
        val validMoves = board.getValidMoves()

        if (validMoves.isEmpty())
            throw IllegalStateException("Невозможная ситуация")

        if (validMoves.size == 1)
            return validMoves.first()

        val index = ThreadLocalRandom.current().nextInt(0, validMoves.size - 1)
        return validMoves.toList()[index]
    }

}