package com.example.reckeyboard.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.reckeyboard.CustomRecKeyboard.Companion.count
import com.example.reckeyboard.R
import com.example.reckeyboard.database.DataBaseHandler
import com.example.reckeyboard.database.*
import kotlinx.android.synthetic.main.activity_database.*


class DatabaseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_database)

        val context = this
        var db = DataBaseHandler(
            context,
            null,
            null,
            1
        )



        btnRead1.setOnClickListener {
            var data = db.readData1(TABLE1_NAME)
            listResult.text = ""
            for (i in 0..(data.size - 1)) {
                listResult.append(data.get(i).character + "  |  count: " + data.get(i).count + "\n")
            }
        }
        btnRead2.setOnClickListener {
            var data = db.readData2(TABLE2_NAME)
            listResult.text = ""
            for (i in 0..(data.size - 1)) {
                listResult.append(
                    data.get(i).characters + "  |  time: " + data.get(i).time + "ms  |  count: " + data.get(
                        i
                    ).count + "\n"
                )
            }
        }
        btnRead3.setOnClickListener {
            var data = db.readData3(TABLE3_NAME)
            listResult.text = ""
            for (i in 0..(data.size - 1)) {
                listResult.append(
                    data.get(i).character + "  |  x = " + data.get(i).x + ", y =  " + data.get(
                        i
                    ).y + "  |  count: " + data.get(i).count + "\n"
                )
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