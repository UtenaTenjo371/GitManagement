package HWTest;

import java.io.File;
import java.io.Serializable;

public class ObjectStore {

    private static final String rootDir=".mygit\\objects";

    //构造函数，在当前目录的给定路径下存储各个objects
    public ObjectStore(){
        File dirFile = new File(rootDir);
        if (!dirFile.isDirectory()){
            dirFile.mkdirs();
        }
    }

    public static String add(GitObject f) {
        String hash = f.getKey();
        //模仿git的存法，上层路径名是hash前两位，文件名是hash后38位
        String dirname = hash.substring(0,2);
        String filename = hash.substring(2);
        File dirFile = new File(rootDir+"\\"+dirname);
        if (!dirFile.isDirectory()){
            dirFile.mkdir();
        }
        SaveObject.writeObjectToFile(dirFile.getPath()+"\\"+filename,f);
        return hash;
    }

    public static GitObject get(String key) {
        String dirname = key.substring(0,2);
        String filename = key.substring(2);
        File dirFile = new File(rootDir+"\\"+dirname);
        if (!dirFile.isDirectory()){
            return null;
        }
        return (GitObject)SaveObject.readObjectFromFile(dirFile.getPath()+"\\"+filename);
    }

    //对应返回不同种类的GitObject的get方法
    public static BlobObject getBlob(String key){
        return (BlobObject)get(key);
    }

    public static TreeObject getTree(String key){
        return (TreeObject)get(key);
    }

    public static CommitObject getCommit(String key){
        return (CommitObject)get(key);
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

        ObjectStore a = new ObjectStore();

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


        ObjectStore b=new ObjectStore();
        GitObject gto3 = b.get(hash1);
        System.out.println(gto3.getKey());

    }
}

