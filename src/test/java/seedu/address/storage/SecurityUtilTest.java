package seedu.address.storage;

import static org.junit.Assert.assertEquals;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;

import org.junit.Ignore;
import org.junit.Test;

@Ignore
public class SecurityUtilTest {
    @Test
    public void encryptDecrypt_successful_emptyResult() throws Exception {
        File testFile = new File("./src/test/data/sandbox/temp.xml");
        PrintWriter writer = new PrintWriter(testFile);
        String plaintext = new String("testing encryption and decryption");

        writer.write(plaintext);
        writer.close();

        SecurityUtil.encrypt(testFile);
        SecurityUtil.decrypt(testFile);

        BufferedReader br = new BufferedReader(new FileReader(testFile));
        String testResult = br.readLine();
        assertEquals(testResult, plaintext);
    }
}
