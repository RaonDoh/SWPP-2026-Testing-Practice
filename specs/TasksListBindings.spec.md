# Spec: `TasksListBindings.kt`

> Post-conditions are the test oracle: write the expected behavior from intent, not from the code.
> Tag each post-condition [Unit] if its test runs on the JVM; otherwise tag it [Integration].
> End each valid case with `Tested by <test>` if an existing test checks it, or `Tip:` describing the test to write.

## `fun setDueDateLabel(textView: TextView, task: Task?)`

**Intent**: Bind a task's D-day label to its list-item `TextView`. Show the view with the exact label when a label exists; otherwise clear and hide the view so recycled list items cannot display stale due-date information. The label must be updated whenever the task is checked or unchecked as completed.

**Pre-conditions**
- `textView` is an initialized Android `TextView` that may be updated by the binding adapter.
- The device's local date determines today. Because the test cannot fix the device date, each Tip states due dates relative to `today = LocalDate.now()` read at test time (for example `today.plusDays(2)`), and states the exact expected label.

**Post-conditions**
- Success cases
  - A completed task with a due date sets the text to `"CLEAR"` and the visibility to `View.VISIBLE`. `[Integration]`
    - Tested by `TasksListBindingsTest.setDueDateLabel_completedTaskWithDueDate_showsClear`.
  - A completed task without a due date sets the text to `"CLEAR"` and the visibility to `View.VISIBLE`. `[Integration]`
    - Tested by `TasksListBindingsTest.setDueDateLabel_completedTaskWithoutDueDate_showsClear`.
  - An active task due today sets the text to `"D-Day"` and the visibility to `View.VISIBLE`. `[Integration]`
    - Tested by `TasksListBindingsTest.setDueDateLabel_activeTaskDueToday_showsDDay`.
  - An active task due in two calendar days sets the text to `"D-2"` and the visibility to `View.VISIBLE`. `[Integration]`
    - Tested by `TasksListBindingsTest.setDueDateLabel_activeTaskDueInTwoDays_showsDMinus2`.
  - An active task overdue by two calendar days sets the text to `"D+2"` and the visibility to `View.VISIBLE`. `[Integration]`
    - Tested by `TasksListBindingsTest.setDueDateLabel_activeTaskOverdueByTwoDays_showsDPlus2`.
  - An active task without a due date clears the text to `""` and sets the visibility to `View.GONE`. `[Integration]`
    - Tested by `TasksListBindingsTest.setDueDateLabel_activeTaskWithoutDueDate_clearsAndHidesLabel`.
  - A null task clears the text to `""` and sets the visibility to `View.GONE`. `[Integration]`
    - Tested by `TasksListBindingsTest.setDueDateLabel_nullTask_clearsAndHidesLabel`.
- Error cases
  - None. Both a valid `Task` and `task = null` are accepted. `[Integration]`
    - Tested by `TasksListBindingsTest.setDueDateLabel_nullTask_completesAndClearsAndHidesLabel`.
- Edge cases
  - At the overdue/today boundary, an active task due one day before today sets the text to `"D+1"` and the visibility to `View.VISIBLE`. `[Integration]`
    - Tested by `TasksListBindingsTest.setDueDateLabel_activeTaskDueYesterday_showsDPlus1`.
  - At the overdue/today boundary, an active task due today sets the text to `"D-Day"` and the visibility to `View.VISIBLE`. `[Integration]`
    - Tested by `TasksListBindingsTest.setDueDateLabel_activeTaskAtOverdueTodayBoundary_showsDDay`.
  - At the today/future boundary, an active task due one day after today sets the text to `"D-1"` and the visibility to `View.VISIBLE`. `[Integration]`
    - Tested by `TasksListBindingsTest.setDueDateLabel_activeTaskDueTomorrow_showsDMinus1`.
  - At the no-label/label boundary, changing a recycled view from an active task due tomorrow to an active task with no due date removes the old label. `[Integration]`
    - Tested by `TasksListBindingsTest.setDueDateLabel_recycledViewToNoDueDate_clearsAndHidesOldLabel`.
  - At the null/non-null task boundary, changing a recycled view from a task due yesterday to `task = null` removes the old label. `[Integration]`
    - Tested by `TasksListBindingsTest.setDueDateLabel_recycledViewToNullTask_clearsAndHidesOldLabel`.
  - At the completed/active boundary without a due date, unchecking removes the stale `"CLEAR"` label. `[Integration]`
    - Tested by `TasksListBindingsTest.setDueDateLabel_completedToActiveWithoutDueDate_removesClear`.
  - At the completed/active boundary with a due date, unchecking replaces `"CLEAR"` with the D-day label. `[Integration]`
    - Tested by `TasksListBindingsTest.setDueDateLabel_completedToActiveWithDueDate_replacesClear`.
  - At the active/completed boundary, checking replaces a D-day label with `"CLEAR"`. `[Integration]`
    - Tested by `TasksListBindingsTest.setDueDateLabel_activeToCompleted_replacesDdayWithClear`.
  - On the task list screen, checking and then unchecking a task with no due date shows `"CLEAR"` and then removes it. `[Integration]`
    - Tested by `TasksListBindingsTest.setDueDateLabel_checkThenUncheckNoDueDate_showsThenRemovesClear`.

**Undefined behavior** (optional)
- None. Calendar-day arithmetic across month, year, and leap-year boundaries is specified and tested in `DueDateUtils.spec.md`; this spec only checks that the computed label is bound to the view.
