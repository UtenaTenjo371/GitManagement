package HWTest;

import java.io.File;
import java.io.Serializable;
import java.util.Vector;

public class ObjectStore {

    private static final String rootDir=".mygit";

    //构造函数，在当前目录的给定路径下存储各个objects
    public ObjectStore(){
        File dirFile = new File(rootDir);
        if (!dirFile.isDirectory()){
            dirFile.mkdirs();
        }
    }
    //对tree,blob进行操作
    /**存储tree,blob*/
    public static String add(GitObject f) {
        String hash = f.getKey();
        //模仿git的存法，上层路径名是hash前两位，文件名是hash后38位
        String dirname = hash.substring(0,2);
        String filename = hash.substring(2);
        //根据gitObject类型，存储到不同位置
        File dirFile = new File(rootDir+"\\objects\\"+dirname);
        if (!dirFile.isDirectory()){
            dirFile.mkdirs();
        }
        SaveObject.writeObjectToFile(dirFile.getPath()+"\\"+filename,f);
        return hash;
    }
    /**获得tree,blob*/
    public static GitObject get(String key) {
        String dirname = key.substring(0,2);
        String filename = key.substring(2);
        File dirFile = new File(rootDir+"\\objects\\"+dirname);
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
    //对branch进行操作
    /**存储branch*/
    public static String saveBranch(Branch b) {
        String filename = b.getBranchName();
        String hash=b.getCommitHash();
        File dirFile = new File(rootDir+"\\refs");
        if (!dirFile.isDirectory()){
            dirFile.mkdirs();
        }
        SaveObject.writeObjectToFile(dirFile.getPath()+"\\"+filename,b);
        return hash;
    }
    /**获取branch*/
    public static Branch getBranch(String bName){
        File dirFile = new File(rootDir+"\\refs\\"+bName);
        if (!dirFile.isFile()){
            return null;
        }
        return (Branch)SaveObject.readObjectFromFile(dirFile.getPath());
    }
    /**判断branch是否存在*/
    public static boolean isBranch(String bName){
        File dirFile = new File(rootDir+"\\refs\\"+bName);
        if (!dirFile.isFile()){
            return false;
        }
        return true;
    }
    /**存储head*/
    public static String saveHead(String head) {
        //String headName=head.getBranchName();
        File dirFile = new File(rootDir);
        if (!dirFile.isDirectory()){
            dirFile.mkdir();
        }
        SaveObject.writeObjectToFile(dirFile.getPath()+"\\HEAD",head);
        return head;
    }
    /**获得head*/
    public static String getHead(){
        File dirFile = new File(rootDir+"\\HEAD");
        if (!dirFile.isFile()){
            return null;
        }
        String branch=(String)SaveObject.readObjectFromFile(dirFile.getPath());
        return branch;
    }
    /**获得所有branch*/
    public static Vector<Branch> getAllBranch(){
        Vector<Branch> br=new Vector<Branch>();
        File dir = new File(rootDir+"\\refs");
        File[] fs = dir.listFiles();
        for(int i=0;i<fs.length;i++) {
            br.addElement((Branch)SaveObject.readObjectFromFile(fs[i].getPath()));
        }
        return br;
    }
}

