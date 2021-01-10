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

    // 测试用路径
    // private static final String rootDir="F:\\20Java\\testgit\\.mygit\\logs";
    private static final String rootDir=".mygit\\logs";
    private final String logDir = rootDir + "\\" + "HEAD";

    public GitLog(){}

    /**初始化，建立相应目录*/
    public void iniGitLog(){
        File file=new File(rootDir + "\\refs\\heads");
        if(!file.exists())//如果文件夹不存在
            file.mkdirs();//创建文件夹
    }

    /**向log中添加一条commit记录*/
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

    /**获取日志的最新一条记录*/
    public String get() {
        return SaveString.readStringFromFile(logDir, -1);
    }

    /**获取日志的所有记录*/
    public String getAll(){
        return SaveString.readStringFromFile(logDir, 0);
    }

    /**获取当前分支的日志记录*/
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
}

