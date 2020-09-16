package diogeo

import org.scalajs.dom.document
import org.scalajs.dom.raw.{ SVGGElement, SVGPathElement }
import slinky.core._
import slinky.core.annotations.react
import slinky.core.facade.ReactElement
import slinky.web.html
import slinky.web.svg._

@react class MapComponent extends Component {
  case class Props(initialViewBox: ViewBox, answerFn: String => Unit)
  case class State(viewBox: ViewBox, anchorPoint: Option[Point], pressPoint: Option[Point])

  def initialState: State = State(props.initialViewBox, None, None)

  def render(): ReactElement =
    html.div(
      html.id := "mapContainer",
      html.onWheel := { e =>
        val scaleAmount = clamp(-0.1, 0.1)(e.deltaY * 0.005)
        println(s"scale ${scaleAmount}   ${state.viewBox}   -> ${state.viewBox.scale(scaleAmount)}")
        setState(s => s.copy(viewBox = s.viewBox.scale(scaleAmount)))
      },
      html.onMouseUp := { e =>
        val newPressPoint = Point(e.clientX, e.clientY)

        if (state.pressPoint.contains(newPressPoint)) {
          // Finds which country was clicked on and propagates it through the answer callback.
          // TODO: Find a nicer way of doing this. We want to avoid adding an event handler for every single country,
          // but this current method is hacky in that it needs to check the parent.
          e.target match {
            case el: SVGPathElement =>
              el.parentNode match {
                case g: SVGGElement => props.answerFn(g.id)
                case _              => ()
              }
            case _                  => ()
          }
        }

        setState(_.copy(anchorPoint = None, pressPoint = None))
      },
      html.onMouseDown := { e =>
        val pressLocation = Point(e.clientX, e.clientY)
        setState(_.copy(anchorPoint = Some(pressLocation), pressPoint = Some(pressLocation)))
      },
      html.onMouseMove := { e =>
        val clientX   = e.clientX
        val clientY   = e.clientY
        val element   = document.getElementById("map")
        val mapWidth  = element.clientWidth.toDouble
        val mapHeight = element.clientHeight.toDouble
        val scaleX    = state.viewBox.width / mapWidth
        val scaleY    = state.viewBox.height / mapHeight
        // Since we're maintaining the aspect ratio, we need to take into account that the SVG viewbox may not match the
        // actual HTML element's ratio (there can be "extra" space in one dimension).
        val scale     = scaleX max scaleY

        if (e.buttons == 1) {
          setState { s =>
            s.anchorPoint match {
              case None              => s
              case Some(anchorPoint) =>
                val dx = (anchorPoint.x - clientX) * scale
                val dy = (anchorPoint.y - clientY) * scale
                s.copy(viewBox = s.viewBox.pan(dx, dy), anchorPoint = Some(Point(clientX, clientY)))
            }
          }
        }
      },
      svg(
        id := "map",
        viewBox := s"${state.viewBox.x} ${state.viewBox.y} ${state.viewBox.width} ${state.viewBox.height}",
        Maps.world
      )
    )

  private def clamp(lower: Double, upper: Double)(number: Double): Double = Math.max(lower, Math.min(number, upper))
}
