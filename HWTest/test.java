package HWTest;

import java.awt.*;

public class test {
    public static void main(String[] args) {
        //测试branch的存储和读取
        /*Branch b=new Branch("main",new CommitObject(ConvertFolder.dfs("D:\\MyFile\\project\\PKU-1stSemester\\Java\\homework")));
        if(ObjectStore.isBranch("main")){
            System.out.println(b.getBranchName());
            System.out.println(b.getCommitHash());
        }
        CommitObject c=ObjectStore.getCommit(b.getCommitHash());
        TreeObject t=ObjectStore.getTree(c.getRootTree().getKey());
        System.out.println(t.getDirName());
        System.out.println(ObjectStore.isBranch("main"));
        Branch head=new Branch();
        ObjectStore.saveHead(head.getBranchName());
        System.out.println(ObjectStore.getHead());
        //System.out.println(getHead());*/
        //测试add commit
        VersionController vc=new VersionController("D:\\MyFile\\project\\PKU-1stSemester\\Java\\repository");
        vc.addCommit();
        vc.createBranch("Imfine");
        vc.createBranch("Imdead");
        vc.printBranch();
        vc.switchToBranch("Imfine");
        vc.printBranch();
        //Branch current=ObjectStore.getBranch(vc.getHead());
        //TreeObject tree=ConvertFolder.dfs(vc.getPath());
        //System.out.println(current.getBranchName());
        //System.out.println(current.getLatestCommit()!=null);
        //System.out.println(tree.compareTo(current.getLatestCommit().getRootTree())!=0);
    }
}
