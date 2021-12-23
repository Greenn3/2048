import kotlinx.browser.document
import kotlinx.browser.localStorage
import kotlinx.coroutines.MainScope
import org.jetbrains.compose.web.attributes.EventsListenerBuilder.Companion.KEYDOWN
import org.jetbrains.compose.web.attributes.EventsListenerBuilder.Companion.TOUCHEND
import org.jetbrains.compose.web.attributes.EventsListenerBuilder.Companion.TOUCHSTART
import org.jetbrains.compose.web.css.*
import org.jetbrains.compose.web.dom.*
import org.jetbrains.compose.web.renderComposable
import org.w3c.dom.TouchEvent
import org.w3c.dom.events.KeyboardEvent
import org.w3c.dom.get
import org.w3c.dom.set
import kotlin.math.abs
import kotlin.reflect.KProperty

external interface Options {
    var passive: Boolean
}

var touchStartX: Int? = null
var touchStartY: Int? = null

val scope = MainScope()
const val score = "score"

fun main() {
    var hs by object {
        operator fun getValue(thisRef: Nothing?, property: KProperty<*>) = localStorage[score]?.toInt()
        operator fun setValue(thisRef: Nothing?, property: KProperty<*>, value: Int?) {
            localStorage[score] = value.toString()
        }
    }




    val logic = Logic(hs)
    val options = (js("{}") as Options).apply {
        passive = false
    }



    logic.RandomN()
    logic.RandomN()

    document.addEventListener(KEYDOWN, {
        when ((it as KeyboardEvent).key) {
            "ArrowLeft" -> logic.MoveLeft()
            "ArrowRight" -> logic.MoveRight()
            "ArrowUp" -> logic.MoveUp()
            "ArrowDown" -> logic.MoveDown()
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
        if (abs(difX) < abs(difY)) {
            if (difY > 0) {
                logic.MoveDown()
            }
            if (difY < 0) {
                logic.MoveUp()
            }
        }
        if (abs(difX) > abs(difY)) {
            if (difX > 0) {
                logic.MoveRight()
            }
            if (difX < 0) {
                logic.MoveLeft()
            }
        }
    }, options)

    renderComposable(rootElementId = "root") {

        Button(attrs = {
            onClick { logic.Reset() }
            onTouchStart { logic.Reset() }

            style {
                border(25.px)
                width(200.px)
                height(50.px)
                fontSize(30.px)
                textDecorationColor(Color.orange)
                backgroundColor(Color.lightsalmon)
                margin(10.px)

            }
        }
        ) {
            Text("Reset")

        }


        Span({
            style {
                border(25.px)
                width(400.px)
                height(50.px)
                fontSize(30.px)
                textDecorationColor(Color.orange)
                backgroundSize("length")
                margin(10.px)

            }
        }) {
            Text("Score: ${logic.scoreCount}")
        }
        Span({
            style {
                border(25.px)
                width(400.px)
                height(50.px)
                fontSize(30.px)
                textDecorationColor(Color.orange)
                backgroundSize("length")
                margin(10.px)

            }
        }) {
            Text("High Score: ${logic.highScore}")
        }

        Table {


            for (i in 0..3) {
                Tr {
                    for (j in 0..3) Td({
                        style {
                            border(5.px, LineStyle.Solid, Color.chocolate)
                            width(200.px)
                            height(200.px)
                            textAlign("center")
                            fontSize(100.px)
                            backgroundColor(logic.ColorMatch(logic.list[i][j]))
                            property("vertical-align", "center")
                        }
                    }) {
                        Text(logic.list[i][j]?.toString() ?: "")
                    }
                }
            }
        }

    }
}






