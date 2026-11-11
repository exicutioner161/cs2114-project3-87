# Gao 的界面部分说明

## 上次与这次的关系

上次写的是 Model/View Boundary（模型和界面的边界）。这次把其中 ConsoleView 的设计变成实际 Java 代码，并测试它。没有替组员完成模型，也没有修改队列。

## 我的文件

1. src/main/ConsoleView.java：实际界面类。
2. src/main/ConsoleViewDemo.java：独立演示输入、输出和退出。
3. tests/maintest/ConsoleViewTest.java：9个输入输出测试。
4. tests/maintest/ConsoleViewDemoTest.java：3个演示退出测试。
5. README.md：补充运行方式、Reflection、Next Steps 和真实的 AI 贡献说明。

## 简单理解代码

Scanner 用来读一行输入。PrintStream 用来显示文字。

readLine() 先检查有没有下一行。有就返回这一行，没有就返回 null。空行返回空字符串，它和 null 不一样。

readLine(String prompt) 先显示提示，再读输入。

showMessage、showMenu、showLine、showPosition 都只显示 Controller 给它的文字。它们不接收 StudentRequest 或 OfficeHoursSession，不会操作队列。

默认构造方法连接键盘和屏幕。另一个构造方法让测试提供模拟输入和接收输出的对象，所以测试不用手动输入。

## 为什么不在这里检查 PIN 或名字

Controller 把菜单选择转成操作，并处理无法转换的输入。Model 负责队列、PIN 和学生状态的规则。View 保留原始输入并显示结果。这样换一个界面时，模型不用重写。

## 测试检查什么

9个 View 测试：完整姓名、原始空格/非数字输入、空行、空输入、最后一行没有换行符、提示显示、输出顺序、缺少输入对象、缺少输出对象。

3个 Demo 测试：输入名字后 quit、直接结束输入、输入名字后结束输入。最后一个测试验证不输入 quit 也能正常结束。测试会恢复原来的 System.in 和 System.out，避免影响其他测试。

## 当前还没完成的部分

仓库下载时 OfficeHoursSession 是空类，Controller.run() 也是空方法。完整排队应用目前还不能工作。这里的 Demo 只是展示你的界面能独立运行，不是假装完整 MVP。

下一步需要和组员确认 Controller 的方法调用。README 中的 skip/requeue 是下一阶段的建议，不是已经写好的功能，也不是已确认的小组决定。

## Eclipse 运行

File > Import > General > Existing Projects into Workspace，选择这个项目文件夹。确保同一工作区已有课程 CS2-Support 项目。

右键 ConsoleViewDemo.java > Run As > Java Application。输入 Gao，再输入 quit。

右键 ConsoleViewTest.java 或 ConsoleViewDemoTest.java > Run As > JUnit Test。

## 可以向 TA 解释的简单英文

My view only reads and shows text. It does not change the queue. The controller handles choices, and the model checks the rules. I can test the view without the model. A blank line is an empty string. The end of input is null.

请先运行并读懂代码，再如实补充你自己的修改和审阅记录。当前 README 没有声称你手工编写或审阅了尚未做的内容。
