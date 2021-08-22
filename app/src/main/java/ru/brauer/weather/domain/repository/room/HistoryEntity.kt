package ru.brauer.weather.domain.repository.room

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity
data class HistoryEntity(

    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val city: String,
    val date: Long,
    val temperature: Int,
    val condition: String
)