package HWTest;

import java.io.Serializable;

public abstract class GitObject implements Serializable, Comparable<GitObject>{
    private String key;
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
}