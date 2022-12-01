package com.udacity.project4.locationreminders.data

import com.udacity.project4.locationreminders.data.ReminderDataSource
import com.udacity.project4.locationreminders.data.dto.ReminderDTO
import com.udacity.project4.locationreminders.data.dto.Result
private var returningError = false

class FakeDataSource(var items: MutableList<ReminderDTO>? = mutableListOf()) : ReminderDataSource {


    override suspend fun getReminders(): Result<List<ReminderDTO>> {
        items?.let { return Result.Success(it) }
        return Result.Error("No reminders found")
    }

    override suspend fun saveReminder(reminder: ReminderDTO) {
        items?.add(reminder)
    }

    // when the reminder list arent loaded somehow this give the right path
    override suspend fun getReminder(id: String): Result<ReminderDTO> {
        items?.firstOrNull { it.id == id }?.let { return Result.Success(it) }
        return Result.Error("Reminder not found")
    }


    override suspend fun deleteAllReminders() {
//delete all the items
        items = mutableListOf()


    }
}