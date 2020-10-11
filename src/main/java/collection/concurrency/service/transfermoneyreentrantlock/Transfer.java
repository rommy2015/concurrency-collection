package collection.concurrency.service.transfermoneyreentrantlock;

import collection.concurrency.domain.Account;
import collection.concurrency.exceptions.InsufficientResourcesAccountException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

import static collection.concurrency.service.transfermoneyreentrantlock.MainRunTransfer.LOCK_WAIT_SEC;


public class Transfer {

    private static final Logger LOGGER = LoggerFactory.getLogger(Transfer.class);

    private final Account accountFrom;
    private final Account accountTo;
    private final int amount;


    public Transfer(Account accountFrom, Account accountTo, int amount) {
        this.accountFrom = accountFrom;
        this.accountTo = accountTo;
        this.amount = amount;
    }


    public static void transfer(Account accountFrom, Account accountTo, int amount) {
        while (true) {
            try {
                if (accountFrom.getLock().tryLock(LOCK_WAIT_SEC, TimeUnit.SECONDS)) {
                    if (accountTo.getLock().tryLock(LOCK_WAIT_SEC, TimeUnit.SECONDS)) {
                        try {

                            if (accountFrom.getBalance() < amount) {
                                throw new InsufficientResourcesAccountException("На счету недостаточно денег.");
                            }

                            accountFrom.withdrawFromAccount(amount);/*списание со счета*/
                            LOGGER.warn("Деньги списаны со счета. Остаток (accountFrom) : "
                                    + accountFrom.getBalance());

                            accountTo.depositToAccount(amount);/*пополнение счета*/
                            LOGGER.warn("Деньги зачислены на счет. Остаток (accountTo) : " + accountTo.getBalance());

                            break;
                        } catch (InsufficientResourcesAccountException e) {
                            LOGGER.error(e.getLocalizedMessage());
                        } finally {
                            accountTo.getLock().unlock();
                        }
                    }
                }
            } catch (InterruptedException e) {
                LOGGER.error("Время ожидания попытки" +
                        " захвата блокировки объекта 'isLockAccountTo' - превышено!");
            } finally {
                accountFrom.getLock().unlock();
            }
        }
    }

    public static void showBalance(Account accountFrom, Account accountTo) {
        LOGGER.warn("Баланс accountFrom: " + accountFrom.getBalance());
        LOGGER.warn("Баланс accountTo: " + accountTo.getBalance());
    }
}
