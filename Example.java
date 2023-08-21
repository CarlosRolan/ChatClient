
public class Example {
    public static void main(String[] args) {
        final Runnable doHelloWorld = new Runnable() {
            public void run() {
                System.out.println("Hello World on " + Thread.currentThread());
            }
        };

        Thread appThread = new Thread() {
            public void run() {
                try {
                    SwingUtilities.invokeAndWait(doHelloWorld);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                System.out.println("Finished on " + Thread.currentThread());
            }
        };
        appThread.start();
    }

}
