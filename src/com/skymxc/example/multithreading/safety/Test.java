package com.skymxc.example.multithreading.safety;

public class Test {

    public static void main(String[] args) {
        CountThread safetyThread = new CountThread();
        Thread threadA = new Thread(safetyThread, "A");
        Thread threadB = new Thread(safetyThread, "B");
        Thread threadC = new Thread(safetyThread, "C");
        Thread threadD = new Thread(safetyThread, "D");
        Thread threadE = new Thread(safetyThread, "E");

        threadA.start();
        threadB.start();
        threadC.start();
        threadD.start();
        threadE.start();
    }
}
