package collection.concurrency.service.synchronizeblock.cyclicbarrier.ferry;

import java.util.concurrent.CyclicBarrier;
import java.util.stream.IntStream;

public class FerryRun {

    public static void main(String[] args) {

        /*Этот объект описывает задачу, которую нужно будет выплонить потоку,
        * когда барьер будет готов открыться.
        * */
        FerryBoat ferryBoatTaskAfterBarrier = new FerryBoat();

        /*Количество задач, которые могут быть запущены одновременно от
        * некоторого барьера.*/
        int limitForBarrier = 3;

        CyclicBarrier cyclicBarrier = new CyclicBarrier(limitForBarrier, ferryBoatTaskAfterBarrier);

        IntStream.range(0,9)
                .forEach(i -> {

                    Car car = new Car(i, cyclicBarrier);

                    Thread thread = new Thread(car);

                    thread.start();

                    try {
                        //System.out.println("Имитация посадки автомобиля на паром.");
                        Thread.sleep(400);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                });

    }
}
