package com.zw.zwbase.domain

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "channel")
data class Channel(@PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id") val id : Long,
                   @ColumnInfo(name = "title") val title : String)
