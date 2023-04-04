import kotlin.random.Random

class Ship(val type: ShipType, initialPosition: Pair<Int, Int>, initialOrientation: ShipOrientation) {
    val positions = mutableSetOf<Pair<Int, Int>>()
    val hits = mutableSetOf<Pair<Int, Int>>()

    init {
        val x = initialPosition.first
        val y = initialPosition.second
        if (initialOrientation == ShipOrientation.HORIZONTAL) {
            for (i in 0 until type.size) {
                if (x + i >= BOARD_SIZE) break
                positions.add(Pair(x + i, y))
            }
        } else {
            for (i in 0 until type.size) {
                if (y + i >= BOARD_SIZE) break
                positions.add(Pair(x, y + i))
            }
        }
    }

    fun isHit(x: Int, y: Int): Boolean {
        val position = Pair(x, y)
        if (positions.contains(position)) {
            hits.add(position)
            return true
        }
        return false
    }

    fun isSunk(): Boolean {
        return hits.size == type.size
    }

    fun overlapsWith(other: Ship): Boolean {
        return positions.any { other.positions.contains(it) }
    }
}