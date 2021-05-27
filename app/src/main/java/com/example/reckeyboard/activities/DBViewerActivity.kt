package com.example.reckeyboard.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.reckeyboard.CustomRecKeyboard.Companion.count
import com.example.reckeyboard.R
import com.example.reckeyboard.database.DataBaseHandler
import com.example.reckeyboard.database.*
import kotlinx.android.synthetic.main.activity_dbviewer.*
import kotlinx.android.synthetic.main.activity_dbviewer.btnRead1
import kotlinx.android.synthetic.main.activity_dbviewer.btnRead2
import kotlinx.android.synthetic.main.activity_dbviewer.btnRead3
import kotlinx.android.synthetic.main.activity_dbviewer.column1
import kotlinx.android.synthetic.main.activity_dbviewer.column1Label
import kotlinx.android.synthetic.main.activity_dbviewer.column2
import kotlinx.android.synthetic.main.activity_dbviewer.column2Label
import kotlinx.android.synthetic.main.activity_dbviewer.column3
import kotlinx.android.synthetic.main.activity_dbviewer.column3Label
import kotlinx.android.synthetic.main.activity_dbviewer.column4
import kotlinx.android.synthetic.main.activity_dbviewer.column4Label
import kotlinx.android.synthetic.main.activity_temp_dbviewer.*


class DBViewerActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dbviewer)

        val context = this
        var db = DataBaseHandler(
            context,
            null,
            null,
            1
        )

        btnRead1.setOnClickListener {
            var data = db.readData1(TABLE1_NAME)
            column1Label.text = COL_KEY
            column2Label.text = COL_HOLD_TIME
            column3Label.text = ""
            column4Label.text = COL_C
            column1.text = ""
            column2.text = ""
            column3.text = ""
            column4.text = ""
            for (i in 0..(data.size - 1)) {
                column1.append(data.get(i).character + "\n")
                column2.append(data.get(i).holdTime.toString() + "\n")
                column3.append("\n")
                column4.append(data.get(i).count.toString() + "\n")
            }
        }
        btnRead2.setOnClickListener {
            var data = db.readData3(TABLE3_NAME)
            column1Label.text = COL_KEY
            column2Label.text = COL_X
            column3Label.text = COL_Y
            column4Label.text = COL_C
            column1.text = ""
            column2.text = ""
            column3.text = ""
            column4.text = ""
            for (i in 0..(data.size - 1)) {
                column1.append(data.get(i).character + "\n")
                column2.append(data.get(i).x.toString() + "\n")
                column3.append(data.get(i).y.toString() + "\n")
                column4.append(data.get(i).count.toString() + "\n")
            }
        }

        btnRead3.setOnClickListener {
            var data = db.readData2(TABLE2_NAME)
            column1Label.text = COL_KEYS
            column2Label.text = COL_TIME
            column3Label.text = ""
            column4Label.text = COL_C
            column1.text = ""
            column2.text = ""
            column3.text = ""
            column4.text = ""
            for (i in 0..(data.size - 1)) {
                column1.append(data.get(i).characters + "\n")
                column2.append(data.get(i).time.toString() + "\n")
                column3.append("\n")
                column4.append(data.get(i).count.toString() + "\n")
            }
        }

        btnDeleteDB.setOnClickListener {
            db.deleteData()
            btnRead1.performClick()
        }

        btnTempDB.setOnClickListener {
            val intent = Intent(this, TempDBViewerActivity::class.java)
            startActivity(intent)
            count = 0
        }

    }
}