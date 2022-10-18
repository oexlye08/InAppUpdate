package id.my.okisulton.inappupdatefirebase

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat.startActivity
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfig
import id.my.okisulton.inappupdatefirebase.RemoteConfigUtils.DESCRIPTION_DIALOG
import id.my.okisulton.inappupdatefirebase.RemoteConfigUtils.IS_FORCE
import id.my.okisulton.inappupdatefirebase.RemoteConfigUtils.TITLE_DIALOG
import id.my.okisulton.inappupdatefirebase.databinding.CustomDialogBinding


/**
 * Created by Oki Sulton on 17/10/2022.
 */
class CustomDialog(context: Context, remoteConfig: FirebaseRemoteConfig) : Dialog(context) {
    private var _binding: CustomDialogBinding? = null
    private var activity: Activity? = null
    private val binding get() = _binding!!
    var mContext: Context? = null

    private val config = Firebase.remoteConfig

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        this.activity = activity;
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = CustomDialogBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setUi()
    }

    private fun setUi() {
        binding.apply {
            titleDialog.text = config.getString(TITLE_DIALOG)
            descriptionDialog.text = config.getString(DESCRIPTION_DIALOG)

            if (config.getBoolean(IS_FORCE)) {
                btnSkip.visibility = View.GONE
                setCancelable(false)
            } else {
                btnSkip.visibility = View.VISIBLE
            }
            btnSkip.setOnClickListener { dismiss() }

            btnUpdate.setOnClickListener {
                Log.d(TAG, "setUi: KEPENCET")
                (mContext as Activity).startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("http://www.stackoverflow.com")))
            }
        }
    }



    override fun onBackPressed() {
        super.onBackPressed()
        if (config.getBoolean(IS_FORCE)) activity?.finish()
    }
    
    companion object {
        private const val TAG = "CustomDialog"
    }
}