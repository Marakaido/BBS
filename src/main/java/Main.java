import java.io.*;
import java.math.BigInteger;
import java.nio.file.Paths;

public class Main {
    public static void main(String[] args) {
        try {
            switch(args[0]) {
                case "gen": gen(args); break;
                case "enc":
                case "dec": apply(args); break;
            }
        }
        catch(Exception e) {
            System.out.println("Operation failed, please check arguments and try again\n" + e.getMessage());
        }
    }

    static void gen(String[] args) {
        BigInteger p = new BigInteger(args[1]);
        BigInteger q = new BigInteger(args[2]);
        BBS bbs = new BBS(p, q);
        System.out.println("x = " + bbs.getX());
        System.out.println("n = " + bbs.getN());
    }

    static void apply(String[] args) throws IOException {
        BigInteger x = new BigInteger(args[1]);
        BigInteger n = new BigInteger(args[2]);
        InputStream in = new BufferedInputStream(new FileInputStream(Paths.get(args[3]).toFile()));
        OutputStream out = new BufferedOutputStream(new FileOutputStream(Paths.get(args[4]).toFile()));
        BBS.encrypt(in, out, x, n);
    }
}
