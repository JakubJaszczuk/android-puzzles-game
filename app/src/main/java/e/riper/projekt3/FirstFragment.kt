package e.riper.projekt3

import android.content.res.Configuration
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.fragment_first.*
import kotlinx.android.synthetic.main.puzzles_list_item.view.*

class ImagesGridAdapter(private val model: MainModel, private val navigator: NavController) : RecyclerView.Adapter<ImagesGridAdapter.ViewHolder>() {

	class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
		val image: ImageView = itemView.item_image
	}

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
		val view = LayoutInflater.from(parent.context).inflate(R.layout.puzzles_list_item, parent, false)
		return ViewHolder(view)
	}

	override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val uri = Uri.parse(Utils.fullAssetPath + model.data[position])
		Glide.with(viewHolder.image).load(uri).centerCrop().into(viewHolder.image)
		viewHolder.itemView.setOnClickListener {
			model.currentSelected = position
			navigator.navigate(R.id.action_FirstFragment_to_SecondFragment)
		}
	}

	override fun getItemCount() = model.data.size
}

class FirstFragment : Fragment() {

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
		return inflater.inflate(R.layout.fragment_first, container, false)
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)

        // Ilość kolumn zależna od orientacji
		val spanCount = getSpanCount()

		val model: MainModel by activityViewModels()
        val navigator = findNavController()
		recyclerView_image_list.layoutManager = GridLayoutManager(context, spanCount)
		recyclerView_image_list.setHasFixedSize(true)
		recyclerView_image_list.adapter = ImagesGridAdapter(model, navigator)
	}

	private fun getSpanCount(): Int {
		return if(resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
			2
		}
		else {
			3
		}
	}
}
