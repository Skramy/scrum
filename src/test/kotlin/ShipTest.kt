import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class ShipTest {
    @Test
    fun `create vertical ship`() {
        val ship = Ship(ShipType.TWO, Pair(0,0), ShipOrientation.VERTICAL)

        assertThat(ship.positions).contains(Pair(0,0))
        assertThat(ship.positions).contains(Pair(0,1))
        assertThat(ship.positions).doesNotContain(Pair(0,2))
    }

    @Test
    fun `create horizontal ship`() {
        val ship = Ship(ShipType.TWO, Pair(0,0), ShipOrientation.HORIZONTAL)

        assertThat(ship.positions).contains(Pair(0,0))
        assertThat(ship.positions).contains(Pair(1,0))
        assertThat(ship.positions).doesNotContain(Pair(2,0))
    }

    @Test
    fun `create ship, hit it`() {
        val ship = Ship(ShipType.THREE, Pair(0,0), ShipOrientation.HORIZONTAL)

        assertThat(ship.isHit(0, 0)).isTrue()
        assertThat(ship.hits).contains(Pair(0, 0))
        assertThat(ship.hits.size).isEqualTo(1)
        assertThat(ship.isSunk()).isFalse()
    }

    @Test
    fun `create ship, sink it`() {
        val ship = Ship(ShipType.ONE, Pair(0,0), ShipOrientation.HORIZONTAL)

        assertThat(ship.isHit(0, 0)).isTrue()
        assertThat(ship.hits).contains(Pair(0, 0))
        assertThat(ship.hits.size).isEqualTo(1)
        assertThat(ship.isSunk()).isTrue()
    }

    @Test
    fun `Try to create ship out of bounds`() {
        val boardSize = 10
        val ship = Ship(ShipType.THREE, Pair(10, 10), ShipOrientation.VERTICAL)

        assertThat(ship.fitsOnBoard(boardSize)).isFalse()
    }

    @Test
    fun `Validate ships cant overlap`() {
        val ship = Ship(ShipType.ONE, Pair(2, 2), ShipOrientation.VERTICAL)
        val invaderShip = Ship(ShipType.TWO, Pair(2, 2), ShipOrientation.VERTICAL)

        assertThat(invaderShip.overlapsWith(ship)).isTrue()
    }

    @Test
    fun `Validate ship safeguards its zone of control`() {
        val ship = Ship(ShipType.ONE, Pair(2, 2), ShipOrientation.VERTICAL)
        val invaderShip = Ship(ShipType.TWO, Pair(1, 2), ShipOrientation.VERTICAL)

        assertThat(invaderShip.breaksZoneOfControl(ship)).isTrue()
    }
}