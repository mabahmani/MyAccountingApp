package ir.mab.myaccounting.entity

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(indices = [Index(value = ["category"], unique = true)])
data class Category(
    @PrimaryKey(autoGenerate = true) val categoryId: Long = 0,
    val category: String
){
    @Ignore
    var selected: Boolean = false;
}
