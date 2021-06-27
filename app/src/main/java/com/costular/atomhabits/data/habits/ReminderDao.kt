package com.costular.atomhabits.data.habits

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy

@Dao
abstract class ReminderDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertReminder(reminderEntity: ReminderEntity)

}