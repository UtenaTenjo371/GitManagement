package HWTest;

import java.util.Vector;

public class TreeObject extends GitObject{
    private final String dirName;
    private final String[] blobs;
    private final String[] trees;
    // 为了内存考虑，每个TreeObject只存其中文件和文件夹的key，不再重复存文件。

    /**传Vector的构造方法*/
    public TreeObject(String dirName, Vector<BlobObject> vb, Vector<TreeObject> vt){
        BlobObject[] blobObjects = new BlobObject[vb.size()];
        TreeObject[] treeObjects = new TreeObject[vt.size()];
        vb.toArray(blobObjects);
        vt.toArray(treeObjects);
        this.dirName = dirName;
        this.blobs = new String[blobObjects.length];
        this.trees = new String[treeObjects.length];

        for (int i=0; i<blobObjects.length; i++){
            // 保证所存的blob哈希值对应的object能在存储中找到
            blobObjects[i].save();
            this.blobs[i] =blobObjects[i].getKey();
        }
        for (int i=0; i<treeObjects.length; i++){
            // 保证所存的tree哈希值对应的object能在存储中找到
            treeObjects[i].save();
            this.trees[i] =treeObjects[i].getKey();
        }
        updateKey(); // 设置key
    }

    /**数组的构造方法*/
    public TreeObject(String dirName, BlobObject[] blobs, TreeObject[] trees){
        this.dirName = dirName;
        this.blobs = new String[blobs.length];
        this.trees = new String[trees.length];

        for (int i=0; i<blobs.length; i++){
            // 保证所存的blob哈希值对应的object能在存储中找到
            blobs[i].save();
            this.blobs[i] =blobs[i].getKey();
        }
        for (int i=0; i<trees.length; i++){
            // 保证所存的tree哈希值对应的object能在存储中找到
            trees[i].save();
            this.trees[i] =trees[i].getKey();
        }
        updateKey(); // 设置key
    }

    public String getDirName() {
        return dirName;
    }

    public BlobObject[] getBlobs() {
        BlobObject[] bos = new BlobObject[blobs.length];
        for(int i=0; i<blobs.length; i++){
            bos[i] = ObjectStore.getBlob(blobs[i]);
        }
        return bos;
    }

    public TreeObject[] getTrees() {
        TreeObject[] trs = new TreeObject[trees.length];
        for(int i=0; i< trees.length; i++){
            trs[i] = ObjectStore.getTree(trees[i]);
        }
        return trs;
    }

    /**计算hash key*/
    public String calcKey() {
        // 1 排序
        Quick.sort(blobs);
        Quick.sort(trees);

        // 2 UPDATE HASH
        CalcHash ch = new CalcHash();

        for(int i=0; i<blobs.length; i++){
            BlobObject bo = ObjectStore.getBlob(blobs[i]);
            ch.addString(bo.getFileName());
            ch.addString(bo.getKey()); //这个其实也可以直接加blobs[i]
        }

        for(int i=0; i< trees.length; i++){
            TreeObject tr = ObjectStore.getTree(trees[i]);
            ch.addString(tr.getDirName());
            ch.addString(tr.getKey()); //这个其实也可以直接加trees[i]
        }

        return ch.getHash();
    }
}

/**一段普普通通的快排*/
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