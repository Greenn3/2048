import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import kotlinx.browser.localStorage
import kotlinx.browser.window
import org.jetbrains.compose.web.css.rgb
import org.w3c.dom.get
import org.w3c.dom.set
import kotlin.random.Random

class Logic(var hs: Int?) {

    var scoreCount: Int by mutableStateOf(0)


    var highScore: Int by mutableStateOf(localStorage[score]?.toInt() ?: 0)


    val list: MutableList<MutableList<Int?>> = mutableStateListOf(
        mutableStateListOf(null, null, null, null),
        mutableStateListOf(null, null, null, null),
        mutableStateListOf(null, null, null, null),
        mutableStateListOf(null, null, null, null)
    )

    fun MoveLeft() {
        var yesOrNo = false
        list.forEach { row ->
            for (index in 1..3) {
                if (row[index] != null) {
                    var x = index - 1
                    while (x > 0 && row[x] == null) {
                        x--
                    }
                    for (k in x until index) {
                        if (row[k] == null) {
                            row[k] = row[index]
                            row[index] = null
                            yesOrNo = true
                        } else if (row[k] == row[index]) {
                            row[k] = 2 * row[k]!!
                            row[index] = null
                            scoreCount = scoreCount + row[k]!!
                            if (row[k] == 2048) {
                                window.setTimeout({
                                    window.alert("You win, congrats")
                                }, 500)
                            }
                            yesOrNo = true
                        }
                    }
                }
            }
        }
        if (yesOrNo == true) {
            RandomN()
        }


    }

    fun MoveRight() {
        var yesOrNo = false
        list.forEach { row ->
            for (index in 2 downTo 0) {
                if (row[index] != null) {
                    var x = index + 1
                    while (x < 3 && row[x] == null) {
                        x++
                    }
                    for (k in x downTo index + 1) {
                        if (row[k] == null) {
                            row[k] = row[index]
                            row[index] = null
                            yesOrNo = true
                        } else if (row[k] == row[index]) {
                            row[k] = 2 * row[k]!!
                            row[index] = null
                            scoreCount = scoreCount + row[k]!!
                            if (row[k] == 2048) {
                                window.setTimeout({
                                    window.alert("You win, congrats")
                                }, 500)
                            }
                            yesOrNo = true
                        }
                    }

                }
            }
        }
        if (yesOrNo == true) {
            RandomN()
        }

    }

    fun MoveUp() {
        var yesOrNo = false
        for (column in 0..3) {
            for (j in 1..3) {
                val row = list[j]
                if (row[column] != null) {
                    var x = j - 1
                    while (x > 0 && list[x][column] == null) {
                        x--
                    }
                    for (k in x until j) {
                        if (list[k][column] == null) {
                            list[k][column] = row[column]
                            row[column] = null
                            yesOrNo = true
                        } else if (list[k][column] == row[column]) {
                            list[k][column] = 2 * list[k][column]!!
                            row[column] = null
                            scoreCount = scoreCount + list[k][column]!!
                            if (row[k] == 2048) {
                                window.setTimeout({
                                    window.alert("You win, congrats")
                                }, 500)
                            }
                            yesOrNo = true
                        }
                    }
                }
            }
        }
        if (yesOrNo == true) {
            RandomN()
        }
    }

    fun MoveDown() {
        var yesOrNo = false
        for (column in 0..3) {
            for (j in 2 downTo 0) {
                val row = list[j]
                if (row[column] != null) {
                    var x = j + 1
                    while (x < 3 && list[x][column] == null) {
                        x++
                    }
                    for (k in x downTo j + 1) {
                        if (list[k][column] == null) {
                            list[k][column] = row[column]
                            row[column] = null
                            yesOrNo = true
                        } else if (list[k][column] == row[column]) {
                            list[k][column] = 2 * list[k][column]!!
                            row[column] = null
                            scoreCount = scoreCount + list[k][column]!!
                            if (row[k] == 2048) {
                                window.setTimeout({
                                    window.alert("You win, congrats")
                                }, 500)
                            }
                            yesOrNo = true
                        }
                    }
                }
            }
        }
        if (yesOrNo == true) {
            RandomN()
        }
    }

    fun EmptiesCount() = list.sumOf { row -> row.count { cell -> cell == null } }
    fun IsGameOver(): Boolean {
        if (EmptiesCount() == 0) {
            list.forEach { row ->
                for (cell in 1..3) {
                    if (row[cell] == row[cell - 1]) {
                        return false
                    }
                }
            }
            for (column in 0..3) {
                for (row in 1..3) {
                    if (list[row][column] == list[row - 1][column]) {
                        return false
                    }
                }
            }
            return true
        }
        return false
    }

    fun RandomN() {
        val empties = EmptiesCount()
        val randI = Random.nextInt(empties)
        var count = 0
        list.forEach { row ->
            for (index in 0..3) {

                if (row[index] == null) {
                    if (count == randI) {
                        if (Random.nextInt(7) == 3) {
                            row[index] = 4
                        } else {
                            row[index] = 2
                        }

                    }
                    count++

                }
            }
        }
        if (IsGameOver()) {

            window.setTimeout({
                hs =  scoreCount
                if (scoreCount > highScore) {
                    highScore = scoreCount
                }
                window.alert("Game over, hahahaha \n Your score: $scoreCount \n High Score: $highScore")
            }, 500)
            console.log("Game over")
        }
    }
    fun Reset() {
        hs = scoreCount
        if(scoreCount>highScore){
            highScore = scoreCount
        }

        for (i in 0..3) {
            for (j in 0..3) {
                list[i][j] = null
            }
        }

        scoreCount = 0
        RandomN()
        RandomN()
    }

    fun ColorMatch(value: Int?) =
        when (value) {

            null -> rgb(106,106,106)
            2 -> rgb(248,252,245)
            4 -> rgb(230,244,220)
            8 -> rgb(207,235,188)
            16 -> rgb(185,226,157)
            32 -> rgb(162,217,125)
            64 -> rgb(140,207,94)
            128 -> rgb(117,198,62)
            256 -> rgb(98,170,49)
            512 -> rgb(80,138,40)
            1024 -> rgb(62,107,31)
            2048 -> rgb(43,75,22)
            else -> rgb(32,56,16)
        }



}