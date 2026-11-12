# Office Hours Queue — CS 2114 Project 3 (Group 87)

A terminal app that manages an office hours line. Students join with a name and a help type and get a ticket. They can check their position or cancel. A TA uses a PIN to serve the next student. The model is built on our generic `LinkedQueue`, and it runs and is tested without any view.

> Items marked **[TODO]** need the group's real details. Delete each marker once it's filled in.

---

## How to Run

Import this folder in Eclipse with **File > Import > General > Existing Projects into Workspace**. Keep the course `CS2-Support` project in the same workspace, because the build path needs it.

### 1. The model (no view required)
- **Session tests:** right-click `tests/[TODO]/OfficeHoursSessionTest.java` > Run As > JUnit Test.
- **Queue tests:** right-click `tests/projectutilstest/LinkedQueueTest.java` > Run As > JUnit Test.
- **[TODO, optional] Driver:** right-click `src/main/[ModelDriver].java` > Run As > Java Application. It joins students, serves them, and cancels one, then prints the results. It uses no view.

### 2. The thin view
- **View demo:** right-click `src/main/ConsoleViewDemo.java` > Run As > Java Application. Type a name, then type `quit` to stop. This demo only shows input and output. It does not touch the queue.
- **View tests:** `tests/maintest/ConsoleViewTest.java` and `tests/maintest/ConsoleViewDemoTest.java` > Run As > JUnit Test.

### 3. The full app (MVP path)
- Right-click `src/main/OfficeHoursApp.java` > Run As > Java Application.
- **[TODO]** List the menu options and one sample run, for example: join "Ana" (Debugging) → join "Ben" (Concept) → TA serves with PIN → Ana is served → Ben cancels.

---

## Reflection

### What changed from our specification
| Area | Spec said | What we built | Why / what prompted it |
| --- | --- | --- | --- |
| Scope | Cancelling/leaving the line was a stretch goal | Cancel is in the MVP: the spot is marked cancelled and skipped when it reaches the front | [TODO] e.g. we realized students leaving is a core case, not an extra |
| Help request | [TODO] | Students pick a help-type **enum** instead of typing a description | It keeps input simple to validate and supports the wait-time stretch goal |
| ConsoleView | Reads raw lines, displays text, imports no model classes | Same, plus a second constructor that takes `Scanner`/`PrintStream` | That constructor let us test the view with fixed input instead of a keyboard |
| Validation | [TODO: where the spec put it] | [TODO: e.g. the session checks the PIN and empty names; the controller checks menu numbers] | [TODO] |
| Method signatures | [TODO] | [TODO] | [TODO] |
| Tests | [TODO: tests the spec planned] | [TODO: tests that exist now, and any that were added or cut] | [TODO] |

### Did the model really stand alone?
- **Model → view:** No model class (`OfficeHoursSession`, `StudentRequest`, `StudentStatus`, `LinkedQueue`) imports or calls `ConsoleView` or `System.out`. The model's tests run with no view at all. **[TODO: confirm by checking the imports]**
- **View → model:** `ConsoleView` imports no model classes. The controller turns model results into strings before it passes them to the view.
- **Anything moved across the line:** [TODO] e.g. "Nothing moved," or "We moved X from the controller into the session because..."

### What happened to our unknowns
| Unknown from our scope | Status | What we learned / what's still open |
| --- | --- | --- |
| [TODO, copy from Deliverable 1] | Figured out / Still open | |
| [TODO] | | |
| Testing console input and output without a keyboard | Figured out | We inject a `Scanner`/`PrintStream` into `ConsoleView` |

### What doesn't work yet
- **Features:** [TODO, or "none on the MVP path"]
- **Bad-input cases not yet handled:** [TODO: check invalid menu numbers, empty names, a wrong PIN, serving from an empty line, and end of input (`readLine()` returns null)]
- **Missing or failing tests:** [TODO]

---

## Next Steps

### 1. Finishing the MVP (in order)
| # | Item | Owner |
| --- | --- | --- |
| 1 | [TODO: remaining model fix or feature] | [TODO] |
| 2 | [TODO: controller handling for bad input] | [TODO] |
| 3 | Send all error messages through `ConsoleView` instead of `System.out` in the controller | [TODO] |
| 4 | Test every bad-input case at every prompt | [TODO] |
| 5 | Run the full MVP end to end and update the run instructions | [TODO] |

### 2. Change feature: Skip / Requeue
The TA can send the student currently being helped to the **back of the line**, keeping the same ticket number. One example is a student who needs to go run their code first. This adds new state and logic to the model, and users reach it through the controller and view. **[TODO: confirm no one already built this]**

**Predicted to change:**
| Class | Layer | Predicted change |
| --- | --- | --- |
| `OfficeHoursSession` | Model | New method `requeueCurrentStudent(String pin)`. It checks the PIN and that a current student exists, then sets the status back to `WAITING` and calls `enqueue` again |
| `StudentRequest` / `StudentStatus` | Model | Make sure a request can change from `BEING_HELPED` [TODO: real name] back to `WAITING` |
| `OfficeHoursController` | Controller | New menu choice in `run()` and a new `handleRequeue()` method |

**Predicted *not* to change:**
- `ConsoleView` does not change. The controller supplies the menu text and result text.
- `LinkedQueue` does not change. The existing `enqueue`/`dequeue` methods are enough.

We'll compare this prediction to what actually changed in Deliverable 4.

### 3. Stretch goals
| Stretch goal | Decision | Reason |
| --- | --- | --- |
| Estimated wait time (fixed default per help type) | [TODO: Committing / Dropping] | |
| [TODO: others from scope] | | |

### 4. New tests needed
- **`requeueCurrentStudent`:** wrong PIN, no current student, empty waiting line, the student goes to the back, the ticket number stays the same, and the order of everyone else is unchanged.
- **Controller:** the new menu choice, plus invalid input at that prompt.
- **Remaining MVP:** [TODO: tests for any open bad-input cases above]

### 5. Who does what
| Member | Remaining items |
| --- | --- |
| Zhaojin Gao | [TODO, e.g. view integration + view tests] |
| [TODO] | |
| [TODO] | |
| [TODO] | |

---

## Attribution

Each row lists who worked on the file and how (wrote, revised, reviewed, tested, GenAI-assisted).

| File | Who / how |
| --- | --- |
| `src/projectutils/LinkedQueue.java` | [TODO] |
| `src/[TODO]/StudentRequest.java` | [TODO] |
| `src/[TODO]/StudentStatus.java` | [TODO] |
| `src/[TODO]/HelpType.java` (if it exists) | [TODO] |
| `src/main/OfficeHoursSession.java` | [TODO] |
| `src/main/OfficeHoursController.java` | [TODO] |
| `src/main/OfficeHoursApp.java` | [TODO] |
| `src/main/ConsoleView.java` | Zhaojin Gao: GenAI-assisted (Codex drafted it from our Model/View Boundary design); [TODO: reviewed / revised / tested] |
| `src/main/ConsoleViewDemo.java` | Zhaojin Gao: GenAI-assisted (Codex); [TODO] |
| `tests/projectutilstest/LinkedQueueTest.java` | [TODO] |
| `tests/[TODO]/OfficeHoursSessionTest.java` | [TODO] |
| `tests/maintest/ConsoleViewTest.java` | Zhaojin Gao: GenAI-assisted (Codex drafted and ran it); [TODO] |
| `tests/maintest/ConsoleViewDemoTest.java` | Zhaojin Gao: GenAI-assisted (Codex drafted and ran it); [TODO] |

## GenAI Note

- **Zhaojin Gao:** Used Codex to draft `ConsoleView`, `ConsoleViewDemo`, and their tests from our Model/View Boundary design, in our `student.TestCase` style. Codex also drafted the first version of this README's view sections. [TODO: what Gao reviewed or changed]
- **Change feature check:** We described our current classes to GenAI and asked which ones skip/requeue would force us to edit. It named `OfficeHoursSession`, `OfficeHoursController`, and possibly `StudentStatus`. That list does not cross into `ConsoleView` or `LinkedQueue`, so our model/view boundary held. [TODO: confirm who asked and which tool]
- **[TODO, each other member]:** how they used GenAI, or "did not use GenAI."
