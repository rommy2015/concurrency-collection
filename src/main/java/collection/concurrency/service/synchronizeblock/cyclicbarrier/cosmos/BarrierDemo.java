package collection.concurrency.service.synchronizeblock.cyclicbarrier.cosmos;

import java.util.Arrays;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class BarrierDemo {

    public static void main(String[] args) throws InterruptedException {

        CyclicBarrier cyclicBarrier =
                new CyclicBarrier(RocketDetail.values().length, () -> System.out.println("Пуск"));

        ExecutorService executorServiceForThreadPool = Executors.newCachedThreadPool();

        Arrays.stream(RocketDetail.values())/*преобразуем в байтовый поток, массив статических переменных в Enum*/
                /*для каждой детали, создаем поток, который будет работать только со своей деталью*/
                .map(detail -> new RocketDetailRunnable(detail, cyclicBarrier))
                /*используя потоки из пула, мы начинаем выполнять ранее созданные задачи,
                * каждую итерацию будет запускаться поток, который будет работать со своей деталью,
                * стыкуя ее в корабль-ракету*/
                .forEach(executorServiceForThreadPool::submit);

        /*останавливаем прием новых задач, но те задачи которые выполняются, будут завершены*/
        executorServiceForThreadPool.shutdown();
        executorServiceForThreadPool.awaitTermination(1L, TimeUnit.MINUTES);
    }
}
