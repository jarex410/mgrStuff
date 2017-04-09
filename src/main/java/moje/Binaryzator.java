package moje;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import static moje.GeneratorHistogramow2NEW.*;

/**
 * Created by JaroLP on 2017-03-11.
 */
public class Binaryzator {


    public void binaryzujDesc(String pathToDataBase, int liczbaPktGenerowanychPrzezAlgorytm) throws IOException {
        GeneratorHistogramow2NEW gen = new GeneratorHistogramow2NEW();
        File folder;
        if (liczbaPktGenerowanychPrzezAlgorytm == SUFR) {
            folder = new File(pathToDataBase + "\\desc");
        } else {
            folder = new File(pathToDataBase + "\\descSIFT");
        }
        File[] listOfFiles = folder.listFiles();

        File folderUczacych = new File(pathToDataBase + "\\Uczace\\");

        List<String> nazwyUczacych = new ArrayList<File>(Arrays.asList(folderUczacych.listFiles())).stream().map(x -> x.getName()).collect(Collectors.toList());
        List<File> listaUczacych = new ArrayList<File>(Arrays.asList(listOfFiles)).stream().filter(x -> nazwyUczacych.contains(x.getName().replace(".DESC.txt", ""))).collect(Collectors.toList());
        File[] plikiUczace = listaUczacych.toArray(new File[listaUczacych.size()]);


        HashMap<Integer, Double> progi = wyliczProgi(plikiUczace, liczbaPktGenerowanychPrzezAlgorytm);

        for (File fileWithDESC : listOfFiles) {

            if (fileWithDESC.isFile()) {

                ArrayList<String> lista = gen.pktToArrayList(fileWithDESC, liczbaPktGenerowanychPrzezAlgorytm,false);
                File plik;
                FileWriter zapis;
                if (liczbaPktGenerowanychPrzezAlgorytm == SUFR) {
                    FileWriter fw = new FileWriter(pathToDataBase + "\\BinarSurf\\" + fileWithDESC.getName());
                    BufferedWriter bw = new BufferedWriter(fw);
                    for (String it : binaryzuj(lista, SUFR, progi)) {
                        bw.write(it);
                        bw.newLine();
                    }
                    bw.close();
                    fw.close();
                } else {
                    FileWriter fw = new FileWriter(pathToDataBase + "\\BinarSift\\" + fileWithDESC.getName());
                    BufferedWriter bw = new BufferedWriter(fw);
                    for (String it : binaryzuj(lista, SIFT, progi)) {
                        bw.write(it);
                        bw.newLine();
                    }
                    bw.close();
                    fw.close();
                }
            }
        }
    }

    public List<String> binaryzuj(ArrayList<String> descList, int liczbaPktGenPrzezAlgo, HashMap<Integer, Double> progi) throws IOException {

        Iterator<String> it2 = descList.iterator();// DESKRYPTORY
        int z = 0;
        String pom5;
        List<String> wyjscie = new ArrayList<>();

        while (z < descList.size() / (liczbaPktGenPrzezAlgo + 1)) {
            wyjscie.add(it2.next());
            for (int jj = 1; jj <= liczbaPktGenPrzezAlgo; jj++) {
                pom5 = it2.next();
                if (Double.parseDouble(pom5) >= progi.get(jj)) {
                    wyjscie.add("1");
                } else {
                    wyjscie.add("0");
                }
            }
            z++;
        }
        return wyjscie;
    }

    public HashMap<Integer, Double> wyliczProgi(File[] plikiUczace, int liczbaPktGenerowanychPrzezAlgorytm) throws IOException {
        GeneratorHistogramow2NEW gen = new GeneratorHistogramow2NEW();
        HashMap<Integer, Double> progi = new HashMap<>();

        for (int kk = 1; kk <= liczbaPktGenerowanychPrzezAlgorytm; kk++) {
            progi.put(kk, -5.0);
            progi.put(-kk, 5.0);
        }

        for (File fileWithDESC : plikiUczace) {
            if (fileWithDESC.isFile()) {

                ArrayList<String> descList = gen.pktToArrayList(fileWithDESC, liczbaPktGenerowanychPrzezAlgorytm, false);
                Iterator<String> it2 = descList.iterator();// DESKRYPTORY
                int z = 0;
                String pom7, pom5;

                double maxValDesc = 0;
                double minValDesc = 0;

                // WYLICZENIU PRZESUNIECIA
                while (z < descList.size() / (liczbaPktGenerowanychPrzezAlgorytm + 1)) {
                    pom7 = it2.next();
                    for (int jj = 0; jj < liczbaPktGenerowanychPrzezAlgorytm ; jj++) { // wyliczanie wartosci dla binaryzacji
                        pom5 = it2.next();
                        if (Double.parseDouble(pom5) > progi.get(jj+1)) {
                            progi.put(jj+1, Double.parseDouble(pom5));
                        }
                        if (Double.parseDouble(pom5) < progi.get(-jj-1)) {
                            progi.put(-jj-1, Double.parseDouble(pom5));
                        }
                    }
                    z++;
                }
                //sumaProgow += (maxValDesc + minValDesc) / 2;
            }
        }
        HashMap<Integer, Double> result = new HashMap<>();
        for (int kk = 1; kk <= liczbaPktGenerowanychPrzezAlgorytm; kk++) {
            result.put(kk, (progi.get(kk)+progi.get(-kk))/2);
        }

        return result;
    }

    public static void main(String[] args) throws IOException {
        Binaryzator binaryzator = new Binaryzator();
        binaryzator.binaryzujDesc(PATH_TO_HOME_DATABASE, SUFR);
        binaryzator.binaryzujDesc(PATH_TO_HOME_DATABASE, SIFT);
    }
}
