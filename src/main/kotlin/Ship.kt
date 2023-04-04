class Ship(val type: ShipType, initialPosition: Pair<Int, Int>, initialOrientation: ShipOrientation) {
    val positions = mutableSetOf<Pair<Int, Int>>()
    val zoneOfControl = mutableSetOf<Pair<Int, Int>>()
    val hits = mutableSetOf<Pair<Int, Int>>()

    init {
        val x = initialPosition.first
        val y = initialPosition.second
        if (initialOrientation == ShipOrientation.HORIZONTAL) {
            for (i in 0 until type.size) {
                zoneOfControl.add(x to y + 1)
                zoneOfControl.add(x to y - 1)
                positions.add(Pair(x + i, y))
            }
        } else {
            for (i in 0 until type.size) {
                zoneOfControl.add(x + 1 to y)
                zoneOfControl.add(x - 1 to y)
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

    fun hasInvalidPosition(other: Ship): Boolean {
        return overlapsWith(other) || breaksZoneOfControl(other)
    }

    fun overlapsWith(other: Ship): Boolean {
        return positions.any { other.positions.contains(it) }
    }

    fun breaksZoneOfControl(other: Ship): Boolean {
        return positions.any { it in other.zoneOfControl }
    }

    fun fitsOnBoard(boardSize: Int): Boolean {
        return positions.any { it.first < boardSize - 1 || it.second < boardSize - 1 }
    }
}