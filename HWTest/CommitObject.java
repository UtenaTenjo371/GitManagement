package HWTest;

public class CommitObject extends GitObject{
    private final TreeObject rootTree;
    private final CommitObject lastCommit;

    public CommitObject(TreeObject rootTree, CommitObject lastCommit){
        this.rootTree = rootTree;
        this.lastCommit = lastCommit;
        updateKey(); // 设置key
    }

    public String calcKey() {
        CalcHash ch = new CalcHash();

        ch.addString(rootTree.getKey());

        if(lastCommit != null){
            ch.addString(lastCommit.getKey());
        }

        return ch.getHash();
    }

}