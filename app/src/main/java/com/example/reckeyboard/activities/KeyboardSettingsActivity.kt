@file:Suppress("DEPRECATION")

package com.example.reckeyboard.activities

import android.content.SharedPreferences.Editor
import android.graphics.Color
import android.inputmethodservice.Keyboard
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.View
import android.widget.RadioButton
import android.widget.SeekBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.reckeyboard.CustomRecKeyboard.Companion.keyboard_normal
import com.example.reckeyboard.CustomRecKeyboard.Companion.kv
import com.example.reckeyboard.R
import kotlinx.android.synthetic.main.activity_keyboard_settings.*


class KeyboardSettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_keyboard_settings)

        loadSettings()

        var percentage = seekBarValue*20
        TextValueSB.text = "$percentage %"
        if (percentage == 0 ){
            TextValueSB.text = "OFF"
            TextValueSB.setTextColor(Color.parseColor("#D64F33"))
        }
        else{
            TextValueSB.text = "$percentage %"
            TextValueSB.setTextColor(Color.parseColor("#000000"))
        }

        seekBarSens.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {

            override fun onProgressChanged(seekBar: SeekBar, i: Int, b: Boolean) {
                // Display the current progress of SeekBar

                seekBarValue = i
                percentage= seekBarValue*20
                if (percentage == 0 ){
                    TextValueSB.text = "OFF"
                    TextValueSB.setTextColor(Color.parseColor("#D64F33"))
                }
                else{
                    TextValueSB.text = "$percentage %"
                    TextValueSB.setTextColor(Color.parseColor("#000000"))
                }

            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                saveSettings()
            }
        })
    }

    fun onRadioButtonClicked(view: View) {
        saveSettings()
        if (view is RadioButton) {
            // Is the button now checked?
            val checked = view.isChecked

            // Check which radio button was clicked
            when (view.getId()) {
                R.id.radio_english ->
                    if (checked) {
                        keyboard_normal = Keyboard(this, R.xml.english)
                        kv!!.keyboard = keyboard_normal
                    }
                R.id.radio_russian ->
                    if (checked) {
                        keyboard_normal = Keyboard(this, R.xml.russian)
                        kv!!.keyboard = keyboard_normal
                    }
            }
        }
    }

    fun saveSettings() {
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)

        val editor: Editor = sharedPreferences.edit()
        editor.putBoolean("english", radio_english.isChecked())
        editor.putBoolean("russian", radio_russian.isChecked())

        if (radio_russian.isChecked) editor.putInt("language", 0)
        if (radio_english.isChecked) editor.putInt("language", 1)

        var seekBarValue: Int = seekBarSens.getProgress()
        editor.putInt("seekBarSensitivity", seekBarValue)

        editor.apply()
    }

    fun loadSettings() {
        var sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        radio_english.setChecked(sharedPreferences.getBoolean("english", false))
        radio_russian.setChecked(sharedPreferences.getBoolean("russian", true))

        seekBarValue = sharedPreferences.getInt("seekBarSensitivity", 0)
        seekBarSens.setProgress(seekBarValue)
    }

    companion object {
        var seekBarValue = 0
    }
}
