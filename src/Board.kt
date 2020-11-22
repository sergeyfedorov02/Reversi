class Board {

    private val board = Array(8) {Array(8) {CellStatus.Empty} }
    private var status: GameStatus = GameStatus.BlackTurn

    data class Square (val cell: Cell, val cellStatus: CellStatus)
    data class Cell(val h: Int, val v : Int)

    init {
        reset()
    }

    enum class  GameStatus {
        WhiteTurn,
        BlackTurn,
        WhiteWin,
        BlackWin,
        Draw
    }

    enum class CellStatus {
        White,
        Black,
        Empty
    }

    //Функция для тестов, чтобы сразу выставить определенную ситуацию на доске
    internal fun setPosition(whites: Set<Cells>, blacks: Set<Cells>, isWhiteTurn: Boolean) {
        reset()

        status = if (isWhiteTurn)
            GameStatus.WhiteTurn
        else GameStatus.BlackTurn

        blacks.forEach { board[it.h][it.v] = CellStatus.Black}
        whites.forEach { board[it.h][it.v] = CellStatus.White}
    }

    //Утсановка стартовой позиции на доске
    private fun setStartPosition() {
        board[3][3] = CellStatus.White
        board[4][4] = CellStatus.White
        board[3][4] = CellStatus.Black
        board[4][3] = CellStatus.Black
    }

    //Получение всех клеток, на которых находятся белые фишки
    fun getWhite() : Set<Cells> {
        val result = mutableSetOf<Cells>()
        board.forEachIndexed { x,it -> it.forEachIndexed { y,k -> if (k == CellStatus.White) {
            result.add(Cells.fromCell(x,y))} } }

        return result
    }

    //Получение всех клеток, на которых находятся черные фишки
    fun getBlack() : Set<Cells> {
        val result = mutableSetOf<Cells>()
        board.forEachIndexed { x,it -> it.forEachIndexed { y,k -> if (k == CellStatus.Black) {
            result.add(Cells.fromCell(x,y))} } }

        return result
    }

    fun getBoard(): Array<Array<CellStatus>> {
        return board
    }

    fun getGameStatus(): GameStatus {
        return status
    }

    //Обновление доски для новой партии
     fun reset(){
        board.forEachIndexed{ x, it -> it.forEachIndexed { y, _ ->  board[x][y] = CellStatus.Empty  } }
        status = GameStatus.BlackTurn
        setStartPosition()
    }

    private fun isWhiteTurn() : Boolean{
        return status != GameStatus.BlackTurn
    }

    //Получение клетки слева
    private fun getLeftSquare(x: Int, y: Int): Square? {
        if (x - 1 in 0..7) {
            return Square(Cell(x - 1, y), board[x - 1][y])
        }
        return null
    }

    //Получение клетки справа сверху
    private fun getRightSquare(x: Int, y: Int): Square? {
        if (x + 1 in 0..7) {
            return Square(Cell(x + 1, y), board[x + 1][y])
        }
        return null
    }

    //Получение клетки сверху
    private fun getUpSquare(x: Int, y: Int): Square? {
        if (y - 1 in 0..7) {
            return Square(Cell(x, y - 1), board[x][y - 1])
        }
        return null
    }

    //Получение клетки снизу
    private fun getDownSquare(x: Int, y: Int): Square? {
        if (y + 1 in 0..7) {
            return Square(Cell(x, y + 1), board[x][y + 1])
        }
        return null
    }

    //Получение клетки сверху слева
    private fun getUpLeftSquare(x: Int, y: Int): Square? {
        if (x - 1 in 0..7 && y - 1 in 0..7) {
            return Square(Cell(x - 1, y - 1), board[x - 1][y - 1])
        }
        return null
    }

    //Получение клетки сверху справа
    private fun getUpRightSquare(x: Int, y: Int): Square? {
        if (x + 1 in 0..7 && y - 1 in 0..7) {
            return Square(Cell(x + 1, y - 1), board[x + 1][y - 1])
        }
        return null
    }

    //Получение снизу слева
    private fun getDownLeftSquare(x: Int, y: Int): Square? {
        if (x - 1 in 0..7 && y + 1 in 0..7) {
            return Square(Cell(x - 1, y + 1), board[x - 1][y + 1])
        }
        return null
    }

    //Получение списка непустых клеток вокруг данной
    private fun listOfAnyNonEmptyCellAround(x: Int, y: Int): List<Square> {

        //Получение клетки снизу справа
        fun getDownRightSquare(x: Int, y: Int): Square? {
            if (x + 1 in 0..7 && y + 1 in 0..7) {
                return Square(Cell(x + 1, y + 1), board[x + 1][y + 1])
            }
            return null
        }


        val result = mutableListOf<Square>()

        getLeftSquare(x, y)?.let { result.add(it) }
        getUpLeftSquare(x, y)?.let { result.add(it) }
        getUpSquare(x, y)?.let { result.add(it) }
        getUpRightSquare(x, y)?.let { result.add(it) }
        getRightSquare(x, y)?.let { result.add(it) }
        getDownRightSquare(x, y)?.let { result.add(it) }
        getDownSquare(x, y)?.let { result.add(it) }
        getDownLeftSquare(x, y)?.let { result.add(it) }

        return result.filter { it.cellStatus != CellStatus.Empty }
    }

    //Получение ряда клеток, начиная со старт в сторону endY
    // функция дойдет до конца доски или же до заданной позиции - stopPos
    private fun getRow(startX: Int, startY: Int, endY: Int, stopPos: Cell): List<Square> {

        val flagGoRight = startY < endY

        return if (flagGoRight)
            getPass(startX, startY, 8, 8, 0, 1, stopPos ) //движемся вправо
        else getPass(startX, startY, 8, -1, 0, -1, stopPos )  //движемся влево

    }

    //Получение столбца клеток, начиная со старт в сторону endX
    // функция дойдет до конца доски или же до заданной позиции - stopPos
    private fun getColumn(startX: Int, startY: Int, endX: Int, stopPos: Cell): List<Square> {

        val flagGoDown = startX < endX

        return if (flagGoDown)
            getPass(startX, startY, 8, 8, 1, 0, stopPos ) //движемся вниз
        else getPass(startX, startY, -1, 8, -1, 0, stopPos )  //движемся вверх

    }

    /*Функция для получения пути в любом направлении:
        Задается стартовая позиция - startX и startY
        Берутся ограничения в каком-то направлении(признак конца доски в этом направлении - lastX и lastY)
        Берется значение для получения следующей клетки - newX и newY(прибавляем 1 или вычитаем или же не меняем)
        Устанавливается последняя клетка - stopPos(дальше нее не идем)
    */
    private fun getPass(startX: Int, startY: Int, lastX: Int, lastY: Int, newX: Int, newY: Int, stopPos: Cell): List<Square> {
        val result = mutableListOf<Square>()

        var x = startX + newX
        var y = startY + newY
        var cell = Cell(x,y)

        while (x != lastX && y != lastY && cell != stopPos) {
            val squareStatus = board[x][y]
            if(squareStatus == CellStatus.Empty)
                break
            result.add(Square(cell,squareStatus))

            x += newX
            y += newY
            cell = Cell(x,y)
        }

        return result
    }

    //Получение диагонали клеток, функция дойдет до конца доски или же до заданной позиции - stopPos
    private fun getDiagonal(startX: Int, startY: Int, endX: Int, endY: Int, stopPos: Cell): List<Square> {

        return when {
            //движение вправо вверх
            startX < endX && startY > endY -> getPass(startX, startY, 8, -1, 1, -1,stopPos)

            //движение влево вниз
            startX > endX && startY < endY -> getPass(startX, startY, -1, 8, -1, 1,stopPos)

            //движение влево вверх
            startX > endX && startY > endY -> getPass(startX, startY, -1, -1, -1, -1,stopPos)

            //движение вправо вниз
            else -> getPass(startX, startY, 8 , 8, 1, 1,stopPos)
        }

    }

    //Нахождение последней клетки с определенным статусом в заданном ряду
    private fun getLastSquare(startX: Int, startY: Int, secondX: Int, secondY: Int, currentStatus: CellStatus, stopPos: Cell ): Square? {

        val route = getRoute(startX,startY,secondX,secondY,stopPos)

        var result: Square? = null

        //Поиск последней фишки с заданном ряду с таким же цветом
        val x = 0 // чекнуть правила, мб тут надо до первой бежать
        route.forEach {
            if (it.cellStatus == currentStatus)
                result = it
        }

        return result
    }

    //Получение списка клеток от start до end
    private fun getRoute(startX: Int, startY: Int, endX: Int, endY: Int, stopPos: Cell ): List<Square> {
        return when {

            //получение столбца
            startY == endY -> getColumn(startX, startY, endX, stopPos)

            //получение строки
            startX == endX -> getRow(startX, startY, endY, stopPos)

            //получение идагонали
            else -> getDiagonal(startX, startY,endX, endY, stopPos)
        }
    }

    fun getValidMoves(): Set<Cells> {

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
                val list = listOfAnyNonEmptyCellAround(i , j)


                //Если все клетки вокруг данной пусты -> пропускаем
                if (list.isEmpty())
                    continue

                //Проверяем каждую клетку из списка, на наличие в ее ряду фишки такого же цвета чей сейчас ход
                list.forEach{

                    val currentStatus = if (isWhiteTurn())
                        CellStatus.White
                    else CellStatus.Black

                    //Получение последней фишки в заданном ряду с таким же цветом, чей сейчас ход
                    val lastSquare = getLastSquare(i, j, it.cell.h, it.cell.v, currentStatus, Cell(9, 9))

                    val currentCell = Cells.fromCell(i, j)
                    //Если такая фишка присутствует и она еще не в result
                    if (lastSquare != null && currentCell !in result) {

                        //Получаем путь и делаем фильтрацию по фишкам противоположного цвета, так как по правилам Реверси
                        //валидным считается тот ход, при котором переворачиваются фишки соперника
                        val route = getRoute(i, j, lastSquare.cell.h, lastSquare.cell.v, Cell(lastSquare.cell.h, lastSquare.cell.v)).
                            filter { k -> k.cellStatus != currentStatus }


                        //Если в пути встречается хотя бы одна фишка соперника -> ход валидный (добавляем его в result)
                        if (route.isNotEmpty())
                            result.add(currentCell)
                    }

                }
            }
        }

        return result
    }

    fun isValidMove(cell: Cells): Boolean {
        val listOfValidMoves = getValidMoves()
        return cell in listOfValidMoves
    }

    fun makeCopyAndMove(){

    }

    fun move (){

    }

    fun canMove(){

    }

}