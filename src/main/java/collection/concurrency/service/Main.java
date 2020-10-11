package collection.concurrency.service;

import collection.concurrency.ConsoleRun;
import collection.concurrency.domain.Account;
import collection.concurrency.exceptions.InsufficientResourcesAccountException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static collection.concurrency.service.transfermoney.Transfer.transfer;

@SuppressWarnings("DuplicatedCode")
public class Main {

    private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);

/*    public static void main(String[] args) {
        Account accountFrom = new Account(1000);

        Account accountTo = new Account(2000);

        Thread threadFirst = new Thread(() -> {

            transfer(accountFrom, accountTo, 500);


            LOGGER.warn("Первый поток: деньги списаны со счета. Остаток (accountFrom) : "
                    + accountFrom.getBalance());
        });

        Thread threadSecond = new Thread(() -> {

            transfer(accountTo, accountFrom, 300);

            LOGGER.warn("Второй поток: деньги списаны со счета. Остаток (accountTo) : " + accountTo.getBalance());
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

        LOGGER.warn("Баланс accountFrom: " + accountFrom.getBalance());
        LOGGER.warn("Баланс accountTo: " + accountTo.getBalance());

        System.exit(0);
    }*/
}
