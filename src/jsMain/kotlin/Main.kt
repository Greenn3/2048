import androidx.compose.runtime.*
import kotlinx.browser.document
import org.jetbrains.compose.web.attributes.*
import org.jetbrains.compose.web.attributes.EventsListenerBuilder.Companion.KEYDOWN
import org.jetbrains.compose.web.attributes.EventsListenerBuilder.Companion.TOUCHEND
import org.jetbrains.compose.web.attributes.EventsListenerBuilder.Companion.TOUCHSTART
import org.jetbrains.compose.web.css.*
import org.jetbrains.compose.web.dom.*
import org.jetbrains.compose.web.renderComposable
import org.w3c.dom.TouchEvent
import org.w3c.dom.events.KeyboardEvent
import org.w3c.dom.get
import kotlin.math.abs
import kotlin.random.Random

external interface Options{
    var passive: Boolean
}
var touchStartX: Int? = null
var touchStartY: Int? = null

fun main() {
    val options = (js("{}") as Options).apply {
        passive = false
    }

    var count: Int by mutableStateOf(0)


    val list: MutableList<MutableList<Int?>> = mutableStateListOf(
        mutableStateListOf(null, null, null, null),
        mutableStateListOf(null, null, null, null),
        mutableStateListOf(null, null, null, null),
        mutableStateListOf(null, null, null, null)
    )

    fun EmptiesCount() = list.sumOf { row -> row.count { cell -> cell == null } }
    fun IsGameOver(): Boolean {
        if(EmptiesCount() == 0){
            list.forEach { row ->
                for(cell in 1..3){
                    if(row[cell] == row[cell - 1]){
                        return false
                    }
                }
            }
            for(column in 0..3){
                for(row in 1..3){
                    if(list[row][column] == list[row-1][column]){
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
                       if( Random.nextInt(7) == 3){
                           row[index] = 4
                       }
                        else{
                           row[index] = 2
                       }

                    }
                    count++

                }
            }
        }
        if(IsGameOver()){
            console.log("Game over")
        }
    }

    fun MoveLeft() {
        var yesOrNo = false
        list.forEach { row ->
            for (index in 1..3) {
                if (row[index] != null) {
                    var x = index-1
                    while(x > 0 && row[x]==null){
                        x--
                    }
                    for (k in x until index) {
                        if (row[k] == null) {
                            row[k] = row[index]
                            row[index] = null
                            yesOrNo = true
                        }
                        else if(row[k] == row[index]){
                            row[k] = 2*row[k]!!
                            row[index] = null
                            yesOrNo = true
                        }
                    }
                }
            }
        }
        if(yesOrNo == true){
            RandomN()
        }



    }

    fun MoveRight() {
        var yesOrNo = false
        list.forEach { row ->
            for (index in 2 downTo 0) {
                if (row[index] != null) {
                    var x = index +1
                    while(x < 3 && row[x]==null){
                        x++
                    }
                    for (k in x downTo index + 1) {
                        if (row[k] == null) {
                            row[k] = row[index]
                            row[index] = null
                            yesOrNo = true
                        }
                        else if(row[k] == row[index]){
                            row[k] = 2*row[k]!!
                            row[index] = null
                            yesOrNo = true
                        }
                    }

                }
            }
        }
        if(yesOrNo == true){
            RandomN()
        }

    }

    fun MoveUp() {
        var yesOrNo = false
        for (column in 0..3) {
            for (j in 1..3) {
                val row = list[j]
                if (row[column] != null) {
                    var x = j-1
                    while(x>0 && list[x][column] == null){
                        x--
                    }
                    for (k in x until j) {
                        if (list[k][column] == null) {
                            list[k][column] = row[column]
                            row[column] = null
                            yesOrNo = true
                        }
                        else if(list[k][column] == row[column]){
                            list[k][column]  = 2*list[k][column]!!
                            row[column] = null
                            yesOrNo = true
                        }
                    }
                }
            }
        }
        if(yesOrNo == true){
            RandomN()
        }
    }

    fun MoveDown() {
        var yesOrNo = false
        for (column in 0..3) {
            for (j in 2 downTo 0) {
                val row = list[j]
                if (row[column] != null) {
                    var x = j+1
                    while(x<3 && list[x][column] == null){
                        x++
                    }
                    for (k in x downTo j+1) {
                        if (list[k][column] == null) {
                            list[k][column] = row[column]
                            row[column] = null
                            yesOrNo = true
                        }
                        else if(list[k][column] == row[column] ){
                            list[k][column]  = 2*list[k][column]!!
                            row[column] = null
                            yesOrNo = true
                        }
                    }
                }
            }
        }
        if(yesOrNo == true){
            RandomN()
        }
    }


    RandomN()
    RandomN()

    document.addEventListener(KEYDOWN, {
        when ((it as KeyboardEvent).key) {
            "ArrowLeft" -> MoveLeft()
            "ArrowRight" -> MoveRight()
            "ArrowUp" -> MoveUp()
            "ArrowDown" -> MoveDown()
        }

        // console.log((it as KeyboardEvent).key)
    })
    document.addEventListener(TOUCHSTART, {
        val event = it as TouchEvent
        event.preventDefault()
        touchStartX = event.touches[0]?.pageX
        touchStartY = event.touches[0]?.pageY
    }, options)
    document.addEventListener(TOUCHEND, {

        val event = it as TouchEvent
        event.preventDefault()
       val touchEndX = event.changedTouches[0]?.pageX
       val touchEndY = event.changedTouches[0]?.pageY

        val difX = touchEndX!! - touchStartX!!
        val difY = touchEndY!! - touchStartY!!
        if(abs(difX)<abs(difY)){
            if(difY>0){
                MoveDown()
            }
            if(difY<0){
                MoveUp()
            }
        }
        if(abs(difX)>abs(difY)){
            if(difX>0){
                MoveRight()
            }
            if(difX<0){
                MoveLeft()
            }
        }
    }, options)


    renderComposable(rootElementId = "root") {


        Table {


            for (i in 0..3) {
                Tr {
                    for (j in 0..3) Td({
                        style {
                            border(1.px, LineStyle.Solid, Color.chocolate)
                            width(25.px)
                            height(25.px)
                            textAlign("center")
                            property("vertical-align", "center")
                        }
                    }) {
                        Text(list[i][j]?.toString() ?: "")
                    }
                }
            }
        }

    }
}


