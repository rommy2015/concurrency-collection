package collection.concurrency.service.synchronizeblock.cyclicbarrier.cosmos;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class RocketDetailRunnable implements Runnable {

    /*имя детали, для ракеты*/
    private final RocketDetail nameRocketDetail;
    private final CyclicBarrier cyclicBarrier;

    public RocketDetailRunnable(RocketDetail nameRocketDetail, CyclicBarrier cyclicBarrier) {
        this.nameRocketDetail = nameRocketDetail;
        this.cyclicBarrier = cyclicBarrier;
    }

    @Override
    public void run() {

        System.out.println("Готовится деталь: " + nameRocketDetail);

        try {
            Thread.sleep(1000L);

            System.out.println("Деталь готова и ожидает: " + nameRocketDetail);

            cyclicBarrier.await();

            System.out.println("Деталь использована: " + nameRocketDetail);
        } catch (InterruptedException | BrokenBarrierException exception) {
            exception.printStackTrace();
        }
    }
}
