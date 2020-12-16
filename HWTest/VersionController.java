package HWTest;

public class VersionController {
    private Branch head;

    public Branch getHead() {
        return head;
    }

    public void setHead(Branch head) {
        this.head = head;
    }

    //构造时创建main分支(是否需要改成static方法？)
    public VersionController(){
        this.head=new Branch("main",null);
    }
    /**更新分支commit*/
    public void updateHead(TreeObject tree){
        //String类型compareTO方法，返回两个string相差的ascii码
        if(tree.compareTo(head.getLatestCommit().getRootTree())!=0){
            CommitObject commit=new CommitObject(tree,head.getLatestCommit());
            head.setLatestCommit(commit);
        }
    }

    //以下是待实现的方法
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
