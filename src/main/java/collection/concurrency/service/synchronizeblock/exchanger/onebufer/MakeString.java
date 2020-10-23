package collection.concurrency.service.synchronizeblock.exchanger.onebufer;


import java.util.concurrent.Exchanger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.IntStream;

/* Поток типа Thread, формирующий символьную строку.
 * То есть данный поток будет выполнять задачу по формированию
 * некоторой строки из набора символов.
 * Из данного потока, через Exchanger, мы передадим в другой поток исполнения
 * созданную строку*/
public class MakeString implements Runnable {

    Exchanger<String> exchanger;
    String string;

    public MakeString(Exchanger<String> exchanger, String string) {
        this.exchanger = exchanger;
        this.string = string;

        new Thread(this).start();
    }

    @Override
    public void run() {

        IntStream
                .range(0, 3)
                /*внешний цикл, который будет запускать указанное количество раз
                * обмен данными*/
                .forEach(i -> {
                    /*AtomicReference предоставляет
                    операции над базовой ссылкой на объект,
                    данные операции можно читать и записывать атомарно.
                     Это необходимо, чтобы можно было использовать
                     инкремент переменной в лямбда-выражении*/
                    AtomicReference<Character> character =
                            new AtomicReference<>('A');
                    IntStream
                            .range(0, 5)
                            /*внутренний цикл, который
                            * будет формировать строку, начиная с симовла
                            * 'A' (при первой итерации внешнего цикла),
                            * при этом, при каждой новой итерации
                            * внешнего цикла, мы каждый раз будем брать
                            * последний сохраненный символ из строки, сделанный
                            * в  предыдущей  итерации внешнего цикла, здесь же
                            * выполняем приращение и затем от полученного цикла,
                            * будем во внутреннем цикле создавать новую строку из
                            * символов  */
                            .forEach(j -> {
                                /*сначала формируется символ, при каждой новой итерации. Используя AtomicReference,
                                * мы делаем операцию приращения атомарной, и таким образом выполняем не только приращение
                                * но и получение очередного значения*/
                                Character characterNow =
                                        new Character((char) (character.get() + 1));
                                string += (char) character.getAndSet(characterNow);
                            });

                    try {
                        /*обменять заполненный буфер на пустой*/
                        string = exchanger.exchange(string);
                    } catch (InterruptedException exc) {
                        System.out.println(exc);
                    }
                });
    }
}
