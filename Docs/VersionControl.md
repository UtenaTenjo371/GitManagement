## 版本控制相关类

实现用户通过命令行进行交互

```
VersionController init: 初始化本地仓库
VersionController branch: 打印分支
VersionController branch [branch name]: 创建分支
VersionController branch –d [branchName]: 删除分支
VersionController checkout [branch name]: 切换至分支
VersionController commit: 提交一个commit
VersionController reset [commit id]: 回滚到指定commit
VersionController log: 打印当前分支的commit日志
VersionController add: 添加根目录下的文件/文件夹到暂存区，将文件状态切换至staged
```

### Class VersionController

提供用户版本控制所需的功能的接口，也是命令行入口。

变量：

```
private final String path;//文件夹路径
private String head;//head指向工作区的branch分支
```

方法：

```
	/**构造方法，初始化path和head参数*/
	public VersionController(String path)

    /**更新分支commit*/
    public void addCommit(String commitMessage)

    /**判断仓库是否存在*/
    public boolean isRepository()

    /**执行git操作前，判断当前路径是否为仓库，并做相应的准备工作*/
    public boolean checkIfRepository()

    /**创建仓库*/
    public void initRepository()
    
    /**打印commit日志*/
    public void printLog()

    /**回溯到commit*/
    public void resetCommit(String commitKey) throws IOException 

    /**创建分支*/
    public void createBranch(String branchName)

    /**切换分支*/
    public void switchToBranch(String branchName)

    /**打印分支*/
    public void printBranch()
    
    /**删除分支*/
    public boolean deleteBranch(String branchName)
    
    /**恢复文件到指定版本*/
    public void changeToCommit(String cHash)
```

### Class ConvertFolder

遍历文件夹，实现存储Blob，Tree以及文件的恢复

方法：

```
    /**gitObject比较器*/
    public static Comparator<GitObject> comparatorObject =new Comparator <GitObject>()
    
	/**遍历文件夹，存储Tree，Blob*/
    public static TreeObject dfs(String path)
    
    /**获取工作区目录*/
    public static TreeObject changeFile(String path,TreeObject curTree)
    
    /**删除文件路径*/
    public static boolean deleteFolder(String path)
    
    /**删除文件*/
    public static boolean deleteFile(String sPath) 
    
    /**删除文件夹*/
    public static boolean deleteDirectory(String sPath) 
```

#### Class Branch

分支类，存储分支类的分支名和Commit

变量：

```
private final String branchName;//分支名
private String latestCommit;//存储哈希值
```

方法：

```
	/**获得分支名*/
	public String getBranchName()

	/**获得最新Commit*/
    public CommitObject getLatestCommit() 

	/**获得Commit的哈希值*/
    public String getCommitHash()
	
	/**通过Commit更改Branch最新Commit*/
    public void setLatestCommit(CommitObject latestCommit) 
    
    /**通过哈希值更改Branch最新Commit*/
    public void setLatestCommit(String commitKey)

    /**branch:main构造方法*/
    public Branch() 
    
    /**一般branch构造方法*/
    public Branch(String branchName, CommitObject latestCommit)
    
	/**保存branch到本地*/
    public void save()
```
