package game

class Board {

    private val board = Array(8) { Array(8) { CellStatus.Empty } }
    private var status: GameStatus = GameStatus.BlackTurn

    data class Square(val cell: Cell, val cellStatus: CellStatus)
    data class Cell(val h: Int, val v: Int)

    init {
        reset()
    }

    //enum класс, в котором хранятся возможные значения для клеток доски
    enum class CellStatus {
        White, //на клетке расположена белая фишка
        Black, //черная фишка
        Empty //клетка пуста
    }

    //Функция для тестов, чтобы сразу выставить определенную ситуацию на доске
    internal fun setPosition(whites: Set<Cells>, blacks: Set<Cells>, isWhiteTurn: Boolean) {
        clear()

        status = if (isWhiteTurn)
            GameStatus.WhiteTurn
        else GameStatus.BlackTurn

        blacks.forEach { board[it.h][it.v] = CellStatus.Black }
        whites.forEach { board[it.h][it.v] = CellStatus.White }
    }

    //Утсановка стартовой позиции на доске
    private fun setStartPosition() {
        board[3][3] = CellStatus.White
        board[4][4] = CellStatus.White
        board[3][4] = CellStatus.Black
        board[4][3] = CellStatus.Black
    }

    //Получение всех клеток, на которых находятся белые фишки
    fun getWhite(): Set<Cells> {
        val result = mutableSetOf<Cells>()
        board.forEachIndexed { x, it ->
            it.forEachIndexed { y, k ->
                if (k == CellStatus.White) {
                    result.add(Cells.fromCell(x, y))
                }
            }
        }

        return result
    }

    //Получение всех клеток, на которых находятся черные фишки
    fun getBlack(): Set<Cells> {
        val result = mutableSetOf<Cells>()
        board.forEachIndexed { x, it ->
            it.forEachIndexed { y, k ->
                if (k == CellStatus.Black) {
                    result.add(Cells.fromCell(x, y))
                }
            }
        }

        return result
    }

    //Получение всех клеток, на которых нет фишек (пустые клетки)
    fun getEmptyCells(): Set<Cells> {
        val result = mutableSetOf<Cells>()
        board.forEachIndexed { x, it ->
            it.forEachIndexed { y, k ->
                if (k == CellStatus.Empty) {
                    result.add(Cells.fromCell(x, y))
                }
            }
        }

        return result
    }

    //Получение ткущего статуса игры
    fun getGameStatus(): GameStatus {
        return status
    }

    //Обновление текущего статуса игры
    private fun updateGameStatus() {
        status = if (isWhiteTurn())
            GameStatus.BlackTurn
        else GameStatus.WhiteTurn
    }

    //Очистка доски - всем клеткам присваивается значение Empty
    private fun clear() {
        board.forEachIndexed { x, it ->
            it.forEachIndexed { y, _ ->
                board[x][y] =
                    CellStatus.Empty
            }
        }
    }

    //Обновление доски для новой партии
    fun reset() {
        clear()
        status = GameStatus.BlackTurn
        setStartPosition()
    }

    //Функция для получения значения, кто сейчас ходит
    // Если true - ходит игрок с белыми фишками
    // Иначе - игрок с черными фишками
    fun isWhiteTurn(): Boolean {
        return status != GameStatus.BlackTurn
    }

    //Получение списка непустых клеток вокруг данной
    private fun getListOfAnyNonEmptyCellAround(x: Int, y: Int): List<Square> {

        //Получение соседней клетки в определенном направлении
        fun getNeighboringSquare(startX: Int, startY: Int, newX: Int, newY: Int): Square? {

            val nextX = startX + newX
            val nextY = startY + newY

            if (nextX in 0..7 && nextY in 0..7) {
                return Square(
                    Cell(nextX, nextY),
                    board[nextX][nextY]
                )
            }

            return null
        }

        val result = mutableListOf<Square>()

        //Получение клетки слева
        getNeighboringSquare(x, y, -1, 0)?.let { result.add(it) }

        //Получение клетки справа
        getNeighboringSquare(x, y, 1, 0)?.let { result.add(it) }

        //Получение клетки сверху
        getNeighboringSquare(x, y, 0, -1)?.let { result.add(it) }

        //Получение клетки снизу
        getNeighboringSquare(x, y, 0, 1)?.let { result.add(it) }

        //Получение клетки сверху слева
        getNeighboringSquare(x, y, -1, -1)?.let { result.add(it) }

        //Получение клетки сверху справа
        getNeighboringSquare(x, y, 1, -1)?.let { result.add(it) }

        //Получение клетки снизу справа
        getNeighboringSquare(x, y, 1, 1)?.let { result.add(it) }

        //Получение снизу слева
        getNeighboringSquare(x, y, -1, 1)?.let { result.add(it) }

        return result.filter { it.cellStatus != CellStatus.Empty }
    }

    //Получение ряда клеток, начиная со старт в сторону endY
    // функция дойдет до конца доски или же до заданной позиции - stopPos
    private fun getRow(startX: Int, startY: Int, endY: Int, stopPos: Cell): List<Square> {

        val flagGoRight = startY < endY

        return if (flagGoRight)
            getPass(startX, startY, 8, 8, 0, 1, stopPos) //движемся вправо
        else getPass(startX, startY, 8, -1, 0, -1, stopPos)  //движемся влево

    }

    //Получение столбца клеток, начиная со старт в сторону endX
    // функция дойдет до конца доски или же до заданной позиции - stopPos
    private fun getColumn(startX: Int, startY: Int, endX: Int, stopPos: Cell): List<Square> {

        val flagGoDown = startX < endX

        return if (flagGoDown)
            getPass(startX, startY, 8, 8, 1, 0, stopPos) //движемся вниз
        else getPass(startX, startY, -1, 8, -1, 0, stopPos)  //движемся вверх

    }

    /*Функция для получения пути в любом направлении:
        Задается стартовая позиция - startX и startY
        Берутся ограничения в каком-то направлении(признак конца доски в этом направлении - lastX и lastY)
        Берется значение для получения следующей клетки - newX и newY(прибавляем 1 или вычитаем или же не меняем)
        Устанавливается последняя клетка - stopPos(дальше нее не идем)
    */
    private fun getPass(
        startX: Int,
        startY: Int,
        lastX: Int,
        lastY: Int,
        newX: Int,
        newY: Int,
        stopPos: Cell
    ): List<Square> {
        val result = mutableListOf<Square>()

        var x = startX + newX
        var y = startY + newY
        var cell = Cell(x, y)

        while (x != lastX && y != lastY && cell != stopPos) {
            val squareStatus = board[x][y]
            if (squareStatus == CellStatus.Empty)
                break
            result.add(Square(cell, squareStatus))

            x += newX
            y += newY
            cell = Cell(x, y)
        }

        return result
    }

    //Получение диагонали клеток, функция дойдет до конца доски или же до заданной позиции - stopPos
    private fun getDiagonal(startX: Int, startY: Int, endX: Int, endY: Int, stopPos: Cell): List<Square> {

        return when {
            //движение вправо вверх
            startX < endX && startY > endY -> getPass(startX, startY, 8, -1, 1, -1, stopPos)

            //движение влево вниз
            startX > endX && startY < endY -> getPass(startX, startY, -1, 8, -1, 1, stopPos)

            //движение влево вверх
            startX > endX && startY > endY -> getPass(startX, startY, -1, -1, -1, -1, stopPos)

            //движение вправо вниз
            else -> getPass(startX, startY, 8, 8, 1, 1, stopPos)
        }

    }

    //Нахождение последней клетки с определенным статусом в заданном ряду
    private fun getLastSquare(
        startX: Int,
        startY: Int,
        secondX: Int,
        secondY: Int,
        currentStatus: CellStatus,
        stopPos: Cell
    ): Square? {

        var route = getRoute(startX, startY, secondX, secondY, stopPos)
        var currentCell = route.first()

        //Поиск последней фишки с заданном ряду с таким же цветом
        //По правилам игры Реверси клетка должна быть первой после непрерывного ряда фишек соперника

        //Если следующая фишка нашего цвета -> такой ход не валидный
        if (currentCell.cellStatus == currentStatus)
            return null


        //теперь найдем первую фишку нашего цвета, после неперывного ряда фишек соперника
        while (currentCell.cellStatus != currentStatus) {
            route = route.drop(1)

            //Если мы пробежались по списку и после нашей фишки все остальные-это фишки соперника -> такой ход не валидный
            if (route.isEmpty())
                return null

            currentCell = route.first()
        }

        return currentCell
    }

    //Получение списка клеток от start до end
    private fun getRoute(startX: Int, startY: Int, endX: Int, endY: Int, stopPos: Cell): List<Square> {
        return when {

            //получение столбца
            startY == endY -> getColumn(startX, startY, endX, stopPos)

            //получение строки
            startX == endX -> getRow(startX, startY, endY, stopPos)

            //получение идагонали
            else -> getDiagonal(startX, startY, endX, endY, stopPos)
        }
    }

    //Функция дляполученых все валидных ходов для игрока, чей сейчас ход - isWhiteTurn
    fun getValidMoves(isWhiteTurn: Boolean): Set<Cells> {

        val result = mutableSetOf<Cells>()

        //Бежим по всем клеткам доски
        for (i in 0..7) {

            for (j in 0..7) {

                //текущий статус клетки
                val squareStatus = board[i][j]

                //Если текущая клетка не пуста, то на нее нельзя поставить фишку -> пропускаем
                if (squareStatus != CellStatus.Empty)
                    continue


                //Получаем лист, соджержащий непустые клетки вокруг данной -> пропускаем
                val list = getListOfAnyNonEmptyCellAround(i, j)


                //Если все клетки вокруг данной пусты -> пропускаем
                if (list.isEmpty())
                    continue

                //Проверяем каждую клетку из списка, на наличие в ее ряду фишки такого же цвета чей сейчас ход
                list.forEach {

                    val currentStatus = if (isWhiteTurn)
                        CellStatus.White
                    else CellStatus.Black

                    //Получение последней фишки в заданном ряду с таким же цветом, чей сейчас ход (после ряда фишек соперника)
                    val lastSquare = getLastSquare(
                        i, j, it.cell.h, it.cell.v, currentStatus,
                        Cell(9, 9)
                    )

                    val currentCell = Cells.fromCell(i, j)
                    //Если такая фишка присутствует и она еще не в result -> такой ход валидный
                    if (lastSquare != null && currentCell !in result) {
                        result.add(currentCell)
                    }

                }
            }
        }

        return result
    }

    //Является ли ход в данную клетку валидным
    fun isValidMove(cell: Cells): Boolean {
        val listOfValidMoves = getValidMoves(isWhiteTurn())
        return cell in listOfValidMoves
    }

    fun move(cell: Cells): Boolean {

        //Нельзя сделать ход в эту клетку
        if (!isValidMove(cell)) {
            return false
        }

        //ход сделать можно -> переворачиваем фишки соперника
        updateBoard(cell)

        //передаем очередность хода
        updateGameStatus()

        //Если у следующего игрока нет владиных ходов -> ходим опять мы или же игра окончена
        if (getValidMoves(isWhiteTurn()).isEmpty()) {

            //Если у нас теперь тоже нет валидных ходов -> игра закончена
            updateGameStatus()

            if (getValidMoves(isWhiteTurn()).isEmpty()) {
                status = GameStatus.GameOver
            }

        }

        return true
    }

    //При вызове данной функции ход в клетку всегда будет возможен -> надо перевернуть фишки соперника
    private fun updateBoard(cell: Cells) {

        //Получим статус, кто сейчас ходит
        val currentStatus = if (isWhiteTurn())
            CellStatus.White
        else CellStatus.Black

        //Получим список клеток, в направлении которых могут перевернуться фишки соперника
        val listOfSquaresAround =
            getListOfAnyNonEmptyCellAround(cell.h, cell.v).filter { it.cellStatus != currentStatus }

        //Заменяем фишки соперника на свои, если это возможно
        listOfSquaresAround.forEach { it ->

            //Ищем первую фишку своего цвета после ряда фишек соперника
            val lastSquare = getLastSquare(
                cell.h, cell.v, it.cell.h, it.cell.v, currentStatus,
                Cell(9, 9)
            )

            //Если такая фишка есть -> переворачиваем фишки соперника
            if (lastSquare != null) {

                //Получаем список фишек, которые надо перевенуть
                val route = getRoute(
                    cell.h,
                    cell.v,
                    lastSquare.cell.h,
                    lastSquare.cell.v,
                    Cell(lastSquare.cell.h, lastSquare.cell.v)
                )

                //Переворачиваем фишки
                route.forEach { k -> board[k.cell.h][k.cell.v] = currentStatus }
                board[cell.h][cell.v] = currentStatus

            }

        }

    }

    //Функция, которая создает копию доски и делает на ней move
    fun makeCopyAndMove(cell: Cells): Board {

        val newBoard = Board()

        newBoard.setPosition(getWhite(), getBlack(), isWhiteTurn())
        newBoard.move(cell)

        return newBoard

    }

}