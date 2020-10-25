package collection.concurrency.service.synchronizeblock.countdownlatch;

import java.util.concurrent.atomic.LongAdder;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Account {

    private final Lock lock = new ReentrantLock();

    private final LongAdder failCounter = new LongAdder();

    private final int idAccount;

    private int balanceInitial;

    public Account(int idAccount, int balanceInitial) {
        this.idAccount = idAccount;
        this.balanceInitial = balanceInitial;
    }

    public void incFailedTransferCount() {
        failCounter.increment();
    }

    public void deposit(final int amount) {
        balanceInitial += amount;
    }

    public void withdraw(final int amount) {
        balanceInitial -= amount;
    }

    public int getIdAccount() {
        return idAccount;
    }

    public int getBalance() {
        return balanceInitial;
    }


    public Lock getLock() {
        return lock;
    }

    @Override
    public String toString() {
        return "Account{" +
                "idAccount=" + idAccount +
                ", balanceInitial=" + balanceInitial +
                '}';
    }
}
