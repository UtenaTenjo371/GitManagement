package HWTest;

import java.io.File;
import java.io.IOException;
import java.util.Vector;

public class VersionController {
    private final String path; // 文件夹路径
    private String head; // head指向工作区的branch分支
    // 测试用路径
    // private String savePath="F:\\20Java\\testgit\\.mygit";
    private String savePath=".mygit";
    private GitLog gitlog;
    private Stage stage;

    public String getPath() {
        return path;
    }

    public String getHead() {
        return head;
    }

    /**构造方法*/
    public VersionController(String path) throws IOException {
        this.path=path;
        this.head="main";
        this.gitlog = new GitLog();
        this.stage = new Stage();
    }

    /**添加文件到暂存区*/
    public void addToStage(String fileName) throws IOException {
        File file = new File(path+File.separator+fileName);
        // 对文件的处理
        if(file.isFile()){
            BlobObject blobObject = new BlobObject(file);
            stage.addToIndex(blobObject.getKey());
            System.out.println("Add "+fileName+" to stage");
        }
        // 对文件夹的处理
        // 支持将根目录下的文件夹添加到暂存区，所选文件夹下的所有文件将被添加到暂存区
        else{
            TreeObject treeObject = ConvertFolder.genTree(path+File.separator+fileName, stage);
            stage.addToIndex(treeObject.getKey());
            System.out.println("Add "+fileName+" to stage");
        }
    }

    /**更新分支commit*/
    public void addCommit(String commitMessage) throws IOException {
        if(!checkIfRepository()){
            System.out.println("not a git repository");
            return;
        }
        TreeObject tree=ConvertFolder.dfs(path, stage, 0);
        //String类型compareTo方法，返回两个string相差的ascii码
        Branch current=ObjectStore.getBranch(this.head);
        if(current.getLatestCommit()==null){
            CommitObject commit=new CommitObject(tree);
            commit.setComment(commitMessage);
            commit.save();
            current.setLatestCommit(commit);
            current.save();
            gitlog.add(commit);
            System.out.println("add commit successfully"+" "+commit.getKey());
        }
        else if(current.getLatestCommit()!=null && tree.compareTo(current.getLatestCommit().getRootTree())!=0){
            CommitObject commit=new CommitObject(tree,current.getLatestCommit());
            commit.setComment(commitMessage);
            commit.save();
            current.setLatestCommit(commit);
            current.save();
            gitlog.add(commit);
            System.out.println("add commit successfully"+" "+commit.getKey());
        }
        else{
            System.out.println("No data changes");
        }
    }

    /**判断仓库是否存在*/
    public boolean isRepository(){
        File reposDir= new File(savePath);
        if (!reposDir.isDirectory()){
            return false;
        }
        else{
            return true;
        }
    }

    /**执行git操作前，判断当前路径是否为仓库，并做相应的准备工作*/
    public boolean checkIfRepository(){
        //当该路径无head文件时，返回仓库不存在提示；有head文件时，更新head
        if(!isRepository()){
            System.out.println("not a git repository");
            return false;
        }
        else {
            this.head=ObjectStore.getHead();
            return true;
        }
    }

    /**创建仓库*/
    public void initRepository() throws IOException {
        if(!isRepository()){
            Branch b=new Branch();
            b.save();
            ObjectStore.saveHead(this.head);
            gitlog.iniGitLog();
            stage.initStage();
            System.out.println("initialized a repository");
        }
        else{
            System.out.println("repository already exists");
        }
    }

    /**打印commit日志*/
    public void printLog(){
        System.out.println(gitlog.getAll());
    }

    /**回溯到commit*/
    public void resetCommit(String commitKey) throws IOException {
        if(!checkIfRepository()){
            System.out.println("not a git repository");
            return;
        }
        Branch branch = ObjectStore.getBranch(head);
        branch.setLatestCommit(commitKey);
        gitlog.updateLogAfterReset(commitKey);
        // --hard
        changeToCommit(commitKey);
    }

    /**创建分支*/
    public void createBranch(String branchName) throws IOException {
        if(!checkIfRepository()){
            System.out.println("not a git repository");
            return;
        }
        if(ObjectStore.isBranch(branchName)){
            System.out.print("branch already exists");
            return;
        }
        Branch branch= new Branch(branchName,ObjectStore.getBranch(head).getLatestCommit());
        branch.save();
        switchToBranch(branchName);
    }

    /**切换分支*/
    public void switchToBranch(String branchName) throws IOException {
        if(!checkIfRepository()){
            System.out.println("not a git repository");
            return;
        }
        gitlog.switchBranchLog(head, branchName);
        head=branchName;
        Branch target=ObjectStore.getBranch(branchName);
        ObjectStore.saveHead(head);
        changeToCommit(target.getCommitHash());
    }

    /**打印分支*/
    public void printBranch(){
        if(!checkIfRepository()){
            System.out.println("not a git repository");
            return;
        }
        Vector<Branch> branches=ObjectStore.getAllBranch();
        for(int i=0;i<branches.size();i++) {
            if(branches.get(i).getBranchName().equals(ObjectStore.getHead())){
                System.out.print("*");
            }else{
                System.out.print("-");
            }
            System.out.println(branches.get(i).getBranchName());
        }
    }
    /**删除分支*/
    public boolean deleteBranch(String branchName){
        if(branchName=="main") return false;
        return ObjectStore.deleteBranch(branchName);
    }

    /**恢复文件到指定版本*/
    public void changeToCommit(String cHash) throws IOException {
        CommitObject commit=ObjectStore.getCommit(cHash);
        ConvertFolder.changeFile(path,commit.getRootTree(), stage, 0);
    }

    /**合并分支*/
    public void mergeBranch(Branch branch1,Branch branch2){

    }

    /**支持命令行操作*/
    public static void main(String[] args) throws IOException {
        if(args.length < 1)
            return;

        // VersionController versionController = new VersionController(System.getProperty("user.dir"));
        VersionController versionController = new VersionController("F:\\20Java\\testgit");

        switch(args[0]){
            // git init
            case "init" :
                versionController.initRepository();
                break;
            // git add [filename]
            case "add" :
                if(args.length < 2)
                    System.out.println("invalid input");
                else
                    versionController.addToStage(args[1]);
                break;
            // git branch
            // git branch [branchname]
            // git branch -d [branchname]
            case "branch" :
                if(args.length == 1)
                    versionController.printBranch();
                else if (args.length == 2)
                    versionController.createBranch(args[1]);
                else if(args.length == 3 && args[1].equals("-d"))
                    versionController.deleteBranch(args[2]);
                else
                    System.out.println("invalid input");
                break;
            // git checkout [branchname]
            case "checkout" :
                if(args.length < 2)
                    System.out.println("invalid input");
                else
                    versionController.switchToBranch(args[1]);
            // git commit -m "Commit message"
            case "commit" :
                if(args.length < 3)
                    System.out.println("invalid input");
                else
                    versionController.addCommit(args[2]);
                break;
            // git log
            case "log" :
                versionController.printLog();
                break;
            // git reset [hashkey]
            case "reset" :
                if(args.length < 2)
                    System.out.println("invalid input");
                else
                    versionController.resetCommit(args[1]);
                break;
            default :
                return;
        }
    }
}
