package HWTest;

import java.io.File;
import java.io.IOException;

public class Stage {
    private static final String rootDir=".mygit\\index";
    // 测试用路径
    // private static final String rootDir="F:\\20Java\\testgit\\.mygit\\index";

    public Stage(){}

    public void initStage() throws IOException {
        File file = new File(rootDir);
        if(!file.exists())
            file.createNewFile();
    }

    /**支持git add，文件状态从Untracked变为Staged*/
    public void addToIndex(String objectKey) throws IOException {
        if(!SaveString.containsKeyword(rootDir, objectKey)){
            String content = objectKey + "\n";
            SaveString.appendToFile(rootDir, content);
        }
    }

    /**检查文件状态是否为Staged*/
    public boolean inIndex(String objectKey) throws IOException {
        return SaveString.containsKeyword(rootDir, objectKey);
    }
}
