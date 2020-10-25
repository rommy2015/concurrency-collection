package collection.concurrency.service.synchronizeblock.exchanger.onebufer;

import java.util.concurrent.Exchanger;

public class ExchangerRunDemo {

    public static void main(String[] args) {

        Exchanger<String> stringExchanger = new Exchanger<>();

        new UseString(stringExchanger);
        new MakeString(stringExchanger);
    }
}
