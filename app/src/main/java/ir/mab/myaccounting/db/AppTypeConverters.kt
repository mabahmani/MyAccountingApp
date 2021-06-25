package ir.mab.myaccounting.db

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

class AppTypeConverters {
    @TypeConverter
    fun fromStringToListOfLongs(value: String?): List<Long?>? {
        val listType: Type = object : TypeToken<List<Long?>?>() {}.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun fromArrayListOfLongsToString(list: List<Long?>?): String? {
        return Gson().toJson(list)
    }
}