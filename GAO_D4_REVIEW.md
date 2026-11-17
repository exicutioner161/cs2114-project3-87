# Gao D4 review notes

## 本次修改

修改文件：`tests/maintest/OfficeHoursControllerTest.java`。
保留原有 3 个测试，新增 33 个，目前 Controller 共 36 个测试。
全项目使用 Java 17 和 JUnit 5 编译运行：91 个测试通过，0 个失败。

这些新增测试由 Codex 根据 Gao 的要求编写。Gao 已亲自审核全部新增测试，确认全部理解并认为正确。Codex 随后按要求补充了方法与断言的解释。
本次没有改动 Controller、Session、ConsoleView 或队友的测试。

## 怎么读这些测试

每个测试分三步：建立 session、输入菜单选择、检查结果。
`runInput` 是一个小工具方法：把字符串当作键盘输入，运行真正的 Controller，再返回屏幕输出。
字符串里的 `\n` 表示按一次回车。例如 `1\nAna\n2\n5\n` 表示加入队列、输入 Ana、选择第二种帮助、退出。
`assertEquals` 检查实际结果是否等于预期；`assertTrue` / `assertFalse` 检查条件；`assertNull` 检查当前没有学生接受帮助。
没有使用模拟框架、反射或参数化测试。

## 已覆盖

- 退出后不处理后面的输入。
- 主菜单的文字、空白和超出范围输入；错误后还能正常加入。
- 正常加入、去除名字首尾空格、帮助类型和票号输出。
- 空白名字被拒绝；帮助类型错误后重新选择。
- 查询位置、取消指定学生、后面学生位置更新。
- 查询和取消时的非数字、整数溢出、不存在及负数票号。
- 已取消票号不能再次取消或查询位置。
- 错误 PIN 无法进入教师菜单。
- 教师菜单的文字、空白及超出范围输入。
- 叫下一位、完成当前学生、再叫下一位。
- 正在帮助学生时不能再次叫人；原有状态保留。
- 空队列叫人、无人接受帮助时完成操作；错误后仍能加入。
- 显示等待顺序，排除已取消学生；空队列显示；返回主菜单。
- 在主菜单、姓名、帮助类型、票号、PIN 和教师选项处输入结束。

## Requeue 尚待完成

拉取到的 main 提交 a4e54ce 里尚无 requeue 方法或菜单选项。
因此没有编写假接口测试，也没有把 requeue 标为完成。
Brian 提交后需要补上：

1. 从教师菜单执行 requeue，检查实际约定的学生移到正确位置。
2. 检查原票号、状态和其他等待学生的相对顺序符合约定。
3. 再叫下一位，确认服务顺序符合 requeue 的定义。
4. 测试没有可 requeue 学生时的提示、状态保留和后续可用性。
5. 按最终菜单形式补无效输入与输入结束测试。

这些测试针对 Controller 与真实 Model 的交互；不替代 Brian 负责的独立 Session 方法测试。

## 贡献与 AI 使用说明

Gao personally reviewed all 33 added controller tests, understood how they work, and found them correct. Codex drafted the tests, added simple explanations, and ran the full test suite. All 91 tests passed. Requeue tests are pending Brian's implementation.

## 方法解释（Codex 编写）

- `runInput(session, input)`：把预先写好的输入交给真正的 Controller。用 `ByteArrayOutputStream` 收集输出，再转成字符串供断言检查。
- `instructorCanCallCompleteAndCallNext()`：先加入 Ana 和 Ben，通过菜单叫 Ana、完成 Ana、再叫 Ben。检查 Ana 已完成，Ben 正在接受帮助。
- `showLineUsesWaitingOrderAndSkipsCancelledStudents()`：加入三人并取消中间的人，检查显示只包含剩下两人，顺序正确。`indexOf` 返回文字出现的位置。
- `endOfInputAt...`：在某个提示处停止提供输入。检查程序正常返回，而且原来的学生和队列没有被误改。
- 错误输入后的正常操作：确认程序不仅显示错误信息，而且仍可以继续使用。
