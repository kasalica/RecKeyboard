package com.example.reckeyboard.database

import androidx.appcompat.app.AppCompatActivity
import com.example.reckeyboard.activities.KeyboardSettingsActivity.Companion.seekBarValue
import java.lang.Math.abs

class DataAnalyser : AppCompatActivity() {

    var maxTime: Int = 0
    var maxCoor: Float = 0F

    fun recogniseUser(db: DataBaseHandler): Boolean {

        if (compareTable2((db)) && compareTable3(db)) {
            return true
        }
        else
            return false
    }

    fun compareTable2(db: DataBaseHandler): Boolean {

        if (seekBarValue == 0) maxTime = 99999
        else maxTime = 1000 - seekBarValue*200

        var tempdata2 = db.readData2(TEMPTABLE2_NAME)
        var data2 = db.readData2(TABLE2_NAME)
        var timecount: Int = 0

        for (i in 0..(tempdata2.size - 1)) {
            for (j in 0..(data2.size - 1)) {
                if (tempdata2.get(i).characters == data2.get(j).characters) {
                    timecount = timecount + (tempdata2.get(i).time - data2.get(j).time)
                }
            }
        }

        if (abs(timecount) < maxTime)
            return true
        else
            return false
    }

    fun compareTable3(db:DataBaseHandler) : Boolean {

        if (seekBarValue == 0) maxCoor = 99999F
        else maxCoor =  (2000 - seekBarValue*100).toFloat()

        var tempdata3 = db.readData3(TEMPTABLE3_NAME)
        var data3 = db.readData3(TABLE3_NAME)
        var countX: Float = 0F
        var countY: Float = 0F
        for (i in 0..(tempdata3.size - 1)) {
            for (j in 0..(data3.size - 1)) {
                if (tempdata3.get(i).character == data3.get(j).character) {
                    countX = countX + (tempdata3.get(i).x - data3.get(j).x)
                    countY = countY + (tempdata3.get(i).y - data3.get(j).y)
                }
            }
        }

        if ((abs(countX) < maxCoor) && (abs(countY) < maxCoor))
            return true
        else
            return false
    }
}