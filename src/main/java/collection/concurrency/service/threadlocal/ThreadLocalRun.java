package collection.concurrency.service.threadlocal;

public class ThreadLocalRun {

    public static void main(String[] args) {

        ThreadLocal<Integer> threadLocal = ThreadLocal.withInitial(() -> 1);

        Integer integer = threadLocal.get();

        System.out.println("id : " + integer);

        threadLocal.set(10);

        integer = threadLocal.get();

        System.out.println("id (после установки нового значения) : " + integer);

        threadLocal.set(null);

    }
}
