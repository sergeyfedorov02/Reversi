/*
Цифры для оценки позиций и конечной формулы взяты с сайта: https://cutt.ly/XhpmrWl
 */

class EvaluatePosition {

    private val evaluativeMap = Array(8) { IntArray(8) { 0 } }

    init {
        //Проинициализируем матрицу
        evaluativeMap[0] = intArrayOf(20, -3, 11, 8, 8, 11, -3, 20)
        evaluativeMap[1] = intArrayOf(-3, -7, -4, 1, 1, -4, -7, -3)
        evaluativeMap[2] = intArrayOf(11, -4, 2, 2, 2, 2, -4, 11)
        evaluativeMap[3] = intArrayOf(8, 1, 2, -3, -3, 2, 1, 8)
        evaluativeMap[4] = intArrayOf(8, 1, 2, -3, -3, 2, 1, 8)
        evaluativeMap[5] = intArrayOf(11, -4, 2, 2, 2, 2, -4, 11)
        evaluativeMap[6] = intArrayOf(-3, -7, -4, 1, 1, -4, -7, -3)
        evaluativeMap[7] = intArrayOf(20, -3, 11, 8, 8, 11, -3, 20)
    }

    //Функция оценки позиций
    fun evaluatePosition(board: Board, cell: Cells): Double {

        //если true -> я хожу за белых, иначе за черных
        val whatColorIsMine = board.isWhiteTurn()

        val newBoard = board.makeCopyAndMove(cell) //Делаем ход на копии доски

        val emptyCells = newBoard.getEmptyCells()
        val myCells: Set<Cells> //клетки с фишками нашего цвета
        val enemyCells: Set<Cells> //клетки с фишками соперника

        //В зависимости от того, кто сейчас ходит myCells и enemyCells присваиваются по разному
        if (whatColorIsMine) {
            myCells = newBoard.getWhite()
            enemyCells = newBoard.getBlack()
        } else {
            myCells = newBoard.getBlack()
            enemyCells = newBoard.getWhite()
        }

        //Введем параметры для конечной формулы
        val whoHasMoreCells = (myCells.size - enemyCells.size).toDouble() // У кого сейчас больше фишек
        val cellsSizeParameter: Double // количество фишек
        val frontCells: Double // параметр для каревых фишек(с одной из сторон есть пустая клетка)
        val cornerCells: Double // параметр для угловых фишек
        val nearCornerCells: Double // параметр для клеток рядом с углами доски
        val mobility: Double // параметр мобильности игроков (у кого после нашего хода будет больше валидных ходов)

        //Доп функция для вычисления параметров myFrontCellsSize и enemyFrontCellsSize
        fun findFrontCellsSize(whichCells: Set<Cells>): Int {

            var x: Int
            var y: Int
            val listOfX = intArrayOf(-1, -1, 0, 1, 1, 1, 0, -1)
            val listOfY = intArrayOf(0, 1, 1, 1, 0, -1, -1, -1)

            var result = 0

            whichCells.forEach {

                for (k in 0..7) {
                    x = it.h + listOfX[k]
                    y = it.v + listOfY[k]

                    if (x in 0..7 && y in 0..7 && Cells.fromCell(x, y) in emptyCells) {
                        result++
                        break
                    }

                }

            }
            return result
        }

        val myFrontCellsSize = findFrontCellsSize(myCells)
        val enemyFrontCellsSize = findFrontCellsSize(enemyCells)

        var myCellsSize = myCells.size
        var enemyCellsSize = enemyCells.size

        cellsSizeParameter = when {
            myCellsSize > enemyCellsSize -> (100.0 * myCellsSize) / (myCellsSize + enemyCellsSize)
            myCellsSize < enemyCellsSize -> -(100.0 * enemyCellsSize) / (myCellsSize + enemyCellsSize)
            else -> 0.0
        }

        frontCells = when {
            myFrontCellsSize > enemyFrontCellsSize -> -(100.0 * myFrontCellsSize) / (myFrontCellsSize + enemyFrontCellsSize)
            myFrontCellsSize < enemyFrontCellsSize -> (100.0 * enemyFrontCellsSize) / (myFrontCellsSize + enemyFrontCellsSize)
            else -> 0.0
        }

        //Размещение фишек в угловых клетках
        myCellsSize = 0
        enemyCellsSize = 0
        when {
            Cells.A1 in myCells -> myCellsSize++
            Cells.A1 in enemyCells -> enemyCellsSize++

            Cells.H1 in myCells -> myCellsSize++
            Cells.H1 in enemyCells -> enemyCellsSize++

            Cells.A8 in myCells -> myCellsSize++
            Cells.A8 in enemyCells -> enemyCellsSize++

            Cells.H8 in myCells -> myCellsSize++
            Cells.H8 in enemyCells -> enemyCellsSize++
        }

        cornerCells = 25 * (myCellsSize - enemyCellsSize).toDouble()

        //Размещение фишек в близких к углам клетках
        myCellsSize = 0
        enemyCellsSize = 0
        if (Cells.A1 in emptyCells) {
            when {
                Cells.A2 in myCells -> myCellsSize++
                Cells.A2 in enemyCells -> enemyCellsSize++

                Cells.B1 in myCells -> myCellsSize++
                Cells.B1 in enemyCells -> enemyCellsSize++

                Cells.B2 in myCells -> myCellsSize++
                Cells.B2 in enemyCells -> enemyCellsSize++
            }
        }

        if (Cells.H1 in emptyCells) {
            when {
                Cells.H2 in myCells -> myCellsSize++
                Cells.H2 in enemyCells -> enemyCellsSize++

                Cells.G1 in myCells -> myCellsSize++
                Cells.G1 in enemyCells -> enemyCellsSize++

                Cells.G2 in myCells -> myCellsSize++
                Cells.G2 in enemyCells -> enemyCellsSize++
            }
        }

        if (Cells.A8 in emptyCells) {
            when {
                Cells.A7 in myCells -> myCellsSize++
                Cells.A7 in enemyCells -> enemyCellsSize++

                Cells.B8 in myCells -> myCellsSize++
                Cells.B8 in enemyCells -> enemyCellsSize++

                Cells.B7 in myCells -> myCellsSize++
                Cells.B7 in enemyCells -> enemyCellsSize++
            }
        }

        if (Cells.H8 in emptyCells) {
            when {
                Cells.H7 in myCells -> myCellsSize++
                Cells.H7 in enemyCells -> enemyCellsSize++

                Cells.G7 in myCells -> myCellsSize++
                Cells.G7 in enemyCells -> enemyCellsSize++

                Cells.G8 in myCells -> myCellsSize++
                Cells.G8 in enemyCells -> enemyCellsSize++
            }
        }

        nearCornerCells = -12.5 * (myCellsSize - enemyCellsSize)

        //Мобильность игроков
        myCellsSize = newBoard.getValidMoves(whatColorIsMine).size
        enemyCellsSize = newBoard.getValidMoves(!whatColorIsMine).size
        mobility = when {
            myCellsSize > enemyCellsSize -> (100.0 * myCellsSize) / (myCellsSize + enemyCellsSize)
            myCellsSize < enemyCellsSize -> -(100.0 * enemyCellsSize) / (myCellsSize + enemyCellsSize)
            else -> 0.0
        }

        return (10 * cellsSizeParameter) + (801.724 * cornerCells) + (382.026 * nearCornerCells) +
                (78.922 * mobility) + (74.396 * frontCells) + (10 * whoHasMoreCells)

    }
}