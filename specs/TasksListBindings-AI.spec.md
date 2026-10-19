# Spec: `TasksListBindings.kt`

> Post-conditions are the test oracle: write the expected behavior from intent, not from the code.
> Tag each post-condition [Unit] if its test runs on the JVM; otherwise tag it [Integration].
> End each valid case with `Tested by <test>` if an existing test checks it, or `Tip:` describing the test to write.

## `fun setDueDateLabel(textView: TextView, task: Task?)`

**Intent**: Bind a task's D-day label to its list-item `TextView`. Show the view with the exact label when a label exists; otherwise clear and hide the view so recycled list items cannot display stale due-date information.

**Pre-conditions**
- `textView` is an initialized Android `TextView` that may be updated by the binding adapter.
- The device's local date determines today; tests that assert date-relative labels fix the local date to the exact date stated in the Tip.

**Post-conditions**
- Success cases
  - A completed task sets the text to `"CLEAR"` and the visibility to `View.VISIBLE`. `[Integration]`
    - Tip: Given local date `2026-10-14`, a `TextView` whose text is `"OLD"` and visibility is `View.GONE`, and a task with `dueDate = 2026-10-20` and `isCompleted = true` / When `setDueDateLabel()` is called / Then the text is exactly `"CLEAR"` and visibility is exactly `View.VISIBLE`.
  - An active task due today sets the text to `"D-Day"` and the visibility to `View.VISIBLE`. `[Integration]`
    - Tip: Given local date `2026-10-14`, a `TextView` whose text is `"OLD"` and visibility is `View.GONE`, and a task with `dueDate = 2026-10-14` and `isCompleted = false` / When `setDueDateLabel()` is called / Then the text is exactly `"D-Day"` and visibility is exactly `View.VISIBLE`.
  - An active task due in two calendar days sets the text to `"D-2"` and the visibility to `View.VISIBLE`. `[Integration]`
    - Tip: Given local date `2026-10-14`, a `TextView` whose text is `"OLD"` and visibility is `View.GONE`, and a task with `dueDate = 2026-10-16` and `isCompleted = false` / When `setDueDateLabel()` is called / Then the text is exactly `"D-2"` and visibility is exactly `View.VISIBLE`.
  - An active task overdue by two calendar days sets the text to `"D+2"` and the visibility to `View.VISIBLE`. `[Integration]`
    - Tip: Given local date `2026-10-14`, a `TextView` whose text is `"OLD"` and visibility is `View.GONE`, and a task with `dueDate = 2026-10-12` and `isCompleted = false` / When `setDueDateLabel()` is called / Then the text is exactly `"D+2"` and visibility is exactly `View.VISIBLE`.
  - An active task without a due date clears the text to `""` and sets the visibility to `View.GONE`. `[Integration]`
    - Tip: Given local date `2026-10-14`, a recycled `TextView` whose text is `"D-3"` and visibility is `View.VISIBLE`, and a task with `dueDate = null` and `isCompleted = false` / When `setDueDateLabel()` is called / Then the text is exactly `""` and visibility is exactly `View.GONE`.
  - A null task clears the text to `""` and sets the visibility to `View.GONE`. `[Integration]`
    - Tip: Given local date `2026-10-14`, a recycled `TextView` whose text is `"D+4"` and visibility is `View.VISIBLE`, and `task = null` / When `setDueDateLabel()` is called / Then the text is exactly `""` and visibility is exactly `View.GONE`.
- Error cases
  - None. Both a valid `Task` and `task = null` are accepted. `[Integration]`
    - Tip: Given local date `2026-10-14`, a `TextView` whose text is `"OLD"` and visibility is `View.VISIBLE`, and `task = null` / When `setDueDateLabel()` is called / Then it completes without an exception, the text is exactly `""`, and visibility is exactly `View.GONE`.
- Edge cases
  - At the overdue/today boundary, an active task due one day before the local date sets the text to `"D+1"` and the visibility to `View.VISIBLE`. `[Integration]`
    - Tip: Given local date `2026-10-14`, a `TextView` whose text is `"OLD"` and visibility is `View.GONE`, and a task with `dueDate = 2026-10-13` and `isCompleted = false` / When `setDueDateLabel()` is called / Then the text is exactly `"D+1"` and visibility is exactly `View.VISIBLE`.
  - At the overdue/today boundary, an active task due on the local date sets the text to `"D-Day"` and the visibility to `View.VISIBLE`. `[Integration]`
    - Tip: Given local date `2026-10-14`, a `TextView` whose text is `"OLD"` and visibility is `View.GONE`, and a task with `dueDate = 2026-10-14` and `isCompleted = false` / When `setDueDateLabel()` is called / Then the text is exactly `"D-Day"` and visibility is exactly `View.VISIBLE`.
  - At the today/future boundary, an active task due one day after the local date sets the text to `"D-1"` and the visibility to `View.VISIBLE`. `[Integration]`
    - Tip: Given local date `2026-10-14`, a `TextView` whose text is `"OLD"` and visibility is `View.GONE`, and a task with `dueDate = 2026-10-15` and `isCompleted = false` / When `setDueDateLabel()` is called / Then the text is exactly `"D-1"` and visibility is exactly `View.VISIBLE`.
  - At the no-label/label boundary, changing a recycled view from an active task due tomorrow to an active task with no due date removes the old label. `[Integration]`
    - Tip: Given local date `2026-10-14`, a `TextView` whose text is `"D-1"` and visibility is `View.VISIBLE`, and a task with `dueDate = null` and `isCompleted = false` / When `setDueDateLabel()` is called / Then the text is exactly `""` and visibility is exactly `View.GONE`.
  - At the null/non-null task boundary, changing a recycled view from a task due yesterday to `task = null` removes the old label. `[Integration]`
    - Tip: Given local date `2026-10-14`, a `TextView` whose text is `"D+1"` and visibility is `View.VISIBLE`, and `task = null` / When `setDueDateLabel()` is called / Then the text is exactly `""` and visibility is exactly `View.GONE`.

**Undefined behavior** (optional)
- None.
