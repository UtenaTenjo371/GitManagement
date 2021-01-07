package HWTest;

import java.io.*;
import java.util.Date;

public class GitLog {
    /*
    现有命令行交互：
    每条commit都有log
    commit hash
    author //这个我们没有
    date(日期与时区) //这个我不会译
    注释
     */
    //理论上还要实现branch,reset,checkout，不过现在还都没有

    private static final String rootDir=".mygit\\logs";
    private final String logDir = rootDir + "\\" + "HEAD";

    public GitLog(){}

    public void iniGitLog(){
        File file=new File(rootDir + "\\refs\\heads");
        if(!file.exists())//如果文件夹不存在
            file.mkdirs();//创建文件夹
    }

    public String add(CommitObject co) {
        Date dateCreated=new Date();
        //从1970年到现在的秒数
        long millsec = dateCreated.getTime();
        //记录当前commit的key和parent的key
        String hash = co.getKey();
        CommitObject parent = co.getParent();
        String parenthash = "0000000000000000000000000000000000000000";
        if(parent != null){
            parenthash = co.getParent().getKey();
        }

        //记录注释
        String comment = co.getComment();

        String log = parenthash+" "+hash+" "+millsec+" commit: "+comment;
        SaveString.writeStringToFile(logDir, log);
        return log;

    }

    public String get() {
        return SaveString.readStringFromFile(logDir, -1);
    }

    public String getAll(){
        //理论上好像得把日期翻译一下但我不会。。。。。
        return SaveString.readStringFromFile(logDir, 0);
    }

    public String getBranchAll(String branchName){
        String branchLogPath = rootDir + "\\refs\\heads\\" + branchName;
        return SaveString.readStringFromFile(branchLogPath, 0);
    }

    /**切换分区时，切换log*/
    public void switchBranchLog(String currentHead, String desBranchName){
        String branchLogPath = rootDir + "\\refs\\heads\\" + currentHead;
        String currentHeadLog = get();
        SaveString.overwriteStringToFile(branchLogPath, currentHeadLog);
        String src = getBranchAll(desBranchName);
        SaveString.overwriteStringToFile(logDir, src);
    }

    /**reset后，更新log*/
    public void updateLogAfterReset(String commitKey) throws IOException {
        SaveString.deleteContentAfterKeyword(logDir, commitKey);
    }

    public static void main(String[] args){
        ObjectStore a = new ObjectStore();

        String dir1 = "F:\\test.txt";
        String dir2 = "F:\\test1.txt";
        File f1 = new File(dir1);
        File f2 = new File(dir2);
        BlobObject[] blobs = new BlobObject[2];
        blobs[0] = new BlobObject(f1);
        blobs[1] = new BlobObject(f2);
        TreeObject[] trees = new TreeObject[0];
        TreeObject tree = new TreeObject("10401206", blobs, trees);
        TreeObject tree2 = new TreeObject("14141416", blobs, trees);

        CommitObject cm = new CommitObject(tree);
        CommitObject cm2 = new CommitObject(tree2, cm);
        cm.setComment("This is a test 1 commit.");
        cm2.setComment("Hello, World!");
        GitLog g = new GitLog();
        g.add(cm);
        g.add(cm2);
        String res = g.get();
        String resAll = g.getAll();
        System.out.println(res);
        System.out.println(resAll);
    }
}

