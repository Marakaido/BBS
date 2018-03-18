import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Random;
import org.apache.commons.io.IOUtils;

public class BBS {
    public static byte[] encrypt(byte[] msg, BigInteger x,  BigInteger n) {
        return apply(msg, x, n);
    }

    public static void encrypt(InputStream in, OutputStream out, BigInteger x, BigInteger n) throws IOException {
        apply(in, out, x, n);
    }

    public static byte[] encrypt(String msg, Charset charset, BigInteger x, BigInteger n) {
        return apply(msg, charset, x, n);
    }

    public BBS(BigInteger p, BigInteger q) {
        this.n = p.multiply(q);
        this.x = genX(p, q);
    }

    public BigInteger getN() {
        return n;
    }

    public BigInteger getX() {
        return x;
    }

    public byte[] decrypt(byte[] msg) {
        return apply(msg, x, n);
    }

    public void decrypt(InputStream in, OutputStream out) throws IOException {
        apply(in, out, x, n);
    }

    static byte[] apply(byte[] msg, BigInteger x, BigInteger n) {
        byte[] result = Arrays.copyOf(msg, msg.length);
        BigInteger xi = x.modPow(BigInteger.valueOf(2), n);
        for (int i = 0; i < result.length; i++) {
            xi = xi.modPow(BigInteger.valueOf(2), n);
            byte[] bytes = xi.toByteArray();
            result[i] ^= bytes[bytes.length - 1];
        }
        return result;
    }

    static void apply(InputStream in, OutputStream out, BigInteger x, BigInteger n) throws IOException {
        byte[] data = IOUtils.toByteArray(in);
        data = apply(data, x, n);
        out.write(data);
    }

    static byte[] apply(String msg, Charset charset, BigInteger x, BigInteger n) {
        byte[] data = msg.getBytes(charset);
        return apply(data, x, n);
    }

    BigInteger genX(BigInteger p, BigInteger q) {
        BigInteger n = p.multiply(q);
        Random random = new Random();
        BigInteger x = new BigInteger(n.bitLength(), random);
        while(!isCorrectX(p, q, x))
            x = new BigInteger(n.bitLength(), random);
        return x;
    }

    boolean isCorrectX(BigInteger p, BigInteger q, BigInteger x) {
        return !(x.divideAndRemainder(p)[1].equals(BigInteger.ZERO) ||
                x.divideAndRemainder(q)[1].equals(BigInteger.ZERO) ||
                p.divideAndRemainder(x)[1].equals(BigInteger.ZERO) ||
                q.divideAndRemainder(x)[1].equals(BigInteger.ZERO)) &&
                !x.equals(BigInteger.ZERO) &&
                !x.equals(BigInteger.ONE);
    }

    private BigInteger n;
    private BigInteger x;
}
