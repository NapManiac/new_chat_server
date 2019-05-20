import java.math.BigInteger;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        BigInteger ans = BigInteger.valueOf(1);
        BigInteger i = BigInteger.valueOf(2);
        BigInteger t = BigInteger.valueOf(1);
        while (--n > 0) {
            t = t.multiply(i);
            i = i.add(BigInteger.ONE);
            ans = ans.add(t);
        }
        System.out.println(ans);
    }


}
