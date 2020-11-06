package e.riper.projekt3

import android.graphics.Bitmap
import android.graphics.Path
import kotlin.math.sqrt

data class Vertex(var x: Float = 0.0f, var y: Float = 0.0f) {
    operator fun plus(other: Vertex): Vertex {
        return Vertex(x + other.x, y + other.y)
    }

    operator fun minus(other: Vertex): Vertex {
        return Vertex(x - other.x, y - other.y)
    }

    operator fun times(mul: Float): Vertex {
        return Vertex(x * mul, y * mul)
    }

    operator fun div(divisor: Float): Vertex {
        return Vertex(x / divisor, y / divisor)
    }

    operator fun unaryMinus(): Vertex {
        return Vertex(-x, -y)
    }

    fun distanceSquared(other: Vertex): Float {
        val difX = x - other.x
        val difY = y - other.y
        return difX * difX + difY * difY
    }

    fun distanceSquared(x: Float, y: Float): Float {
        val difX = this.x - x
        val difY = this.y - y
        return difX * difX + difY * difY
    }

    fun distance(other: Vertex): Float {
        return sqrt(distanceSquared(other))
    }

    fun distance(x: Float, y: Float): Float {
        return sqrt(distanceSquared(x, y))

    }
}

/**
 * Klasa reprezentująca puzel w logice
 */
class Shape(val p0: Vertex, val p1: Vertex, val p2: Vertex, val p3: Vertex) {
    val origin: Vertex = getCenter()  // Aby nie liczyć za każdym razem
    var position: Vertex = getCenter()
    var atCorrectPosition = false  // Zablokowanie po poprawnym

    fun getCenter(): Vertex {
        return (p0 + p1 + p2 + p3) / 4.0f
    }

    fun move(vertex: Vertex) {
        position += vertex
    }

    fun move(coordinates: Pair<Float, Float>) {
        position.x += coordinates.first
        position.y += coordinates.second
    }

    fun move(x: Float, y: Float) {
        position.x += x
        position.y += y
    }

    fun isNearOrigin(threshold: Float): Boolean {
        val r = position.distance(origin)
        return r < threshold
    }

    fun setCorrectPosition() {
        atCorrectPosition = true
        position = origin
    }
}

/**
 * Klasa reprezentująca puzel w przy rysowaniu
 */
class DrawableShape(val shape: Shape, image: Bitmap) {
    val path: Path = Path()
    init {
        path.moveTo(shape.p0.x * image.width, shape.p0.y * image.height)
        path.lineTo(shape.p1.x * image.width, shape.p1.y * image.height)
        path.lineTo(shape.p2.x * image.width, shape.p2.y * image.height)
        path.lineTo(shape.p3.x * image.width, shape.p3.y * image.height)
        path.close()
    }

    fun getTranslation(): Vertex {
        return shape.position
    }
}
