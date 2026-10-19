package com.example.swpp.tododapp.tasks

import android.view.View
import android.widget.TextView
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import androidx.test.platform.app.InstrumentationRegistry
import com.example.swpp.tododapp.R
import com.example.swpp.tododapp.ServiceLocator
import com.example.swpp.tododapp.data.Task
import com.example.swpp.tododapp.data.source.FakeAndroidTestRepository
import org.hamcrest.Matchers.allOf
import org.hamcrest.Matchers.not
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.time.LocalDate

@MediumTest
@RunWith(AndroidJUnit4::class)
class TasksListBindingsTest {

    private lateinit var repository: FakeAndroidTestRepository

    @Before
    fun setUpRepository() {
        repository = FakeAndroidTestRepository()
        ServiceLocator.tasksRepository = repository
    }

    @After
    fun resetRepository() {
        ServiceLocator.resetRepository()
    }

    @Test
    fun setDueDateLabel_completedTaskWithDueDate_showsClear() {
        // Given
        val today = LocalDate.now()
        val task = task(dueDate = today.plusDays(6), isCompleted = true)

        // When
        val result = bind("OLD", View.GONE, task)

        // Then
        assertEquals("CLEAR", result.text)
        assertEquals(View.VISIBLE, result.visibility)
    }

    @Test
    fun setDueDateLabel_completedTaskWithoutDueDate_showsClear() {
        // Given
        val task = task(dueDate = null, isCompleted = true)

        // When
        val result = bind("", View.GONE, task)

        // Then
        assertEquals("CLEAR", result.text)
        assertEquals(View.VISIBLE, result.visibility)
    }

    @Test
    fun setDueDateLabel_activeTaskDueToday_showsDDay() {
        // Given
        val today = LocalDate.now()
        val task = task(dueDate = today, isCompleted = false)

        // When
        val result = bind("OLD", View.GONE, task)

        // Then
        assertEquals("D-Day", result.text)
        assertEquals(View.VISIBLE, result.visibility)
    }

    @Test
    fun setDueDateLabel_activeTaskDueInTwoDays_showsDMinus2() {
        // Given
        val today = LocalDate.now()
        val task = task(dueDate = today.plusDays(2), isCompleted = false)

        // When
        val result = bind("OLD", View.GONE, task)

        // Then
        assertEquals("D-2", result.text)
        assertEquals(View.VISIBLE, result.visibility)
    }

    @Test
    fun setDueDateLabel_activeTaskOverdueByTwoDays_showsDPlus2() {
        // Given
        val today = LocalDate.now()
        val task = task(dueDate = today.minusDays(2), isCompleted = false)

        // When
        val result = bind("OLD", View.GONE, task)

        // Then
        assertEquals("D+2", result.text)
        assertEquals(View.VISIBLE, result.visibility)
    }

    @Test
    fun setDueDateLabel_activeTaskWithoutDueDate_clearsAndHidesLabel() {
        // Given
        val task = task(dueDate = null, isCompleted = false)

        // When
        val result = bind("D-3", View.VISIBLE, task)

        // Then
        assertEquals("", result.text)
        assertEquals(View.GONE, result.visibility)
    }

    @Test
    fun setDueDateLabel_nullTask_clearsAndHidesLabel() {
        // Given
        val task: Task? = null

        // When
        val result = bind("D+4", View.VISIBLE, task)

        // Then
        assertEquals("", result.text)
        assertEquals(View.GONE, result.visibility)
    }

    @Test
    fun setDueDateLabel_nullTask_completesAndClearsAndHidesLabel() {
        // Given
        val task: Task? = null

        // When
        val result = bind("OLD", View.VISIBLE, task)

        // Then
        assertEquals("", result.text)
        assertEquals(View.GONE, result.visibility)
    }

    @Test
    fun setDueDateLabel_activeTaskDueYesterday_showsDPlus1() {
        // Given
        val today = LocalDate.now()
        val task = task(dueDate = today.minusDays(1), isCompleted = false)

        // When
        val result = bind("OLD", View.GONE, task)

        // Then
        assertEquals("D+1", result.text)
        assertEquals(View.VISIBLE, result.visibility)
    }

    @Test
    fun setDueDateLabel_activeTaskAtOverdueTodayBoundary_showsDDay() {
        // Given
        val today = LocalDate.now()
        val task = task(dueDate = today, isCompleted = false)

        // When
        val result = bind("OLD", View.GONE, task)

        // Then
        assertEquals("D-Day", result.text)
        assertEquals(View.VISIBLE, result.visibility)
    }

    @Test
    fun setDueDateLabel_activeTaskDueTomorrow_showsDMinus1() {
        // Given
        val today = LocalDate.now()
        val task = task(dueDate = today.plusDays(1), isCompleted = false)

        // When
        val result = bind("OLD", View.GONE, task)

        // Then
        assertEquals("D-1", result.text)
        assertEquals(View.VISIBLE, result.visibility)
    }

    @Test
    fun setDueDateLabel_recycledViewToNoDueDate_clearsAndHidesOldLabel() {
        // Given
        val task = task(dueDate = null, isCompleted = false)

        // When
        val result = bind("D-1", View.VISIBLE, task)

        // Then
        assertEquals("", result.text)
        assertEquals(View.GONE, result.visibility)
    }

    @Test
    fun setDueDateLabel_recycledViewToNullTask_clearsAndHidesOldLabel() {
        // Given
        val task: Task? = null

        // When
        val result = bind("D+1", View.VISIBLE, task)

        // Then
        assertEquals("", result.text)
        assertEquals(View.GONE, result.visibility)
    }

    @Test
    fun setDueDateLabel_completedToActiveWithoutDueDate_removesClear() {
        // Given
        val task = task(dueDate = null, isCompleted = false)

        // When
        val result = bind("CLEAR", View.VISIBLE, task)

        // Then
        assertEquals("", result.text)
        assertEquals(View.GONE, result.visibility)
    }

    @Test
    fun setDueDateLabel_completedToActiveWithDueDate_replacesClear() {
        // Given
        val today = LocalDate.now()
        val task = task(dueDate = today.plusDays(3), isCompleted = false)

        // When
        val result = bind("CLEAR", View.VISIBLE, task)

        // Then
        assertEquals("D-3", result.text)
        assertEquals(View.VISIBLE, result.visibility)
    }

    @Test
    fun setDueDateLabel_activeToCompleted_replacesDdayWithClear() {
        // Given
        val today = LocalDate.now()
        val task = task(dueDate = today.plusDays(3), isCompleted = true)

        // When
        val result = bind("D-3", View.VISIBLE, task)

        // Then
        assertEquals("CLEAR", result.text)
        assertEquals(View.VISIBLE, result.visibility)
    }

    @Test
    fun setDueDateLabel_checkThenUncheckNoDueDate_showsThenRemovesClear() {
        // Given
        val task = Task(
            title = "NO DUE",
            description = "Task without a due date",
            isCompleted = false,
            id = "no-due-task",
            dueDate = null
        )
        repository.tasksServiceData[task.id] = task
        launchFragmentInContainer<TasksFragment>(themeResId = R.style.AppTheme)

        // When
        onView(withId(R.id.complete_checkbox)).perform(click())

        // Then
        onView(withId(R.id.due_date_text)).check(
            matches(allOf(withText("CLEAR"), isDisplayed()))
        )

        // When
        onView(withId(R.id.complete_checkbox)).perform(click())

        // Then
        onView(withId(R.id.due_date_text)).check(
            matches(allOf(withText(""), not(isDisplayed())))
        )
    }

    private fun bind(
        initialText: String,
        initialVisibility: Int,
        task: Task?
    ): BindingResult {
        lateinit var result: BindingResult
        InstrumentationRegistry.getInstrumentation().runOnMainSync {
            val context = InstrumentationRegistry.getInstrumentation().targetContext
            val textView = TextView(context).apply {
                text = initialText
                visibility = initialVisibility
            }

            setDueDateLabel(textView, task)
            result = BindingResult(textView.text.toString(), textView.visibility)
        }
        return result
    }

    private data class BindingResult(val text: String, val visibility: Int)

    private fun task(dueDate: LocalDate?, isCompleted: Boolean) = Task(
        title = "TASK",
        description = "DESCRIPTION",
        isCompleted = isCompleted,
        id = "task-id",
        dueDate = dueDate
    )
}
