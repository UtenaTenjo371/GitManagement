package HWTest;

import java.util.Vector;

public class TreeObject extends GitObject{
    private final String dirName;
    private final BlobObject[] blobs;
    private final TreeObject[] trees;

    //传Vector的构造方法
    public TreeObject(String dirName, Vector<BlobObject> vb, Vector<TreeObject> vt){
        BlobObject[] blobs = new BlobObject[vb.size()];
        TreeObject[] trees = new TreeObject[vt.size()];
        vb.toArray(blobs);
        vt.toArray(trees);
        this.dirName = dirName;
        this.blobs = blobs;
        this.trees = trees;
        updateKey(); // 设置key
    }

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


}