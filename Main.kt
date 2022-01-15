package processor

fun main() {

    while (true) {
        print("""
            1. Add matrices
            2. Multiply matrix by a constant
            3. Multiply matrices
            4. Transpose matrix
            5. Calculate a determinant
            6. Inverse matrix
            0. Exit
            Your choice: > 
        """.trimIndent())
        when (readLine()!!) {
            "1" -> {
                print("Enter size of first matrix: > ")
                val (rows1, columns1) = readLine()!!.split(" ").map { it.toInt() }
                val matrix1 = Matrix(rows1, columns1)
                println("Enter first matrix:")
                matrix1.fill()
                print("Enter size of second matrix: > ")
                val (rows2, columns2) = readLine()!!.split(" ").map { it.toInt() }
                val matrix2 = Matrix(rows2, columns2)
                println("Enter second matrix:")
                matrix2.fill()
                // necessary conditions to perform operation
                if (matrix1.row != matrix2.row && matrix1.column != matrix2.column) {
                    println("The operation cannot be performed.")
                } else {
                    matrix1.add(matrix2)
                    println("The result is:")
                    matrix1.printMatrix()
                }
            }
            "2" -> {
                print("Enter size of matrix: > ")
                val (rows, columns) = readLine()!!.split(" ").map { it.toInt() }
                val matrix = Matrix(rows, columns)
                println("Enter matrix:")
                matrix.fill()
                print("Enter constant: > ")
                matrix.multipleScalar(readLine()!!.toDouble())
                println("The result is:")
                matrix.printMatrix()
            }
            "3" -> {
                print("Enter size of first matrix: > ")
                val (rows1, columns1) = readLine()!!.split(" ").map { it.toInt() }
                val matrix1 = Matrix(rows1, columns1)
                println("Enter first matrix:")
                matrix1.fill()
                print("Enter size of second matrix: > ")
                val (rows2, columns2) = readLine()!!.split(" ").map { it.toInt() }
                val matrix2 = Matrix(rows2, columns2)
                println("Enter second matrix:")
                matrix2.fill()
                // necessary conditions to perform operation
                if (matrix1.column != matrix2.row) {
                    println("The operation cannot be performed.")
                } else {
                    matrix1.multiplyMatrices(matrix2)
                    println("The result is:")
                    matrix1.printMatrix()
                }
            }
            "4" -> {
                print("""
                    
                    1. Main diagonal
                    2. Side diagonal
                    3. Vertical line
                    4. Horizontal line
                    Your choice: > 
                """.trimIndent())
                val choice = readLine()!!
                print("Enter matrix size: > ")
                val (rows, columns) = readLine()!!.split(" ").map { it.toInt() }
                val matrix = Matrix(rows, columns)
                println("Enter matrix:")
                matrix.fill()
                matrix.transpose(choice)
                println("The result is:")
                matrix.printMatrix()
            }
            "5" -> {
                print("Enter matrix size: > ")
                val (rows, columns) = readLine()!!.split(" ").map { it.toInt() }
                val matrix = Matrix(rows, columns)
                println("Enter matrix:")
                matrix.fill()
                println("The result is:")
                println(matrix.determinant(matrix))
            }
            "6" -> {
                print("Enter matrix size: > ")
                val (rows, columns) = readLine()!!.split(" ").map { it.toInt() }
                val matrix = Matrix(rows, columns)
                println("Enter matrix:")
                matrix.fill()
                println("The result is:")
                matrix.inverse()
            }
            "0" -> {
                return
            }
        }
    }
}

class Matrix(var row: Int, var column: Int) {
    private var matrix = mutableListOf<MutableList<Double>>()

    fun fill() {
        for (i in 0 until row) {
            matrix.add(readLine()!!.split(" ").map { it.toDouble() }.toMutableList())
        }
    }

    private fun fill0() {
        for (i in 0 until row) {
            matrix.add(MutableList(column) { 0.0 })
        }
    }

    fun printMatrix() {
        for (i in 0 until row) {
            println(matrix[i].joinToString(" "))
        }
        println()
    }

    fun add(m: Matrix) {
        if (row != m.row && column != m.column) {
            println("The operation cannot be performed.")
            return
        }
        for (i in 0 until row) {
            for (j in 0 until column) {
                matrix[i][j] += m.matrix[i][j]
            }
        }
    }

    fun multipleScalar(scalar: Double) {
        for (i in 0 until row) {
            for (j in 0 until column) {
                matrix[i][j] *= scalar
            }
        }
    }

    fun multiplyMatrices(m: Matrix) {
        val temp = Matrix(row, m.column)
        temp.fill0()
        for (i in 0 until temp.row) {
            for (j in 0 until temp.column) {
                var a1 = 0.0
                    for (i1 in 0 until column) {
                        a1 += matrix[i][i1] * m.matrix[i1][j]
                    }
                temp.matrix[i][j] = a1
            }
        }
        matrix = temp.matrix
    }

    fun transpose(choice: String) {
        val temp = Matrix(row, column)
        temp.fill0()
        when (choice) {
            "1" -> {
                for (i in 0 until row) {
                    for (j in 0 until column) {
                        temp.matrix[i][j] = matrix[j][i]
                    }
                }
            }
            "2" -> {
                for (i in 0 until row) {
                    for (j in 0 until column) {
                        temp.matrix[i][j] = matrix[row - 1 - j][column - 1 - i]
                    }
                }
            }
            "3" -> {
                for (i in 0 until row) {
                    for (j in 0 until column) {
                        temp.matrix[i][j] = matrix[i][column - 1 - j]
                    }
                }

            }
            "4" -> {
                for (i in 0 until row) {
                    for (j in 0 until column) {
                        temp.matrix[i][j] = matrix[row - 1 - i][j]
                    }
                }
            }
        }
        matrix = temp.matrix
    }

    fun determinant(m: Matrix): Double {
        // calculates a determinant of a 1-order matrix
        if (m.matrix.lastIndex == 0) return m.matrix[0][0]
        // calculates a determinant of a 2-order matrix
        var det = 0.0
        if (m.matrix.lastIndex == 1) {
            return m.matrix[0][0] * m.matrix[1][1] - m.matrix[0][1] * m.matrix[1][0]
        }
        // storage for temp matrices (Minors)
        val listTemp = mutableListOf<Matrix>()
        // computing the determinant of a matrix of order 3 and more
        for (z in 0 until m.column) {
            //  the submatrix we get from the remaining elements after removing the z row and q column from this matrix.
            listTemp.add(Matrix(m.row, m.column))
            listTemp[z].fill0()
            for (i in 0 until listTemp[z].row) {
                for (j in 0 until listTemp[z].column) {
                    listTemp[z].matrix[i][j] = m.matrix[i][j]
                }
            }
            // removing the z row and q column to get the Minor
            listTemp[z].matrix.removeAt(0)
            for (q in listTemp[z].matrix.lastIndex downTo 0) {
                listTemp[z].matrix[q].removeAt(z)
            }
            // Correction the Minor size
            listTemp[z].row -= 1
            listTemp[z].column -= 1
            // Cofactor of a matrix is the corresponding Minor multiplied by (−1) or (+1)
            val cofactor = if (z % 2 == 0) 1.0 else -1.0
            // a recursive method to calculate a determinant
            det += cofactor * m.matrix[0][z] * determinant(listTemp[z])
        }
        return det
    }

    fun inverse() {
        val det = determinant(this)
        if (det == 0.0) {
            println("This matrix doesn't have an inverse.\n")
            return
        }
        val temp = Matrix(row, column)
        temp.fill0()
        for (i in 0 until row) {
            for (j in 0 until column) {
                temp.matrix[i][j] = cofactor(this, i, j)
            }
        }
        temp.transpose("1")
        temp.multipleScalar(1 / det)
        matrix = temp.matrix
        printMatrix()
    }

    private fun cofactor(m: Matrix, row: Int, column: Int): Double {
        val temp = Matrix(m.row, m.column)
        temp.fill0()
        for (i in 0 until temp.row) {
            for (j in 0 until temp.column) {
                temp.matrix[i][j] = m.matrix[i][j]
            }
        }
        temp.matrix.removeAt(row)
        for (q in temp.matrix.lastIndex downTo 0) {
            temp.matrix[q].removeAt(column)
        }
        temp.row -= 1
        temp.column -= 1
        val cofactorCof = if ((row + column) % 2 == 0) 1.0 else -1.0
        return cofactorCof * determinant(temp)
    }
}