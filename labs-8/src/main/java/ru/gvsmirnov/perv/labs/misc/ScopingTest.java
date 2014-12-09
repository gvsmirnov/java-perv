package ru.gvsmirnov.perv.labs.misc;

public class ScopingTest {

    private static final int SIZE = (int) (Runtime.getRuntime().maxMemory() * 0.6);

    public static void main(String[] args) throws Exception {
        for (int i = 0; i < 1000; i++) {
            allocateMemory(i);
        }
    }

    private static void allocateMemory(int i) {
        try {
            {
                byte[] bytes = new byte[SIZE];
                System.out.println(bytes.length);
            }

            byte[] moreBytes = new byte[SIZE];
            System.out.println(moreBytes.length);

            System.out.println("I allocated memory successfully " + i);

        } catch (OutOfMemoryError e) {
            System.out.println("I failed to allocate memory " + i);
        }
    }

}
