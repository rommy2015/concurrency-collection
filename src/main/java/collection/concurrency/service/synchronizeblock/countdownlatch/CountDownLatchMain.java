package collection.concurrency.service.synchronizeblock.countdownlatch;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

public class CountDownLatchMain {

    private static final int NUMBER_ACCOUNT_FROM_A_TO_ACCOUNT_TO_B_TRANSFERS = 10;

    private static AtomicInteger counter = new AtomicInteger();

    public static void main(String[] args) {

        final Account accountFromA = new Account(1, 1000);
        final Account accountToB = new Account(2, 1000);

        Random random = new Random();

        /*Защелка для запуска всех потоков одновременно). Указывается, что как только один
         * поток вызовет метод await(), тогда поток заснет, но в тоже время, защелка откроеткся
         * и начнется выполнение*/
        CountDownLatch startLatch = new CountDownLatch(1);

        /*Выбираем этот тип пула, так как заранее неизвестно количество потоков,
        которое будет использоваться*/
        ExecutorService service = Executors.newCachedThreadPool();

        List<Future<Boolean>> transfersFutureList = new ArrayList<>();

        IntStream
                .range(0, NUMBER_ACCOUNT_FROM_A_TO_ACCOUNT_TO_B_TRANSFERS)
                .forEach(idThread -> {

                    int amount = random.nextInt(400);

                    Transfer transfer = new Transfer(idThread,
                            accountFromA,
                            accountToB,
                            amount,
                            true,
                            startLatch);

                    Future<Boolean> resultJobThread = service.submit(transfer);

                    transfersFutureList.add(resultJobThread);
                });

        service.shutdown();

        startLatch.countDown(); /*уменьшается значение счетчика защелки*/


        /*ожидаем, чтобы все задачи были выплонены*/
        boolean rezultWait = false;
        try {
            rezultWait = service.awaitTermination(
                    (NUMBER_ACCOUNT_FROM_A_TO_ACCOUNT_TO_B_TRANSFERS) * 2, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if (!rezultWait) {
            System.err.println("Не все задачи были выполнены за указанное время.");
        }

        showResultFromCallable(transfersFutureList);

    }

    private static void showResultFromCallable(List<Future<Boolean>> transfersFutureList) {
        transfersFutureList
                .stream()
                .forEach(booleanFuture -> {
                    int count = counter.getAndIncrement();
                    try {
                        System.out.println("Результат работы потока [ " + count + "] " + booleanFuture.get());
                    } catch (Exception e ) {
                        System.out.println("Результат работы потока [ " + count + "] :  "
                                + e.getLocalizedMessage());
                    }
                });
    }

}
