import java.util.PriorityQueue

class KonverToKotlin {

    private val grid: Array<Array<Cell?>> = arrayOf(arrayOf())

    private val openCells: PriorityQueue<Cell>
    private val closedCells: Array<BooleanArray>

    private var startI: Int = 0
    private var startJ: Int = 0
    private var endI: Int = 0
    private var endJ: Int = 0

    constructor(width: Int, height: Int, si: Int, sj: Int, ei: Int, ej: Int, blocks: Array<IntArray>) {
        closedCells = Array(width) { BooleanArray(height) }
        openCells =
            PriorityQueue { c1: Cell, c2: Cell -> if (c1.finalCost < c2.finalCost) -1 else if (c1.finalCost > c2.finalCost) 1 else 0 }

        startCell(si, sj)
        endCell(ei, ej)

        for (i in grid.indices) {
            for (j in 0 until grid[i].size) {
                grid[i][j] = Cell(i, j)
                grid[i][j]?.heruisticCost = Math.abs(i - endI) + Math.abs(j - endJ)
                grid[i][j]?.solution = false
            }
        }

        grid[startI][startJ]?.finalCost = 0

        for (i in blocks.indices) {
            addBlockOnCell(blocks[i][0], blocks[i][1])
        }
    }

    fun startCell(i: Int, j: Int) {
        startI = i
        startJ = j
    }

    fun endCell(i: Int, j: Int) {
        endJ = j
        endI = i
    }

    fun addBlockOnCell(i: Int, j: Int) {
        grid[i][j] = null
    }

    fun updateCostIfNeeded(current: Cell, t: Cell?, cost: Int) {
        if (t == null || closedCells[t.i][t.j]) {
            return
        }

        val tFinalCost = t.heruisticCost + cost
        val isOpen = openCells.contains(t)

        if (!isOpen || t.finalCost > tFinalCost) {
            t.finalCost = tFinalCost
            t.parent = current

            if (!isOpen)
                openCells.add(t)
        }
    }

    fun process() {

        openCells.add(grid[startI][startJ])
        var current: Cell?

        while (true) {

            current = openCells.poll()

            if (current == null)
                break

            closedCells[current.i][current.j] = true

            if (current == grid[endI][endJ])
                return

            var t: Cell

            if (current.i - 1 >= 0) {
                t = grid[current.i - 1][current.j]!!
                updateCostIfNeeded(current, t, current.finalCost + V_H_COST)

                if (current.j - 1 >= 0) {
                    t = grid[current.i - 1][current.j - 1]!!
                    updateCostIfNeeded(current, t, current.finalCost + DIAGONAL_COST)
                }

                if (current.j + 1 < grid[0].size) {
                    t = grid[current.i - 1][current.j + 1]!!
                    updateCostIfNeeded(current, t, current.finalCost + DIAGONAL_COST)
                }
            }
            if (current.j - 1 >= 0) {
                t = grid[current.i][current.j - 1]!!
                updateCostIfNeeded(current, t, current.finalCost + V_H_COST)
            }

            if (current.j + 1 < grid[0].size) {
                t = grid[current.i][current.j + 1]!!
                updateCostIfNeeded(current, t, current.finalCost + V_H_COST)
            }

            if (current.i + 1 < grid.size) {
                t = grid[current.i + 1][current.j]!!
                updateCostIfNeeded(current, t, current.finalCost + V_H_COST)

                if (current.j - 1 >= 0) {
                    t = grid[current.i + 1][current.j - 1]!!
                    updateCostIfNeeded(current, t, current.finalCost + DIAGONAL_COST)
                }
                if (current.j + 1 < grid[0].size) {
                    t = grid[current.i + 1][current.j + 1]!!
                    updateCostIfNeeded(current, t, current.finalCost + DIAGONAL_COST)
                }
            }
        }
    }

    fun display() {

        println("Grid")

        for (i in grid.indices) {
            for (j in 0 until grid[i].size) {
                if (i == startI && j == startJ)
                    print("Start   ")
                else if (i == endI && j == endJ)
                    print("DE      ")
                else if (grid[i][j] == null)
                    System.out.printf("%-3d ", 0)
                else
                    println("BL")
            }
            println()
        }
        println()
    }

    fun displayScores() {
        println("\nScores for cells: ")

        for (i in grid.indices) {
            for (j in 0 until grid[i].size) {
                if (grid[i][j] != null)
                    System.out.printf("%-3d", grid[i][j]?.finalCost)
                else
                    println("BL")
            }
        }
    }

    fun displaySolution() {
        println("Path: \n")
        var current = grid[endI][endJ]
        println(current)
        grid[current!!.i][current!!.j]?.solution = true

        while (current?.parent != null) {
            print(" -> " + current.parent)
            grid[current?.parent.i][current.parent.j]?.solution = true
            current = current.parent
        }

        println("\n")

        for (i in grid.indices) {
            for (j in 0 until grid[i].size) {
                if (i == startI && j == startJ)
                    println("Start: \n")
                else if (i == endI && j == endJ)
                    print("DE  ")
                else if (grid[i][j] != null)
                    System.out.printf("%-3s ", if (grid[i][j]!!.solution) "X" else "O")
                else
                    print("BL  ")
            }
            println()
        }
    }

    companion object {

        val DIAGONAL_COST = 14
        val V_H_COST = 10
    }
}
