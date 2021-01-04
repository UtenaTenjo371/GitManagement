package HWTest;

import java.io.File;
import java.io.FileInputStream;
import java.security.MessageDigest;
import java.util.Vector;

public class CalcHash {
    private MessageDigest complete;

    public CalcHash() {
        try {
            complete = MessageDigest.getInstance("SHA-1");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addFile(File f) {
        try {
            FileInputStream is = new FileInputStream(f);
            // 这里将PPT5中 SHA1Checksum 的程序提出来，不作为子程序运行
            byte[] buffer = new byte[1024];
            int numRead = 0;
            do {
                numRead = is.read(buffer);
                if (numRead > 0) {
                    complete.update(buffer, 0, numRead);
                }
            } while (numRead != -1);
            is.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addString(String s) {
        complete.update(s.getBytes());
    }

    public void addBytes(Vector<byte[]> bv) {
        for(byte[] b : bv){
            complete.update(b);
        }
    }

    public String getHash() {
        byte[] hash = complete.digest();
        StringBuilder resultStr = new StringBuilder("");
        for (int i = 0; i < hash.length; i++) {
            resultStr.append(Integer.toString((hash[i] >> 4) & 0x0F, 16));
            resultStr.append(Integer.toString(hash[i] & 0x0F, 16));
            //控制输出位数
        }

        return resultStr.toString();
    }
}