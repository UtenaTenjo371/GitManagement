package HWTest;

import java.io.*;

public class SaveString {
    public static void writeStringToFile(String fileDir,String line)
    {
        File file =new File(fileDir);
        FileOutputStream out;
        try {
            out = new FileOutputStream(file,true);
            out.write(line.getBytes());
            out.write('\n');
            out.flush();
            out.close();
            //System.out.println("write object success!");
        } catch (IOException e) {
            //System.out.println("write object failed");
            e.printStackTrace();
        }
    }

    // line参数为负值 则表示读最后一行; 0表示读全部
    public static String readStringFromFile(String fileDir, int line)
    {
        File file =new File(fileDir);
        StringBuilder result = new StringBuilder();
        try {
            BufferedReader bw = new BufferedReader(new FileReader(file));

            int index = 0;
            if(line > 0){
                String temp = "";
                while(index < line && temp != null){
                    result = new StringBuilder(temp);
                    temp = bw.readLine();
                    index++;
                }
            }else if(line < 0){
                String temp = "";
                while(temp != null){
                    result = new StringBuilder(temp);
                    temp = bw.readLine();
                }
            } else {
                String temp = "";
                while(temp != null){
                    result.append(temp);
                    result.append('\n');
                    temp = bw.readLine();
                }
            }
            bw.close();
            System.out.println("read string success!");
        } catch (IOException e) {
            System.out.println("read string failed");
            e.printStackTrace();
        }
        return result.toString();
    }

    // 覆写文件，只写一行的写法
    public static void overwriteStringToFile(String fileDir,String content)
    {
        File file =new File(fileDir);
        FileOutputStream out;
        try {
            out = new FileOutputStream(file);
            out.write(content.getBytes());
            out.write('\n');
            out.flush();
            out.close();
            //System.out.println("write object success!");
        } catch (IOException e) {
            //System.out.println("write object failed");
            e.printStackTrace();
        }
    }
}
