package e.riper.projekt3

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.DialogFragment
import kotlinx.android.synthetic.main.settings_fragment.view.*

class SettingsFragment : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            val inflater = requireActivity().layoutInflater;
            val view = inflater.inflate(R.layout.settings_fragment, null)
            builder.setView(view)
            builder.setPositiveButton(R.string.save) { _, _ -> save(view)}
            builder.setNegativeButton(R.string.cancel) { _, _ -> }

            val preferences = it.getSharedPreferences(Settings.getSharedPreferencesName(it), Context.MODE_PRIVATE)
            view.text_width.setText(preferences.getInt(Settings.keyWidth, Settings.defaultWidth).toString())
            view.text_height.setText(preferences.getInt(Settings.keyHeight, Settings.defaultHeight).toString())
            view.grid_switch.isChecked = preferences.getBoolean(Settings.keyGrid, Settings.defaultGrid)

            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    private fun save(view: View) {
        activity?.let {
            val preferences = it.getSharedPreferences(Settings.getSharedPreferencesName(it), Context.MODE_PRIVATE)
            val editor = preferences.edit()
            editor.putInt(Settings.keyWidth, view.text_width.text.toString().toInt())
            editor.putInt(Settings.keyHeight, view.text_height.text.toString().toInt())
            editor.putBoolean(Settings.keyGrid, view.grid_switch.isChecked)
            editor.apply()
        }
    }
}

object Settings {
    const val defaultWidth: Int = 4
    const val defaultHeight: Int = 3
    const val defaultGrid: Boolean = true
    const val keyWidth = "width"
    const val keyHeight = "height"
    const val keyGrid = "grid"

    fun getSharedPreferencesName(context: Context): String {
        return context.packageName + "_preferences"
    }
}
