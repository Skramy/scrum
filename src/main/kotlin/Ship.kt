class Ship(val size: Int) {
    val positions = mutableSetOf<Pair<Int, Int>>()
    val hits = mutableSetOf<Pair<Int, Int>>()

    fun isHit(x: Int, y: Int): Boolean {
        val position = Pair(x, y)
        if (positions.contains(position)) {
            hits.add(position)
            return true
        }
        return false
    }

    fun isSunk(): Boolean {
        return hits.size == size
    }

    fun addPosition(x: Int, y: Int) {
        positions.add(Pair(x, y))
    }

    fun overlapsWith(other: Ship): Boolean {
        return positions.any { other.positions.contains(it) }
    }
}