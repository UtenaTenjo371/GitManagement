## Key-Value功能 (Task1)

最简单的key-value存储方式

•Key作为文件名，value作为文件内容

支持以下功能

•给定value，向存储中添加对应的key-value

•给定key，查找得到对应的value值

封装成class对外提供接口

### Class ObjectStore

提供Key-Value存储所需的功能。

变量（静态，不变。指定存储路径为当前文件夹下的.mygit\\objects）：

```
存储路径：
	private static final String rootDir=".mygit\\objects";
```

构造方法：

```
public ObjectStore() 构造时检查所需存储路径是否存在，不存在则新建存储路径文件夹
```

方法（静态）：

```
Key-Value存储的基本方法：
    给定value，向存储中添加对应的key-value。调用saveObject，直接向磁盘中存入文件，仿照git的存法在存储路径下新建文件夹和文件，文件夹和文件名分别为hash值的前2位和后38位。
        public static String add(GitObject f) 
    给定key，查找得到对应的value值。调用saveObject，按照hash值从磁盘中找相应的文件，根据文件夹和文件命名寻找。
        public static GitObject get(String key)
    删除GitObject的方法
        public static String delete(GitObject f) 
	
对应返回不同种类的GitObject的get方法：
    public static BlobObject getBlob(String key)
    public static TreeObject getTree(String key)
    public static CommitObject getCommit(String key)
    
对branch进行操作的方法：
    存储branch，返回branch最近一次commit的hash值
    	public static String saveBranch(Branch b)
    根据分支名获取branch对象
    	public static Branch getBranch(String bName)
    判断branch是否存在
    	public static boolean isBranch(String bName)
    存储指向工作区branch的head
    	public static String saveHead(String head)
    获得head
    	public static String getHead()
    获得所有branch
    	public static Vector<Branch> getAllBranch()
    删除branch
    	public static boolean deleteBranch(String bName)
```

#### Class SaveObject

用于完成以二进制方式存储到磁盘的功能。

方法：

```
写入文件
	public static void writeObjectToFile(String fileDir,Object obj)

读取文件
	public static Object readObjectFromFile(String fileDir)
```

### Class GitObject

抽象类，待存储的对象。共同点为均有key和value，但value内容不同，key的算法不同。实现 Serializable和Comparable接口。

变量：

```
该对象的哈希值
	private String key; 
检查文件本身是否已经存储
	private boolean isSaved = false;

实现Serializable接口需要的ID（以序列化存储到磁盘）
	private static final long serialVersionUID =9876543212345L;
```

方法：

```
设置对象当前key值，调用计算key值的calcKey
	public void updateKey()

获取key值
	public String getKey()

计算key值，由于子类的key值计算方法不同所以为抽象
	public abstract String calcKey();

比较大小
	public int compareTo(GitObject gitObject)

存储GitObject对象
	public void save()
 	查看存储情况
 		public boolean isSaved()
```

#### Class BlobObject

待存储的Blob（文件）对象，GitObject为父类。

变量：

```
文件名和文件内容
	private final String fileName;
	private Vector<byte[]> data;
```

构造方法：

```
只给文件，则自动获取文件名，调用以下一种构造
	public BlobObject(File file)

给出文件名和文件的构造，构造同时设置key，调用readfile把目标文件的value存到BlobObject内
	public BlobObject(String fileName, File file)
```

方法：

```
读文件方法
	private void readfile(File f)
写文件方法
	private boolean writefile(File f)

BlobObject的计算key值方法，调用CalcHash进行具体运算
	public String calcKey() 

文件名的get方法
	public String getFileName() 
文件的get方法
	public Vector<byte[]> getData()

将Blob的数据恢复到目标位置，调用writefile把BlobObject还原到目标路径中
	public boolean restoreData(String targetDir)
```

#### Class TreeObject

待存储的Tree（文件夹）对象，GitObject为父类。TreeObject的key计算，包含文件夹内的子文件的名称、子文件夹的名称；包含每个子文件blob的key；包含每个子文件夹tree的key。

变量：

```
文件夹路径名、其中的子文件与子文件夹的哈希值
	private final String dirName;
	private final String[] blobs;
	private final String[] trees;
```

构造方法：

```
传入文件夹路径、遍历后得到的子文件与子文件夹进行构造，并确保TreeObject中的子内容能在现有存储中找到构造，同时设置key
	传入Vector的构造方法
    public TreeObject(String dirName, Vector<BlobObject> vb, Vector<TreeObject> vt)
    传入数组的构造方法
	public TreeObject(String dirName, BlobObject[] blobs, TreeObject[] trees)
```

方法：

```
文件夹路径的get方法
	public String getDirName() 
子文件的get方法，调用ObjectStore从文件的key还原文件本身
	public BlobObject[] getBlobs()
子文件夹的get方法，调用ObjectStore从文件的key还原文件本身
	public TreeObject[] getTrees()

TreeObject的计算key值方法，调用快速排序Quick使传入内容有序，调用CalcHash进行具体运算，调用ObjectStore从文件的key还原文件本身以找到计算key所需的子文件和子文件夹的名称
	public String calcKey()
		class Quick
```

#### Class CommitObject

待存储的Commit提交对象，GitObject为父类。CommitObject的key计算，包含根目录tree对象的key，包含前一次commit的key（parent）（若是第一次commit就不包含），包含merge的key（mergeParent）（若没有merge就不包含）。

变量：

```
根目录tree的哈希值、上一次的commit哈希值和merge的哈希值
    private final String rootTree;
    private final String parent;
    private final String mergeParent;
```

构造方法：

```
构造同时计算key并确保其中涉及的treeobject和commitobject已经存储，并存储自身

第一次commit的构造方法，只需根目录tree
    public CommitObject(TreeObject rootTree)
一般commit的构造方法，只需根目录tree和前一次commit
    public CommitObject(TreeObject rootTree, CommitObject parent)
merge后的commit的构造方法
    public CommitObject(TreeObject rootTree, CommitObject parent, CommitObject mergeParent)
```

方法：

```
CommitObject的计算key值方法，调用CalcHash进行具体运算
	public String calcKey() 

根目录tree对象的get方法
	public TreeObject getRootTree()
前一次commit对象的get方法
	public CommitObject getParent() 
merge的commit对象的get方法
    public CommitObject getMergeParent() 
```

### Class CalcHash

用于计算hash值的方法。

变量：

```
记录哈希值
	private MessageDigest complete;
```

构造方法：

```
	public CalcHash()
```

方法：

```
向哈希值结果中加入新的内容
	public void addFile(File f) 
	public void addString(String s)
	public void addBytes(Vector<byte[]> bv)
获取String类型的40位哈希值
	public String getHash()
```