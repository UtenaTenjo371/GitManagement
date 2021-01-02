package HWTest;

public class CommitObject extends GitObject{
    private final String rootTree;
    private final String parent;
    private final String mergeParent;
//只存各个变量的哈希值，对内存好一点

    public TreeObject getRootTree() {
        return ObjectStore.getTree(rootTree);
    }

    public CommitObject getParent() {
        return ObjectStore.getCommit(parent);
    }

    public CommitObject getMergeParent() {
        return ObjectStore.getCommit(mergeParent);
    }

    /** 一般commit的构造方法*/
    public CommitObject(TreeObject rootTree, CommitObject parent){
        rootTree.save();
        this.rootTree = rootTree.getKey();
        parent.save();
        this.parent = parent.getKey();
        this.mergeParent=null;
        updateKey(); // 设置key
        save();
    }
    /**merge后的commit的构造方法*/
    public CommitObject(TreeObject rootTree, CommitObject parent, CommitObject mergeParent){
        rootTree.save();
        this.rootTree = rootTree.getKey();
        parent.save();
        this.parent = parent.getKey();
        mergeParent.save();
        this.mergeParent=mergeParent.getKey();
        updateKey(); // 设置key
        save();
    }

    public String calcKey() {
        CalcHash ch = new CalcHash();

        ch.addString(rootTree);

        if(parent != null){
            ch.addString(parent);
        }

        if(mergeParent != null){
            ch.addString(mergeParent);
        }

        return ch.getHash();
    }

}