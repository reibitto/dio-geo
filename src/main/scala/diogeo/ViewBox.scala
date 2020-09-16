package diogeo

final case class ViewBox(x: Double, y: Double, width: Double, height: Double) {
  def pan(dx: Double, dy: Double): ViewBox = copy(x = x + dx, y = y + dy)

  def scale(amount: Double): ViewBox = {
    val dx = width * amount
    val dy = height * amount
    ViewBox(x - dx, y - dy , width + dx * 2, height + dy * 2)
  }
}
