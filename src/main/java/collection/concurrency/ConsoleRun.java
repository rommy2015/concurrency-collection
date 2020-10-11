package collection.concurrency;

import collection.concurrency.domain.Account;
import collection.concurrency.exceptions.InsufficientResourcesAccountException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.concurrent.locks.Lock;

import static collection.concurrency.service.transfermoney.Transfer.*;

@Component
public class ConsoleRun implements CommandLineRunner {

    private static final Logger LOGGER = LoggerFactory.getLogger(ConsoleRun.class);

    public static final int LOCK_WAIT_SEC = 5;

    @Override
    public void run(String... args) throws Exception {

        /*Здесь код запуска. Это точка входа*/

        Account accountFrom = new Account(1000);

        Account accountTo = new Account(2000);

        Thread threadFirst = new Thread(() -> {

            transfer(accountFrom, accountTo, 500);
            LOGGER.warn("Первый поток: деньги списаны со счета. Остаток (accountFrom) : "
                    + accountFrom.getBalance());
        });

        Thread threadSecond = new Thread(() -> {

            transfer(accountTo, accountFrom ,300);

            LOGGER.warn("Второй поток: деньги списаны со счета. Остаток (accountTo) : " + accountTo.getBalance());
        });

        threadFirst.start();
        threadSecond.start();

        threadFirst.join();
        threadSecond.join();
        System.out.println("Главный поток");
        showBalance(accountFrom, accountTo);
        System.exit(0);
    }
}
