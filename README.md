# cs2114-project3-87

## Console View Contribution

The console view reads raw input and shows text from the controller. It does not import model classes or change the queue. This section describes Gao's GenAI-assisted view draft only.

### How to run

Import this folder in Eclipse with File > Import > General > Existing Projects into Workspace. Keep the course CS2-Support project in the same workspace, as required by the existing build path.

- View demo: right-click `src/main/ConsoleViewDemo.java` > Run As > Java Application. Type a name. Type `quit` to stop.
- View tests: right-click `tests/maintest/ConsoleViewTest.java` > Run As > JUnit Test.
- Demo tests: right-click `tests/maintest/ConsoleViewDemoTest.java` > Run As > JUnit Test.
- Queue tests: right-click `tests/projectutilstest/LinkedQueueTest.java` > Run As > JUnit Test. These run without the view.

The view demo is only an input/output demo. It does not join or serve students. In the main branch used for this draft, OfficeHoursSession is empty and OfficeHoursController.run is a stub. OfficeHoursApp is therefore not yet a working full MVP.

## Reflection

### Console view and model/view boundary

The spec says ConsoleView reads raw lines, displays text, and imports no model classes. This implementation follows that rule. `readLine()` returns null when input ends. An empty line stays an empty string. The view keeps spaces and non-number input so the controller can decide what they mean.

A second constructor accepts Scanner and PrintStream. This allows tests to give fixed input and check output without a keyboard. The normal constructor still uses System.in and System.out. The view does not close the shared streams. A separate ConsoleViewDemo was added to show the view before the full controller is ready.

The view compiles and runs without the model. The queue tests also run without the view. These checks do not prove that the full session and controller work. Connecting the view to the real session is still open. The downloaded spec extract did not list the original scope unknowns, so the group still needs to compare those unknowns with its current work.

The full join-and-serve path, session rules, and controller input handling are not finished in the main branch reviewed for this contribution. The group should add its model/controller changes and remaining failures here.

## Next Steps

### View integration plan

1. The session owner finishes the model methods and rules.
2. The controller owner connects the menu loop to the model and this view.
3. Gao's proposed follow-up is to check view integration and extend view tests; the group should confirm ownership.
4. Test invalid menu numbers, empty names, bad PINs, and end of input at every prompt.
5. Run the whole MVP and add the exact full-app run instructions.

The view methods are `readLine()`, `readLine(String prompt)`, `showMenu(String text)`, `showMessage(String text)`, `showLine(String text)`, and `showPosition(String text)`. The controller formats model results before calling these methods. Error messages should also go through the view instead of System.out in the controller.

### Proposed Deliverable 4 feature

Skip/requeue could move the current student to the back of the waiting line. First confirm that it is not already part of the MVP and that the group wants this feature. It is a proposal, not an implemented feature.

- OfficeHoursSession: add a method such as `requeueCurrentStudent`, with PIN and current-student checks.
- OfficeHoursController: update `run()` and add a handler for the new choice.
- ConsoleView: no change expected, because the controller supplies menu and result text.
- LinkedQueue: no change expected if existing enqueue/dequeue methods are enough.
- StudentRequest/StudentStatus: check whether the finished model already supports changing back to WAITING.

Tests should cover no current student, an empty waiting line, a wrong PIN, queue order, and keeping the same ticket number. The group still needs to record agreed owners, committed/dropped stretch goals, and any other model/controller work. These decisions cannot be inferred from the current code.

## Attribution

### Files changed in this view contribution

| File | Contribution |
| --- | --- |
| src/main/ConsoleView.java | Codex drafted the view at Zhaojin Gao's request, using the group's Model/View Boundary design. |
| src/main/ConsoleViewDemo.java | Codex drafted a view-only demo at Zhaojin Gao's request. |
| tests/maintest/ConsoleViewTest.java | Codex drafted and ran the view tests at Zhaojin Gao's request. |

| tests/maintest/ConsoleViewDemoTest.java | Codex drafted and ran demo exit tests at Zhaojin Gao's request. |

This table covers only these changed files. The group must add a row for every other class and test file, using actual contributions. Add personal review or revisions only after doing them.

## GenAI Note

I asked Codex to continue my Model/View Boundary part using simple Java and simple English. It read our design extract and current repository, filled in ConsoleView, added a demo, and wrote view tests using our student.TestCase style. It also drafted this README section and predicted which classes a skip/requeue feature could change. The full session/controller integration is not yet verified. The model and queue implementations were left unchanged.
