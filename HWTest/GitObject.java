package HWTest;

public abstract class GitObject implements Comparable<GitObject>{
    private String key;

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