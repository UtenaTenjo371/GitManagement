package HWTest;

public class CommitObject extends GitObject{
    private static final String defaultParent = "0000000000000000000000000000000000000000";

    private final String rootTree;
    private final String parent;
    private final String mergeParent;
    private String comment="";
    //只存各个变量的哈希值，对内存好一点
    //加了一个每个commit可以做的注释

    public TreeObject getRootTree() {
        return ObjectStore.getTree(rootTree);
    }

    public CommitObject getParent() {
        return ObjectStore.getCommit(parent);
    }

    public CommitObject getMergeParent() {
        return ObjectStore.getCommit(mergeParent);
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    /** 第一次commit的构造方法*/
    public CommitObject(TreeObject rootTree){
        rootTree.save();
        this.rootTree = rootTree.getKey();
        this.parent = defaultParent;
        this.mergeParent = defaultParent;
        updateKey(); // 设置key
        save();
    }
    /** 一般commit的构造方法*/
    public CommitObject(TreeObject rootTree, CommitObject parent){
        rootTree.save();
        this.rootTree = rootTree.getKey();
        parent.save();
        this.parent = parent.getKey();
        this.mergeParent=defaultParent;
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

    /**计算hashkey*/
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