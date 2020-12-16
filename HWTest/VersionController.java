package HWTest;

public class VersionController {
    private final String path;
    private final String sPath;
    private final Task1 store;
    private Branch head;


    public String getPath() {
        return path;
    }

    public Branch getHead() {
        return head;
    }

    public void setHead(Branch head) {
        this.head = head;
    }

    //构造时创建main分支(是否需要改成static方法？)
    public VersionController(String path){
        this.path=path;
        this.sPath=path+"\\gitSaving";
        System.out.println(sPath);
        this.store=new Task1(20,sPath);
        ConvertFolder convertFolder = new ConvertFolder();
        TreeObject initTree=convertFolder.dfs(path, store);
        this.head=new Branch("main",new CommitObject(initTree,null));
    }
    /**更新分支commit*/
    public Branch updateHead(TreeObject tree,Task1 store){
        //String类型compareTo方法，返回两个string相差的ascii码
        if(head.getLatestCommit()!=null && tree.compareTo(head.getLatestCommit().getRootTree())!=0){
            CommitObject commit=new CommitObject(tree,head.getLatestCommit());
            head.setLatestCommit(commit);
            //存储新的commit，之后应添加存储branch,head也要存储
            store.add(commit);
        }
        return head;
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
