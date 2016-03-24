import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * Created by darksnow on 3/17/16.
 */
public class Main {
    public static boolean found = false;
    public static boolean canStopConsumer = false;
    public static long startedTime;
    public static char[] possibilities = new char[]{
            'A',
            'B',
            'C',
            'D',
            'E',
            'F',
            'G',
            'H',
            'I',
            'J',
            'K',
            'L',
            'M',
            'N',
            'O',
            'P',
            'Q',
            'R',
            'S',
            'T',
            'U',
            'V',
            'W',
            'X',
            'Y',
            'Z',
            'a',
            'b',
            'c',
            'd',
            'e',
            'f',
            'g',
            'h',
            'i',
            'j',
            'k',
            'l',
            'm',
            'n',
            'o',
            'p',
            'q',
            'r',
            's',
            't',
            'u',
            'v',
            'w',
            'x',
            'y',
            'z',
            '0',
            '1',
            '2',
            '3',
            '4',
            '5',
            '6',
            '7',
            '8',
            '9'
    };

    public static void main(String[] args) {

        if(args.length != 2){
            System.out.println("Usage:\njava -jar labo2.jar <hashed password> <nb concurrent threads>\nExample:\njava -jar labo2.jar "+MD5("titi")+" 4");
            System.exit(0);
        } else {
            String encryptedPw = args[0];
            BlockingQueue<Thread> baq = new ArrayBlockingQueue<Thread>(Integer.parseInt(args[1]));
            Producer p = new Producer(baq, encryptedPw);
            startedTime = System.nanoTime();
            new Thread(p).start();
            Consumer c = new Consumer(baq);
            new Thread(c).start();
        }



    }

    public static String MD5(String md5){
        try{
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] array = md.digest(md5.getBytes());
            StringBuffer sb = new StringBuffer();
            for(int i = 0; i < array.length; ++i){
                sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100).substring(1,3));
            }
            return sb.toString();

        } catch(NoSuchAlgorithmException e){
            e.printStackTrace();
        }
        return null;
    }
}

class BruteForce implements Runnable{
    char startingChar;
    String goal;
    BruteForce(char startingChar, String goal){
        this.startingChar = startingChar;
        this.goal = goal;
    }

    public void run(){
        for(int i = 0; i < Main.possibilities.length; i++){
            for(int j = 0; j < Main.possibilities.length; j++){
                for(int k = 0; k < Main.possibilities.length; k++){
                    StringBuilder sb = new StringBuilder();
                    sb.append(startingChar);
                    sb.append(Main.possibilities[i]);
                    sb.append(Main.possibilities[j]);
                    sb.append(Main.possibilities[k]);
                    String res = Main.MD5(sb.toString());
                    if(res.equals(goal)){
                        Main.found = true;
                        System.out.println("Found password: "+sb.toString() + " ( "+res+" )");
                    }
                }
            }
        }
    }
}

class Producer implements Runnable {
    private final BlockingQueue queue;
    String goal;
    Producer(BlockingQueue q, String goal){
        queue = q;
        this.goal = goal;
    }

    public void run(){

        try{
            for(int i = 0; i<Main.possibilities.length && !Main.found; i++){
                BruteForce b = new BruteForce(Main.possibilities[i], goal);
                Thread t = new Thread(b);
                t.start();
                queue.put(t);
            }
            Main.canStopConsumer = true;
            long elapsedTime = System.nanoTime() - Main.startedTime;
            if(Main.found){
                System.out.println("Elapsed time : "+(elapsedTime/1000000000.0f)+"s.");
            } else {
                System.out.println("No match...");
                System.out.println("Elapsed time : "+(elapsedTime/1000000000.0f)+"s.");
            }


        }catch(InterruptedException ex){
            ex.printStackTrace();
        }

    }
    Object produce(){
        return null;
    }
}

class Consumer implements Runnable{
    private final BlockingQueue queue;
    Consumer(BlockingQueue q) { queue = q; }
    public void run() {
        try {
            while (!Main.canStopConsumer) {
                consume(queue.peek());
            }
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
    }
    void consume(Object x) throws  InterruptedException{
        if(x != null){
            Thread t = (Thread)x;
            if(t.getState() == Thread.State.TERMINATED){
                queue.take();
            }

        }

    }
}
