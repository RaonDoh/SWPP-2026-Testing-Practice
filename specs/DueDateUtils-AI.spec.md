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
    - Tip: Given `dueDate = 2026-10-20`, `today = 2026-10-14`, and `isCompleted = true` / When `dueDateLabel()` is called / Then it returns exactly `"CLEAR"`.
  - A completed task without a due date returns `"CLEAR"`. `[Unit]`
    - Tip: Given `dueDate = null`, `today = 2026-10-14`, and `isCompleted = true` / When `dueDateLabel()` is called / Then it returns exactly `"CLEAR"`.
  - An active task without a due date returns the empty string `""`. `[Unit]`
    - Tip: Given `dueDate = null`, `today = 2026-10-14`, and `isCompleted = false` / When `dueDateLabel()` is called / Then it returns exactly `""`.
  - An active task due today returns `"D-Day"`. `[Unit]`
    - Tip: Given `dueDate = 2026-10-14`, `today = 2026-10-14`, and `isCompleted = false` / When `dueDateLabel()` is called / Then it returns exactly `"D-Day"`.
  - An active task due in two calendar days returns `"D-2"`. `[Unit]`
    - Tip: Given `dueDate = 2026-10-16`, `today = 2026-10-14`, and `isCompleted = false` / When `dueDateLabel()` is called / Then it returns exactly `"D-2"`.
  - An active task overdue by two calendar days returns `"D+2"`. `[Unit]`
    - Tip: Given `dueDate = 2026-10-12`, `today = 2026-10-14`, and `isCompleted = false` / When `dueDateLabel()` is called / Then it returns exactly `"D+2"`.
- Error cases
  - None. Every combination of a nullable due date, a valid current date, and completion state is accepted. `[Unit]`
    - Tip: Given `dueDate = null`, `today = 2026-10-14`, and `isCompleted = false` / When `dueDateLabel()` is called / Then it completes without an exception and returns exactly `""`.
- Edge cases
  - At the overdue/today boundary, an active task due one day before today returns `"D+1"`. `[Unit]`
    - Tip: Given `dueDate = 2026-10-13`, `today = 2026-10-14`, and `isCompleted = false` / When `dueDateLabel()` is called / Then it returns exactly `"D+1"`.
  - At the overdue/today boundary, an active task due today returns `"D-Day"`. `[Unit]`
    - Tip: Given `dueDate = 2026-10-14`, `today = 2026-10-14`, and `isCompleted = false` / When `dueDateLabel()` is called / Then it returns exactly `"D-Day"`.
  - At the today/future boundary, an active task due one day after today returns `"D-1"`. `[Unit]`
    - Tip: Given `dueDate = 2026-10-15`, `today = 2026-10-14`, and `isCompleted = false` / When `dueDateLabel()` is called / Then it returns exactly `"D-1"`.
  - Calendar-day counting remains correct across a month boundary. `[Unit]`
    - Tip: Given `dueDate = 2026-03-01`, `today = 2026-01-31`, and `isCompleted = false` / When `dueDateLabel()` is called / Then it returns exactly `"D-29"`.
  - Calendar-day counting remains correct across a year boundary. `[Unit]`
    - Tip: Given `dueDate = 2027-01-01`, `today = 2026-12-30`, and `isCompleted = false` / When `dueDateLabel()` is called / Then it returns exactly `"D-2"`.
  - Calendar-day counting includes February 29 in a leap year. `[Unit]`
    - Tip: Given `dueDate = 2028-03-01`, `today = 2028-02-28`, and `isCompleted = false` / When `dueDateLabel()` is called / Then it returns exactly `"D-2"`.

**Undefined behavior** (optional)
- None.
