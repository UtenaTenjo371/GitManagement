package HWTest;

public class CommitObject extends GitObject{
    private final TreeObject rootTree;
    private final CommitObject parent;
    private final CommitObject mergeParent;

    public TreeObject getRootTree() {
        return rootTree;
    }

    public CommitObject getParent() {
        return parent;
    }

    public CommitObject getMergeParent() {
        return mergeParent;
    }

    /** 一般commit的构造方法*/
    public CommitObject(TreeObject rootTree, CommitObject parent){
        this.rootTree = rootTree;
        this.parent = parent;
        this.mergeParent=null;
        updateKey(); // 设置key
    }
    /**merge后的commit的构造方法*/
    public CommitObject(TreeObject rootTree, CommitObject parent,CommitObject mergeParent){
        this.rootTree = rootTree;
        this.parent = parent;
        this.mergeParent=mergeParent;
        updateKey(); // 设置key
    }

    public String calcKey() {
        CalcHash ch = new CalcHash();

        ch.addString(rootTree.getKey());

        if(parent != null){
            ch.addString(parent.getKey());
        }

        if(mergeParent != null){
            ch.addString(mergeParent.getKey());
        }

        return ch.getHash();
    }

}