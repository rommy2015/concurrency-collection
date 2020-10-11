package collection.concurrency.service.transfermoney;

import collection.concurrency.domain.Account;
import collection.concurrency.exceptions.InsufficientResourcesAccountException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static collection.concurrency.ConsoleRun.LOCK_WAIT_SEC;

public class Transfer {

    private final Lock lock = new ReentrantLock();

    private static final Logger LOGGER = LoggerFactory.getLogger(Transfer.class);

    private static final Lock lockForFirstAccount = new ReentrantLock();

    private static final Lock lockForSecondAccount = new ReentrantLock();

    private final Account accountFrom;
    private final Account accountTo;
    private final int amount;


    public Transfer(Account accountFrom, Account accountTo, int amount) {
        this.accountFrom = accountFrom;
        this.accountTo = accountTo;
        this.amount = amount;
    }


   /* private static void takeLocks(Lock lockFirst, Lock lockSecond) throws InterruptedException {

        while (true) {
            *//*создаем флаги, для сингнализации об успешной взятии блокировки*//*
            boolean isLockAccountFrom = false;

            boolean isLockAccountTo = false;
            */

    /**
     * пытаемся взять блокировку на указанным объектах
     *//*
            isLockAccountFrom = lockFirst.tryLock(LOCK_WAIT_SEC, TimeUnit.SECONDS);

            isLockAccountTo = lockSecond.tryLock(LOCK_WAIT_SEC, TimeUnit.SECONDS);

            *//*Данный блок кода проверяет, если блокировка взята успешно,
     * тогда мы не заходим в блок if(), если же блокировка
     * не взята, мы проверяем, какой из объектов не отдал ее и тогда
     * принудительно снимаем*//*
            if (isLockAccountFrom && isLockAccountTo) return;

            if (isLockAccountFrom) lockFirst.unlock();

            if (isLockAccountTo) lockSecond.unlock();
        }
    }
*/
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
                            accountTo.depositToAccount(amount);/*пополнение счета*/
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

    /*деньги списываются со счета accountFrom и зачисляются на счет accountTo*/
  /*  public static void transfer1(Account accountFrom, Account accountTo, int amount)
            throws InsufficientResourcesAccountException, InterruptedException {


        if (accountFrom.getLock().tryLock(LOCK_WAIT_SEC, TimeUnit.SECONDS)) {
            try {

                if (accountTo.getLock().tryLock(LOCK_WAIT_SEC, TimeUnit.SECONDS)) {
                    LOGGER.warn("isLockAccountTo - захвачен");
                    if (accountFrom.getBalance() < amount) {
                        accountFrom.getLock().unlock();
                        accountTo.getLock().unlock();
                        throw new InsufficientResourcesAccountException("На счету недостаточно денег.");
                    }

                    accountFrom.withdrawFromAccount(amount); *//*списание со счета*//*
                    accountTo.depositToAccount(amount);*//*пополнение счета*//*

                    LOGGER.warn("Баланс accountFrom: " + accountFrom.getBalance());
                    LOGGER.warn("Баланс accountTo: " + accountTo.getBalance());
                }


            } catch (InterruptedException e) {
                LOGGER.error("Время ожидания попытки захвата блокировки объекта 'isLockAccountTo' - превышено!");
            } finally {
              //  accountFrom.getLock().unlock();
              //  accountTo.getLock().unlock();
            }
        }
    }*/
}
