package collection.concurrency.service.callable;

import collection.concurrency.domain.Account;
import collection.concurrency.exceptions.InsufficientResourcesAccountException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import static collection.concurrency.service.transfermoneyreentrantlock.MainRunTransfer.LOCK_WAIT_SEC;

public class Transfer implements Callable<Boolean> {


    private static final Logger LOGGER =
            LoggerFactory.getLogger(Transfer.class);

    private final Account accountFrom;
    private final Account accountTo;
    private final int amount;
    private final int idThread;


    public Transfer(Account accountFrom, Account accountTo, int amount, int idThread) {
        this.accountFrom = accountFrom;
        this.accountTo = accountTo;
        this.amount = amount;
        this.idThread = idThread;
    }

    private final Random waitRandom = new Random();

    @Override
    public Boolean call() throws Exception  {

        while (true) {
            try {
                boolean isTryLockAccountFrom = accountFrom.getLock().tryLock(LOCK_WAIT_SEC, TimeUnit.SECONDS);

                if (isTryLockAccountFrom) {

                    boolean isTryLockAccountTo = accountTo.getLock().tryLock(LOCK_WAIT_SEC, TimeUnit.SECONDS);

                    if (isTryLockAccountTo) {
                        try {

                            if (accountFrom.getBalance() < amount) {

                             throw new InsufficientResourcesAccountException("На счету недостаточно денег.");

                            }

                            accountFrom.withdrawFromAccount(amount);/*списание со счета*/
                            LOGGER.warn("--- Поток № : " + this.idThread);
                            LOGGER.warn("Деньги списаны со счета. Остаток (accountFrom) : "
                                    + accountFrom.getBalance());

                            accountTo.depositToAccount(amount);/*пополнение счета*/
                            LOGGER.warn("Деньги зачислены на счет. Остаток (accountTo) : " + accountTo.getBalance());
                            LOGGER.warn("\n");
                            Thread.sleep(waitRandom.nextInt(2000));

                            return true;
                        } finally {
                            accountTo.getLock().unlock();
                        }
                    } else {
                        accountTo.incrementFailedTransferCounter();
                        return false;
                    }
                } else {
                    accountFrom.incrementFailedTransferCounter();
                    return false;
                }
            } catch (InterruptedException e) {
                LOGGER.error("Время ожидания попытки" +
                        " захвата блокировки объекта 'isLockAccountTo' - превышено!");
            } finally {
                accountFrom.getLock().unlock();
            }
        }
    }
}
