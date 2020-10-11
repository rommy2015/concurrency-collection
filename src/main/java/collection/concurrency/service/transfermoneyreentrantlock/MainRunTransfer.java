package collection.concurrency.service.transfermoneyreentrantlock;

import collection.concurrency.domain.Account;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static collection.concurrency.service.transfermoneyreentrantlock.Transfer.showBalance;
import static collection.concurrency.service.transfermoneyreentrantlock.Transfer.transfer;

public class MainRunTransfer {

    public static final int LOCK_WAIT_SEC = 1;

    private static final Logger LOGGER = LoggerFactory.getLogger(MainRunTransfer.class);

    public static void main(String[] args) {

        Account accountFrom = new Account(1000);

        Account accountTo = new Account(2000);

        Thread threadFirst = new Thread(() -> {

            transfer(accountFrom, accountTo, 500);

        });

        Thread threadSecond = new Thread(() -> {

            transfer(accountTo, accountFrom ,300);

        });

        threadFirst.start();
        threadSecond.start();

        try {
            threadFirst.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        try {
            threadSecond.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println();
        System.out.println("Главный поток (итог банковской операции) :");
        showBalance(accountFrom, accountTo);
        System.exit(0);
    }
}
