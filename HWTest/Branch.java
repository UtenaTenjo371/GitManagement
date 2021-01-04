package HWTest;

public class Branch {
    private final String branchName;
    private String latestCommit;//存储哈希值
    private static final String defaultBranch="main";
    private static final String nullCommitHash="0000000000000000000000000000000000000000";

    public String getBranchName() {return branchName;}

    public CommitObject getLatestCommit() {
        if(latestCommit==null) return null;
        return ObjectStore.getCommit(latestCommit);
    }

    public String getCommitHash(){
        if(latestCommit==null) return nullCommitHash;
        return latestCommit;
    }

    public void setLatestCommit(CommitObject latestCommit) {
        this.latestCommit = latestCommit.getKey();
    }
    /**branch:main构造方法*/
    public Branch() {
        this.branchName = defaultBranch;
        this.latestCommit=null;
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
