package e.riper.projekt3

import org.junit.Test

import org.junit.Assert.*
import kotlin.math.pow
import kotlin.math.sqrt

class VertexTest {
	@Test
	fun add_1() {
        val v1 = Vertex(1.0f, 1.0f)
        val v2 = Vertex(1.0f, 1.0f)
        val res = v1 + v2
		assertTrue(res.x == 2.0f && res.y == 2.0f)
	}

    @Test
    fun add_2() {
        val v1 = Vertex()
        val v2 = Vertex(-11.0f, 51.0f)
        val res = v1 + v2
        assertTrue(res.x == -11.0f && res.y == 51.0f)
    }

    @Test
    fun add_3() {
        val v1 = Vertex()
        val v2 = Vertex()
        val res = v1 + v2
        assertTrue(res.x == 0.0f && res.y == 0.0f)
    }

    @Test
    fun subtract_1() {
        val v1 = Vertex()
        val v2 = Vertex()
        val res = v1 - v2
        assertTrue(res.x == 0.0f && res.y == 0.0f)
    }

    @Test
    fun subtract_2() {
        val v1 = Vertex(10.0f)
        val v2 = Vertex(0.0f, -1.0f)
        val res = v1 - v2
        assertTrue(res.x == 10.0f && res.y == 1.0f)
    }

    @Test
    fun multiply_1() {
        val v1 = Vertex()
        val res = v1 * 2.0f
        assertTrue(res.x == 0.0f && res.y == 0.0f)
    }

    @Test
    fun multiply_2() {
        val v1 = Vertex(10.0f, 5.0f)
        val res = v1 * -2.0f
        assertTrue(res.x == -20.0f && res.y == -10.0f)
    }

    @Test
    fun divide_1() {
        val v1 = Vertex()
        val res = v1 / 2.0f
        assertTrue(res.x == 0.0f && res.y == 0.0f)
    }

    @Test
    fun negation_1() {
        val v1 = Vertex()
        val res = -v1
        assertTrue(res.x == 0.0f && res.y == 0.0f)
    }

    @Test
    fun negation_2() {
        val v1 = Vertex(1.0f, -1.0f)
        val res = -v1
        assertTrue(res.x == -1.0f && res.y == 1.0f)
    }

    @Test
    fun distanceSquared_1() {
        val v1 = Vertex()
        val v2 = Vertex(0.0f, 0.0f)
        val res = v1.distanceSquared(v2)
        assertEquals(0.0f, res)
    }

    @Test
    fun distanceSquared_2() {
        val v1 = Vertex()
        val v2 = Vertex(10.0f, 10.0f)
        val res = v1.distanceSquared(v2)
        assertEquals(200.0f, res)
    }

    @Test
    fun distanceSquared_3() {
        val v1 = Vertex(-1.0f, -1.0f)
        val v2 = Vertex(1.0f, 1.0f)
        val res = v1.distanceSquared(v2)
        assertEquals(8.0f, res)
    }

    @Test
    fun distance_1() {
        val v1 = Vertex()
        val v2 = Vertex(0.0f, 0.0f)
        val res = v1.distance(v2)
        assertEquals(0.0f, res)
    }

    @Test
    fun distance_2() {
        val v1 = Vertex()
        val v2 = Vertex(10.0f, 10.0f)
        val res = v1.distance(v2)
        assertEquals(sqrt(200.0f), res)
    }

    @Test
    fun distance_3() {
        val v1 = Vertex(-1.0f, -1.0f)
        val v2 = Vertex(1.0f, 1.0f)
        val res = v1.distance(v2)
        assertEquals(sqrt(8.0f), res)
    }

    @Test
    fun distance_4() {
        val v1 = Vertex(-1.0f, -1.0f)
        val v2 = Vertex(1.0f, 1.0f)
        assertEquals(v1.distanceSquared(v2), v1.distance(v2).pow(2), 0.001f)
    }

}

class ShapeTest {
    private fun generateShape(): Shape {
        return Shape(
            Vertex(0.0f, 0.0f),
            Vertex(1.0f, 0.0f),
            Vertex(0.0f, 1.0f),
            Vertex(1.0f, 1.0f)
        )
    }

    @Test
    fun isCorrect() {
        val s = generateShape()
        s.move(1.0f, 10.0f)
        s.setCorrectPosition()
        assertTrue(s.atCorrectPosition && s.position.distance(s.origin) == 0.0f)
    }

    @Test
    fun center() {
        val s = generateShape()
        val res = s.getCenter()
        assertTrue(res.x == 0.5f && res.y == 0.5f)
    }

    @Test
    fun move() {
        val s = generateShape()
        s.move(10.0f, 3.0f)
        assertTrue(s.position.x == 10.5f && s.position.y == 3.5f)
    }

    @Test
    fun move_2() {
        val s = generateShape()
        s.move(10.0f, 3.0f)
        assertFalse(s.position.x == 10.0f && s.position.y == 3.0f)
    }
}

class PuzzleManagerTest {
    private fun generateShapes(size: Int): List<Shape> {
        return List<Shape>(size) {
            Shape(
                Vertex(0.0f, 0.0f),
                Vertex(1.0f, 0.0f),
                Vertex(0.0f, 1.0f),
                Vertex(1.0f, 1.0f)
            )
        }
    }

    @Test
    fun isEnd_1() {
        val shapes = generateShapes(12)
        val m = PuzzleManager(shapes)
        m.correctCount = 12
        assertTrue(m.checkEnd())
    }

    @Test
    fun isEnd_2() {
        val shapes = generateShapes(12)
        val m = PuzzleManager(shapes)
        m.correctCount = 11
        assertFalse(m.checkEnd())
    }

    @Test
    fun isEnd_3() {
        val shapes = generateShapes(12)
        val m = PuzzleManager(shapes)
        m.correctCount = 13
        assertFalse(m.checkEnd())
    }

    @Test
    fun isEnd_4() {
        val shapes = generateShapes(4)
        val m = PuzzleManager(shapes)
        m.correctCount = 4
        assertTrue(m.checkEnd())
    }
}
