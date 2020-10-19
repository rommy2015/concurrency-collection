package collection.concurrency.service.executors;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

public class ExecutorsRunnableRun {

    public static void main(String[] args) {

        ScheduledExecutorService scheduledExecutorService =
                Executors.newScheduledThreadPool(4);


        IntStream.range(0, 7).forEach(

                i -> {

                    /*Это нужно для того, чтобы успеть выполнить какое-то количество потоков*/
                    Task task = new Task(i);

                   // scheduledExecutorService.scheduleAtFixedRate(task, 1, 2, TimeUnit.SECONDS);
                    scheduledExecutorService.scheduleWithFixedDelay(task, 1, 2, TimeUnit.SECONDS);
                }
        );

        scheduledExecutorService.shutdown();
        try {
            scheduledExecutorService.awaitTermination(1000, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    private static class Task implements Runnable {

        private final int idThread;

        private Task(int idThread) {
            this.idThread = idThread;
        }

        @Override
        public void run() {

               System.out.println(Thread.currentThread().getName() +
                       ": Запущен в итерациии № " + this.idThread);
        }
    }

}
