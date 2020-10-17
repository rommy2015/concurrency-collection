package collection.concurrency.service.callable;

import collection.concurrency.domain.Account;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class TransferRun {

    public static void main(String[] args) {

        Account accountFrom = new Account(1000);

        Account accountTo = new Account(2000);

        ExecutorService executorService = Executors.newFixedThreadPool(3);

        long timeout = 100000;
        TimeUnit unit = TimeUnit.SECONDS;

        Random random = new Random();

        List<Future<Boolean>> transfersFutureList = new ArrayList<>();

        IntStream.range(0, 10)
                .forEach(i -> {

                    int sumForTransfer = random.nextInt(400);

                    Transfer transfer = new Transfer(accountFrom, accountTo, sumForTransfer, i);
                    Future<Boolean> booleanFuture = executorService.submit(transfer);

                    transfersFutureList.add(booleanFuture);
                });



        executorService.shutdown();


        /*указываем, сколько времени нужно ждать,
        чтобы все потоки смогли выполниться  */
        try {
            executorService.awaitTermination(timeout, unit);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        showResultFromCallable(transfersFutureList);
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
