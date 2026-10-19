# Spec: `DueDateUtils.kt`

> Post-conditions are the test oracle: write the expected behavior from intent, not from the code.
> Tag each post-condition [Unit] if its test runs on the JVM; otherwise tag it [Integration].
> End each valid case with `Tested by <test>` if an existing test checks it, or `Tip:` describing the test to write.

## `internal fun dueDateLabel(dueDate: LocalDate?, today: LocalDate, isCompleted: Boolean): String`

**Intent**: Build the task-list D-day label from completion state and the number of calendar days between today and the due date. Completed tasks show `"CLEAR"`; active tasks show no label, `"D-Day"`, `"D-N"`, or `"D+N"` as appropriate.

**Pre-conditions**
- `today` and every non-null `dueDate` are valid `LocalDate` values.

**Post-conditions**
- Success cases
  - A completed task returns `"CLEAR"` regardless of whether it has a due date. `[Unit]`
    - Tested by `DueDateUtilsTest.dueDateLabel_completedWithDueDate_returnsClear`.
  - A completed task without a due date returns `"CLEAR"`. `[Unit]`
    - Tested by `DueDateUtilsTest.dueDateLabel_completedWithoutDueDate_returnsClear`.
  - An active task without a due date returns the empty string `""`. `[Unit]`
    - Tested by `DueDateUtilsTest.dueDateLabel_activeWithoutDueDate_returnsEmpty`.
  - An active task due today returns `"D-Day"`. `[Unit]`
    - Tested by `DueDateUtilsTest.dueDateLabel_activeDueToday_returnsDDay`.
  - An active task due in two calendar days returns `"D-2"`. `[Unit]`
    - Tested by `DueDateUtilsTest.dueDateLabel_activeDueInTwoDays_returnsDMinus2`.
  - An active task overdue by two calendar days returns `"D+2"`. `[Unit]`
    - Tested by `DueDateUtilsTest.dueDateLabel_activeOverdueByTwoDays_returnsDPlus2`.
- Error cases
  - None. Every combination of a nullable due date, a valid current date, and completion state is accepted. `[Unit]`
    - Tested by `DueDateUtilsTest.dueDateLabel_nullDueDate_completesAndReturnsEmpty`.
- Edge cases
  - At the overdue/today boundary, an active task due one day before today returns `"D+1"`. `[Unit]`
    - Tested by `DueDateUtilsTest.dueDateLabel_activeDueYesterday_returnsDPlus1`.
  - At the overdue/today boundary, an active task due today returns `"D-Day"`. `[Unit]`
    - Tested by `DueDateUtilsTest.dueDateLabel_activeAtOverdueTodayBoundary_returnsDDay`.
  - At the today/future boundary, an active task due one day after today returns `"D-1"`. `[Unit]`
    - Tested by `DueDateUtilsTest.dueDateLabel_activeDueTomorrow_returnsDMinus1`.
  - Calendar-day counting remains correct across a month boundary. `[Unit]`
    - Tested by `DueDateUtilsTest.dueDateLabel_activeAcrossMonthBoundary_returnsDMinus29`.
  - Overdue counting remains correct across a month boundary. `[Unit]`
    - Tested by `DueDateUtilsTest.dueDateLabel_activeOverdueAcrossMonthBoundary_returnsDPlus2`.
  - A due date one month away with the same day-of-month is counted in total calendar days, not treated as today. `[Unit]`
    - Tested by `DueDateUtilsTest.dueDateLabel_activeDueSameDayNextMonth_returnsDMinus31`.
  - Calendar-day counting remains correct across a year boundary. `[Unit]`
    - Tested by `DueDateUtilsTest.dueDateLabel_activeAcrossYearBoundary_returnsDMinus2`.
  - Calendar-day counting includes February 29 in a leap year. `[Unit]`
    - Tested by `DueDateUtilsTest.dueDateLabel_activeAcrossLeapDay_returnsDMinus2`.
  - At the completion boundary, a completed task due today returns `"CLEAR"`, not `"D-Day"`. `[Unit]`
    - Tested by `DueDateUtilsTest.dueDateLabel_completedDueToday_returnsClear`.
  - At the completion boundary, a completed overdue task returns `"CLEAR"`, not `"D+N"`. `[Unit]`
    - Tested by `DueDateUtilsTest.dueDateLabel_completedOverdue_returnsClear`.

**Undefined behavior** (optional)
- None.
