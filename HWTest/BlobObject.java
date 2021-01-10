package HWTest;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Vector;

public class BlobObject extends GitObject {
    private final String fileName;
    private Vector<byte[]> data;
    // 因为要回滚所以BlobObject里面不存file索引直接存文件内容

    /**构造函数*/
    public BlobObject(File file){
        this(file.getName(), file);
    }

    /**带路径构造函数*/
    public BlobObject(String fileName, File file){
        this.fileName = fileName;
        this.data = new Vector<>();
        readfile(file);
        updateKey(); // 设置key
    }

    public String getFileName() {
        return fileName;
    }

    public Vector<byte[]> getData() {
        return data;
    }

    /**读文件*/
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

    /**写文件*/
    private boolean writefile(File f){
        boolean writeDone = false;
        try {
            FileOutputStream os = new FileOutputStream(f);
            for (byte[] d: this.data) {
                os.write(d);
            }
            os.close();
            writeDone = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return writeDone;
    }

    /**计算hash key*/
    public String calcKey() {
        CalcHash ch = new CalcHash();
        ch.addString(fileName);
        ch.addBytes(data);
        return ch.getHash();
    }

    /**将Blob的数据恢复到目标位置*/
    public boolean restoreData(String targetDir){
        File dirFile =new File(targetDir);
        if (!dirFile.isDirectory()){
            dirFile.mkdirs();
        }
        File targetFile = new File(targetDir +"\\" +this.fileName);
        return writefile(targetFile);
    }
}