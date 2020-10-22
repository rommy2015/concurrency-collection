package collection.concurrency.service.synchronizeblock.cyclicbarrier.ferry;

import java.util.concurrent.CyclicBarrier;

/*данный класс будет имиитровать доставку автомобия на паром.
 * Здесь описывается задача для потока : доставить автомобиль на паром.*/
public class Car implements Runnable {

    private int carNumber;

    private final CyclicBarrier barrier;

    public Car(int carNumber, CyclicBarrier barrier) {
        this.carNumber = carNumber;
        this.barrier = barrier;
    }

    @Override
    public void run() {

        try {

            System.out.printf("Автомобиль №%d подъехал к паромной переправе.\n", carNumber);

            /*Для указания потоку о том что он достиг барьера, нужно вызвать метод await()
            После этого данный поток блокируется, и ждет пока остальные стороны достигнут барьера
            */
            this.barrier.await();

            System.out.printf("Автомобиль №%d продолжил движение, после прибытия парома.\n", carNumber);
        } catch (Exception e) {
        }
    }
}
