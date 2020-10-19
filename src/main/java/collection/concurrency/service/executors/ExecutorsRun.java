package collection.concurrency.service.executors;

import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

public class ExecutorsRun {

    public static void main(String[] args) {
        ScheduledExecutorService scheduledExecutorService =
                Executors.newScheduledThreadPool(4);


        IntStream.range(0, 7).forEach(
                i ->  scheduledExecutorService.schedule(new Task(i), 15, TimeUnit.SECONDS)
        );

        scheduledExecutorService.shutdown();
    }

    private static class Task implements Callable<String>{

        private final int idThread;

        private Task(int idThread) {
            this.idThread = idThread;
        }

        @Override
        public String call() throws Exception {

            System.out.println(Thread.currentThread().getName() +
                    ": Запущен в итерациии № " + this.idThread);

            return Thread.currentThread().getName() + ": № " + this.idThread ;
        }
    }
}

