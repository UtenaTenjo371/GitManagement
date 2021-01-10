package HWTest;

import  org.junit.jupiter.api.Test;

import java.io.IOException;

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
}
