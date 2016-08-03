package com.company;

import java.io.*;
import java.util.Random;

public class Main {
    private static void genInput(int elemCount) throws IOException {
        BufferedWriter bw = new BufferedWriter(new FileWriter("input.txt"));
        Random rnd = new Random();
        for (int i = 0; i < elemCount; i++) {
            bw.write(Integer.toString(rnd.nextInt(200)));
            bw.newLine();
        }
        bw.close();
    }

    public static void main(String[] args){
        try {
            genInput(Start.countOfMass * Start.countOfMass);

            Start st = new Start();
            st.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
