package collection.concurrency.service.synchronizeblock.exchanger;

import java.util.concurrent.Exchanger;

public class ExchangerRun {

    public static void main(String[] args) {

        /*Создаем обменник, который будет обмениваться типом String*/
        final Exchanger<String> exchanger = new Exchanger<>();

        /*Формируем груз для 1-го грузовика*/
        String[] p1 = new String[]{
                "{посылка едет по направлению : A -> D}",
                "{посылка в точке E поедет по направлению:  A -> C}"};

        /*Формируем груз для 2-го грузовика*/
        String[] p2 = new String[]{
                "{посылка едет по направлению : B -> C}",
                "{посылка в точке E поедет по направлению: B -> D}"};

        /*1-й грузовик , маршрут  A - D (создаем задачу для потока)*/
        Truck truckForFirstThread =
                new Truck(1, "A", "D", p1, exchanger);

        /*Отправляем 1-й грузовик из А -> D (создаем поток )*/
        Thread threadFirst = new Thread(truckForFirstThread);

        /*указываем, что главный поток подождет, когда дочерний поток выполниться*/
        try {
            threadFirst.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        threadFirst.start();

        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        /*Отправляем 2-й грузовик из А -> D*/
        Truck truckForSecondThread =
                new Truck(2, "B", "C", p2, exchanger);

        Thread threadSecond = new Thread(truckForSecondThread);

        /*указываем, что главный поток подождет, когда дочерний поток выполниться*/
        try {
            threadSecond.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        /*Отправляем 2-й грузовик из В в С*/
        threadSecond.start();

    }
}
