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

    // 覆写文件
    public static void overwriteStringToFile(String fileDir,String content)
    {
        File file =new File(fileDir);
        FileOutputStream out;
        try {
            out = new FileOutputStream(file);
            out.write(content.getBytes());
            out.flush();
            out.close();
            //System.out.println("write object success!");
        } catch (IOException e) {
            //System.out.println("write object failed");
            e.printStackTrace();
        }
    }

    /**删除文件中含有指定关键字之后的内容*/
    public static void deleteContentAfterKeyword(String oldFilepath, String keyword) throws IOException {
        File file = new File(oldFilepath);
        BufferedReader reader = null;
        File tempFile = new File(file.getPath() + "_temp");
        FileWriter fw=new FileWriter(tempFile);
        BufferedWriter  bw=new BufferedWriter(fw);
        try {
            // System.out.println("以行为单位读取文件内容，一次读一整行：");
            reader = new BufferedReader(new FileReader(file));
            String tempString = null;
            int line = 1;
            // 一次读入一行，直到读入null为文件结束
            while ((tempString = reader.readLine()) != null) {
                if (tempString.toLowerCase().contains(keyword.toLowerCase()))
                    break;
                bw.write(tempString+"\n");
            }
            reader.close();
            bw.close();
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                }
            }
        }
        file.delete();
        tempFile.renameTo(new File(oldFilepath));
    }
}
