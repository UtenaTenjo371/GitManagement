# Task3任务

•实现Commit

•给定一个工作区目录，生成对应的blob和tree(上周已完成)以及commit

•写代码之前先理清思路，写设计文档

•提示：

•需要存储指向当前最新commit的HEAD指针

•每次新生成一个commit前，需要把根目录的tree key与已有的最新commit的tree key进行比较，发现不相同时（即文件发生了变动）才添加这个commit

### 层次图（模块间的调用关系）

![GitManage](https://raw.githubusercontent.com/UtenaTenjo371/GitManagement/zjq/3-Task3-Commit/Docs/Img/GitManage.png)



### 类图

![GitManage (3)](https://raw.githubusercontent.com/UtenaTenjo371/GitManagement/zjq/3-Task3-Commit/Docs/Img/GitManage%20(3).png)

VersionController类：管理版本，存储Branch类指针head，指向当前所在commit结点。该类定义了版本管理方法：在当前branch提交commit、创建分支、切换分支、合并分支、版本回退。

Branch类：存放branch名和指向当前branch最后一个commit的指针。

CommitObject类：继承自GitObject类，存储Commit对象。Commit对象分为三种，根结点commit对象没有parent，一般commit对象有一个parent，merge后的commit对象有两个parent。

### Commit结构示意图

![Commit结构](https://raw.githubusercontent.com/UtenaTenjo371/GitManagement/zjq/3-Task3-Commit/Docs/Img/GitManage%20(5).png)
