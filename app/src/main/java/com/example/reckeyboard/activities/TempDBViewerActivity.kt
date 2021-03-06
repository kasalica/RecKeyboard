package com.example.reckeyboard.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.reckeyboard.R
import com.example.reckeyboard.database.*
import kotlinx.android.synthetic.main.activity_temp_dbviewer.*

class TempDBViewerActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_temp_dbviewer)

        val context = this
        var db = DataBaseHandler(
            context,
            null,
            null,
            1
        )

        btnDeleteTempDB.setOnClickListener{
            db.deleteTempData()
            btnRead1.performClick()
        }

        btnRead1.setOnClickListener {
            var data = db.readData1(TEMPTABLE1_NAME)
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
            var data = db.readData3(TEMPTABLE3_NAME)
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
            var data = db.readData2(TEMPTABLE2_NAME)
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

    }
}
