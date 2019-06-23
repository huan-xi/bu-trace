package test;

/**
 * @author: huanxi
 * @date: 2019-06-20 21:11
 */
public class ThreadTest {

    Thread t1=new Thread(new Runnable(){

        @Override
        public void run() {
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("this is t1!");
        }
    });
    Thread t2=new Thread(new Runnable(){

        @Override
        public void run() {
            System.out.println("this is t2!");
        }
    });
    Thread t3=new Thread(new Runnable(){

        @Override
        public void run() {
            System.out.println("this is t3!");
        }
    });
    Thread t4=new Thread(new Runnable(){

        @Override
        public void run() {
            System.out.println("this is t4!");
        }
    });
    Thread t5=new Thread(new Runnable(){

        @Override
        public void run() {
            try {
                Thread.sleep(6000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("this is t5!");
        }
    });
    static ThreadTest  threadTest=new ThreadTest();
    public static void main(String[] args) {
        System.out.println("main thread start!");
        try {
            threadTest.t5.start();
            threadTest.t1.start();
            threadTest.t1.join();
            threadTest.t2.join();
            threadTest.t3.join();
            threadTest.t4.join();

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("main thread end!");
    }
}
