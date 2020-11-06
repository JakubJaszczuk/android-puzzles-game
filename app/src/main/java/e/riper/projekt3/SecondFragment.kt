package e.riper.projekt3

import android.content.Context
import android.graphics.*
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.observe
import kotlinx.android.synthetic.main.fragment_second.*
import java.util.*
import kotlin.concurrent.scheduleAtFixedRate

class GameViewModel : ViewModel() {

    var elapsedTime = MutableLiveData(0)
    val timer = Timer()
    private var isReady = false
    lateinit var manager: PuzzleManager

    init {
        setTask()
    }

    fun setManager(bitmap: Bitmap, context: Context) {
        if (!isReady) {
            isReady = true
            val dims = PuzzleManager.getSplitesNumber(bitmap, context)
            val vertices = PuzzleManager.generateVertices(dims, true)
            val shapes = PuzzleManager.generateShapes(dims, vertices)
            manager = PuzzleManager(shapes)
            manager.randomizePositions()
        }
    }

    private fun setTask() {
        timer.scheduleAtFixedRate(0, 1000) {
            elapsedTime.postValue(elapsedTime.value!! + 1)
        }
    }

    override fun onCleared() {
        super.onCleared()
        timer.cancel()
    }
}

class SecondFragment : Fragment() {

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
		return inflater.inflate(R.layout.fragment_second, container, false)
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val model: MainModel by activityViewModels()
        val gameModel: GameViewModel by viewModels()

        val bitmap = BitmapFactory.decodeStream(activity?.assets?.open(Utils.subAssetPath + model.getCurrent()))
        context?.let {
            gameModel.setManager(bitmap, it)
        }
        val showGrid = activity?.let {
            val preferences = it.getSharedPreferences(Settings.getSharedPreferencesName(it), Context.MODE_PRIVATE)
            preferences.getBoolean(Settings.keyGrid, Settings.defaultGrid)
        } ?: true

        gameModel.elapsedTime.observe(viewLifecycleOwner, {
            textView_timer?.text = Utils.datePrettier((it * 1000).toLong())
        })

        puzzleView.setImage(bitmap)
        puzzleView.setShapes(gameModel.manager.shapes)
        puzzleView.setPuzzleManager(gameModel.manager)
        puzzleView.setTimer(gameModel.timer)
        puzzleView.setImageBorder()
        puzzleView.setDrawGrid(showGrid)
    }
}
