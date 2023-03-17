import java.util.*;

public class Main {
    static final Map<Integer, Integer> sizeToFreq = new HashMap<>();
    static List<Thread> threadList = new ArrayList<>();
    static final int ONE = 1;

    public static void main(String[] args) throws InterruptedException {
        for (int i = 0; i < 100; i++) {
            Runnable runnable = () -> {
                String text = generateRoute("RLRFR", 100);
                int maxSize = 0;
                for (int j = 0; j < text.length(); j++) {
                    if (text.charAt(j) == 'R') {
                        maxSize++;
                    }
                }
                synchronized (sizeToFreq) {
                    if (sizeToFreq.containsKey(maxSize)) {
                        int count = ONE + sizeToFreq.get(maxSize);
                        sizeToFreq.replace(maxSize, count);
                    } else {
                        sizeToFreq.put(maxSize, ONE);
                    }
                    sizeToFreq.notify();
                }
            };
            Thread thread = new Thread(runnable);
            threadList.add(thread);
        }
            Runnable runnable1 = () -> {
                while (!Thread.interrupted()) {
                    synchronized (sizeToFreq) {
                        try {
                            sizeToFreq.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        System.out.println("Самое частое количество повторений " + sizeToFreq.keySet().stream().max(Comparator.comparing(sizeToFreq::get)) + " (встретилось " + Collections.max(sizeToFreq.values()) + " раз)");
                    }
                }
            };
            Thread thread1 = new Thread(runnable1);
            thread1.start();

            for (Thread thread3 : threadList) {
                thread3.start();
                thread3.join();
            }
            thread1.interrupt();
        }

    public static String generateRoute(String letters, int length) {
        Random random = new Random();
        StringBuilder route = new StringBuilder();
        for (int i = 0; i < length; i++) {
            route.append(letters.charAt(random.nextInt(letters.length())));
        }
        return route.toString();
    }
}
