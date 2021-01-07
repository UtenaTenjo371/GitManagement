package HWTest;

import java.io.File;
import java.util.*;

class ConvertFolder{
    public static Comparator<File> comparatorFile =new Comparator <File>(){
        public int compare(File p1,File p2){
            if (p1.getName().compareTo(p2.getName())<0)
                return 1;
            else if (p1.getName().compareTo(p2.getName())>0)
                return -1;
            else
                return 0;
        }
    };
    //gitObject比较器
    public static Comparator<GitObject> comparatorObject =new Comparator <GitObject>(){
        public int compare(GitObject p1,GitObject p2){
            if (p1.getKey().compareTo(p2.getKey())<0)
                return 1;
            else if (p1.getKey().compareTo(p2.getKey())>0)
                return -1;
            else
                return 0;
        }
    };

    public static TreeObject dfs(String path){
        File dir = new File(path);
        File[] fs = dir.listFiles();
        Arrays.sort(fs,comparatorFile);
        Vector<BlobObject> blobs = new Vector<>();
        Vector<TreeObject> trees = new Vector<>();
        for(int i=0;i<fs.length;i++){
            if(fs[i].isFile()){
                BlobObject blob = new BlobObject(fs[i]);
                ObjectStore.add(blob);
                blobs.add(blob);
            }
            if(fs[i].isDirectory()){
                TreeObject tree = dfs(path+File.separator+fs[i].getName());
                trees.add(tree);
            }
        }
        BlobObject[] blobs_array = new BlobObject[blobs.size()];
        for(int j=0;j<blobs.size();j++){
            blobs_array[j] = blobs.get(j);
        }
        TreeObject[] trees_array = new TreeObject[trees.size()];
        for(int j=0;j<trees.size();j++){
            trees_array[j] = trees.get(j);
        }
        Arrays.sort(blobs_array,comparatorObject);
        Arrays.sort(trees_array,comparatorObject);
        TreeObject tree = new TreeObject(dir.getName(), blobs_array, trees_array);
        ObjectStore.add(tree);
        return tree;
    }
    /**获取工作区目录*/
    public static TreeObject changeFile(String path,TreeObject curTree){
        //获得当前commit中树的tree,blob,存成哈希表待恢复列表
        Map<String,TreeObject> treesKey=new HashMap<String,TreeObject>();
        Map<String,BlobObject> blobsKey=new HashMap<String,BlobObject>();
        for(TreeObject o:curTree.getTrees()){
            treesKey.put(o.getDirName(),o);
        }
        for(BlobObject o:curTree.getBlobs()){
            blobsKey.put(o.getKey(),o);
        }
        //遍历工作区,修改文件
        File dir = new File(path);
        File[] fs = dir.listFiles();
        Arrays.sort(fs,comparatorFile);
        Vector<BlobObject> blobs = new Vector<>();
        Vector<TreeObject> trees = new Vector<>();
        //删除多余blob,并在待恢复列表中删除已存在的blob
        for(int i=0;i<fs.length;i++){
            if(fs[i].isFile()){
                BlobObject blob = new   BlobObject(fs[i]);
                if(!blobsKey.containsKey(blob.getKey())){
                    deleteFolder(fs[i].getPath());
                }
                else{
                    blobsKey.remove(blob.getKey());
                }
            }
            if(fs[i].isDirectory()){
                String[] treeNames=fs[i].getPath().split("\\\\");
                String treeName=treeNames[treeNames.length-1];
                for(String s:treeNames){
                    System.out.println(s);
                }
                if(!treesKey.containsKey(treeName)){
                    deleteFolder(fs[i].getPath());
                }
                else{
                    TreeObject target=treesKey.get(treeName);
                    TreeObject tree = changeFile(path+File.separator+fs[i].getName(),target);
                    treesKey.remove(tree.getKey());
                    trees.add(tree);
                }
            }
        }
        //新增缺失的blob,加入Vector中
        for(BlobObject object:blobsKey.values()){
            object.restoreData(path);
            blobs.add(object);
        }
        //新增子文件夹
        for(TreeObject tree:treesKey.values()){
            File dirFile = new File(path+File.separator+tree.getDirName());
            if (!dirFile.isDirectory()){
                dirFile.mkdir();
            }
            TreeObject tr = changeFile(path+File.separator+tree.getDirName(),tree);
            trees.add(tree);
        }
        //返回递归上一层文件夹的object
        BlobObject[] blobs_array = new BlobObject[blobs.size()];
        for(int j=0;j<blobs.size();j++){
            blobs_array[j] = blobs.get(j);
        }
        TreeObject[] trees_array = new TreeObject[trees.size()];
        for(int j=0;j<trees.size();j++){
            trees_array[j] = trees.get(j);
        }
        //对tree，进行排序，保证哈希值的稳定
        Arrays.sort(blobs_array,comparatorObject);
        Arrays.sort(trees_array,comparatorObject);
        TreeObject tree = new TreeObject(dir.getName(), blobs_array, trees_array);
        return tree;
    }
    /**删除文件路径*/
    public static boolean deleteFolder(String path){
        boolean flag = false;
        File file = new File(path);
        // 判断目录或文件是否存在
        if (!file.exists()) {  // 不存在返回 false
            return flag;
        } else {
            // 判断是否为文件
            if (file.isFile()) {  // 为文件时调用删除文件方法
                return deleteFile(path);
            } else {  // 为目录时调用删除目录方法
                return deleteDirectory(path);
            }
        }
    }
    /**删除文件*/
    public static boolean deleteFile(String sPath) {
        boolean flag = false;
        File file = new File(sPath);
        // 路径为文件且不为空则进行删除
        if (file.isFile() && file.exists()) {
            file.delete();
            flag = true;
        }
        return flag;
    }
    /**删除文件夹*/
    public static boolean deleteDirectory(String sPath) {
        //如果sPath不以文件分隔符结尾，自动添加文件分隔符
        if (!sPath.endsWith(File.separator)) {
            sPath = sPath + File.separator;
        }
        File dirFile = new File(sPath);
        //如果dir对应的文件不存在，或者不是一个目录，则退出
        if (!dirFile.exists() || !dirFile.isDirectory()) {
            return false;
        }
        boolean flag = true;
        //删除文件夹下的所有文件(包括子目录)
        File[] files = dirFile.listFiles();
        for (int i = 0; i < files.length; i++) {
            //删除子文件
            if (files[i].isFile()) {
                flag = deleteFile(files[i].getAbsolutePath());
                if (!flag) break;
            } //删除子目录
            else {
                flag = deleteDirectory(files[i].getAbsolutePath());
                if (!flag) break;
            }
        }
        if (!flag) return false;
        //删除当前目录
        if (dirFile.delete()) {
            return true;
        } else {
            return false;
        }
    }
    public static void main(String[] args){
        System.out.println("请输入一个文件夹的绝对路径：");
        Scanner sc = new Scanner(System.in);
        String path = sc.nextLine();
        sc.close();

        VersionController vc=new VersionController(path);
        ObjectStore a = new ObjectStore();
        ConvertFolder convertFolder = new ConvertFolder();
        TreeObject tree = convertFolder.dfs(path);

        System.out.println(tree.getDirName());
        System.out.println("------------");
        System.out.println("Blobs:");
        BlobObject[] blobs = tree.getBlobs();
        for(int i=0; i<blobs.length; i++){
            System.out.println(blobs[i].getFileName());
        }
        System.out.println("------------");
        System.out.println("Trees:");
        TreeObject[] trees = tree.getTrees();
        for(int i=0; i<trees.length; i++){
            System.out.println(trees[i].getDirName());
        }
        //Commit测试
        /*Branch newHead=vc.addCommit();
        System.out.println("------------");
        System.out.println(newHead.getBranchName()+" "+newHead);*/
    }
}