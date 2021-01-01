package HWTest;

import java.io.File;
import java.io.FileInputStream;
import java.util.Vector;

public class BlobObject extends GitObject {
    private final String fileName;
    private Vector<byte[]> data;
//因为要回滚所以BlobObject里面不存file索引直接存文件内容

    public BlobObject(File file){
        this(file.getName(), file);
    }

    public BlobObject(String fileName, File file){
        this.fileName = fileName;
        this.data = new Vector<>();
        readfile(file);
        updateKey(); // 设置key
    }
    private void readfile(File f){
        try {
            FileInputStream is = new FileInputStream(f);
            int numRead = 0;
            do {
                byte[] buffer = new byte[1024];
                numRead = is.read(buffer);
                if (numRead > 0) {
                    data.add(buffer);
                }
            } while (numRead != -1);
            is.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public String calcKey() {
        CalcHash ch = new CalcHash();
        ch.addString(fileName);
        ch.addBytes(data);
        return ch.getHash();
    }

    public String getFileName() {
        return fileName;
    }

    public Vector<byte[]> getData() {
        return data;
    }

    // 预计实现： 将数据恢复到目标文件
    public boolean restoreData(String targetDir){
        return true;
    }
}