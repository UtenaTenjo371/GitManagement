package HWTest;

import java.io.File;
import java.io.Serializable;

public class ObjectStore {
    private BinarySearchST st;
    private String fileDir;

    //默认构造函数，如果不给文件路径就当做不存文件
    public ObjectStore(){
        this(20);
    }
    public ObjectStore(int num) {
        this.st = new BinarySearchST(num);
        this.fileDir= null;
    }

    //给了文件路径的构造函数，用来读取已有的文件目录
    public ObjectStore(String fileDir) {
        this.st = (BinarySearchST)SaveObject.readObjectFromFile(fileDir);
        this.fileDir =fileDir;
    }
    //给了数组长度与文件路径的构造函数，用来写入新的文件
    public ObjectStore(int num, String fileDir) {
        this(num);
        this.fileDir =fileDir;
    }

    public String add(GitObject f) {
        String hash = f.getKey();
        st.add(hash, f);
        if (fileDir != null){
            SaveObject.writeObjectToFile(fileDir, st);
        }
        return hash;
    }

    public GitObject get(String key) {
        return st.find(key);
    }
//main用来测试，运行看起来应该是可以的
    public static void main(String[] args) {
        String dir1 = "F:\\test.txt";
        String dir2 = "F:\\test1.txt";
        File f1 = new File(dir1);
        File f2 = new File(dir2);
        BlobObject[] blobs = new BlobObject[2];
        blobs[0] = new BlobObject(f1);
        blobs[1] = new BlobObject(f2);
        TreeObject[] trees = new TreeObject[0];
        TreeObject tree = new TreeObject("10401206", blobs, trees);

        ObjectStore a = new ObjectStore(20, "F:\\task1test");

        String hash1 = a.add(blobs[0]);
        String hash2 = a.add(blobs[1]);
        String hash3 = a.add(tree);

        GitObject gto1 = a.get(hash3);
        GitObject gto2 = a.get(hash2);

        System.out.println(hash1);
        System.out.println(hash2);
        System.out.println(hash3);
        System.out.println(gto1.getKey());
        System.out.println(gto2.getKey());
        System.out.println(a.st.find(hash1));

        ObjectStore b=new ObjectStore("F:\\task1test");
        GitObject gto3 = b.get(hash1);
        System.out.println(gto3.getKey());

    }
}
//----------------BinarySearchST-------------------
class BinarySearchST implements Serializable{
    // 0:小 -> n-1:大
    //用n标记当前位置
    private String[] key;
    private GitObject[] value;
    private int n;
    private int maxN;

    //Serializable要的ID
    private static final long serialVersionUID =9876543210123L;

    public BinarySearchST(int num){
        this.maxN = num;
        this.n = 0;
        this.key = new String[num];
        this.value = new GitObject[num];
    }
//根据value，向键值对里添加新的内容，如果已经有同样的key就不存
    public boolean add(String key, GitObject f){
        // 判断KEY和f是不是NULL
        if(key == null || f == null) return false;
        // 查找KEY是否已经存下
        if(find(key) != null) return false;
        // 检查n==maxN
        if(n >= maxN) return false;
        this.key[n] = key;
        this.value[n] = f;
        this.n++;
        sort();
        return true;
    }
//根据key找相应的value
    public GitObject find(String key){
        // 判断KEY是否为NULL
        if(key == null) return null;
        // 二分查找
        int index = rank(key);
        if (index==-1) return null;
        return value[index];
    }

    // Sort Relevant
    private void sort(){
        // 存储时依序便于查找
        int N = this.n;
        for(int i=0; i<N; i++){
            for(int j=i; j>0 && less(this.key[j], this.key[j-1]); j--){
                exch(this.key, j, j-1);
                exch(this.value, j, j-1);
            }
        }

    }
    //如果v<w就return true
    public static boolean less(Comparable v, Comparable w){
        return v.compareTo(w) < 0;
    }
    //交换a中的i与j
    public static void exch(Object[] a, int i, int j){
        Object t = a[i]; a[i] = a[j]; a[j] = t;
    }
    //二分查找
    public int rank(String key)
    {
        int lo = 0, hi = n-1;
        while(lo <= hi)
        {
            int mid = lo + (hi - lo) / 2;
            int cmp = key.compareTo(this.key[mid]);
            if(cmp < 0) hi = mid - 1;
            else if(cmp > 0) lo = mid + 1;
            else return mid;
        }
        return -1;
    }

}