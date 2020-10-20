package collection.concurrency.service.callable;

import collection.concurrency.domain.Account;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class TransferRun {

    private final static long timeout = 100000;
    private final static TimeUnit unit = TimeUnit.SECONDS;

    public static void main(String[] args) throws InterruptedException {

        Account accountFrom = new Account(1000);

        Account accountTo = new Account(2000);

        ScheduledExecutorService scheduledExecutorService =
                runScheduleForGettingFailedLocking(accountFrom, accountTo);

        ExecutorService executorService = Executors.newFixedThreadPool(3);

        Random random = new Random();

        List<Future<Boolean>> transfersFutureList = new ArrayList<>();

        IntStream.range(0, 100)
                .forEach(i -> {

                    int sumForTransfer = random.nextInt(400);

                    Transfer transfer = new Transfer(accountFrom, accountTo, sumForTransfer, i);
                    Future<Boolean> booleanFuture = executorService.submit(transfer);

                    transfersFutureList.add(booleanFuture);
                });


        executorService.shutdown();

        runScheduleForGettingFailedLocking(accountFrom, accountTo);

        /*указываем, сколько времени нужно ждать,
        чтобы все потоки смогли выполниться  */
        try {
            executorService.awaitTermination(timeout, unit);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        showResultFromCallable(transfersFutureList);
    }

    private static ScheduledExecutorService runScheduleForGettingFailedLocking(Account accountFrom, Account accountTo) throws InterruptedException {

        ScheduledExecutorService scheduledExecutorService  =
                Executors.newScheduledThreadPool(2);

        Thread threadAccountFrom = new Thread(() -> {
            AtomicInteger failCounter = accountFrom.getFailCounter();
            System.out.println("Количество неудачных попыток захвата монитора для `accountFrom`:"
                    + failCounter);
        });

        scheduledExecutorService
                .scheduleAtFixedRate(threadAccountFrom, 1, 2, TimeUnit.SECONDS);

        Thread threadAccountTo = new Thread(() -> {
            AtomicInteger failCounter = accountTo.getFailCounter();
            System.out.println("Количество неудачных попыток захвата монитора для `accountTo`:"
                    + failCounter);
        });


        scheduledExecutorService
                .scheduleAtFixedRate(threadAccountTo, 1, 2, TimeUnit.SECONDS);

        Thread.sleep(20000);

        scheduledExecutorService.shutdown();

        scheduledExecutorService.awaitTermination(timeout, unit);

       return scheduledExecutorService;
    }

    private static void showResultFromCallable(List< Future<Boolean>> transfersFutureList) {

        final List<Boolean> list = new ArrayList<>();

        List<Boolean> booleanList = transfersFutureList
                .stream()
                .map(booleanFuture -> {

                    Boolean isGetLock;
                    try {
                        isGetLock = booleanFuture.get();
                        list.add(isGetLock);
                    } catch (InterruptedException e) {
                        list.add(false);
                       // e.printStackTrace();
                    } catch (ExecutionException e) {
                        list.add(false);
                       // e.printStackTrace();
                    }
                    return list;

                })
                .flatMap(List::stream)
                .collect(Collectors.toList());
                 System.out.println(booleanList);
    }

}
