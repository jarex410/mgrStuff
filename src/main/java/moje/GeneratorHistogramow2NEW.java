package moje;

import moje.histogramy.LoaderHistograms;

import java.io.*;
import java.util.*;

/**
 * Created by JaroLP on 2015-10-31.
 */
public class GeneratorHistogramow2NEW {

    public final static String PATH_TO_RZESZOW_DATABASE = "H:\\MAGISTERKA\\BazyZdjec\\Rzeszow";
    public final static String PATH_TO_OXFORD_DATABASE = "H:\\MAGISTERKA\\BazyZdjec\\oxbuild_images";
    public final static String PATH_TO_ZuBuDu_DATABASE = "H:\\MAGISTERKA\\BazyZdjec\\ZuBuD";

    public final static String PATH_TO_TEST_DATABASE = "H:\\MAGISTERKA\\BazyZdjec\\BAZA_TESTOWA";

    public final static String PATH_TO_RZESZOW_DATABASE_DESC = PATH_TO_RZESZOW_DATABASE + "\\desc";

    public final static String PATH_TO_RZESZOW_DATABASE_POINTS = PATH_TO_RZESZOW_DATABASE + "\\points";

    public final static String PATH_TO_SAVE_HISTOGRAMS_FOR_RZESZOW = PATH_TO_RZESZOW_DATABASE + "\\histogramy";

    public final static String BAZA = "baza";

    public final static String BASKETS = "baskets";

    public final static int SUFR = 64;

    public final static int SIFT = 128;


    private HashMap<Integer, Integer> Histogram;
    private ArrayList<String> basePointsList;


    /**
     * Funkcja inicjalizująca hash mapy wedlug ilosci pkt jakie mają być brane pod uwagę przy porownywaniu
     *
     * @return
     * @throws IOException
     */
    public HashMap init(int numberOfPoints, String pathToDataBase, int liczbaPktGenerowanychPrzezAlgorytm, boolean binary, boolean koszykiWkoszykach) throws IOException {
        String PATH_TO_BASKETS;
        String PATH_TO_BASKETS2;
        if (binary) {
            PATH_TO_BASKETS = pathToDataBase + "\\500\\baskets\\SrodkiPrzedzialowSurfBin.txt";
            PATH_TO_BASKETS2 = pathToDataBase + "\\500\\baskets\\SrodkiPrzedzialowSiftBin.txt";
        } else {
            PATH_TO_BASKETS = pathToDataBase + "\\500\\baskets\\SrodkiPrzedzialowSurf.txt";
            PATH_TO_BASKETS2 = pathToDataBase + "\\500\\baskets\\SrodkiPrzedzialowSift.txt";
        }

        File fileWithBaskets;
        if (liczbaPktGenerowanychPrzezAlgorytm == SUFR) {
            fileWithBaskets = new File(PATH_TO_BASKETS);
        } else {
            fileWithBaskets = new File(PATH_TO_BASKETS2);
        }
        basePointsList = pktToArrayList(fileWithBaskets, liczbaPktGenerowanychPrzezAlgorytm);

        if (koszykiWkoszykach) {
            HashMap<Integer, Set> mapa = new HashMap();
            for (Integer i = 1; i <= numberOfPoints; i++) {
                mapa.put(i, new HashSet<>());
            }
            return mapa;
        } else {
            HashMap<Integer, Integer> mapa2 = new HashMap();
            for (Integer i = 1; i <= numberOfPoints; i++) {
                mapa2.put(i, 0);
            }
            return mapa2;
        }

    }

    /**
     * Metoda ładująca deskryptory z plików do list
     *
     * @param fileWithDESC
     * @return
     * @throws IOException
     */

    public ArrayList pktToArrayList(File fileWithDESC, int liczbaPktGenerowanaPrzezAlgorytm) throws IOException {

        ArrayList<String> list = new ArrayList<>();
        FileReader fr = new FileReader(fileWithDESC);
        BufferedReader bfr = new BufferedReader(fr);
        String pom;

        while ((pom = bfr.readLine()) != null) {
            list.add(pom);
            for (int ii = 0; ii < liczbaPktGenerowanaPrzezAlgorytm; ii++) {
                list.add(bfr.readLine());
            }
        }
        bfr.close();
        fr.close();

        return list;
    }

    public HashMap histogramGeneratorEUCLIDES(int liczbaKoszykow, ArrayList<String> descList, int liczbaPktGenPrzezAlgo, String pathToDatabase) throws IOException {

        HashMap<Integer, Integer> imgHistogram = this.init(liczbaKoszykow, pathToDatabase, liczbaPktGenPrzezAlgo, false, false);
        Iterator<String> iteratorKoszyka = basePointsList.iterator();
        Iterator<String> iteratorSZUKANEGO = descList.iterator();// DESKRYPTORY
        // 2 OBRAZKA
        int skok = 0;
        int licznikObrotu = 0; // ZMIENNE POMOCNA PRZY
        int z = 0;
        String pom6, pom7, pom4, pom5;
        double suma = 0;
        int nrSlupka = 0;

        // WYLICZENIU PRZESUNIECIA
        while (z < descList.size() / (liczbaPktGenPrzezAlgo + 1) && z < liczbaKoszykow) { // PETELKA
            // PRZECHODZ�CA
            // PO
            // KOLEKCJACH
            double min1 = 100; // Najblizsi sasiedzi
            // String wsp1Max = "";
            // String wsp2Max = "";


            for (int kk = 1; kk < basePointsList.size() / (liczbaPktGenPrzezAlgo + 1); kk++) // PETLA
            // KTORA
            // OBSLUGUJE
            // SPRAWDZANIE PKTx1 z listy
            // 1 z PKTx1....xn z listy 2

            {
                pom6 = iteratorKoszyka.next(); // WSPOLRZEDNE PKT
                pom7 = iteratorSZUKANEGO.next();


                //OBLICZANIE EUKLIDESA

                for (int jj = 0; jj < liczbaPktGenPrzezAlgo; jj++) { // PRZECHODZENIE
                    // PO
                    // KOLEKCJACH
                    // W
                    // CELU POBRANIA DANYCH DESC
                    pom4 = iteratorKoszyka.next();
                    pom5 = iteratorSZUKANEGO.next();
                    // System.out.println("POM4  +  POM 6"+pom4 + "/t"+pom5);
                    suma += (Double.parseDouble(pom4) - Double
                            .parseDouble(pom5))
                            * (Double.parseDouble(pom4) - Double
                            .parseDouble(pom5));

                }

                suma = Math.sqrt(suma);

                if (suma < min1) {
                    min1 = suma;
                    nrSlupka = kk;
                }

                suma = 0;

                iteratorSZUKANEGO = descList.iterator();
                for (int zz = 0; zz < skok; zz++) { // PRZESUWANIE
                    // ITERATORA
                    // LISTY
                    // PIERWSZEJ W CELU JEGO
                    // ODPOWIEDNIGO UMIEJSCOWIENIA
                    iteratorSZUKANEGO.next();
                }

            }
            imgHistogram.put(nrSlupka, imgHistogram.get(nrSlupka) + 1);

            nrSlupka = 0;
            iteratorKoszyka = basePointsList.iterator();

            skok = (liczbaPktGenPrzezAlgo + 1) * ++licznikObrotu; // WYLICZANIE WARTOSCI
            // PRZESUNI�CIA
            // ITERATORA PIERWSZEJ LISTY

            z++;

        }
        return imgHistogram;
    }


    public void generujHistogramyDlaBazy(String pathToDataBase, int liczbaPktGenerowanychPrzezAlgorytm, int liczbaKoszykow, boolean binarny
    ) throws IOException {

        File folder = null;
        if (liczbaPktGenerowanychPrzezAlgorytm == SUFR && !binarny) {
            folder = new File(pathToDataBase + "\\desc");
        } else if (liczbaPktGenerowanychPrzezAlgorytm == SIFT && !binarny) {
            folder = new File(pathToDataBase + "\\descSIFT");
        } else if (liczbaPktGenerowanychPrzezAlgorytm == SUFR && binarny) {
            folder = new File(pathToDataBase + "\\BinarSurf");
        } else if (liczbaPktGenerowanychPrzezAlgorytm == SIFT && binarny) {
            folder = new File(pathToDataBase + "\\BinarSift");
        }

        File[] listOfFiles = folder.listFiles();

        for (File fileWithDESC : listOfFiles) {

            if (fileWithDESC.isFile()) {

                HashMap histogram;

                ArrayList<String> lista = pktToArrayList(fileWithDESC, liczbaPktGenerowanychPrzezAlgorytm);
                if (binarny) {
                    histogram = histogramGeneratorHAMMING(liczbaKoszykow, lista, liczbaPktGenerowanychPrzezAlgorytm, pathToDataBase);
                    //histogram = koszykiWKoszykach(liczbaKoszykow, lista, liczbaPktGenerowanychPrzezAlgorytm, pathToDataBase, 20.0);

                } else {
                    histogram = histogramGeneratorEUCLIDES(liczbaKoszykow, lista, liczbaPktGenerowanychPrzezAlgorytm, pathToDataBase);
                }
                File plik;
                if (liczbaPktGenerowanychPrzezAlgorytm == SUFR) {
                    if (binarny) {
                        plik = new File(pathToDataBase + "\\500\\HistogramySurfBin\\" + fileWithDESC.getName());
                    } else {
                        plik = new File(pathToDataBase + "\\500\\HistogramySurf\\" + fileWithDESC.getName());
                    }
                } else {
                    if (binarny) {
                        plik = new File(pathToDataBase + "\\500\\HistogramySiftBin\\" + fileWithDESC.getName());
                    } else {
                        plik = new File(pathToDataBase + "\\500\\HistogramySift\\" + fileWithDESC.getName());
                    }
                }
                FileWriter zapis = new FileWriter(plik, true);
                zapis.write(histogram.toString());
                zapis.close();
            }
        }
    }


    /**
     * metoda do sumowania histogramow
     *
     * @param pathToDataBase
     * @throws IOException
     */
    public void histogramSumator(String pathToDataBase) throws IOException {

        LoaderHistograms um = new LoaderHistograms();

        File folder1 = new File(pathToDataBase + "\\HistogramySurf\\");
        File folder2 = new File(pathToDataBase + "\\HistogramySift\\");


        File[] listOfFilesSurf = folder1.listFiles();
        File[] listOfFilesSift = folder2.listFiles();


        for (int i = 0; i < listOfFilesSurf.length; i++) {
            if (listOfFilesSurf[i] != null && listOfFilesSurf[i].isFile()) {
                HashMap<Integer, Integer> surfMap = um.fileToHasMap(listOfFilesSurf[i]); //wczytanie bazowej mapy
                HashMap<Integer, Integer> siftMap = um.fileToHasMap(listOfFilesSift[i]);

                HashMap<Integer, Integer> sumOfHistMap = new HashMap<>();

                //sumOfHistMap.putAll(surfMap);

                for (Map.Entry<Integer, Integer> entry : surfMap.entrySet()) {
                    sumOfHistMap.put(entry.getKey() + 1, entry.getValue());
                }

                for (Map.Entry<Integer, Integer> entry : siftMap.entrySet()) {
                    sumOfHistMap.put(entry.getKey() + surfMap.size() + 1, entry.getValue());
                }
                File plik = new File(pathToDataBase + "\\HistogramySurfSift\\" + listOfFilesSurf[i].getName());
                FileWriter zapis = new FileWriter(plik, true);
                zapis.write(sumOfHistMap.toString());
                zapis.close();
            }

        }

    }


    /**
     * MEtoda generujaca histogramy na podstawie hamminga
     *
     * @param liczbaKoszykow
     * @param descList
     * @param liczbaPktGenPrzezAlgo
     * @param pathToDatabase
     * @return
     * @throws IOException
     */
    public HashMap histogramGeneratorHAMMING(int liczbaKoszykow, ArrayList<String> descList, int liczbaPktGenPrzezAlgo, String pathToDatabase) throws IOException {

        HashMap<Integer, Integer> imgHistogram = this.init(liczbaKoszykow, pathToDatabase, liczbaPktGenPrzezAlgo, true, false);
        Iterator<String> iteratorBazowek = basePointsList.iterator();
        Iterator<String> iteratorPorownowanego = descList.iterator();// DESKRYPTORY
        // 2 OBRAZKA
        int skok = 0;
        int licznikObrotu = 0; // ZMIENNE POMOCNA PRZY
        int z = 0;
        String pom6, pom7, pom4, pom5;
        double suma = 0;
        int nrSlupka = 0;

        // WYLICZENIU PRZESUNIECIA
        while (z < descList.size() / (liczbaPktGenPrzezAlgo + 1) && z < liczbaKoszykow) { // PETELKA
            // PRZECHODZ�CA
            // PO
            // KOLEKCJACH
            double min1 = 1000; // Najblizsi sasiedzi
            // String wsp1Max = "";
            // String wsp2Max = "";


            for (int kk = 1; kk < basePointsList.size() / (liczbaPktGenPrzezAlgo + 1); kk++) // PETLA
            // KTORA
            // OBSLUGUJE
            // SPRAWDZANIE PKTx1 z listy
            // 1 z PKTx1....xn z listy 2

            {
                pom6 = iteratorBazowek.next(); // WSPOLRZEDNE PKT
                pom7 = iteratorPorownowanego.next();


                //OBLICZANIE EUKLIDESA

                for (int jj = 0; jj < liczbaPktGenPrzezAlgo; jj++) { // PRZECHODZENIE
                    // PO
                    // KOLEKCJACH
                    // W
                    // CELU POBRANIA DANYCH DESC
                    pom4 = iteratorBazowek.next();
                    pom5 = iteratorPorownowanego.next();
                    // System.out.println("POM4  +  POM 6"+pom4 + "/t"+pom5);


                    //HAMMING


                    suma += Double.parseDouble(pom4) != Double
                            .parseDouble(pom5) ? 1 : 0;     //HAMMING

                }

                if (suma < min1) {
                    min1 = suma;
                    nrSlupka = kk;
                }

                suma = 0;

                iteratorPorownowanego = descList.iterator();
                for (int zz = 0; zz < skok; zz++) { // PRZESUWANIE
                    // ITERATORA
                    // LISTY
                    // PIERWSZEJ W CELU JEGO
                    // ODPOWIEDNIGO UMIEJSCOWIENIA
                    iteratorPorownowanego.next();
                }

            }
            imgHistogram.put(nrSlupka, imgHistogram.get(nrSlupka) + 1);

            nrSlupka = 0;
            iteratorBazowek = basePointsList.iterator();

            skok = (liczbaPktGenPrzezAlgo + 1) * ++licznikObrotu; // WYLICZANIE WARTOSCI
            // PRZESUNI�CIA
            // ITERATORA PIERWSZEJ LISTY

            z++;

        }
        return imgHistogram;
    }





    public HashMap koszykiWKoszykach(int liczbaKoszykow, ArrayList<String> descList, int liczbaPktGenPrzezAlgo, String pathToDatabase, double promien) throws IOException {

        HashMap<Integer, Set<String>> imgHistogram = this.init(liczbaKoszykow + 1, pathToDatabase, liczbaPktGenPrzezAlgo, false, true);
        Iterator<String> iteratorKoszyka = basePointsList.iterator();
        Iterator<String> iteratorSZUKANEGO = descList.iterator();// DESKRYPTORY
        // 2 OBRAZKA
        int skok = 0;
        int licznikObrotu = 0; // ZMIENNE POMOCNA PRZY
        int z = 0;
        String pom6, pom7, pom4, pom5;
        double suma = 0;
        String wsplPierwszegoSasiada = "";
        int nrSlupka = 0;
        double odleglosc = 0.0;
        boolean lowerFlag = false;

        // WYLICZENIU PRZESUNIECIA
        while (z < descList.size() / (liczbaPktGenPrzezAlgo + 1) && z < liczbaKoszykow) { // PETELKA
            // PRZECHODZ�CA
            // PO
            // KOLEKCJACH
            double min1 = 1000; // Najblizsi sasiedzi
            // String wsp1Max = "";
            // String wsp2Max = "";

            Set<String> listaSasiadow = new HashSet<>();

            for (int kk = 1; kk < basePointsList.size() / (liczbaPktGenPrzezAlgo + 1); kk++) // PETLA
            // KTORA
            // OBSLUGUJE
            // SPRAWDZANIE PKTx1 z listy
            // 1 z PKTx1....xn z listy 2


            {
                pom6 = iteratorKoszyka.next(); // WSPOLRZEDNE PKT
                pom7 = iteratorSZUKANEGO.next();


                String[] split2 = pom7.trim().split(" ");

                Double x2 = Double.valueOf(split2[1]);
                Double y2 = Double.valueOf(split2[2]);


                //OBLICZANIE EUKLIDESA

                for (int jj = 0; jj < liczbaPktGenPrzezAlgo; jj++) { // PRZECHODZENIE
                    // PO
                    // KOLEKCJACH
                    // W
                    // CELU POBRANIA DANYCH DESC
                    pom4 = iteratorKoszyka.next();
                    pom5 = iteratorSZUKANEGO.next();
                    // System.out.println("POM4  +  POM 6"+pom4 + "/t"+pom5);
                    suma += Double.parseDouble(pom4) != Double
                            .parseDouble(pom5) ? 1 : 0;     //HAMMING

                }

                listaSasiadow = imgHistogram.get(kk);
                if (listaSasiadow.size() != 0) {
                    for (String sasiad : listaSasiadow) {
                        String[] wspl = sasiad.trim().split(" ");
                        Double x = Double.valueOf(wspl[1]);
                        Double y = Double.valueOf(wspl[2]);

                        odleglosc = Math.sqrt((x2 - x) * (x2 - x) + (y2 - y) * (y2 - y));
                        if (odleglosc < promien) {
                            lowerFlag = true;
                            break;
                        }
                    }
                }


                if (suma < min1 && (lowerFlag || listaSasiadow.size() == 0)) {

                    min1 = suma;
                    nrSlupka = kk;
                    wsplPierwszegoSasiada = pom7;
                }
                lowerFlag = false;
                suma = 0;

                iteratorSZUKANEGO = descList.iterator();
                for (int zz = 0; zz < skok; zz++) { // PRZESUWANIE
                    // ITERATORA
                    // LISTY
                    // PIERWSZEJ W CELU JEGO
                    // ODPOWIEDNIGO UMIEJSCOWIENIA
                    iteratorSZUKANEGO.next();
                }

            }
            Set<String> nowySetSasiadow = imgHistogram.get(nrSlupka);
            nowySetSasiadow.add(wsplPierwszegoSasiada);
            imgHistogram.put(nrSlupka, nowySetSasiadow);

            nrSlupka = 0;
            iteratorKoszyka = basePointsList.iterator();

            skok = (liczbaPktGenPrzezAlgo + 1) * ++licznikObrotu; // WYLICZANIE WARTOSCI
            // PRZESUNI�CIA
            // ITERATORA PIERWSZEJ LISTY

            z++;

        }
        HashMap<Integer, Integer> result = new HashMap<>();
        int su = 0;
        for (int zz = 1; zz < liczbaKoszykow + 1; zz++) {
            su += imgHistogram.get(zz).size();
            result.put(zz, imgHistogram.get(zz).size());
        }

        return result;
    }


    public static void main(String[] args) throws IOException {

        GeneratorHistogramow2NEW generatorHistogramow2NEW = new GeneratorHistogramow2NEW();

        generatorHistogramow2NEW.generujHistogramyDlaBazy(PATH_TO_OXFORD_DATABASE, SUFR, 500, true);
        generatorHistogramow2NEW.generujHistogramyDlaBazy(PATH_TO_OXFORD_DATABASE, SIFT, 500, true);

        //  generatorHistogramow2NEW.histogramSumator(PATH_TO_TEST_DATABASE);

    }
}
