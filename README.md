# GitManagement
## 简介
根据git版本控制原理，使用Java实现的版本管理工具。
## 项目说明
#### 层次图

![GitManage](https://raw.githubusercontent.com/UtenaTenjo371/GitManagement/zjq/3-Task3-Commit/Docs/Img/GitManage.png)

#### Commit管理

UML类图

![UML类图](C:\Users\29574\Pictures\Saved Pictures\GitManage (3).png)

Commit结构示意图

![Commit结构](https://raw.githubusercontent.com/UtenaTenjo371/GitManagement/zjq/3-Task3-Commit/Docs/Img/GitManage%20(5).png)

每个commit都是一个CommitObject，Commit对象分为三种，根结点commit对象没有parent，一般commit对象有一个parent，merge后的commit对象有两个parent。

Branch类存放branch名和指向当前branch最后一个commit的指针。

VersionController类存放head指针和版本控制方法。

#### 类图

待完善。
##### Task3类图
![GitManage (3)](https://raw.githubusercontent.com/UtenaTenjo371/GitManagement/zjq/3-Task3-Commit/Docs/Img/GitManage%20(3).png)

## 成员
ZJQ[@UtenaTenjo371](https://github.com/UtenaTenjo371)  ZSW[@Windyabc](https://github.com/Windyabc)  CYB[@YanbingChen](https://github.com/YanbingChen)

