## Task1任务

最简单的key-value存储方式

•Key作为文件名，value作为文件内容

支持以下功能

•给定value，向存储中添加对应的key-value

•给定key，查找得到对应的value值

封装成class对外提供接口

单元测试

### Class Task1

提供Task1所需的功能。

变量：

```
存储结构：BinarySearchST st;
存储路径：private String fileDir;
```

构造方法：

```
不存储到磁盘仅在内存中放置内容的构造函数，需要数组长度（为空则给定数组长度）
public Task1()
public Task1(int num) 

读取磁盘上已有文件的构造函数，需要文件路径
public Task1(String fileDir)

向磁盘写入新文件的构造函数，需要数组长度和文件路径
public Task1(int num, String fileDir) 
```

方法：

```
给定value，向存储中添加对应的key-value
其中调用BinarySearchST的方法和saveObject的方法进行具体实现
public String add(GitObject f) 

给定key，查找得到对应的value值
其中调用BinarySearchST的方法进行具体实现
public GitObject get(String key)
```

#### Class BinarySearchST

Task1的具体实现，未完成存储到磁盘的功能。实现了Serializable接口，以便被存到磁盘。

变量：

```
用数组进行key-value顺序存储，n记录当前位置，maxN记录数组最大长度
private String[] key;
private GitObject[] value;
private int n;
private int maxN;

实现Serializable接口需要的ID
private static final long serialVersionUID =9876543210123L;
```

构造方法：

```
规定数组长度的构造
public BinarySearchST(int num)
```

方法：

```
向存储中添加对应的key-value的具体实现
public boolean add(String key, GitObject f)
	存储时排序
	private void sort()
		用来比较
		public static boolean less(Comparable v, Comparable w)
		交换
		public static void exch(Object[] a, int i, int j)
  
根据key找相应的value
public GitObject find(String key)
	二分查找
    public int rank(String key)
```

#### Class saveObject

用于完成以二进制方式存储到磁盘的功能。

方法：

```
写入文件，并打印出是否写入成功
public static void writeObjectToFile(String fileDir,Object obj)

读取文件，并打印出是否读取成功
public static Object readObjectFromFile(String fileDir)
```

#### Class GitObject

抽象类，待存储的对象。共同点为均有key和value，但value内容不同，key的算法不同。实现 Serializable和Comparable接口。

变量：

```
该对象的哈希值
private String key;

实现Serializable接口需要的ID（以便被存到磁盘）
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
```

##### Class BlobObject

待存储的Blob（文件）对象，GitObject为父类。

变量：

```
文件名和文件，在构造时确定不再改变
private final String fileName;
private final File file;
```

构造方法：

```
只给文件，则自动获取文件名，调用以下一种构造
public BlobObject(File file)

给出文件名和文件的构造，构造同时设置key
public BlobObject(String fileName, File file)
```

方法：

```
BlobObject的计算key值方法，调用CalcHash进行具体运算
public String calcKey() 

文件名的get方法
public String getFileName() 
文件的get方法
public File getFile() 
```

##### Class TreeObject

待存储的Tree（文件夹）对象，GitObject为父类。TreeObject的key计算，包含文件夹内的子文件的名称、子文件夹的名称；包含每个子文件blob的key；包含每个子文件夹tree的key。

变量：

```
文件夹路径名、其中的子文件与子文件夹
private final String dirName;
private final BlobObject[] blobs;
private final TreeObject[] trees;
```

构造方法：

```
传入文件夹路径、遍历后得到的子文件与子文件夹进行构造，构造同时设置key
public TreeObject(String dirName, BlobObject[] blobs, TreeObject[] trees)
```

方法：

```
TreeObject的计算key值方法，调用快速排序Quick使传入内容有序，调用CalcHash进行具体运算
public String calcKey()
	class Quick

文件夹路径的get方法
public String getDirName() 
子文件的get方法
public BlobObject[] getBlobs()
子文件夹的get方法
public TreeObject[] getTrees()
```

##### Class CommitObject

待存储的Commit提交对象，GitObject为父类。CommitObject的key计算，包含根目录tree对象的key，包含前一次commit的key（若是第一次commit就不包含）。

变量：

```
根目录tree对象和上一次的commit对象
private final TreeObject rootTree;
private final CommitObject lastCommit;
```

构造方法：

```
构造时传入根目录tree对象和前一次commit对象，构造同时设置key
public CommitObject(TreeObject rootTree, CommitObject lastCommit)
```

方法：

```
CommitObject的计算key值方法，调用CalcHash进行具体运算
public String calcKey() 
```

#### Class CalcHash

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

获取String类型的40位哈希值
public String getHash()
```