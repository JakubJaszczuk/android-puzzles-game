package e.riper.projekt3

import java.util.concurrent.TimeUnit

object Utils {

    const val subAssetPath = "puzzle/" // Bezpośrednio w assets jest tego więcej
    const val fullAssetPath = "file:///android_asset/puzzle/"

    fun datePrettier(millis: Long): String {
        val mins = TimeUnit.MILLISECONDS.toMinutes(millis)
        val secs = TimeUnit.MILLISECONDS.toSeconds(millis)
        return String.format("%02d:%02d", mins, secs - TimeUnit.MINUTES.toSeconds(mins));
    }
}
