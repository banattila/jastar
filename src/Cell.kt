class Cell(var i: Int = 0, var j: Int = 0) {

    constructor() :this(0, 0)

    lateinit var parent: Cell
    var heruisticCost: Int = 0
    var finalCost: Int = 0
    var solution = false

    override fun toString() = "[ $i, $j ]"
}