package id.my.okisulton.inappupdatefirebase

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import id.my.okisulton.inappupdatefirebase.databinding.ActivityMainBinding
import kotlin.math.log


class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private val remoteConfig: FirebaseRemoteConfig = Firebase.remoteConfig

    override fun onCreate(savedInstanceState: Bundle?) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        val navController = findNavController(R.id.nav_host_fragment_content_main)
        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)

        binding.fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAnchorView(R.id.fab)
                .setAction("Action", null).show()
        }

        // Firebase
        val defaultsRate: HashMap<String, Any> = HashMap()
        defaultsRate["new_version_code"] = BuildConfig.VERSION_CODE

        val configSettings = remoteConfigSettings {
            minimumFetchIntervalInSeconds = if (BuildConfig.DEBUG) {
                Log.d(TAG, "onCreate: true")
                0
            } else {
                Log.d(TAG, "onCreate: false")
                60*60
            }
        }
        remoteConfig.apply {
            setConfigSettingsAsync(configSettings)
            setDefaultsAsync(defaultsRate)
            fetchAndActivate().addOnCompleteListener {
                Log.d(TAG, "onCreate: Remote Config Fetch Complete")
                dialogShow()
            }
        }
    }

    private fun dialogShow() {
        if (remoteConfig.getLong(RemoteConfigUtils.VERSION_CODES) > BuildConfig.VERSION_CODE) {
            Log.d(TAG, "dialogShow: true")
            val dialog: CustomDialog =  CustomDialog(this, remoteConfig)
            dialog.show()

        }
        Log.d(TAG, "dialogShow: false ${remoteConfig.getLong(RemoteConfigUtils.VERSION_CODES)} -- ${BuildConfig.VERSION_CODE}")
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }
    companion object {
        private const val TAG = "MainActivity"
    }
}