package com.udacity.project4

import com.udacity.project4.locationreminders.data.dto.ReminderDTO
import com.udacity.project4.locationreminders.data.local.RemindersDao


class FakeDataSourceDao : RemindersDao {

    private var SuddenError = false

    val reminderData: LinkedHashMap<String, ReminderDTO> = LinkedHashMap()

    override suspend fun getReminders(): List<ReminderDTO> {
        if (SuddenError) {
            throw (Exception("Test exception"))
        }

        val list = mutableListOf<ReminderDTO>()
        list.addAll(reminderData.values)
        return list
    }

    override suspend fun getReminderById(reminderId: String): ReminderDTO? {
        if (SuddenError) {
            throw Exception("Test exception")
        }
        reminderData[reminderId]?.let {
            return it
        }
        return null
    }

    override suspend fun saveReminder(reminder: ReminderDTO) {
        reminderData[reminder.id] = reminder
    }

    override suspend fun deleteAllReminders() {
        reminderData.clear()
    }

}