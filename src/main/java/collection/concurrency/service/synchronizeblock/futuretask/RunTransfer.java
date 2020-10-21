package collection.concurrency.service.synchronizeblock.futuretask;

import java.util.concurrent.*;

public class RunTransfer {

    public static void main(String[] args) {

        Task runnable = new Task();

        FutureTask<String> futureTask = new FutureTask<>(runnable,"FutureTask1 is complete");

        ExecutorService executor = Executors.newFixedThreadPool(2);
        executor.submit(futureTask);

        try {
            String s = futureTask.get();
            System.out.println(s);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }


        while (true){
            if(futureTask.isDone()) {
                System.out.println("Задача выполнилась.");
                executor.shutdown();
                try {
                    executor.awaitTermination(10, TimeUnit.SECONDS);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return;
            }
        }






    }
}
