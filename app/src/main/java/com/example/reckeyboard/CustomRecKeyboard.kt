@file:Suppress("DEPRECATION")

package com.example.reckeyboard

import android.annotation.SuppressLint
import android.inputmethodservice.InputMethodService
import android.inputmethodservice.Keyboard
import android.inputmethodservice.KeyboardView
import android.inputmethodservice.KeyboardView.OnKeyboardActionListener
import android.preference.PreferenceManager
import android.util.Log
import android.view.KeyEvent
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import com.example.reckeyboard.activities.KeyboardSettingsActivity.Companion.seekBarValue
import com.example.reckeyboard.database.*
import java.lang.Thread.sleep
import kotlin.concurrent.thread


class CustomRecKeyboard : InputMethodService(), OnKeyboardActionListener {

    private var keyboard_symbols : Keyboard? = null
    private var keyboard_symbols2 : Keyboard? = null
    private var isCaps = false
    private var keyList: List<Keyboard.Key>? = null

    var pastkey = ""
    var time = 0.toLong()

    fun retrieveKeys() {
        keyList = kv!!.keyboard.keys
    }

    var myDB =
        DataBaseHandler(this, null, null, 1)

    var Analyzer = DataAnalyser()

    override fun onCreate() {
        super.onCreate()
        initKeyboard()

    }

    private fun initKeyboard() {
        var sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        when (sharedPreferences.getInt("language", 0)) {
            0 ->  keyboard_normal = Keyboard(this, R.xml.russian)
            1 ->  keyboard_normal = Keyboard(this, R.xml.english)
        }
        keyboard_symbols = Keyboard(this, R.xml.symbols)
        keyboard_symbols2 = Keyboard(this, R.xml.symbols2)
    }


    @SuppressLint("ClickableViewAccessibility")
    override fun onStartInputView(info: EditorInfo?, restarting: Boolean) {
        super.onStartInputView(info, restarting)
        pastkey = ""
        time = 0.toLong()
    }

    override fun isInputViewShown(): Boolean {
        return super.isInputViewShown()
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateInputView(): View {

        kv = layoutInflater.inflate(R.layout.keyboard, null) as KeyboardView?

        if (keyboard_normal == null) keyboard_normal = Keyboard(this, R.xml.russian)

        keyboard_symbols = Keyboard(this, R.xml.symbols)
        keyboard_symbols2 = Keyboard(this, R.xml.symbols2)

        kv!!.keyboard = keyboard_normal

        var sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        seekBarValue = sharedPreferences.getInt("seekBarSensitivity", 4)


        kv!!.setOnKeyboardActionListener(this)
        kv!!.isPreviewEnabled = false

        kv!!.setOnTouchListener { _, event ->
            // For each key in the key list

            retrieveKeys()
            for (k in keyList!!) {

                if (event.action == MotionEvent.ACTION_UP) {
                    // If the coordinates from the Motion event are inside of the key
                    if (k.isInside(event.x.toInt(), event.y.toInt())) {
                        // k is the key pressed
                        var centreX: Float
                        var centreY: Float

                        // These values are relative to the Keyboard View
                        centreX = k.width / 2 + k.x.toFloat()
                        centreY = k.height / 2 + k.y.toFloat()

                        //saveing data in temporary tables
                        myDB.insertData1(k.label.toString(), 1 , TEMPTABLE1_NAME)

                        if(pastkey!="") {
                            time = System.currentTimeMillis() - time
                            if (time < 2000)
                            myDB.insertData2(pastkey + k.label.toString(), time.toInt(), 1, TEMPTABLE2_NAME)

                        }
                        //we count press coordinates, not release!!!
                        myDB.insertData3(k.label.toString(),event.x-centreX, centreY-event.y, 1, TEMPTABLE3_NAME)

                        pastkey = k.label.toString()
                        time = System.currentTimeMillis()

                        count = count+1

                        //Analyze and transfer data when 50 characters are typed
                        if (count == 50){
                            if (Analyzer.recogniseUser(myDB)){

                                thread(start = true) {
                                    for( i in 0..2){
                                        Log.e(" sagagasgagasgagag : ", i.toString())
                                        sleep(1000)
                                    }

                                    myDB.transferTempData()

                                    count = 0
                                    myDB.deleteTempData()
                                    pastkey = ""
                                    time = 0.toLong()

                                }



                            }
                            else {
                                requestHideSelf(0)
                                Toast.makeText(
                                    applicationContext,
                                    "You are not Vu!",
                                    Toast.LENGTH_LONG
                                ).show()
                                count = 0
                                myDB.deleteTempData()
                                pastkey = ""
                                time = 0.toLong()
                            }


                        }
                    }
                }
                if (event.action == MotionEvent.ACTION_MOVE){
                    // TODO for language change TODO
                }
            }
            false
        }
        return kv!!
    }

    override fun onPress(i: Int) {}
    override fun onRelease(i: Int) {}


    override fun onKey(i: Int, ints: IntArray) {
        val ic = currentInputConnection

        when (i) {

            KEYCODE_SYMBOLS -> {
                kv!!.keyboard = keyboard_symbols
                retrieveKeys()
            }

            KEYCODE_SYMBOLS2 -> {
                kv!!.keyboard = keyboard_symbols2
                retrieveKeys()
            }

            KEYCODE_BACK -> {
                kv!!.keyboard = keyboard_normal
                retrieveKeys()
            }

            Keyboard.KEYCODE_DELETE -> ic.deleteSurroundingText(1, 0)

            Keyboard.KEYCODE_SHIFT -> {
                isCaps = !isCaps
                keyboard_normal!!.isShifted = isCaps
                kv!!.invalidateAllKeys()
            }
            Keyboard.KEYCODE_DONE -> {

                ic.sendKeyEvent(
                    KeyEvent(
                        KeyEvent.ACTION_DOWN,
                        KeyEvent.KEYCODE_ENTER
                    )
                )
                pastkey = ""
                time = 0.toLong()

            }
            else -> {
                var code = i.toChar()
                if (Character.isLetter(code) && isCaps) code =
                    Character.toUpperCase(code)

                if(isCaps) {
                    isCaps = !isCaps
                    keyboard_normal!!.isShifted = isCaps
                    kv!!.invalidateAllKeys()
                }

                ic.commitText(code.toString(), 1)
            }
        }
    }

    override fun onText(charSequence: CharSequence) {}
    override fun swipeLeft() {}
    override fun swipeRight() {}
    override fun swipeDown() {}
    override fun swipeUp() {}

    companion object {
        var KEYCODE_BACK = -777
        var KEYCODE_SYMBOLS = -997
        var KEYCODE_SYMBOLS2 = -998
        var keyboard_normal: Keyboard? = null
        var kv: KeyboardView? = null
        var count: Int = 0


    }
}