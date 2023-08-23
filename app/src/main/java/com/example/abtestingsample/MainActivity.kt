package com.example.abtestingsample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintLayout.LayoutParams
import com.google.firebase.ktx.Firebase
import com.google.firebase.ktx.initialize
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings

class MainActivity : AppCompatActivity() {

    private lateinit var remoteConfig: FirebaseRemoteConfig

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initializeFirebase()
        fetchAndActivateRemoteConfigValues()
    }

    private fun initializeFirebase() {
        Firebase.initialize(this)
        remoteConfig = Firebase.remoteConfig
        val configSettings = remoteConfigSettings {
            minimumFetchIntervalInSeconds = 0
        }
        remoteConfig.setConfigSettingsAsync(configSettings)
    }

    private fun setButtonConstraints(isOnTop: Boolean) {
        val button1 = findViewById<Button>(R.id.btn1)
        val button2 = findViewById<Button>(R.id.btn2)
        val constraintLayout = findViewById<ConstraintLayout>(R.id.constraintLayout)

        val paramsButton1 = button1.layoutParams as LayoutParams
        val paramsButton2 = button2.layoutParams as LayoutParams

        if (isOnTop) {
            paramsButton1.topToTop = constraintLayout.id
            paramsButton2.topToTop = constraintLayout.id
        } else {
            paramsButton1.bottomToBottom = constraintLayout.id
            paramsButton2.bottomToBottom = constraintLayout.id
        }

        button1.layoutParams = paramsButton1
        button2.layoutParams = paramsButton2
    }

    private fun fetchAndActivateRemoteConfigValues() {
        remoteConfig.fetchAndActivate()
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val isButtonOnTop = remoteConfig.getBoolean("isButtonOnTop")
                    Log.e("button", isButtonOnTop.toString())

                    setButtonConstraints(isButtonOnTop)
                }
            }
    }
}
