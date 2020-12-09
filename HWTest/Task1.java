package HWTest;

import java.security.MessageDigest;
import java.io.File;
import java.io.FileInputStream;

public class Task1 {
    private BinarySearchST st;

    public Task1() {
        this.st = new BinarySearchST(20);
    }

    public String add(GitObject f) {
        String hash = f.getKey();
        st.add(hash, f);
        return hash;
    }

    public GitObject get(String key) {
        return st.find(key);
    }
//main用来测试，运行看起来应该是可以的
    public static void main(String[] args) {
        String dir1 = "E:\\IMG_9726.jpg";
        String dir2 = "E:\\IMG_9727.jpg";
        File f1 = new File(dir1);
        File f2 = new File(dir2);
        BlobObject[] blobs = new BlobObject[2];
        blobs[0] = new BlobObject(f1);
        blobs[1] = new BlobObject(f2);
        TreeObject[] trees = new TreeObject[0];
        TreeObject tree = new TreeObject("10401206", blobs, trees);

        Task1 a = new Task1();

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
    }
}
//----------------BinarySearchST-------------------
class BinarySearchST {
    // 0:小 -> n-1:大
    //用n标记当前位置
    private String[] key;
    private GitObject[] value;
    private int n;
    private int maxN;

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

//---------------- Git Object -------------------
abstract class GitObject implements Comparable<GitObject>{
    private String key;

    public void updateKey(){
        this.key = calcKey();
    }

    public String getKey() {
        return key;
    }

    public abstract String calcKey();

    public int compareTo(GitObject gitObject) {
        return key.compareTo(gitObject.key);
    }
}
//Blob
class BlobObject extends GitObject{
    private final String fileName;
    private final File file;

    public BlobObject(File file){
        this(file.getName(), file);
    }

    public BlobObject(String fileName, File file){
        this.file = file;
        this.fileName = fileName;
        updateKey(); // 设置key
    }

    public String calcKey() {
        CalcHash ch = new CalcHash();
        ch.addString(fileName);
        ch.addFile(file);
        return ch.getHash();
    }

    public String getFileName() {
        return fileName;
    }

    public File getFile() {
        return file;
    }
}
//Tree
class TreeObject extends GitObject{
    private final String dirName;
    private final BlobObject[] blobs;
    private final TreeObject[] trees;

    public TreeObject(String dirName, BlobObject[] blobs, TreeObject[] trees){
        this.dirName = dirName;
        this.blobs = blobs;
        this.trees = trees;
        updateKey(); // 设置key
    }

    public String calcKey() {
        // 1 排序
        Quick.sort(blobs);
        Quick.sort(trees);

        // 2 UPDATE HASH
        CalcHash ch = new CalcHash();
        for(BlobObject bo : blobs){
            ch.addString(bo.getFileName());
            ch.addString(bo.getKey());
        }

        for(TreeObject bo : trees){
            ch.addString(bo.getDirName());
            ch.addString(bo.getKey());
        }

        return ch.getHash();
    }

    public String getDirName() {
        return dirName;
    }

    public BlobObject[] getBlobs() {
        return blobs;
    }

    public TreeObject[] getTrees() {
        return trees;
    }
}

/*一段普普通通的快排*/
class Quick {

    public static void sort(Comparable[] a){
        // StdRandom.shuffle(a);        //消除对输入的依赖
        sort(a, 0, a.length - 1);
    }

    public static void sort(Comparable[] a, int lo, int hi){
        if(hi <= lo) return;
        int j = partition(a, lo, hi);
        sort(a, lo, j-1);
        sort(a, j+1, hi);
    }

    public static int partition(Comparable[] a, int lo, int hi){
        int i = lo, j = hi+1;
        Comparable v = a[lo];
        while(true){
            while(less(a[++i], v)) if(i == hi) break;
            while(less(v, a[--j])) if(j == lo) break;
            if(i >= j) break;
            exch(a, i, j);
        }
        exch(a, lo, j);
        return j;
    }

    public static boolean less(Comparable v, Comparable w){
        return v.compareTo(w) < 0;
    }

    public static void exch(Comparable[] a, int i, int j){
        Comparable t = a[i]; a[i] = a[j]; a[j] = t;
    }


    public static void main(String[] args){
        Integer[] a = {3,6,9,5,2,3};
        sort(a);
        for(int i=0; i<a.length; i++){
            System.out.print(a[i] + " ");
        }
    }

}
//Commit
class CommitObject extends GitObject{
    private final TreeObject rootTree;
    private final CommitObject lastCommit;

    public CommitObject(TreeObject rootTree, CommitObject lastCommit){
        this.rootTree = rootTree;
        this.lastCommit = lastCommit;
        updateKey(); // 设置key
    }

    public String calcKey() {
        CalcHash ch = new CalcHash();

        ch.addString(rootTree.getKey());

        if(lastCommit != null){
            ch.addString(lastCommit.getKey());
        }

        return ch.getHash();
    }

}



// ------------- calc Hash ------------------
class CalcHash {
    private MessageDigest complete;

    public CalcHash() {
        try {
            complete = MessageDigest.getInstance("SHA-1");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addFile(File f) {
        try {
            FileInputStream is = new FileInputStream(f);
            // 这里将PPT5中 SHA1Checksum 的程序提出来，不作为子程序运行
            byte[] buffer = new byte[1024];
            int numRead = 0;
            do {
                numRead = is.read(buffer);
                if (numRead > 0) {
                    complete.update(buffer, 0, numRead);
                }
            } while (numRead != -1);
            is.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addString(String s) {
        complete.update(s.getBytes());
    }

    public String getHash() {
        byte[] hash = complete.digest();
        StringBuilder resultStr = new StringBuilder("");
        for (int i = 0; i < hash.length; i++) {
            resultStr.append(Integer.toString((hash[i] >> 4) & 0x0F, 16));
            resultStr.append(Integer.toString(hash[i] & 0x0F, 16));
            //控制输出位数
        }

        return resultStr.toString();
    }
}
