package collection.concurrency.service.synchronizeblock.countdownlatch;

import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class Transfer implements Callable<Boolean> {

    private static final int LOCK_WAIT_SEC = 5;

    private final int idThread;

    private final Account accountFrom;
    private final Account accountTo;
    private final int amount;

    private boolean retryFail;
    private CountDownLatch startLatch; /*замок с защелкой для замера старта потоков */
    private Random waitRandom = new Random();


    public Transfer(int idThread,
                    Account accountFrom,
                    Account accTo,
                    int amount,
                    boolean retryFail, CountDownLatch startLatch) {
        this.idThread = idThread;
        this.accountFrom = accountFrom;
        this.accountTo = accTo;
        this.amount = amount;
        this.retryFail = retryFail;
        this.startLatch = startLatch;
    }

    @Override
    public Boolean call() throws Exception {

        /*поток засыпает, прежде чем произойдет запус защелки)*/
        if (startLatch != null) {
            System.out.println("[Поток - " + idThread + "] " + "Waiting to start...");
            startLatch.await();
        }

        for (; ; ) {
            if (accountFrom.getLock().tryLock(LOCK_WAIT_SEC, TimeUnit.SECONDS)) {
                try {
                    if (accountTo.getLock()
                            .tryLock(LOCK_WAIT_SEC, TimeUnit.SECONDS)) {

                        try {
                            if (accountFrom.getBalance() < amount) {
                                System.out.println(" Поток - [" + idThread +
                                        "] Передача денег не удалась, передаваемая сумма : " + amount
                                        + " со счета  " + accountFrom.getIdAccount()
                                        + " (Баланс счета, с которого нужно списать сумму  - "
                                        + accountFrom.getBalance() + ")");
                                throw new IllegalStateException("[Поток - " + idThread
                                        + "] " + "Передача денег не удалась : " + amount
                                        + " от  Account " + accountFrom.getIdAccount()
                                        + " (Баланс счета - "
                                        + accountFrom.getBalance() + ")");
                            }

                            accountFrom.withdraw(amount);
                            accountTo.deposit(amount);

                            Thread.sleep(waitRandom.nextInt(2000));

                            System.out.println("[Поток - " + idThread + "] " + "Передача суммы : "
                                    + amount + ", выполнена со счета - " + accountFrom.getIdAccount()
                                    + " на счет  - " + accountTo.getIdAccount());

                            return true;

                        } finally {
                            accountFrom.getLock().unlock();
                            accountTo.getLock().unlock();
                        }

                    } else {
                        accountFrom.incFailedTransferCount();
                        if (!retryFail) {
                            return false;
                        }
                    }
                } finally {
                    accountFrom.getLock().unlock();
                }
            } else {
                accountFrom.incFailedTransferCount();
                if (!retryFail) {
                    return false;
                }
            }
        }
    }

}
