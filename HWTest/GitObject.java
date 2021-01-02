package HWTest;

import java.io.Serializable;

public abstract class GitObject implements Serializable, Comparable<GitObject>{
    private String key;
    private boolean isSaved = false;
    //Serializable要的ID
    private static final long serialVersionUID =9876543212345L;

    public void updateKey(){
        this.key = calcKey();
    }

    public String getKey() {
        return key;
    }

    public abstract String calcKey();

    public int compareTo(GitObject gitObject) {
        return key.compareTo(gitObject.key);
    }

    //给每个GitObject都添加save方法，直接从object这里存
    public void save(){
        if(isSaved) return; //存过了就不用存了
        ObjectStore.add(this);
        isSaved = true;
    }

    //查看存储情况
    public boolean isSaved(){
        return isSaved;
    }
}