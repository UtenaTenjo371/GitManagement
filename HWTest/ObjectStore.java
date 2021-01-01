package HWTest;

import java.io.File;
import java.io.Serializable;

public class ObjectStore {

    private final String rootDir;

    //默认构造函数，如果不给文件路径就我们建一个默认的存进去
    public ObjectStore(){
        this(".mygit\\objects");
    }

    //给了文件路径的构造函数，用来指定文件目录存储
    public ObjectStore(String rootDir) {
        File dirFile = new File(rootDir);
        if (!dirFile.isDirectory()){
            dirFile.mkdirs();
        }
        this.rootDir =rootDir;
    }

    public String add(GitObject f) {
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

    public GitObject get(String key) {
        String dirname = key.substring(0,2);
        String filename = key.substring(2);
        File dirFile = new File(rootDir+"\\"+dirname);
        if (!dirFile.isDirectory()){
            return null;
        }
        return (GitObject)SaveObject.readObjectFromFile(dirFile.getPath()+"\\"+filename);
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

        ObjectStore a = new ObjectStore("F:\\task1test");

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


        ObjectStore b=new ObjectStore("F:\\task1test");
        GitObject gto3 = b.get(hash1);
        System.out.println(gto3.getKey());

    }
}

