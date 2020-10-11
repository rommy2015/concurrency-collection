package collection.concurrency.domain;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Account {

    private int balance;

    private final Lock lock = new ReentrantLock();


    public Account(int balance) {
        this.balance = balance;
    }

    public Lock getLock() {
        return lock;
    }

    /*пополенение счета*/
    public void depositToAccount(final int amount) {
        balance += amount;
    }

    /*списание денег с указанного счета*/
    public void withdrawFromAccount(final int amount) {
        balance -= amount;
    }

    public int getBalance() {
        return balance;
    }
}
