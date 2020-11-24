import java.util.concurrent.ThreadLocalRandom

class EvaluatePosition {

    private val evaluativeMap = Array(8) {IntArray(8) {0} }

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
    fun evaluatePosition(board: Board): Cells {

        var validMoves = board.getValidMoves(board.isWhiteTurn())

        if (validMoves.isEmpty())
            throw IllegalStateException("Невозможная ситуация")

        if (validMoves.size == 1)
            return validMoves.first()

        //Лист в котором будут храниться клетки валидных ходов с их оценкой
        val listOfEvaluatePosition = mutableListOf<Pair<Cells, Double>>()

        //если true -> я хожу за белых, иначе за черных
        val whatColorIsMine = board.isWhiteTurn()


        while (validMoves.isNotEmpty()) {

            val cell = validMoves.first()
            val newBoard = board.makeCopyAndMove(cell)

            val emptyCells = newBoard.getEmptyCells()
            var myCells: Set<Cells> //клетки с фишками нашего цвета
            var enemyCells: Set<Cells> //клетки с фишками соперника

            //В зависимости от того, кто сейчас ходит myCells и enemyCells присваиваются по разному
            if (whatColorIsMine) {
                myCells = newBoard.getWhite()
                enemyCells = newBoard.getBlack()
            } else {
                myCells = newBoard.getBlack()
                enemyCells = newBoard.getWhite()
            }

            //Введем параметры для конечной формулы
            val d = (myCells.size - enemyCells.size).toDouble()
            var p: Double
            var f: Double
            var c: Double
            var l: Double
            var m: Double

            fun findFrontCellsSize(whichCells: Set<Cells>) : Int {

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

            p = when {
                myCellsSize > enemyCellsSize -> (100.0 * myCellsSize) / (myCellsSize + enemyCellsSize)
                myCellsSize < enemyCellsSize -> -(100.0 * enemyCellsSize) / (myCellsSize + enemyCellsSize)
                else -> 0.0
            }

            f = when {
                myFrontCellsSize > enemyFrontCellsSize -> -(100.0 * myFrontCellsSize)/(myFrontCellsSize + enemyFrontCellsSize)
                myFrontCellsSize < enemyFrontCellsSize -> (100.0 * enemyFrontCellsSize)/(myFrontCellsSize + enemyFrontCellsSize)
                else -> 0.0
            }

            //Размещение фишек в угловых клетках
            myCellsSize = 0
            enemyCellsSize = 0
            when{
                Cells.A1 in myCells -> myCellsSize++
                Cells.A1 in enemyCells -> enemyCellsSize++

                Cells.H1 in myCells -> myCellsSize++
                Cells.H1 in enemyCells -> enemyCellsSize++

                Cells.A8 in myCells -> myCellsSize++
                Cells.A8 in enemyCells -> enemyCellsSize++

                Cells.H8 in myCells -> myCellsSize++
                Cells.H8 in enemyCells -> enemyCellsSize++
            }

            c = 25 * (myCellsSize - enemyCellsSize).toDouble()

            //Размещение фишек в близких к углам клетках
            myCellsSize = 0
            enemyCellsSize = 0
            if (Cells.A1 in emptyCells) {
                when{
                    Cells.A2 in myCells -> myCellsSize++
                    Cells.A2 in enemyCells -> enemyCellsSize++

                    Cells.B1 in myCells -> myCellsSize++
                    Cells.B1 in enemyCells -> enemyCellsSize++

                    Cells.B2 in myCells -> myCellsSize++
                    Cells.B2 in enemyCells -> enemyCellsSize++
                }
            }

            if (Cells.H1 in emptyCells) {
                when{
                    Cells.H2 in myCells -> myCellsSize++
                    Cells.H2 in enemyCells -> enemyCellsSize++

                    Cells.G1 in myCells -> myCellsSize++
                    Cells.G1 in enemyCells -> enemyCellsSize++

                    Cells.G2 in myCells -> myCellsSize++
                    Cells.G2 in enemyCells -> enemyCellsSize++
                }
            }

            if (Cells.A8 in emptyCells) {
                when{
                    Cells.A7 in myCells -> myCellsSize++
                    Cells.A7 in enemyCells -> enemyCellsSize++

                    Cells.B8 in myCells -> myCellsSize++
                    Cells.B8 in enemyCells -> enemyCellsSize++

                    Cells.B7 in myCells -> myCellsSize++
                    Cells.B7 in enemyCells -> enemyCellsSize++
                }
            }

            if ( Cells.H8 in emptyCells) {
                when{
                    Cells.H7 in myCells -> myCellsSize++
                    Cells.H7 in enemyCells -> enemyCellsSize++

                    Cells.G7 in myCells -> myCellsSize++
                    Cells.G7 in enemyCells -> enemyCellsSize++

                    Cells.G8 in myCells -> myCellsSize++
                    Cells.G8 in enemyCells -> enemyCellsSize++
                }
            }

            l = -12.5 * (myCellsSize - enemyCellsSize)

            //Мобильность игроков
            myCellsSize = newBoard.getValidMoves(whatColorIsMine).size
            enemyCellsSize = newBoard.getValidMoves(!whatColorIsMine).size
            m = when {
                myCellsSize > enemyCellsSize -> (100.0 * myCellsSize)/(myCellsSize + enemyCellsSize)
                myCellsSize < enemyCellsSize -> -(100.0 * enemyCellsSize)/(myCellsSize + enemyCellsSize)
                else -> 0.0
            }

            val score = (10 * p) + (801.724 * c) + (382.026 * l) + (78.922 * m) + (74.396 * f) + (10 * d)
            listOfEvaluatePosition.add(Pair(cell, score))

            validMoves = validMoves.drop(1).toSet()
        }

        //Если получится несколько клеток с одинаковым и наибольшим значением -> из них выберем рандомную
        var resultList = listOfEvaluatePosition.sortedBy { it.second }.reversed()

        val maxValue = resultList.first().second
        resultList = listOfEvaluatePosition.filter { it.second == maxValue }

        if (resultList.size == 1)
            return resultList.first().first

        val index = ThreadLocalRandom.current().nextInt(0, resultList.size - 1)
        return resultList.toList()[index].first

    }

}