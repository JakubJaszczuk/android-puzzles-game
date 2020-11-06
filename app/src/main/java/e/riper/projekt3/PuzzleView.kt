package e.riper.projekt3

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.view.View
import androidx.core.graphics.withClip
import com.google.android.material.snackbar.Snackbar
import java.util.*
import kotlin.math.max
import kotlin.math.min

class PuzzleView : View {

    private lateinit var image: Bitmap
    private lateinit var manager: PuzzleManager
    private lateinit var timer: Timer
    private lateinit var imageBorder: Path
    private var showGrid: Boolean = true
    private var shapes = emptyList<DrawableShape>()
    private var scaleFactor = 1.0f
    private var transX = 0.0f
    private var transY = 0.0f
    var isDrag = false

    private val painter = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.rgb(140, 140, 140)
        style = Paint.Style.STROKE
        strokeWidth = 10f
    }

    private val borderPainter = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.rgb(120, 210, 100)
        style = Paint.Style.STROKE
        strokeWidth = 10f
    }

    fun setImageBorder() {
        imageBorder = Path().apply {
            moveTo(0.0f, 0.0f)
            lineTo(image.width.toFloat(), 0.0f)
            lineTo(image.width.toFloat(), image.height.toFloat())
            lineTo(0.0f, image.height.toFloat())
            close()
        }
    }

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    fun setImage(image: Bitmap) {
        this.image = image
    }

    fun setPuzzleManager(manager: PuzzleManager) {
        this.manager = manager
    }

    fun setShapes(shapes: List<Shape>){
        this.shapes = shapes.map {shape -> DrawableShape(shape, image)}
    }

    fun setDrawableShapes(shapes: List<DrawableShape>){
        this.shapes = shapes
    }

    fun setTimer(timer: Timer) {
        this.timer = timer
    }

    fun setDrawGrid(show: Boolean) {
        this.showGrid = show
    }

    override fun onDraw(canvas: Canvas) {
        // Save
        canvas.save()
        // Transform
        canvas.scale(scaleFactor, scaleFactor, width / 2.0f, height / 2.0f)
        canvas.translate(transX, transY)
        // Draw
        if (showGrid) {
            // Rysuj kontur puzla
            for (e in shapes) {
                canvas.drawPath(e.path, painter)
            }
        }
        canvas.drawPath(imageBorder, borderPainter)
        for (e in shapes) {
            // Rysuj obraz
            canvas.save()
            val translation = e.getTranslation()
            val x = (translation.x - e.shape.origin.x) * image.width
            val y = (translation.y - e.shape.origin.y) * image.height
            canvas.translate(x, y)
            canvas.withClip(e.path) {
                canvas.drawBitmap(image, 0.0f, 0.0f, painter)
            }
            canvas.restore()
        }
        // Restore
        canvas.restore()
    }

    private fun getNormalizedCoordinates(x: Float, y: Float): Pair<Float, Float> {
        val halfW = width / 2.0f
        val halfH = height / 2.0f
        val newX = (((x - halfW) / scaleFactor) + halfW - transX) / image.width
        val newY = (((y - halfH) / scaleFactor) + halfH - transY) / image.height
        return Pair(newX, newY)
    }

    inner class TransListener : GestureDetector.SimpleOnGestureListener() {
        override fun onDown(e: MotionEvent): Boolean {
            return true
        }

        override fun onScroll(e1: MotionEvent, e2: MotionEvent, distanceX: Float, distanceY: Float): Boolean {
            // Przesuń widok lub puzel
            if(isDrag) {
                val x = (distanceX / image.width) / scaleFactor
                val y = (distanceY / image.height) / scaleFactor
                manager.moveSelectedPuzzle(-x, -y)
            }
            else {
                transX -= distanceX / scaleFactor
                transY -= distanceY / scaleFactor
            }
            return true
        }
    }

    inner class ScaleListener : ScaleGestureDetector.SimpleOnScaleGestureListener() {
        override fun onScale(detector: ScaleGestureDetector): Boolean {
            scaleFactor *= detector.scaleFactor
            scaleFactor = max(0.05f, min(scaleFactor, 10.0f))
            return true
        }
    }

    private val gestureDetector: GestureDetector = GestureDetector(context, TransListener())
    private val scaleGestureDetector: ScaleGestureDetector = ScaleGestureDetector(context, ScaleListener())

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.actionMasked) {
            MotionEvent.ACTION_DOWN -> onActionDown(event)
            MotionEvent.ACTION_UP -> onActionUP()
        }

        var retVal: Boolean = scaleGestureDetector.onTouchEvent(event)
        retVal = gestureDetector.onTouchEvent(event) || retVal
        invalidate()
        return retVal || super.onTouchEvent(event)
    }

    private fun onActionDown(event: MotionEvent) {
        // Convert coordinates
        val (x, y) = getNormalizedCoordinates(event.x, event.y)
        val isNewClosest = manager.setSelectedShape(x, y)
        isDrag = isNewClosest
    }

    private fun onActionUP() {
        isDrag = false
        manager.checkShapeCorrect()
        manager.unsetCurrent()
        val isEnd = manager.checkEnd()
        if (isEnd) {
            timer.cancel()
            manager.isEnd = true
            Snackbar.make(this, R.string.puzzle_end, Snackbar.LENGTH_SHORT).show()
        }
    }
}
