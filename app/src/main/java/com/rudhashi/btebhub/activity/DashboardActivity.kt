package com.rudhashi.btebhub.activity

import android.app.DownloadManager
import android.content.Context
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import com.rudhashi.btebhub.databinding.ActivityDashboardBinding
import com.rudhashi.btebhub.utils.SmartData
import com.rudhashi.btebhub.R

class DashboardActivity : AppCompatActivity() {

    private lateinit var navController: NavController
    private lateinit var binding: ActivityDashboardBinding

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor
    private var isNightMode: Boolean? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashboardBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        nightModeToggle()

        // Set up NavController
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.navHostFragmentContainerView) as NavHostFragment
        navController = navHostFragment.navController

        binding.apply {

            // Set default button background
            setButtonBackground(btnIndividualResult)

            // Set button click listeners
            btnIndividualResult.setOnClickListener {
                setButtonBackground(btnIndividualResult)
                navController.navigate(R.id.individualResultFragment)
            }

            btnGroupResult.setOnClickListener {
                setButtonBackground(btnGroupResult)
                navController.navigate(R.id.groupResultFragment)
            }

            btnNotice.setOnClickListener {
                setButtonBackground(btnNotice)
                navController.navigate(R.id.noticeFragment)
            }

            btnFavorite.setOnClickListener {
                setButtonBackground(btnFavorite)
                navController.navigate(R.id.favouriteFragment)
            }

            btnCoder.setOnClickListener {
                setButtonBackground(btnCoder)
                navController.navigate(R.id.developerFragment)
            }

            navController.addOnDestinationChangedListener { _, destination, _ ->
                when (destination.id) {
                    R.id.individualResultFragment -> setButtonBackground(btnIndividualResult)
                    R.id.groupResultFragment -> setButtonBackground(btnGroupResult)
                    R.id.noticeFragment -> setButtonBackground(btnNotice)
                    R.id.favouriteFragment -> setButtonBackground(btnFavorite)
                    R.id.favouriteFragment -> setButtonBackground(btnCoder)
                    else -> resetButtonBackgrounds()
                }
            }
        }
    }

    private fun setButtonBackground(selectedButton: View) {
        resetButtonBackgrounds()
        selectedButton.setBackgroundResource(R.drawable.sl_button_selected)
    }

    private fun resetButtonBackgrounds() {
        binding.apply {
            btnIndividualResult.setBackgroundResource(R.color.sideBar_Bg)
            btnGroupResult.setBackgroundResource(R.color.sideBar_Bg)
            btnNotice.setBackgroundResource(R.color.sideBar_Bg)
            btnFavorite.setBackgroundResource(R.color.sideBar_Bg)
            btnCoder.setBackgroundResource(R.color.sideBar_Bg)
        }
    }

    // ----------------------------------------------------------------
    override fun onSupportNavigateUp(): Boolean {
        // Set up NavController
        navController = findNavController(R.id.navHostFragmentContainerView)
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    private fun nightModeToggle() {
        // Initialize the binding
        sharedPreferences = getSharedPreferences(SmartData.PREF_KEY, MODE_PRIVATE)!!
        editor = sharedPreferences.edit()

        // Get the current theme mode
        isNightMode = sharedPreferences.getBoolean(SmartData.SWITCH_BUTTON_KEY, false)
        if (isNightMode as Boolean) {
            binding.sideBarBtnTheme.setImageResource(R.drawable.sl_moon)
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            binding.sideBarBtnTheme.setImageResource(R.drawable.sl_sun)
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }

        binding.sideBarBtnTheme.setOnClickListener {
            if (isNightMode as Boolean) {
                editor.putBoolean(SmartData.SWITCH_BUTTON_KEY, true).apply()
                binding.sideBarBtnTheme.setImageResource(R.drawable.sl_moon)
                binding.sideBarBtnTheme.setBackgroundResource(R.drawable.night_mode_thumb_true)
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                isNightMode = false
            } else {
                editor.putBoolean(SmartData.SWITCH_BUTTON_KEY, false).apply()
                binding.sideBarBtnTheme.setImageResource(R.drawable.sl_sun)
                binding.sideBarBtnTheme.setBackgroundResource(R.drawable.night_mode_thumb_false)
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                isNightMode = true
            }
        }
    }

    private fun checkStoragePermission(): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(
                    arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    PERMISSION_REQUEST_CODE
                )
                return false
            }
        }
        return true
    }

    fun startDownloading(url: String) {
        if (checkStoragePermission()) {
            try {
                val request = DownloadManager.Request(Uri.parse(url))
                request.setTitle("Downloading File")
                request.setDescription("Please wait while the file is downloading.")
                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    request.setDestinationInExternalFilesDir(this, Environment.DIRECTORY_DOWNLOADS, "downloaded_file.pdf")
                } else {
                    request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "downloaded_file.pdf")
                }

                val downloadManager = getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
                downloadManager.enqueue(request)
            } catch (e: Exception) {
                Toast.makeText(this, "Download failed: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission granted", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show()
            }
        }
    }

    companion object {
        private const val PERMISSION_REQUEST_CODE = 101
    }

}