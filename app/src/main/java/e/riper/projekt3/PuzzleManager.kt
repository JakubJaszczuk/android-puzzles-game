package e.riper.projekt3

import android.content.Context
import android.graphics.Bitmap
import kotlin.math.sign
import kotlin.random.Random

class PuzzleManager(val shapes: List<Shape>, private val threshold: Float = 0.1f) {

    var selectedShape: Shape? = null
    var correctCount = 0
    var isEnd = false

    fun randomizePositions() {
        Random.nextFloat()
        for (s in shapes) {
            s.position.x += (Random.nextFloat() - 0.5f)
            s.position.y += Random.nextFloat() + 0.5f
        }
    }

    fun checkEnd(): Boolean {
        return correctCount == shapes.size && !isEnd
    }

    fun unsetCurrent() {
        selectedShape = null
    }

    private fun increaseCorrectCount() {
        correctCount++
    }

    fun movePuzzle(transX: Float, transY: Float, puzzle: Shape) {
        puzzle.move(transX, transY)
    }

    fun moveSelectedPuzzle(transX: Float, transY: Float) {
        selectedShape?.move(transX, transY)
    }

    private fun getClosestShape(x: Float, y: Float): Shape? {
        return shapes.minBy { it.position.distanceSquared(x, y) }
    }

    fun setSelectedShape(x: Float, y: Float): Boolean {
        val closestShape = getClosestShape(x, y)
        if (closestShape != null && !closestShape.atCorrectPosition) {
            val distance = closestShape.position.distance(x, y)
            if (distance < threshold) {
                selectedShape = closestShape
                return true
            }
        }
        return false
    }

    fun checkShapeCorrect(): Boolean {
        if (selectedShape != null && selectedShape?.isNearOrigin(threshold) == true) {
            selectedShape?.setCorrectPosition()
            increaseCorrectCount()
            return true
        }
        return false
    }

    fun moveSelectedOutside(shape: Shape) {
        val vec = Vertex(0.5f, 0.5f) - shape.position
        shape.move(-1.0f * vec.x.sign, 1.0f * vec.y.sign)
    }


    companion object {

        fun getSplitesNumber(image: Bitmap, context: Context): Pair<Int, Int> {
            val preferences = context.getSharedPreferences(Settings.getSharedPreferencesName(context), Context.MODE_PRIVATE)
            val dim1 = preferences.getInt(Settings.keyWidth, Settings.defaultWidth)
            val dim2 = preferences.getInt(Settings.keyHeight, Settings.defaultHeight)
            //val dim1 = 4
            //val dim2 = 3
            return if (image.width >= image.height) Pair(dim1, dim2) else Pair (dim2, dim1)
        }

        fun generateVertices(columns: Int, rows: Int, randomize: Boolean = false): List<Vertex> {
            // Normalized coordinates
            val stepX = 1.0f / columns
            val stepY = 1.0f / rows
            // Wygeneruj { (columns + 1) * (rows + 1) } punktów
            val vertices = ArrayList<Vertex>((columns + 1) * (rows + 1))
            for (i in 0..rows) {
                for (j in 0..columns) {
                    val distX = if (randomize && j != 0 && j != columns) (Random.nextFloat() * 0.2f - 0.1f) else 0f
                    val distY = if (randomize && i != 0 && i != rows) (Random.nextFloat() * 0.2f - 0.1f) else 0f
                    vertices.add(Vertex(j * stepX + distX, i * stepY + distY))
                }
            }
            return vertices
        }

        fun generateVertices(dimensions: Pair<Int, Int>, randomize: Boolean = false): List<Vertex> {
            return generateVertices(dimensions.first, dimensions.second, randomize)
        }

        fun generateShapes(columns: Int, rows: Int, vertices: List<Vertex>): List<Shape> {
            assert(vertices.size == (columns + 1) * (rows + 1)) {"Wrong number of vertices to construct shapes!"}
            // Wygeneruj { columns * rows } ścieżek do wycinania w obrazie
            val columnsReal = columns + 1
            val shapes = ArrayList<Shape>()
            for (i in 0 until rows) {
                for (j in 0 until columns) {
                    shapes.add(Shape(
                        vertices[j + columnsReal * i],
                        vertices[j + 1 + columnsReal * i],
                        vertices[j + 1 + columnsReal * (i+1)],
                        vertices[j + columnsReal * (i+1)]
                    ))
                }
            }
            return shapes
        }

        fun generateShapes(dimensions: Pair<Int, Int>, vertices: List<Vertex>): List<Shape> {
            return generateShapes(dimensions.first, dimensions.second, vertices)
        }

        fun build(image: Bitmap, context: Context): List<Shape> {
            val splits = getSplitesNumber(image, context)
            val vertices = generateVertices(splits)
            return generateShapes(splits, vertices)
        }
    }
}
