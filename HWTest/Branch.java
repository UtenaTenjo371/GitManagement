package HWTest;

public class Branch {
    private final String branchName;
    private CommitObject latestCommit;

    public String getBranchName() {
        return branchName;
    }

    public CommitObject getLatestCommit() {
        return latestCommit;
    }

    public void setLatestCommit(CommitObject latestCommit) {
        this.latestCommit = latestCommit;
    }

    public Branch(String branchName, CommitObject latestCommit){
        this.branchName=branchName;
        this.latestCommit=latestCommit;
    }

    public String getKey(){
        return latestCommit.getKey();
    }

}
