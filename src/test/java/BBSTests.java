import org.junit.Test;
import static org.junit.Assert.*;

import java.io.*;
import java.math.BigInteger;
import java.nio.charset.Charset;

public class BBSTests {
    @Test public void byteArrayTest() {
        BBS bbs = new BBS(BigInteger.valueOf(19), BigInteger.valueOf(23));
        byte[] msg = new byte[] {-86, 97, 112, 98, 99, 100};
        byte[] result = BBS.encrypt(msg, bbs.getX(), bbs.getN());
        result = bbs.decrypt(result);
        assertArrayEquals(msg, result);
    }
    @Test public void byteArrayImmutableOriginalTest() {
        BBS bbs = new BBS(BigInteger.valueOf(19), BigInteger.valueOf(23));
        byte[] msg = new byte[] {-86, 97, 112, 98, 99, 100};
        BBS.encrypt(msg, bbs.getX(), bbs.getN());
        assertArrayEquals(new byte[] {-86, 97, 112, 98, 99, 100}, msg);
    }
    @Test public void stringTest() {
        BBS bbs = new BBS(BigInteger.valueOf(19), BigInteger.valueOf(23));
        Charset charset = Charset.forName("UTF-16");
        String msg = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789~!@#$%^&*()_+]}[{\"'\\|";
        byte[] resultBytes = BBS.encrypt(msg, charset, bbs.getX(), bbs.getN());
        String result = new String(bbs.decrypt(resultBytes), charset);
        assertEquals(msg, result);
    }
    @Test public void streamTest() throws IOException {
        byte[] data = new byte[]{10, 20, 30, 40, 50, 60, 70, 80, -10, -20, -30, -40, -50, -60, -70, -80};
        InputStream in = new ByteArrayInputStream(data);
        ByteArrayOutputStream out = new ByteArrayOutputStream(data.length);
        BBS bbs = new BBS(BigInteger.valueOf(19), BigInteger.valueOf(23));
        BBS.encrypt(in, out, bbs.getX(), bbs.getN());
        in.close();
        out.close();
        in = new ByteArrayInputStream(out.toByteArray());
        out = new ByteArrayOutputStream(data.length);
        bbs.decrypt(in, out);
        assertArrayEquals(data, out.toByteArray());
    }
}
