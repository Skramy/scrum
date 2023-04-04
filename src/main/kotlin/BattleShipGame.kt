import kotlin.random.Random

const val BOARD_SIZE = 10
const val SHIPS_COUNT = 5

class BattleShipGame {
    private val myHitsBoard = Array(BOARD_SIZE) { Array(BOARD_SIZE) { FieldValue.EMPTY } }
    private val myShipsBoard = Array(BOARD_SIZE) { Array(BOARD_SIZE) { FieldValue.EMPTY } }
    private val enemyShips = mutableListOf<Ship>()
    private val playerShips = mutableListOf<Ship>()
    private var turns = 0
    private val shipTypes = listOf(ShipType.TWO)


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
            printBoard()
            val (x, y) = getPlayerMove()
            if (hasHitShip(x, y, enemyShips)) {
                println("Hit!")
                myHitsBoard[x][y] = FieldValue.HIT
                turns++
                if (isGameOver(enemyShips)) {
                    printBoard()
                    println("You won in $turns turns!")
                    return
                }
            } else {
                println("Miss!")
                myHitsBoard[x][y] = FieldValue.MISS
                turns++
            }
            val (x2, y2) = generateAiShot()
            if (hasHitShip(x2, y2, playerShips)) {
                println("Enemy hits!")
                myShipsBoard[x2][y2] = FieldValue.HIT
                if (isGameOver(playerShips)) {
                    printBoard()
                    println("Enemy won!")
                    return
                }
            } else {
                println("Enemy miss!")
                myShipsBoard[x2][y2] = FieldValue.MISS
            }
        }
    }

    private fun enemySetup() {

        shipTypes.forEach { type ->
            var ship: Ship
            do {
                val x = Random.nextInt(BOARD_SIZE)
                val y = Random.nextInt(BOARD_SIZE)
                val orientation = Random.nextInt(2)
                ship = Ship(type, Pair(x,y), ShipOrientation.getFromInt(orientation))
            } while (enemyShips.any { it.overlapsWith(ship) })
            enemyShips.add(ship)
        }
    }

    fun userSetup(){
        shipTypes.forEach { type ->
            var ship: Ship? = null
            do {
                println("Enter coordinates for ships $type")
                println("Enter X Y O:")
                val input = readln().split(" ").map { it.toInt() }
                if (input.any { it < 0 }) {
                    println("Invalid coordinates. Try again.")
                    continue
                }
                ship = Ship(type, Pair(input[0],input[1]), ShipOrientation.getFromInt(input[2]))
            } while (ship == null || playerShips.any { it.overlapsWith(ship) && it.fitsOnBoard(BOARD_SIZE) })
            playerShips.add(ship)
        }

    }

    private fun getPlayerMove(): Pair<Int, Int> {
        var x = -1
        var y = -1
        while (x < 0 || x >= BOARD_SIZE || y < 0 || y >= BOARD_SIZE) {
            print("Enter move (row col): ")
            val input = readlnOrNull() ?: ""
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
        val spacing = "                            "
        println()
        print("    " + (0 until BOARD_SIZE).joinToString(" "){EmojiMapper.getNumber(it)})
        print(spacing)
        print("    " + (0 until BOARD_SIZE).joinToString(" "){EmojiMapper.getNumber(it)})
        println()
        playerShips.forEach{ ship->
            ship.positions.forEach {
                myShipsBoard[it.first][it.second] = FieldValue.SHIP
            }
        }
        for (i in 0 until BOARD_SIZE) {
            print("${EmojiMapper.getNumber(i)}  ${myShipsBoard[i].joinToString(" ")}")
            print(spacing)
            print("${EmojiMapper.getNumber(i)}  ${myHitsBoard[i].joinToString(" ")}")
            println()
        }
        println()
    }
    private fun printMyBoard(ships: MutableList<Ship>) {
        println()
        println("   " + (0 until BOARD_SIZE).joinToString(" "))
        for (i in 0 until BOARD_SIZE) {
            println("${EmojiMapper.getNumber(i)}  ${myShipsBoard[i].joinToString(" ")}")
        }
        println()
    }

    private fun generateAiShot(): Pair<Int, Int> {
        var x: Int
        var y: Int
        do {
            x = Random.nextInt(BOARD_SIZE)
            y = Random.nextInt(BOARD_SIZE)
        } while (myHitsBoard[x][y] != FieldValue.EMPTY)
        return x to y
    }
}