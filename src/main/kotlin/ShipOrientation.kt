enum class ShipOrientation(val representation: Int) {
    HORIZONTAL(0),
    VERTICAL(1);

    companion object {
        fun getFromInt(i: Int): ShipOrientation {
            return when (i) {
                HORIZONTAL.representation -> HORIZONTAL
                else -> VERTICAL
            }
        }
    }
}