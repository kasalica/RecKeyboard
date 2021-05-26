package com.example.reckeyboard.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.reckeyboard.R
import com.example.reckeyboard.database.DataBaseHandler
import com.example.reckeyboard.database.TEMPTABLE1_NAME
import com.example.reckeyboard.database.TEMPTABLE2_NAME
import com.example.reckeyboard.database.TEMPTABLE3_NAME
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
            listResult.text = ""
            for (i in 0..(data.size - 1)) {
                listResult.append(data.get(i).character + "  |  count: " + data.get(i).count + "\n")
            }
        }
        btnRead2.setOnClickListener {
            var data = db.readData2(TEMPTABLE2_NAME)
            listResult.text = ""
            for (i in 0..(data.size - 1)) {
                listResult.append(data.get(i).characters + "  |  time: " + data.get(i).time + "ms  |  count: " + data.get(i).count + "\n")
            }
        }
        btnRead3.setOnClickListener {
            var data = db.readData3(TEMPTABLE3_NAME)
            listResult.text = ""
            for (i in 0..(data.size - 1)) {
                listResult.append(data.get(i).character + "  |  x = " + data.get(i).x + ", y =  " + data.get(i).y + "  |  count: " + data.get(i).count + "\n")
            }
        }



    }
}
