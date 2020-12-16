package HWTest;

import java.io.File;

public class BlobObject extends GitObject{
    private final String fileName;
    private final File file;

    public BlobObject(File file){
        this(file.getName(), file);
    }

    public BlobObject(String fileName, File file){
        this.file = file;
        this.fileName = fileName;
        updateKey(); // 设置key
    }

    public String calcKey() {
        CalcHash ch = new CalcHash();
        ch.addString(fileName);
        ch.addFile(file);
        return ch.getHash();
    }

    public String getFileName() {
        return fileName;
    }

    public File getFile() {
        return file;
    }
}