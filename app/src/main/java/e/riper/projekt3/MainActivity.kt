package e.riper.projekt3

import android.app.Application
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import androidx.lifecycle.AndroidViewModel
import kotlinx.android.synthetic.main.activity_main.*

class MainModel(application: Application) : AndroidViewModel(application) {
    // Można też dać LiveData i ładować adresy obrazków asynchronicznie
    val data = loadImages()
    var currentSelected = 0

	fun getCurrent(): String {
		return data[currentSelected]
	}

    private fun loadImages(): List<String> {
        return getApplication<Application>().assets?.list(Utils.subAssetPath)?.toCollection(ArrayList()) ?: emptyList<String>()
    }
}

class MainActivity : AppCompatActivity() {

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_main)
		setSupportActionBar(toolbar)
	}

	override fun onCreateOptionsMenu(menu: Menu): Boolean {
		menuInflater.inflate(R.menu.menu_main, menu)
		return true
	}

	override fun onOptionsItemSelected(item: MenuItem): Boolean {
		return when (item.itemId) {
			R.id.action_settings -> {
				val dialog = SettingsFragment()
				dialog.show(supportFragmentManager, "SET")
				true
			}
			else -> super.onOptionsItemSelected(item)
		}
	}
}
