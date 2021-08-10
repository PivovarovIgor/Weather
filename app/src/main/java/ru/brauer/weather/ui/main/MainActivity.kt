package ru.brauer.weather.ui.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import ru.brauer.weather.R
import ru.brauer.weather.databinding.ActivityMainBinding
import ru.brauer.weather.ui.showSnackbar

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    private var messageBeforeExit: Snackbar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(binding.container.id, MainFragment.newInstance())
                .commitNow()
        }
    }

    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount > 0) {
            super.onBackPressed()
            return
        }
        messageBeforeExit = if (messageBeforeExit?.isShown == true) {
            super.onBackPressed()
            null
        } else {
            binding.root.showSnackbar(R.string.press_back_again_to_exit)
        }
    }
}