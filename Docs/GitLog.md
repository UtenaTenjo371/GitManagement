## GitLog相关类

git中现有 git log命令行交互的结果：

```
每条commit都对应一条log
commit hash
author 
date(日期与时区) 
提交时的注释
```

git中现有logs路径保存的HEAD文件内容的结果：

```
commit的前一次parent的hash值 当前commit的hash值 用户名及用户邮箱 从1970.1.1 08:00:00:000起的时间戳 操作名（如commit、branch、checkout、clone等）：操作具体注释
```

由于根据最初的ppt中要求，只需进行命令行查看commit历史，因此当前直接使用logs下的HEAD文件存储当前分支下的commit记录。
实际上.git的log里HEAD文件是存储所有的操作记录日志的，包括创建、切换分支等等。目前暂未实现。

由于当前没有设置用户的功能，去掉author的部分。暂无时区的获取。

未实现将存储的字符串内容转译成可读性强的文字的功能。

### Class GitLog

提供git log所需的功能。

变量：

```
所有logs的指定存储根目录：
	private static final String rootDir=".mygit\\logs";
当前分支logs的存储路径：
	private final String logDir = rootDir + "\\" + "HEAD";
```

方法：

```
初始化log，建log同时需要一个存对应本地branch的log的文件夹
    public void iniGitLog()

添加对应commit的log的方法
    public String add(CommitObject co) 
（暂未实现的对应实际HEAD的添加log方法：
	添加对应branch的log的方法
	public String add(Branch br)
	添加对应checkout的log的方法
	public String add(String currenthead, Branch br)
	……）

获取当前分支下对应行log的方法，目前默认为获取最新的一条log
    public String get() 
获取当前分支下全部行log的方法
    public String getAll()
获取给定分支下全部行log的方法
    public String getBranchAll(String branchName)
    
切换分支时，切换log
    public void switchBranchLog(String currentHead, String desBranchName)
reset后，更新log到reset回的位置，其后的内容删除
    public void updateLogAfterReset()
```

#### Class SaveString

用于完成向磁盘中文件写入字符串内容的功能。

方法：

```
将String写入文件
	public static void writeStringToFile(String fileDir,String line)

根据输入的line值读取文件中的String
	public static String readStringFromFile(String fileDir, int line)
	
覆写文件
    public static void overwriteStringToFile(String fileDir,String content)

删除文件中含有指定关键字之后的内容
    public static void deleteContentAfterKeyword(String oldFilepath, String keyword) 
```

