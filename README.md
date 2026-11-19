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
4. In the