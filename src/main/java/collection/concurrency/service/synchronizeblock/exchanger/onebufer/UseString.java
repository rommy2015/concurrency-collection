package collection.concurrency.service.synchronizeblock.exchanger.onebufer;


import java.util.concurrent.Exchanger;
import java.util.stream.IntStream;

/*Поток типа Thread, использующий символьную строку. То есть здесь описывается задача
 * для потока, который будет получать из буфера Exchanger данные, которые перед этим
 * были туда помещены другим потоком.*/
public class UseString implements Runnable {

    Exchanger<String> exchanger;
    String string;

    public UseString(Exchanger<String> exchanger) {
        this.exchanger = exchanger;
        new Thread(this::run).start();
    }

    @Override
    public void run() {

        IntStream
                .range(0, 3)
                .forEach(i -> {
                    try {
                        string = exchanger.exchange(new String());
                        System.out.println("Через буфер, от потока `MakeString`, для потока `UseString`, " +
                                "получены данные : " + string);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                });
    }
}
