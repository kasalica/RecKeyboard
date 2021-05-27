package com.example.reckeyboard

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.inputmethod.InputMethodInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_setup.*


class SetupActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setup)
        CheckEnable()

        btnOK.setOnClickListener {
            restartActivity()

        }
    }

    fun CheckEnable(){
        val packageLocal = packageName
        val inputMethodManager: InputMethodManager =
            getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        val list: List<InputMethodInfo> =
            inputMethodManager.getEnabledInputMethodList()

        for (inputMethod in list) {
            val packageName = inputMethod.packageName
            if (packageName == packageLocal) {

                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)

                Toast.makeText(applicationContext, "RecKeyboard is NOT enabled!", Toast.LENGTH_LONG)
                    .show()
            }
            else Toast.makeText(applicationContext, "RecKeyboard is enabled!", Toast.LENGTH_LONG)
                .show()
        }

    }

    fun restartActivity() {

        val intent = Intent(this, SetupActivity::class.java)
        startActivity(intent)
    }

}
