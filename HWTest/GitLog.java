package HWTest;

import java.io.*;
import java.util.Date;

public class GitLog {
    /*
    现有命令行交互：
    每条commit都对应一条log
    commit hash
    author
    date(日期与时区)
    提交时的注释
     */

    private static final String rootDir="F:\\20Java\\testgit\\.mygit\\logs";
    // private static final String rootDir=".mygit\\logs";
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
/*
由于根据最初的ppt中要求，只需进行命令行查看commit历史，因此当前直接使用logs下的HEAD文件存储当前分支下的commit记录。
实际上.git的log里HEAD文件是存储所有的操作记录日志的，包括创建、切换分支等等。目前暂未实现。

    public String add(Branch br){
        Date dateCreated=new Date();
        //从1970年到现在的秒数
        long millsec = dateCreated.getTime();
        //记录当前branch的key
        String hash = br.getCommitHash();
        //记录注释
        String comment = "Reset to "+br.getBranchName();
        String log = hash+" "+hash+" "+millsec+" branch: "+comment;
        SaveString.writeStringToFile(logDir, log);
        return log;
    }

    public String add(String currenthead, Branch br){
        Date dateCreated=new Date();
        //从1970年到现在的秒数
        long millsec = dateCreated.getTime();
        //记录当前branch的key
        String currenthash= ObjectStore.getBranch(currenthead).getCommitHash();
        //记录切换后branch的key
        String hash = br.getCommitHash();
        //记录注释
        String comment = "Reset to "+br.getBranchName();
        String log = hash+" "+hash+" "+millsec+" branch: "+comment;
        SaveString.writeStringToFile(logDir, log);
        return log;
    }

 */

    public String get() {
        return SaveString.readStringFromFile(logDir, -1);
    }

    public String getAll(){
        return SaveString.readStringFromFile(logDir, 0);
    }

    public String getBranchAll(String branchName){
        String branchLogPath = rootDir + "\\refs\\heads\\" + branchName;
        return SaveString.readStringFromFile(branchLogPath, 0);
    }

    /**切换分支时，切换log*/
    public void switchBranchLog(String currentHead, String desBranchName) throws IOException {
        String branchLogPath = rootDir + "\\refs\\heads\\" + currentHead;
        String currentHeadLog = get();
        SaveString.overwriteStringToFile(branchLogPath, currentHeadLog);
        File file = new File(desBranchName);
        if(file.isFile())
            file.createNewFile();
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

