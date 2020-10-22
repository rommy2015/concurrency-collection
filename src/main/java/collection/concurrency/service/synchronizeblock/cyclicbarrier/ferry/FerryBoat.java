package collection.concurrency.service.synchronizeblock.cyclicbarrier.ferry;

/*описываем задачу для потока, который будет работать с паромным судном*/
public class FerryBoat implements Runnable {

    /*Здесь описывается задача для потоков, по работе парома.
    * Когда барьер будет достигнут (то есть счетчик  CyclicBarrier достигент 0),
    * тогда запуститься поток, который описывает работу парома.
    * Thread.sleep(500); - имитируют выполнение некторой работы, в данном случае
    * - движение парома через море или реку*/
    @Override
    public void run() {
        try {
            Thread.sleep(500);
            System.out.println("Паром переправил автомобили!");
        } catch (InterruptedException e) {
        }
    }
}
