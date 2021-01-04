package HWTest;

public class Branch {
    private final String branchName;
    private String latestCommit;//存储哈希值
    private final String defaultBranch="main";

    public String getBranchName() {return branchName;}

    public CommitObject getLatestCommit() {return ObjectStore.getCommit(latestCommit);}

    public String getCommitHash(){
        return latestCommit;
    }

    public void setLatestCommit(CommitObject latestCommit) {
        this.latestCommit = latestCommit.getKey();
    }
    /**branch:main构造方法*/
    public Branch() {
        this.branchName = defaultBranch;
        this.latestCommit=null;
        save();
    }
    /**一般branch构造方法*/
    public Branch(String branchName, CommitObject latestCommit) {
        this.branchName = branchName;
        this.latestCommit = latestCommit.getKey();
        save();
    }

    public void save(){
        ObjectStore.saveBranch(this);
    }
}
