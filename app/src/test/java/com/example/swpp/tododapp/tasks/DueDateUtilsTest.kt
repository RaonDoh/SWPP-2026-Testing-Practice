package com.example.swpp.tododapp.tasks

import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.LocalDate

class DueDateUtilsTest {

    @Test
    fun dueDateLabel_completedWithDueDate_returnsClear() {
        // Given
        val dueDate = LocalDate.of(2026, 10, 20)
        val today = LocalDate.of(2026, 10, 14)

        // When
        val result = dueDateLabel(dueDate, today, isCompleted = true)

        // Then
        assertEquals("CLEAR", result)
    }

    @Test
    fun dueDateLabel_completedWithoutDueDate_returnsClear() {
        // Given
        val today = LocalDate.of(2026, 10, 14)

        // When
        val result = dueDateLabel(dueDate = null, today = today, isCompleted = true)

        // Then
        assertEquals("CLEAR", result)
    }

    @Test
    fun dueDateLabel_activeWithoutDueDate_returnsEmpty() {
        // Given
        val today = LocalDate.of(2026, 10, 14)

        // When
        val result = dueDateLabel(dueDate = null, today = today, isCompleted = false)

        // Then
        assertEquals("", result)
    }

    @Test
    fun dueDateLabel_activeDueToday_returnsDDay() {
        // Given
        val today = LocalDate.of(2026, 10, 14)

        // When
        val result = dueDateLabel(today, today, isCompleted = false)

        // Then
        assertEquals("D-Day", result)
    }

    @Test
    fun dueDateLabel_activeDueInTwoDays_returnsDMinus2() {
        // Given
        val dueDate = LocalDate.of(2026, 10, 16)
        val today = LocalDate.of(2026, 10, 14)

        // When
        val result = dueDateLabel(dueDate, today, isCompleted = false)

        // Then
        assertEquals("D-2", result)
    }

    @Test
    fun dueDateLabel_activeOverdueByTwoDays_returnsDPlus2() {
        // Given
        val dueDate = LocalDate.of(2026, 10, 12)
        val today = LocalDate.of(2026, 10, 14)

        // When
        val result = dueDateLabel(dueDate, today, isCompleted = false)

        // Then
        assertEquals("D+2", result)
    }

    @Test
    fun dueDateLabel_nullDueDate_completesAndReturnsEmpty() {
        // Given
        val today = LocalDate.of(2026, 10, 14)

        // When
        val result = dueDateLabel(dueDate = null, today = today, isCompleted = false)

        // Then
        assertEquals("", result)
    }

    @Test
    fun dueDateLabel_activeDueYesterday_returnsDPlus1() {
        // Given
        val dueDate = LocalDate.of(2026, 10, 13)
        val today = LocalDate.of(2026, 10, 14)

        // When
        val result = dueDateLabel(dueDate, today, isCompleted = false)

        // Then
        assertEquals("D+1", result)
    }

    @Test
    fun dueDateLabel_activeAtOverdueTodayBoundary_returnsDDay() {
        // Given
        val dueDate = LocalDate.of(2026, 10, 14)
        val today = LocalDate.of(2026, 10, 14)

        // When
        val result = dueDateLabel(dueDate, today, isCompleted = false)

        // Then
        assertEquals("D-Day", result)
    }

    @Test
    fun dueDateLabel_activeDueTomorrow_returnsDMinus1() {
        // Given
        val dueDate = LocalDate.of(2026, 10, 15)
        val today = LocalDate.of(2026, 10, 14)

        // When
        val result = dueDateLabel(dueDate, today, isCompleted = false)

        // Then
        assertEquals("D-1", result)
    }

    @Test
    fun dueDateLabel_activeAcrossMonthBoundary_returnsDMinus29() {
        // Given
        val dueDate = LocalDate.of(2026, 3, 1)
        val today = LocalDate.of(2026, 1, 31)

        // When
        val result = dueDateLabel(dueDate, today, isCompleted = false)

        // Then
        assertEquals("D-29", result)
    }

    @Test
    fun dueDateLabel_activeOverdueAcrossMonthBoundary_returnsDPlus2() {
        // Given
        val dueDate = LocalDate.of(2026, 9, 30)
        val today = LocalDate.of(2026, 10, 2)

        // When
        val result = dueDateLabel(dueDate, today, isCompleted = false)

        // Then
        assertEquals("D+2", result)
    }

    @Test
    fun dueDateLabel_activeDueSameDayNextMonth_returnsDMinus31() {
        // Given
        val dueDate = LocalDate.of(2026, 11, 14)
        val today = LocalDate.of(2026, 10, 14)

        // When
        val result = dueDateLabel(dueDate, today, isCompleted = false)

        // Then
        assertEquals("D-31", result)
    }

    @Test
    fun dueDateLabel_activeAcrossYearBoundary_returnsDMinus2() {
        // Given
        val dueDate = LocalDate.of(2027, 1, 1)
        val today = LocalDate.of(2026, 12, 30)

        // When
        val result = dueDateLabel(dueDate, today, isCompleted = false)

        // Then
        assertEquals("D-2", result)
    }

    @Test
    fun dueDateLabel_activeAcrossLeapDay_returnsDMinus2() {
        // Given
        val dueDate = LocalDate.of(2028, 3, 1)
        val today = LocalDate.of(2028, 2, 28)

        // When
        val result = dueDateLabel(dueDate, today, isCompleted = false)

        // Then
        assertEquals("D-2", result)
    }

    @Test
    fun dueDateLabel_completedDueToday_returnsClear() {
        // Given
        val dueDate = LocalDate.of(2026, 10, 14)
        val today = LocalDate.of(2026, 10, 14)

        // When
        val result = dueDateLabel(dueDate, today, isCompleted = true)

        // Then
        assertEquals("CLEAR", result)
    }

    @Test
    fun dueDateLabel_completedOverdue_returnsClear() {
        // Given
        val dueDate = LocalDate.of(2026, 10, 12)
        val today = LocalDate.of(2026, 10, 14)

        // When
        val result = dueDateLabel(dueDate, today, isCompleted = true)

        // Then
        assertEquals("CLEAR", result)
    }
}
