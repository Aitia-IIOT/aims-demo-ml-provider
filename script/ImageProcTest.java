import java.util.Random;

public class ImageProcTest {
    private static Random rnd = new Random(System.currentTimeMillis());

    public static void main(String[] args) throws InterruptedException {
        Thread.sleep(1000);
        final int choice = rnd.nextInt(10);
        
        int exitCode = 0;
        if (choice < 7) {
        	// find humans
        	final int confidence = 30 + rnd.nextInt(70);
        	System.out.println("y " + confidence);
        	System.out.println("10 20 30 40");
        } else if (choice < 9) {
        	// no humans
        	System.out.println("n");
        } else {
        	// problem
        	System.err.println("Something went wrong.");
        	exitCode = -2;
        }
        
        System.exit(exitCode);
    }
}