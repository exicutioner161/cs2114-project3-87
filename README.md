# Queue the Next Student — CS 2114 Project 3 (Group 87)

A text-menu app that runs a TA's office hours line. Students join with their name and help type, get a ticket, and can check their position or leave the line. The instructor sets a PIN at startup. With it, they can call the next student, complete them, send them to the back of the line, or view the line. The line is a `LinkedQueue<StudentRequest>` inside `OfficeHoursSession`.

| Layer | Classes |
| --- | --- |
| Model | `LinkedQueue<T>`, `EmptyQueueException`, `StudentRequest`, `HelpType`, `StudentStatus`, `OfficeHoursSession` |
| View | `ConsoleView` (plus the view-only `ConsoleViewDemo`) |
| Controller | `OfficeHoursController`, `OfficeHoursApp` |

---

## How to Run

You need **Java 17 or newer** (the code uses text blocks and switch arrows). **JUnit 5** is included in `lib/`, and every test file uses it.

- **VS Code:** open the folder. The Java extension reads `.vscode/settings.json`.
- **Eclipse:** File > New > Java Project. Uncheck "Use default location" and select this folder. Then set `src` and `tests` as source folders, add JUnit 5, and set compliance to 17. After pulling, run **Project > Clean** so Eclipse doesn't run old classes.

**Tests:** right-click the `tests` folder and choose Run As > JUnit Test, or run any single file the same way. All 127 tests pass.

| Test file | Tests | Covers |
| --- | --- | --- |
| `LinkedQueueTest` | 24 | Every queue method, order after interleaved enqueues and dequeues, refill after emptying, bad input |
| `OfficeHoursSessionTest` | 20 | A normal and a bad-input case for every session method, including `requeueCurrent()` |
| `StudentRequestTest` | 10 | Constructor and getters, every `setStatus` change, and bad input (null name, null help type, `setStatus(null)`) |
| `HelpTypeTest`, `StudentStatusTest` | 4, 3 | Enum values, labels, minutes, invalid names |
| `OfficeHoursControllerTest` | 43 | Every menu path, bad input and end of input at every prompt, requeue |
| `OfficeHoursAppTest` | 11 | Startup PIN: blank PINs, end of input, the PIN reaching the instructor menu |
| `ConsoleViewTest`, `ConsoleViewDemoTest` | 9, 3 | The view on its own |

The first five files are **model tests**: they create the classes directly, with no view. The controller and app tests feed fixed input through `ConsoleView`'s test constructor, then check both the output and the model's state.

**App:** `src/main/OfficeHoursApp.java` > Run As > Java Application.

```
Set instructor PIN   -> any non-blank PIN (a blank one re-prompts)
1. Join the line     -> name, help type (1 Debug, 2 Concept, 3 Setup, 4 Other), get a ticket
2. Check position    -> ticket number
3. Cancel request    -> ticket number
4. Instructor menu   -> PIN, then 1 Call next, 2 Complete current, 3 Show line,
                        4 Requeue current, 5 Back (the menu stays open until Back)
5. Quit
```

**Sample run:**
1. Press Enter at the PIN prompt; it re-prompts. Then set the PIN to `4321`.
2. Ana, Ben, and Cara join and get tickets 1–3. Check position for ticket 3 ("Your position is 3"), then cancel ticket 3.
3. Try the instructor menu with a wrong PIN: "Access denied."
4. In the instructor menu with `4321`:
   - **Requeue current** with no one being helped → "No student is being helped."
   - **Call next** → "Ticket 1: Ana"
   - **Requeue current**, then **Show line** → "1. 2 Ben", "2. 1 Ana"
   - **Call next** → "Ticket 2: Ben"

The app exits cleanly at end of input (Ctrl+D).

**Command line (optional):**
```
javac -d bin -cp lib/junit-platform-console-standalone-1.10.2.jar $(find src tests -name "*.java")
java -jar lib/junit-platform-console-standalone-1.10.2.jar execute -cp bin --scan-classpath
java -cp bin main.OfficeHoursApp
```

---

## Reflection

### How did our design absorb the change?

Our feature is **Skip / Requeue**: the instructor sends the student being helped to the back of the line. The student keeps the same ticket and goes from `BEING_HELPED` back to `WAITING`.

| Class touched | Change |
| --- | --- |
| `OfficeHoursSession` (model) | New `requeueCurrent()`. It throws `IllegalStateException` if no one is being helped. Otherwise it sets the student to `WAITING`, enqueues them, clears `currentStudent`, and returns the student. |
| `OfficeHoursController` | New "4. Requeue current" option, and the instructor menu now loops until Back. |

- **What matched:**
  - D3 predicted exactly these two classes.
  - `LinkedQueue.enqueue` already adds to the back.
  - `setStatus` already allowed `WAITING`, and the ticket is `final`.
  - `ConsoleView` didn't change.
- **What surprised us:** the model change took only about 15 lines. The real work was the controller: the old instructor menu returned to the main menu after every action, so the TA would have had to re-enter the PIN between "Call next" and "Requeue." We made the menu loop instead.
- **Separate D4 changes:** `OfficeHoursApp` (PIN at startup) and `LinkedQueue` (removed an unused `peek()`) also changed. Neither was part of the feature. Both were committed before the Requeue code (`a4e54ce`, ahead of `6b64dcd`).

### Did the change cross the model/view line?

**No.** `ConsoleView` is unchanged and still imports no model classes. The controller builds the menu text and the result message ("Ticket 1: Ana") from what `requeueCurrent()` returns and passes them to the view as plain strings. D4 also fixed the boundary issue we reported in D3: the hard-coded `1234` PIN constructor in the model is gone, and the app now reads the PIN through the view.

### What changed from our D3 plan

| Change | Why |
| --- | --- |
| `requeueCurrent()` returns the student instead of nothing | So the controller can show who moved, the same way `callNext()` does. |
| The instructor menu loops, and Back moved to option 5 | Requeue only makes sense right after Call next. |
| Removed `peek()` from `LinkedQueue` | Unused, and our spec uses `getFront()`. |
| Joining no longer prints the whole line | So students don't see each other's names. |
| `HelpTypeTest` and `StudentStatusTest` were rewritten to match our enums, and every test file now uses JUnit 5 | The old tests expected values from the original spec. |
| No requeue test for a one-student line | The two-student test already covers moving to the back and keeping the order. |

### What doesn't work yet

- **Some error messages are generic.** The main menu and instructor menu show "Invalid input! Try again.", and a bad help type shows "Invalid help type." Neither says what a valid choice is.

Nothing crashes on any bad input we've tested.

### Bad input and its tests

Every bad-input case from our scope, plus the one Requeue added, is handled without crashing and has its own test:

| Bad input | What happens | Tested in |
| --- | --- | --- |
| Blank PIN at startup | Re-prompts | `OfficeHoursAppTest` |
| Wrong PIN | "Access denied." | `OfficeHoursControllerTest`, `OfficeHoursSessionTest` |
| Main or instructor menu choice that's text, blank, or out of range | Error, then the menu again | `OfficeHoursControllerTest` |
| Blank name, invalid help type | Re-prompts; no student added | `OfficeHoursControllerTest`, `OfficeHoursSessionTest` |
| Ticket that's text, negative, too large, unknown, or already cancelled | Re-prompts; the line is unchanged | `OfficeHoursControllerTest`, `OfficeHoursSessionTest` |
| Call next while busy, or on an empty line; Complete with no one being helped | Error message; state unchanged | `OfficeHoursControllerTest`, `OfficeHoursSessionTest` |
| **Requeue with no one being helped** (new) | "No student is being helped." | `OfficeHoursControllerTest`, `OfficeHoursSessionTest` |
| End of input at any prompt | Exits cleanly; state unchanged | `OfficeHoursControllerTest`, `OfficeHoursAppTest` |
| Null enqueue; dequeue or getFront on an empty queue | Exception; the queue still works afterward | `LinkedQueueTest` |

### What design change would make the next feature cheaper?

Each controller menu is written twice: once as the text the user sees, and once as the `switch` that runs each option. Adding Requeue meant editing both and renumbering Back. If each menu were a single list of (label, action) pairs, the controller could print the menu and run the chosen action from that same list. Then a new option, or the planned Student/Instructor menu split, would be one new entry, and the two could never get out of sync.

---

## Next Steps

### What's left for the final build (in order)

| # | Item | Owner |
| --- | --- | --- |
| 1 | Split the main menu into Student and Instructor menus, as our spec describes | Brian Tran |
| 2 | Make the menu and help-type errors say what a valid choice is | Rahul Rajendran |
| 3 | Estimated wait time: a session method that adds up the minutes ahead of a ticket, shown after joining and after checking position, plus tests | Zayd Pathan |
| 4 | Controller tests for the role menus and the wait-time display | Zhaojin Gao |
| 5 | Final README, demo script, and a full team run-through | Rahul Rajendran (lead), whole team |

### Stretch goals

- **Estimated wait time: committing** (Zayd). `HelpType` already stores the default minutes for each type.
- **Leaving the line:** already done and moved into the MVP in D3.
- We are not dropping any stretch goals.

### 3-minute demo

| Time | Step |
| --- | --- |
| 0:00–0:20 | Set the PIN; show that a blank PIN re-prompts |
| 0:20–1:00 | Ana, Ben, and Cara join; Cara checks her position |
| 1:00–1:20 | Cara cancels |
| 1:20–2:10 | **New feature:** Call next (Ana) → Requeue → Show line (Ben, Ana) → Call next (Ben) |
| 2:10–2:40 | Bad input: a wrong PIN, and Requeue with no one being helped |
| 2:40–3:00 | Run the test suite and show every test passing |

---

## Attribution

| File | Who worked on it and how |
| --- | --- |
| `LinkedQueue.java` | Rahul Rajendran: wrote it (Claude-assisted). **D4:** Rahul removed the unused `peek()`. |
| `EmptyQueueException.java` | Rahul Rajendran: wrote it. |
| `StudentRequest.java` | Zayd Pathan: wrote it (ChatGPT-assisted). |
| `HelpType.java` | Zayd Pathan: wrote it (ChatGPT-assisted). |
| `StudentStatus.java` | Zayd Pathan: wrote it (ChatGPT-assisted). |
| `OfficeHoursSession.java` | Brian Tran: drafted it with GenAI and reviewed it. **D4:** Brian added `requeueCurrent()` by drafting and reviewing using Copilot. Rahul removed the `1234` PIN constructor (Claude-assisted). |
| `OfficeHoursController.java` | Brian Tran: drafted it with GenAI and reviewed it. **D4:** Brian added the Requeue option, the looping instructor menu, and the re-prompts by drafting and reviewing using Copilot. |
| `OfficeHoursApp.java` | Brian Tran: wrote it. **D4:** Rahul Rajendran added the startup PIN prompt and `start(ConsoleView)` (Claude-assisted). |
| `ConsoleView.java` | Zhaojin Gao: requested it from Codex using the team's model/view design, then personally reviewed and understood the code. |
| `ConsoleViewDemo.java` | Zhaojin Gao: requested a view-only demo from Codex, then personally reviewed and understood it. |
| `LinkedQueueTest.java` | Rahul Rajendran: wrote it. Brian Tran converted it to JUnit 5. **D4:** Rahul added 6 tests covering refill after empty or `clear()`, interleaved order, and recovery after bad input (Claude-assisted). |
| `OfficeHoursSessionTest.java` | Rahul Rajendran: fixed two enum names in D3. **D4:** Brian Tran expanded it from 3 to 20 tests, including requeue by drafting and reviewing using Copilot. |
| `OfficeHoursControllerTest.java` | Brian Tran: wrote the original tests with GenAI. **D4:** Zhaojin Gao requested 33 tests from Codex and reviewed all of them; Brian added 7, including the requeue tests by drafting and reviewing using Copilot. |
| `OfficeHoursAppTest.java` | **D4:** Rahul Rajendran wrote 9 startup PIN tests (Claude-assisted). Brian Tran added 2 by drafting and reviewing using Copilot. |
| `ConsoleViewTest.java` | Zhaojin Gao: requested it from Codex and reran all 9 tests after the merge. |
| `ConsoleViewDemoTest.java` | Zhaojin Gao: requested it from Codex and reran all 3 tests after the merge. |
<<<<<<< HEAD
| `StudentRequestTest.java` | *Zayd ran it through codex and double checked it by running it.* |
=======
| `StudentRequestTest.java` | **D4:** Zayd Pathan wrote it *[Zayd: GenAI use]*. |
>>>>>>> b3509323020052f765fed7689bad170f0e51452c
| `HelpTypeTest.java` | Zayd Pathan: wrote it. **D4:** Rahul Rajendran fixed the expected order, labels, and minutes by hand and converted it to JUnit 5. |
| `StudentStatusTest.java` | Zayd Pathan: wrote it. **D4:** Rahul Rajendran changed `COMPLETED` to `SERVED` by hand and converted it to JUnit 5. |

## GenAI Note

**Rahul Rajendran:**
- **D4:** Claude compared our repo against the rubric, drafted the startup PIN code and its tests and the new queue tests, explained why the enum tests and Eclipse runs were failing, and drafted this README from our team's answers. I fixed the enum tests by hand, chose to remove `peek()`, and reviewed and ran everything before committing.

**Zhaojin Gao:**
- **D4:** I asked Codex to add controller tests using simple Java. It drafted 33 additional tests for normal menu actions, bad input, and input ending at prompts. I personally reviewed all of the added tests, understood how they work, and found them correct. At my request, Codex also added simple explanations of the test helper and important checks. These tests run the real controller with a real session and a console view using test input. They check both printed messages and model state. They do not replace the separate model tests.

**Brian Tran:**
- **D4:** I used Copilot to draft the re-queue feature, session, and controller changes in addition to the tests, and reviewed the code manually.

**Zayd Pathan:**
<<<<<<< HEAD
- **D4:** 

I used ChatGPT to help create the JUnit test cases for my StudentRequest class. It helped identify the different methods that needed testing, including the constructor, getter methods, setting student status, and handling invalid inputs. I reviewed the generated tests and adjusted them to match my HelpType and StudentStatus enums. I used the tests to better understand how to test my class and verify that it works as intended.*
=======
- **D4:** *[Zayd: fill in]*
>>>>>>> b3509323020052f765fed7689bad170f0e51452c
