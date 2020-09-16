package diogeo

import org.scalajs.dom.{KeyboardEvent, document}
import slinky.core._
import slinky.core.annotations.react
import slinky.core.facade.ReactElement
import slinky.web.html._

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport
import scala.util.Random

@JSImport("resources/App.css", JSImport.Default)
@js.native
object AppCSS extends js.Object

@react class App extends Component {
  type Props = Unit
  case class State(questions: List[Country], correctCount: Int, incorrectCount: Int, skipCount: Int)

  private val css = AppCSS

  def initialState: State = State(Random.shuffle(Maps.countries), 0, 0, 0)

  def render(): ReactElement =
    div(className := "App")(
      div(id := "messageAnimations"),
      div(id := "header")(
        button(className := "skip", onMouseDown := { e =>
          skipQuestion()
        })("Skip"),
        div(className := "question")(createQuestion),
        div(className := "score")(s"✅ ${state.correctCount}"),
        div(className := "score")(s"❌ ${state.incorrectCount}"),
        div(className := "score")(s"👋 ${state.skipCount}")
      ),
      MapComponent(
        ViewBox(31, 242, 785, 459),
        onCountryAnswer _
      )
    )

  def createQuestion: String =
    state.questions match {
      case Nil       => "Congrats! You finished the quiz! 🎉"
      case head :: _ => s"Where is ${head.name}?"
    }

  def skipQuestion(): Unit =
    state.questions match {
      case Nil => ()
      case _   =>
        setState { s =>
          s.copy(questions = s.questions.tail, skipCount = s.skipCount + 1)
        }
    }

  def onCountryAnswer(countryId: String): Unit =
    state.questions match {
      case Nil       => ()
      case head :: _ =>
        if (head.id == countryId) {
          document.getElementById("messageAnimations").innerHTML = "<div class=\"correctMessage\">Correct!</div>"

          setState { s =>
            s.copy(questions = s.questions.tail, correctCount = s.correctCount + 1)
          }
        } else {
          document.getElementById("messageAnimations").innerHTML = "<div class=\"wrongMessage\">Wrong!</div>"

          setState { s =>
            s.copy(incorrectCount = s.incorrectCount + 1)
          }
        }
    }

  def onKeyPress: KeyboardEvent => Unit = { e =>
    if (e.keyCode == 'S') {
      skipQuestion()
    }
  }

  override def componentDidMount(): Unit = document.addEventListener("keydown", onKeyPress)

  override def componentWillUnmount(): Unit = document.removeEventListener("keydown", onKeyPress, false)
}

