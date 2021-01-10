package HWTest;

import  org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class test {
    @Test
    void testInit() throws IOException {
        VersionController versionController = new VersionController("F:\\20Java\\test");
        versionController.initRepository();
    }

    @Test
    void testDeleteContentAfterKeyword() throws IOException {
        SaveString.deleteContentAfterKeyword("F:\\20Java\\testgit\\test.txt", "abc");
    }

    @Test
    void testObjectStore(){
        ObjectStore a = new ObjectStore();
        String dir1 = "F:\\test.txt";
        String dir2 = "F:\\test1.txt";
        File f1 = new File(dir1);
        File f2 = new File(dir2);
        BlobObject[] blobs = new BlobObject[2];
        blobs[0] = new BlobObject(f1);
        //Blob的写入读出
        String hash1=ObjectStore.add(blobs[0]);
        assertEquals(blobs[0],ObjectStore.get(hash1));
        blobs[1] = new BlobObject(f2);
        TreeObject[] trees = new TreeObject[0];
        TreeObject tree = new TreeObject("10401206", blobs, trees);
        //tree的写入读出
        String hash2=ObjectStore.add(tree);
        assertEquals(tree,ObjectStore.get(hash2));
        CommitObject cm = new CommitObject(tree);
        cm.setComment("This is a test 1 commit.");
        //commit的写入读出
        String hash3=ObjectStore.add(cm);
        assertEquals(cm,ObjectStore.get(hash3));
        GitLog g = new GitLog();
        g.add(cm);
        //log是否存在
        assertNotNull(g);
    }
}
