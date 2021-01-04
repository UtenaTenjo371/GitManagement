package HWTest;

import java.io.File;

public class VersionController {
    private final String path;//文件夹路径
    private Branch head;//head指向工作区的branch分支
    private String savePath=".mygit";

    public String getPath() {
        return path;
    }

    public Branch getHead() {
        return head;
    }

    public void setHead(Branch head) {
        this.head = head;
    }

    public VersionController(String path){
        this.path=path;
        this.head=null;
        //当该路径无head文件时，报错；有head文件时，更新head
        if(!isRepository()){
            System.out.println("not a git repository");
            initRepository();
        }
        else{
            this.head=ObjectStore.getHead();
        }
    }
    /**更新分支commit*/
    public Branch updateHead(TreeObject tree, ObjectStore store){
        //String类型compareTo方法，返回两个string相差的ascii码
        if(head.getLatestCommit()!=null && tree.compareTo(head.getLatestCommit().getRootTree())!=0){
            CommitObject commit=new CommitObject(tree,head.getLatestCommit());
            head.setLatestCommit(commit);
            //存储新的commit，之后应添加存储branch,head也要存储
            store.add(commit);
        }
        return head;
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

    }

    /**切换分支*/
    public void switchToBranch(String branchName){

    }

    /**合并分支*/
    public void mergeBranch(Branch branch1,Branch branch2){

    }
}
