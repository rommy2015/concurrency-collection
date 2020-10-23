package collection.concurrency.service.synchronizeblock.exchanger;
import java.util.concurrent.Exchanger;

public class Truck <V>  implements Runnable  {

    private int numberTruck;
    private String departure; /*пункт отправки посылок*/
    private String destination; /*пункт доставки посылок*/
    private V [] parcels; /*массив посылок*/

    private Exchanger<V> exchanger;

    public Truck(int numberTruck,
                 String departure,
                 String destination,
                 V [] parcels,
                 Exchanger<V> exchanger) {
        this.numberTruck = numberTruck;
        this.departure = departure;
        this.destination = destination;
        this.parcels = parcels;
        this.exchanger = exchanger;
    }


    @Override
    public void run() {

        long milliSecondsFirst = 1000 + (long) Math.random() * 5000;

        System.out.printf("В грузовик №%d погрузили: %s и %s.\n",
                numberTruck, parcels[0], parcels[1]);
        System.out.printf("Грузовик №%d выехал из пункта %s в пункт %s.\n",
                numberTruck, departure, destination);

        try {
            Thread.sleep(milliSecondsFirst);
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }

        System.out.printf("Грузовик №%d приехал в пункт Е.\n", numberTruck);

        try {
            /*При вызове exchange() поток блокируется и ждет*/
            this.parcels[1] = this.exchanger.exchange(this.parcels[1]);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        /*пока другой поток вызовет exchange(),
        после этого произойдет обмен посылками*/
        System.out.printf("В грузовик №%d переместили посылку для пункта %s.\n",
                numberTruck, destination);

        long milliSecondsSecond = 1000 + (long) Math.random() * 5000;
        try {
            Thread.sleep(milliSecondsSecond);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.printf("Грузовик №%d приехал в %s и доставил: %s и %s.\n",
                numberTruck, destination, parcels[0], parcels[1]);
    }


}
