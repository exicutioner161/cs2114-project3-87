# Queue the Next Student — CS 2114 Project 3 (Group 87)

A text-menu app that runs a TA's office hours line. Students join with their name and the kind of help they need, then get a ticket number. They can check their position or leave the line. The instructor enters a PIN to see the line, call the next student, and mark that student complete. The line is a `LinkedQueue<StudentRequest>` inside `OfficeHoursSession`, so students are always helped in the order they arrived.

| Layer | Classes |
| --- | --- |
| Model | `LinkedQueue<T>` (private inner `Node<T>`), `EmptyQueueException`, `StudentRequest`, `HelpType`, `StudentStatus`, `OfficeHoursSession` |
| View | `ConsoleView` (plus the view-only `ConsoleViewDemo`) |
| Controller | `OfficeHoursController`, `OfficeHoursApp` |

---

## How to Run

The project needs **Java 17 or newer** (the controller uses switch arrows and a text block) and **JUnit 5**. The JUnit 5 jar is included in `lib/`.

The repo does not track Eclipse project files (`.classpath`, `.project`, `.settings/`), because our team uses both Eclipse and VS Code.
- **VS Code:** open the folder. The Java extension picks up `.vscode/settings.json`.
- **Eclipse:** go to File > New > Java Project, uncheck "Use default location," and select this folder. Then set `src` and `tests` as source folders, add the JUnit 5 library, and set the compiler compliance to 17.

### 1. Run the model without a view

The model tests create the classes directly, with no `ConsoleView`, keyboard, or screen output.

- **Queue tests:** `tests/projectutilstest/LinkedQueueTest.java` > Run As > JUnit Test
- **Session tests:** `tests/maintest/OfficeHoursSessionTest.java` > Run As > JUnit Test

### 2. Run the thin view by itself

- **View demo:** `src/main/ConsoleViewDemo.java` > Run As > Java Application. Type a name, then `quit` to stop. It only shows input and output; it never touches the queue.
- **View tests:** `tests/maintest/ConsoleViewTest.java` and `tests/maintest/ConsoleViewDemoTest.java` > Run As > JUnit Test

### 3. Run the full app (MVP path)

`src/main/OfficeHoursApp.java` > Run As > Java Application.

```
1. Join the line        -> enter a name, pick a help type (1 Debug, 2 Concept, 3 Setup, 4 Other), get a ticket
2. Check position       -> enter your ticket number
3. Cancel request       -> enter your ticket number; your spot is marked cancelled
4. Instructor menu      -> PIN is 1234, then: 1 Call next, 2 Complete current, 3 Show line, 4 Back
5. Quit
```

**Sample run:**
1. Join as "Ana" (Debug), who gets ticket 1.
2. Join as "Ben" (Concept), who gets ticket 2.
3. Cancel ticket 1.
4. Open the Instructor menu with PIN 1234 and choose Call next. The app shows "Ticket 2: Ben" because Ana's cancelled spot is skipped.
5. Choose Complete current.
6. Choose Call next again. The app shows "The line is empty."

The app also exits cleanly when input ends (Ctrl+D).

### 4. Run everything from the command line (optional)

```
javac -d bin -cp lib/junit-platform-console-standalone-1.10.2.jar $(find src tests -name "*.java")
java -jar lib/junit-platform-console-standalone-1.10.2.jar -cp bin --scan-classpath
java -cp bin main.OfficeHoursApp
```

---

## Reflection

### What changed from our specification

| Area | Spec said | What we built | Why |
| --- | --- | --- | --- |
| Main menu | The user first picks a role (student or instructor), and each role gets its own menu. | One main menu with five options. Instructor actions sit behind option 4, which asks for the PIN. | One menu loop was the fastest way to get the full MVP path running. Splitting into role menus is listed in Next Steps. |
| Session PIN | The instructor sets a PIN when the session starts. | `OfficeHoursApp` uses a no-argument constructor that defaults the PIN to `1234`. The model's `OfficeHoursSession(String pin)` constructor works and is what the tests use. | This let the app run end to end before a startup prompt existed. The fix only touches `OfficeHoursApp` (see Next Steps). |
| `HelpType` | Four categories: Quick Question, Concept, Debugging, Setup. Each has a label (for example "Debugging") and default minutes. | Four categories: `DEBUGGING` ("Debug"), `CONCEPT`, `SETUP`, `OTHER`. Each has `getLabel()` and `getMinutes()`. | Quick Question was replaced with Other, so any request outside the three specific categories still has a valid choice. |
| `StudentStatus` | `WAITING`, `BEING_HELPED`, `COMPLETED`, `CANCELLED` | `WAITING`, `BEING_HELPED`, `SERVED`, `CANCELLED`. `completeCurrent()` sets `SERVED`. | The enum was written with `SERVED`, and we kept it. The session test still used `COMPLETED` from the spec and didn't compile until we updated it. |
| `LinkedQueue` | `peek()` returns the front item. Fields are named `firstNode`, `lastNode`, `size`. | `getFront()` in place of `peek()`, plus `clear()` and `toString()`. The fields match the spec. | We built the queue from the class's linked implementation, which uses `getFront()`. `clear()` and `toString()` came with that implementation and are tested. |
| `ConsoleView` | Holds one field, `in` (a `Scanner`). | Holds a `Scanner` and a `PrintStream`. It has a second constructor that takes both. | Tests can pass in fixed input and capture output without a keyboard. The normal constructor still uses `System.in` and `System.out`. |
| Where validation lives | The controller parses input with a shared `readChoice` method and re-prompts. Error messages state what a valid answer looks like. The model throws exceptions. | The model throws exceptions exactly as the spec's validation table says. The controller parses each prompt with its own try/catch, and `readHelpType` re-prompts until the choice is valid. A blank name shows the model's error and returns to the main menu instead of re-prompting. The main menu's error message is a generic "Invalid input! Try again." | The model's rules matched the spec, so those carried over unchanged. The controller's re-prompt and message wording are still open (see "What doesn't work yet"). |
| Tests | Normal and bad-input tests for every public model method (`LinkedQueue`, `OfficeHoursSession`, `StudentRequest`, enums), plus a manual run-through of the controller. | `LinkedQueue`: 18 tests. `OfficeHoursSession`: 3 tests (cancel skipping, complete then call next, wrong PIN and empty line). `ConsoleView`/demo: 12 tests. The controller has 3 automated tests (quit, invalid choice, end of input at the PIN prompt). There are no tests yet for `StudentRequest`, `HelpType`, or `StudentStatus`. | The view's injectable constructor made automated controller tests easy. All tests use JUnit 5 so they run the same way in Eclipse and VS Code, which fixed a settings conflict between the two. |
| Division of work | Gao: `OfficeHoursSession`. Zayd: `ConsoleView` plus the student classes. Brian: controller and app. Rahul: `LinkedQueue`. | Brian built `OfficeHoursSession` along with the controller and app. Gao built `ConsoleView`. Zayd built the enums and `StudentRequest`. Rahul built `LinkedQueue`. | The attribution table below records who actually worked on each file. |

### Did the model really stand alone?

**Yes.** `OfficeHoursSession` imports only `LinkedQueue`, `HelpType`, `StudentRequest`, and `StudentStatus`. No model class imports `ConsoleView`, reads input, or prints. The queue and session tests run with no view at all. The view also stayed thin: `ConsoleView` imports no model classes and only displays the strings the controller gives it.

Two things moved across the model/view line to get the app running:
- **The PIN default.** The no-argument `OfficeHoursSession()` constructor, which defaults to `1234`, exists only so `OfficeHoursApp` could start without a PIN prompt. That is an app concern living in the model, and it goes away once the app asks for the PIN.
- **The controller's error message.** `invalidInputMessage()` started out calling `System.out.println`. It now goes through `view.showMessage(...)`, so every piece of output goes through the view.

### What happened to our unknowns

| Unknown from our scope | Status | What we learned |
| --- | --- | --- |
| **Queue design and edge cases:** moving between empty, one-student, and many-student states, and keeping the front and rear references correct. | Figured out, with one test still missing | `enqueue` sets both `firstNode` and `lastNode` when the queue is empty. `dequeue` clears `lastNode` when the last item leaves. `LinkedQueueTest` covers the empty, one-item, and multiple-item cases for `enqueue`, `dequeue`, `getFront`, `toArray`, `toString`, and `clear`, plus a null enqueue. Still open: the spec's "dequeue the last item, then enqueue again" test, which confirms both references reset correctly. |
| **Representing student status:** how to track waiting, being helped, completed, and cancelled, and what belongs in each student object versus the model. | Figured out | `StudentStatus` is an enum, and each `StudentRequest` stores its own status. The ticket, name, and help type are final. The session holds the PIN, the ticket counter, and `currentStudent`, who is dequeued and stored separately while being helped. Cancelled students stay in the queue and are discarded only when they reach the front, so we only ever remove from the front. |

### What doesn't work yet

- **The PIN is fixed at `1234`.** The instructor can't set it at startup.
- **There's no role menu.** Students and instructors share one main menu instead of separate menus.
- **Several bad-input cases fall short of the spec:**
  - A blank name sends the user back to the main menu instead of re-prompting for a name.
  - A non-numeric ticket shows "Invalid ticket." and returns to the main menu.
  - The main menu's error message doesn't say what a valid choice looks like.
- **Several tests are missing or empty:**
  - `StudentRequestTest`, `HelpTypeTest`, `StudentStatusTest`, and `OfficeHoursAppTest` have no tests yet.
  - `OfficeHoursSessionTest` covers 3 of the spec's planned cases. It still needs `getPosition`, the bad-input cases for `joinLine` and `cancel`, `getWaitingStudents`, `isLineEmpty`, and a test that calling next while someone is being helped is blocked.
  - The `LinkedQueue` test for resetting front and rear after emptying the queue is missing.
- **Nothing currently fails.** All existing tests pass, and the MVP path runs without crashing.

---

## Next Steps

### 1. Finishing the MVP (in order)

| # | Item | Owner |
| --- | --- | --- |
| 1 | The TA sets the PIN at startup: `OfficeHoursApp` asks for it through `ConsoleView`, re-prompts on a blank entry, and passes it to `OfficeHoursSession(String pin)`. Then remove the `1234` no-argument constructor. | Rahul Rajendran |
| 2 | Split the main menu into Student and Instructor menus, as the spec describes. | Brian Tran |
| 3 | Match the spec's bad-input rules: re-prompt for a blank name, re-prompt for a bad ticket, and make every error message say what a valid answer looks like. | Brian Tran |
| 4 | Write `StudentRequestTest`, `HelpTypeTest`, and `StudentStatusTest` from the spec's test plan. | Zayd Pathan |
| 5 | Finish `OfficeHoursSessionTest` with the spec's remaining cases. | Brian Tran |
| 6 | Add the `LinkedQueue` front/rear reset test, and either remove the empty `OfficeHoursAppTest` or turn it into a test that starts the app and quits. | Rahul Rajendran |
| 7 | Add controller tests for the join, position, cancel, and instructor paths, including bad input at each prompt. | Zhaojin Gao |
| 8 | Do a full manual run-through of the spec's validation table as a team, then update How to Run. | Brian Tran (lead), whole team |

### 2. Change feature: Skip / Requeue

While helping a student, the instructor can send that student to the **back of the line**, for example so they can go try a fix and come back. The student keeps their ticket number, and their status goes from `BEING_HELPED` back to `WAITING`.

This is non-trivial because it changes model state: the current student goes back into the queue, and their status moves backward for the first time. The instructor reaches it through the controller and view.

**Predicted to change:**

| Class | Layer | Change |
| --- | --- | --- |
| `OfficeHoursSession` | Model | New method `requeueCurrent()`. It throws `IllegalStateException` if no one is being helped. Otherwise it sets the current student to `WAITING`, enqueues them at the back, and clears `currentStudent`. The method takes no PIN, because the controller already checks the PIN before any instructor action. |
| `OfficeHoursController` | Controller | New option in the instructor menu ("Send current student to back") and a handler that calls `requeueCurrent()` and shows the result. |

**Predicted *not* to change:**
- **`ConsoleView`:** the controller supplies the new menu text and result message.
- **`LinkedQueue`:** the existing `enqueue` adds the student at the back.
- **`StudentRequest`:** `setStatus` already allows any status, and the ticket is final, so it stays the same.
- **`StudentStatus`:** `WAITING` and `BEING_HELPED` already exist.
- **`HelpType` and `OfficeHoursApp`:** the feature doesn't involve either one.

We'll compare this prediction with what actually changed in Deliverable 4.

### 3. Stretch goals

| Stretch goal | Decision | Reason |
| --- | --- | --- |
| Leaving the line (marked cancelled, skipped at the front) | Done, moved into the MVP | A TA suggested it in our Revised Scope. `cancel()` and the skip logic in `callNext()` are working. |
| Estimated wait times (fixed default minutes per help type) | Committing | `HelpType` already stores the default minutes. We'll add a session method that adds up the minutes for the current student and each waiting student ahead of a ticket. The controller will show the total after a student joins or checks their position. |

### 4. New tests needed

- **`requeueCurrent()`:**
  - With no current student, it throws `IllegalStateException`.
  - The requeued student goes to the back with status `WAITING` and the same ticket number.
  - The order of everyone else is unchanged.
  - With a one-student line, the requeued student is the next one called.
- **Estimated wait time:**
  - The first student in line waits 0 minutes, plus any current student's time.
  - Cancelled students ahead don't count.
  - An unknown ticket throws `IllegalArgumentException`.
- **PIN at startup:** a blank PIN re-prompts, and end of input at the PIN prompt exits cleanly.
- **Controller:**
  - The new requeue option.
  - Invalid choices in the instructor menu.
  - Blank-name and bad-ticket re-prompts.
- **The remaining spec test-plan cases** listed under "What doesn't work yet."

### 5. Who does what

| Member | Remaining items |
| --- | --- |
| Rahul Rajendran | PIN setup at startup in `OfficeHoursApp`; the `LinkedQueue` front/rear test; `OfficeHoursAppTest`; README updates |
| Brian Tran | Role menus; bad-input re-prompts and error wording; finishing `OfficeHoursSessionTest`; the Skip/Requeue model and controller changes; leading the full run-through |
| Zhaojin Gao | Controller tests for every menu path and the requeue option; keeping `ConsoleView` and its tests current |
| Zayd Pathan | `StudentRequestTest`, `HelpTypeTest`, `StudentStatusTest`; the estimated wait time feature and its tests |

---

## Attribution

| File | Who worked on it and how |
| --- | --- |
| `src/projectutils/LinkedQueue.java` | Rahul Rajendran: wrote the class (GenAI-assisted with Claude), including the null check in `enqueue`. |
| `src/projectutils/EmptyQueueException.java` | Rahul Rajendran: wrote it. |
| `src/student/StudentRequest.java` | |
| `src/student/HelpType.java` | |
| `src/student/StudentStatus.java` | |
| `src/main/OfficeHoursSession.java` | Brian Tran: drafted using GenAI and reviewed manually |
| `src/main/OfficeHoursController.java` | Brian Tran: drafted using GenAI and reviewed manually |
| `src/main/OfficeHoursApp.java` | Brian Tran: wrote manually |
| `src/main/ConsoleView.java` | Zhaojin Gao: Provided the group's Model/View Boundary design to Codex and requested the view implementation. Codex drafted the code and checked its controller calls and separation from the model after the merge. Gao personally reviewed the code and confirmed that he understood it and found it correct. |
| `src/main/ConsoleViewDemo.java` | Zhaojin Gao: Requested a view-only demo. Codex drafted it and checked its input and exit behavior after the merge. Gao personally reviewed and understood the demo code. |
| `tests/projectutilstest/LinkedQueueTest.java` | Rahul Rajendran: wrote the tests, including the enqueue-null test. |
| `tests/maintest/OfficeHoursSessionTest.java` | Rahul Rajendran: revised it, fixing two enum constant names (`HelpType.CONCEPT`, `StudentStatus.SERVED`) so the tests compile and pass. |
| `tests/maintest/OfficeHoursControllerTest.java` | |
| `tests/maintest/OfficeHoursAppTest.java` | |
| `tests/maintest/ConsoleViewTest.java` | Zhaojin Gao: Requested view tests. Codex drafted them and reran all 9 tests successfully after the merge. |
| `tests/maintest/ConsoleViewDemoTest.java` | Zhaojin Gao: Requested demo exit tests. Codex drafted them and reran all 3 tests successfully after the merge. |
| `tests/studenttest/StudentRequestTest.java` | |
| `tests/studenttest/HelpTypeTest.java` | |
| `tests/studenttest/StudentStatusTest.java` | |

## GenAI Note

**Rahul Rajendran:** I used Claude to help build `LinkedQueue`. I also used it to find why Eclipse wasn't compiling the project: the Eclipse project files had been removed from the repo, and the compiler level was set below Java 17. Claude compiled and ran our code and tests to find the two broken enum names in `OfficeHoursSessionTest`. It also helped me write this README by checking it against the rubric, our Scope, our Spec, and the actual code and git history. I reviewed every change before committing it.

**Zhaojin Gao:** I asked Codex to continue my Model/View Boundary part using simple Java and simple English. It read our design extract and the current repository, filled in `ConsoleView`, added a demo, and wrote view tests. It also drafted the first version of the README's view sections and predicted which classes a skip/requeue feature could change. The model and queue implementations were left unchanged. On November 11, I asked Codex to check the merged code and update my contribution notes. Codex checked the view boundary and controller calls, ran all 36 existing tests successfully, and checked the join, cancel, serve, complete, empty-line, and quit flow. I then personally reviewed the code for my part. I understood how it works and found it correct. Codex ran the tests described above. The tests do not cover every input case; the missing tests are listed above.

**Brian Tran:**

**Zayd Pathan:**
