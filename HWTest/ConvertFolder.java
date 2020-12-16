package HWTest;

import java.io.File;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Scanner;
import java.util.Vector;

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

    public TreeObject dfs(String path, Task1 a){
        File dir = new File(path);
        File[] fs = dir.listFiles();
        Arrays.sort(fs,comparatorFile);
        Vector<BlobObject> blobs = new Vector<>();
        Vector<TreeObject> trees = new Vector<>();
        for(int i=0;i<fs.length;i++){
            if(fs[i].isFile()){
                BlobObject blob = new BlobObject(fs[i]);
                a.add(blob);
                blobs.add(blob);
            }
            if(fs[i].isDirectory()){
                TreeObject tree = dfs(path+File.separator+fs[i].getName(), a);
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
        TreeObject tree = new TreeObject(dir.getName(), blobs_array, trees_array);
        a.add(tree);
        return tree;
    }

    public static void main(String[] args){
        System.out.println("请输入一个文件夹的绝对路径：");
        Scanner sc = new Scanner(System.in);
        String path = sc.nextLine();
        sc.close();
        String sPath= path+"\\gitSaving";

        VersionController vc=new VersionController(path);
        Task1 a = new Task1(20,vc.getPath());
        ConvertFolder convertFolder = new ConvertFolder();
        TreeObject tree = convertFolder.dfs(path, a);

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
        Branch newHead=vc.updateHead(tree,a);
        System.out.println("------------");
        System.out.println(newHead.getBranchName()+" "+newHead.getKey());
    }
}