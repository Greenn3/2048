import androidx.compose.runtime.*
import kotlinx.browser.document
import org.jetbrains.compose.web.attributes.*
import org.jetbrains.compose.web.attributes.EventsListenerBuilder.Companion.KEYDOWN
import org.jetbrains.compose.web.css.*
import org.jetbrains.compose.web.dom.*
import org.jetbrains.compose.web.renderComposable
import org.w3c.dom.events.KeyboardEvent
import kotlin.random.Random



fun main() {
    var count: Int by mutableStateOf(0)

    val list: MutableList<MutableList<Int?>> = mutableStateListOf(
        mutableStateListOf(null, null, null, null),
        mutableStateListOf(null, null, null, null),
        mutableStateListOf(null, null, null, null),
        mutableStateListOf(null, null, null, null)
    )

    fun RandomN() {
        val empties = list.sumOf { row -> row.count { cell -> cell == null } }
        val randI = Random.nextInt(empties)
        var count = 0
        list.forEach { row ->
            for (index in 0..3) {

                if (row[index] == null) {
                    if (count == randI) {
                        row[index] = 2
                    }
                    count++

                }
            }
        }
    }
    fun MoveLeft (){
        list.forEach { row ->
            for (index in 1..3){
                if (row[index] != null){
                    for(k in 0 until index){
                        if(row[k] == null){
                            row[k] = row[index]
                            row[index] = null
                        }
                    }
                }
            }
        }
    }
    RandomN()
    RandomN()

    document.addEventListener(KEYDOWN,{
        when((it as KeyboardEvent).key){
            "ArrowLeft" -> MoveLeft()
        }

       // console.log((it as KeyboardEvent).key)
    })

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
            Div({ style { padding(25.px) } }) {
                Button(attrs = {
                    onClick { count -= 1 }
                }) {
                    Text("-")
                }

                Span({ style { padding(15.px) } }) {
                    Text("$count")
                }

                Button(attrs = {
                    onClick { count += 1 }
                }) {
                    Text("+")
                }
            }
        }
    }
