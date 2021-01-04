package HWTest;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

//------------- 保存对象用 -------------

public class SaveObject{
  public static void writeObjectToFile(String fileDir,Object obj)
  {
    File file =new File(fileDir);
    FileOutputStream out;
    try {
      out = new FileOutputStream(file);
      ObjectOutputStream objOut=new ObjectOutputStream(out);
      objOut.writeObject(obj);
      objOut.flush();
      objOut.close();
      //System.out.println("write object success!");
    } catch (IOException e) {
      //System.out.println("write object failed");
      e.printStackTrace();
    }
  }

  public static Object readObjectFromFile(String fileDir)
  {
    Object temp=null;
    File file =new File(fileDir);
    FileInputStream in;
    try {
      in = new FileInputStream(file);
      ObjectInputStream objIn=new ObjectInputStream(in);
      temp=objIn.readObject();
      objIn.close();
      //System.out.println("read object success!");
    } catch (IOException e) {
      System.out.println("read object failed");
      e.printStackTrace();
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
    }
    return temp;
  }
}
