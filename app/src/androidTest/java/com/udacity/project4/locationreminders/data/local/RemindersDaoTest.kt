package com.udacity.project4.locationreminders.data.local

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.SmallTest;
import com.udacity.project4.locationreminders.data.dto.ReminderDTO

import org.junit.runner.RunWith;

import kotlinx.coroutines.ExperimentalCoroutinesApi;
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.notNullValue
import org.hamcrest.MatcherAssert.assertThat
import org.junit.*
import java.util.*

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
//Unit test the DAO
@SmallTest
class RemindersDaoTest {

    @get:Rule
    var instantExcuteRule =InstantTaskExecutorRule()
    private lateinit var db :RemindersDatabase
    // we currently using in - memory database due to the ability that destroys data after process killed
    @Before
    fun initDb(){
        db=Room.inMemoryDatabaseBuilder(ApplicationProvider.getApplicationContext(),RemindersDatabase::class.java).build()
    }


    @After
    fun closeDb()=db.close()


    @Test fun insertReminders()= runBlockingTest {
        //starting by putting in the task & inserting the item
        val reminder=ReminderDTO("TITLE","DESCRIPTION","LOCATION",(-360..360).random()
            .toDouble(),(-360..360).random().toDouble())

        db.reminderDao().saveReminder(reminder)
        //then we get all the reminders
        val reminders=db.reminderDao().getReminders()
//we get how many reminders (items ) are in there which is one!
        Assert.assertThat(reminders.size, `is`(1))
        Assert.assertThat(reminders[0].id, `is`(reminder.id))
        Assert.assertThat(reminders[0].title, `is`(reminder.title))
        Assert.assertThat(reminders[0].description, `is`(reminder.description))
        Assert.assertThat(reminders[0].location, `is`(reminder.location))
        Assert.assertThat(reminders[0].latitude, `is`(reminder.latitude))
        Assert.assertThat(reminders[0].longitude, `is`(reminder.longitude))
    }
    @Test fun insertandGetById()= runBlockingTest {
        // inserting the reminder item
        val reminder=ReminderDTO("TITLE","DESCRIPTION","LOCATION",(-360..360).random()
            .toDouble(),(-360..360).random().toDouble())//getting the item by the id
        val loaded = db.reminderDao().getReminderById(reminder.id)
        // The loaded data contains the expected values.
        Assert.assertThat<ReminderDTO>(loaded as ReminderDTO, notNullValue())
        Assert.assertThat(loaded.id, `is`(reminder.id))
        Assert.assertThat(loaded.title, `is`(reminder.title))
        Assert.assertThat(loaded.description, `is`(reminder.description))
        Assert.assertThat(loaded.location, `is`(reminder.location))
        Assert.assertThat(loaded.latitude, `is`(reminder.latitude))
        Assert.assertThat(loaded.longitude, `is`(reminder.longitude))

    }
    @Test fun deletingAll()= runBlockingTest {
        //inserting list of remidners and then saving them
        val remindersList = listOf<ReminderDTO>(ReminderDTO("title", "description","location",(-360..360).random().toDouble(),(-360..360).random().toDouble()),
            ReminderDTO("title", "description","location",(-360..360).random().toDouble(),(-360..360).random().toDouble()),
            ReminderDTO("title", "description","location",(-360..360).random().toDouble(),(-360..360).random().toDouble()),

            ReminderDTO("title", "description","location",(-360..360).random().toDouble(),(-360..360).random().toDouble()))
        remindersList.forEach {
            db.reminderDao().saveReminder(it)
        }
        //now we are testing to delete them all
        db.reminderDao().deleteAllReminders()
        //after we delete we trying to get items in database
        val load = db.reminderDao().getReminders()
        //the list is empty as it should be
        Assert.assertThat(load.isEmpty(), `is`(true))

    }
    @Test fun gettingDelletingErros()= runBlockingTest {
        //inserting reminder
        val reminder=ReminderDTO("TITLE","DESCRIPTION","LOCATION",(-360..360).random()
            .toDouble(),(-360..360).random().toDouble())
        //deleting all the reminders
        db.reminderDao().deleteAllReminders()
        //trying to get a remidner by id that should be empty
        val load=  db.reminderDao().getReminderById(reminder.id)
        //should returning null
        Assert.assertNull(load)

    }
}