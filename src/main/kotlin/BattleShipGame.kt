import kotlin.random.Random

const val BOARD_SIZE = 10
const val SHIPS_COUNT = 5

class BattleShipGame {
    private val myHitsBoard = Array(BOARD_SIZE) { Array(BOARD_SIZE) { '-' } }
    private val myShipsBoard = Array(BOARD_SIZE) { Array(BOARD_SIZE) { '-' } }
    private val enemyShips = mutableListOf<Ship>()
    private val playerShips = mutableListOf<Ship>()
    private var turns = 0
    private val shipSizes = listOf(2)


    fun play() {
        println("""
                                     |__
                                     |\/
                                     ---
                                     / | [
                              !      | |||
                            _/|     _/|-++'
                        +  +--|    |--|--|_ |-
                     { /|__|  |/\__|  |--- |||__/
                    +---------------___[}-_===_.'____                 /\
                ____`-' ||___-{]_| _[}-  |     |_[___\==--            \/   _
 __..._____--==/___]_|__|_____________________________[___\==--____,------' .7
|                                                                     BB-61/
 \_________________________________________________________________________|
                        
                        Welcome to BattleShip game!
             
             
             
        """.trimIndent())
        userSetup()
        enemySetup()
        println("Welcome to Battleships game!")
        while (true) {
            printMyBoard(playerShips)
            printBoard()
            val (x, y) = getPlayerMove()
            if (hasHitShip(x, y, enemyShips)) {
                println("Hit!")
                myHitsBoard[x][y] = 'X'
                turns++
                if (isGameOver(enemyShips)) {
                    printBoard()
                    println("You won in $turns turns!")
                    return
                }
            } else {
                println("Miss!")
                myHitsBoard[x][y] = 'O'
                turns++
            }
            val x2 = Random.nextInt(BOARD_SIZE)
            val y2 = Random.nextInt(BOARD_SIZE)
            if (hasHitShip(x2, y2, playerShips)) {
                println("Enemy hits!")
                myShipsBoard[x2][y2] = 'X'
                if (isGameOver(playerShips)) {
                    printBoard()
                    println("Enemy won!")
                    return
                }
            } else {
                println("Enemy miss!")
                myShipsBoard[x2][y2] = 'O'
            }
        }
    }

    private fun enemySetup() {

        shipSizes.forEach { size ->
            var ship: Ship
            do {
                val x = Random.nextInt(BOARD_SIZE)
                val y = Random.nextInt(BOARD_SIZE)
                val orientation = Random.nextInt(2)
                ship = Ship(size)
                if (orientation == 0) {
                    for (i in 0 until size) {
                        if (x + i >= BOARD_SIZE) break
                        ship.addPosition(x + i, y)
                    }
                } else {
                    for (i in 0 until size) {
                        if (y + i >= BOARD_SIZE) break
                        ship.addPosition(x, y + i)
                    }
                }
            } while (enemyShips.any { it.overlapsWith(ship) })
            enemyShips.add(ship)
        }
    }

    fun userSetup(){
        shipSizes.forEach { size ->
            var ship: Ship
            do {
                println("Enter coordinates for ships $size")
                println("Enter X")
                val x = readlnOrNull()!!.toInt()
                println("Enter Y")
                val y = readlnOrNull()!!.toInt()

                println("Enter orientation (0 = horizontal, 1 = vertical)")

                val orientation = readlnOrNull()!!.toInt()
                ship = Ship(size)
                if (orientation == 0) {
                    for (i in 0 until size) {
                        if (x + i >= BOARD_SIZE) break
                        ship.addPosition(x + i, y)
                    }
                } else {
                    for (i in 0 until size) {
                        if (y + i >= BOARD_SIZE) break
                        ship.addPosition(x, y + i)
                    }
                }
            } while (playerShips.any { it.overlapsWith(ship) })
            playerShips.add(ship)
        }

    }

    private fun getPlayerMove(): Pair<Int, Int> {
        var x = -1
        var y = -1
        while (x < 0 || x >= BOARD_SIZE || y < 0 || y >= BOARD_SIZE) {
            print("Enter move (row col): ")
            val input = readLine() ?: ""
            val parts = input.split(" ")
            if (parts.size != 2) {
                println("Invalid input, please try again.")
                continue
            }
            try {
                x = parts[0].toInt()
                y = parts[1].toInt()
            } catch (e: NumberFormatException) {
                println("Invalid input, please try again.")
            }
        }
        return Pair(x, y)
    }

    private fun hasHitShip(x: Int, y: Int, ships:MutableList<Ship>): Boolean {
        ships.forEach { ship ->
            if (ship.isHit(x, y)) {
                return true
            }
        }
        return false
    }

    private fun isGameOver(ships:MutableList<Ship>): Boolean {
        return ships.all { it.isSunk() }
    }

    private fun printBoard() {
        println()
        println("   " + (0 until BOARD_SIZE).joinToString(" "))
        for (i in 0 until BOARD_SIZE) {
            println("$i  ${myHitsBoard[i].joinToString(" ")}")
        }
        println()
    }
    private fun printMyBoard(ships: MutableList<Ship>) {
        println()
        ships.forEach{ ship->
            ship.positions.forEach {
                myShipsBoard[it.first][it.second] = 'L'
            }
        }
        println("   " + (0 until BOARD_SIZE).joinToString(" "))
        for (i in 0 until BOARD_SIZE) {
            println("$i  ${myShipsBoard[i].joinToString(" ")}")
        }
        println()
    }
}