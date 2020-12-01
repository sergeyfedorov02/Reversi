package player.evaluator

import java.io.File


class EvaluatorSettings {

    var evaluativeMap = Array(8) { IntArray(8) { 0 } }
    private var coefficientsOfTheLastFormula = mutableListOf(10.0, 801.724, 382.026, 78.922, 74.396, 10.0)

    fun getA(): Double {
        return coefficientsOfTheLastFormula[0]
    }

    fun getB(): Double {
        return coefficientsOfTheLastFormula[1]
    }

    fun getC(): Double {
        return coefficientsOfTheLastFormula[2]
    }

    fun getD(): Double {
        return coefficientsOfTheLastFormula[3]
    }

    fun getE(): Double {
        return coefficientsOfTheLastFormula[4]
    }

    fun getF(): Double {
        return coefficientsOfTheLastFormula[5]
    }

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

    fun readFromFile(inputFile: String): Boolean {
        var index = 0
        val list = mutableListOf<Int>()
        var lastCoefficientsFlag = false

        val file = File(inputFile)

        if (!file.exists()) {
            return false
        }

        //чтение из файла
        for (line in file.readLines()) {
            if (line.matches(Regex("""-?\d+(,\s-?\d+)+"""))) {
                for (coefficient in line.split(", ")) {
                    list.add(coefficient.toInt())
                }
                evaluativeMap[index] = list.toIntArray()
                index++
                list.clear()
            } else if (line.matches(Regex("""(-?\d+(.\d+)?)"""))) {

                if (!lastCoefficientsFlag) {
                    coefficientsOfTheLastFormula.clear()
                    lastCoefficientsFlag = true
                }
                coefficientsOfTheLastFormula.add(line.toDouble())
            }
        }
        return true
    }
}