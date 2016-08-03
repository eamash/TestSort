package com.company;

import java.io.*;
import java.util.Arrays;

/**
 * Created by Екатерина on 03.08.2016.
 */
public class Start {
    static int countOfMass = 10;
    static int countElement = countOfMass * countOfMass / 2;
    static String[] outTmp = {"outputMin.txt", "outputMax.txt"};
    // Массив с ограничением по памяти
    private int[] masInt = new int[countOfMass];
    private BufferedReader[] masReader = new BufferedReader[countOfMass];
    private int countEndFiles = 0;
    Boolean stateMinMax;

    public void start() throws IOException {
        sortMinMax(true);
        sortMinMax(false);

        BufferedReader[] masReaderOut = new BufferedReader[2];
        masReaderOut[1] = new BufferedReader(new FileReader(outTmp[0]));
        masReaderOut[0] = new BufferedReader(new FileReader(outTmp[1]));
        BufferedWriter output = new BufferedWriter(new FileWriter("output.txt"));

        for (int i = 0; i < countElement; i++){
            for (int j = 0; j < 2; j++){
                output.write(masReaderOut[j].readLine());
                output.newLine();
            }
        }
        output.close();
        masReaderOut[0].close();
        masReaderOut[1].close();
        deleteTmpFiles();
    }

    private void sortMinMax(Boolean state) throws IOException {
        countEndFiles = 0;
        stateMinMax = state;
        createTempFiles();
        initReader();
        sort();
    }

    private void createTempFiles() throws IOException {
        // создание промежуточных файлов
        BufferedReader input = new BufferedReader(new FileReader("input.txt"));
        BufferedWriter outTmp;
        for (int i = 0; i < countOfMass; i++) {
            // собираем массив
            for (int j = 0; j < countOfMass; j++) {
                masInt[j] = Integer.parseInt(input.readLine());
            }
            // сортируем
            Arrays.sort(masInt);
            // обратная сортировка
            if (!stateMinMax){
                for (int j = 0; j < (countOfMass / 2); j++) {
                    masInt[j] = masInt[j] + masInt[countOfMass - j - 1];
                    masInt[countOfMass - j - 1] = masInt[j] - masInt[countOfMass - j - 1];
                    masInt[j] = masInt[j] - masInt[countOfMass - j - 1];
                }
            }

            // записываем
            outTmp = new BufferedWriter(new FileWriter(getStrTempFileName(i)));
            for (int j = 0; j < countOfMass; j++) {
                outTmp.write(Integer.toString(masInt[j]));
                outTmp.newLine();
            }
            outTmp.close();
        }
    }

    private void initReader() throws FileNotFoundException {
        // инициализируем reader и writer для всех необходимых файлов
        for (int i = 0; i < countOfMass; i++){
            masReader[i] = new BufferedReader(new FileReader(getStrTempFileName(i)));
        }
    }

    private void sort() throws IOException {
        String strFile = stateMinMax ? outTmp[0] : outTmp[1];
        BufferedWriter output = new BufferedWriter(new FileWriter(strFile));
        int minMax;

        // первое заполнение
        for (int i = 0; i < countOfMass; i++){
            getNext(i);
        }
        do {
            if (stateMinMax){
                minMax = getMin();
            }else{
                minMax = getMax();
            }
            output.write(Integer.toString(minMax));
            output.newLine();
        }while (countEndFiles < countOfMass);

        output.close();
    }

    private void getNext(int i) throws IOException {
        String strTmp = masReader[i].readLine();
        if (strTmp != null){
            masInt[i] = Integer.parseInt(strTmp);
        }else{
            if (stateMinMax){
                masInt[i] = Integer.MAX_VALUE;
            }else{
                masInt[i] = Integer.MIN_VALUE;
            }
            countEndFiles++;
        }
    }

    private int getMin() throws IOException {
        int min = Integer.MAX_VALUE;
        int index = -1;
        for (int i = 0; i < countOfMass; i++){
            if (masInt[i] <= min){
                min = masInt[i];
                index = i;
            }
        }
        getNext(index);
        return min;
    }

    private int getMax() throws IOException {
        int max = Integer.MIN_VALUE;
        int index = -1;
        for (int i = 0; i < countOfMass; i++){
            if (masInt[i] >= max){
                max = masInt[i];
                index = i;
            }
        }
        getNext(index);
        return max;
    }

    private void deleteTmpFiles() throws IOException {
        for (int i = 0; i < 2; i++){
            deleteFile(outTmp[i]);
        }
        for (int i = 0; i < countOfMass; i++){
            masReader[i].close();
            deleteFile(getStrTempFileName(i));
        }
    }

    private static void deleteFile(String nameFile) throws FileNotFoundException {
        File file = new File(nameFile);
        if (file.exists()){
            file.delete();
        }
    }

    private String getStrTempFileName(int ind){
        return "temp" + ind + ".txt";
    }
}
