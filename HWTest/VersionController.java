package HWTest;

import com.sun.source.tree.Tree;

import java.io.File;
import java.util.Vector;

public class VersionController {
    private final String path;//文件夹路径
    private String head;//head指向工作区的branch分支
    private String savePath=".mygit";

    public String getPath() {
        return path;
    }

    public String getHead() {
        return head;
    }

    public VersionController(String path){
        this.path=path;
        this.head="main";
        //当该路径无head文件时，创建仓库；有head文件时，更新head
        if(!isRepository()){
            System.out.println("not a git repository");
            initRepository();
        }
        else{
            this.head=ObjectStore.getHead();
        }
    }
    /**更新分支commit*/
    public Branch addCommit(){
        TreeObject tree=ConvertFolder.dfs(path);
        //String类型compareTo方法，返回两个string相差的ascii码
        Branch current=ObjectStore.getBranch(this.head);
        if(current.getLatestCommit()==null){
            CommitObject commit=new CommitObject(tree);
            commit.save();
            current.setLatestCommit(commit);
            current.save();
            System.out.println("add commit successfully"+" "+commit.getKey());
        }
        else if(current.getLatestCommit()!=null && tree.compareTo(current.getLatestCommit().getRootTree())!=0){
            CommitObject commit=new CommitObject(tree,current.getLatestCommit());
            commit.save();
            current.setLatestCommit(commit);
            current.save();
            System.out.println("add commit successfully"+" "+commit.getKey());
        }
        else{
            System.out.println("No data changes");
        }
        return current;
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
    /**创建仓库*/
    public void initRepository(){
        Branch b=new Branch();
        b.save();
        ObjectStore.saveHead(this.head);
    }

    /**打印commit日志*/
    public void printLog(){

    }

    /**回溯到commit*/
    public void resetCommit(String commitKey){

    }

    /**创建分支*/
    public void createBranch(String branchName){
        Branch branch= new Branch(branchName,ObjectStore.getBranch(head).getLatestCommit());
        branch.save();
    }

    /**切换分支*/
    public void switchToBranch(String branchName){
        head=branchName;
        Branch target=ObjectStore.getBranch(branchName);
        ObjectStore.saveHead(head);
        changeFile(target.getCommitHash());
    }

    /**打印分支*/
    public void printBranch(){
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

    /**合并分支*/
    public void mergeBranch(Branch branch1,Branch branch2){

    }
    /**恢复文件到指定版本*/
    public void changeFile(String cHash){
        System.out.println("ChangeFile");
    }
}
