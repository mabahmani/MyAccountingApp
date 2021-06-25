package ir.mab.myaccounting.util

import android.annotation.SuppressLint
import java.sql.Timestamp
import java.text.SimpleDateFormat

class DateFormatter {
    companion object{
        @SuppressLint("SimpleDateFormat")
        fun formatDateDayNameYearMonthDay(timestamp: Long): String{
            val sdf = SimpleDateFormat("EEE, MMM dd, yyyy")
            return sdf.format(timestamp)
        }
    }
}