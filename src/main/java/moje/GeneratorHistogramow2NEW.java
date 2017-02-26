package moje;

import moje.histogramy.LoaderHistograms;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by JaroLP on 2015-10-31.
 */
public class GeneratorHistogramow2NEW {

    public final static String PATH_TO_RZESZOW_DATABASE = "H:\\MAGISTERKA\\BazyZdjec\\Rzeszow";

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
    public HashMap init(int numberOfPoints, String pathToDataBase, int liczbaPktGenerowanychPrzezAlgorytm) throws IOException {

        final String PATH_TO_BASKETS = pathToDataBase + "\\baskets\\SrodkiPrzedzialowSurf.txt";
        final String PATH_TO_BASKETS2 = pathToDataBase + "\\baskets\\SrodkiPrzedzialowSift.txt";

        HashMap<Integer, Integer> mapa = new HashMap();
        File fileWithBaskets;
        if (liczbaPktGenerowanychPrzezAlgorytm == SUFR) {
            fileWithBaskets = new File(PATH_TO_BASKETS);
        } else {
            fileWithBaskets = new File(PATH_TO_BASKETS2);
        }
        basePointsList = pktToArrayList(fileWithBaskets, liczbaPktGenerowanychPrzezAlgorytm);
        for (Integer i = 1; i <= numberOfPoints; i++) {
            mapa.put(i, 0);
        }
        return mapa;
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

    public HashMap histogramGenerator(int liczbaKoszykow, ArrayList<String> descList, int liczbaPktGenPrzezAlgo) throws IOException {

        HashMap<Integer, Integer> imgHistogram = this.init(liczbaKoszykow, PATH_TO_RZESZOW_DATABASE, liczbaPktGenPrzezAlgo);
        Iterator<String> it = basePointsList.iterator();
        Iterator<String> it2 = descList.iterator();// DESKRYPTORY
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
                pom6 = it.next(); // WSPOLRZEDNE PKT
                pom7 = it2.next();


                //OBLICZANIE EUKLIDESA

                for (int jj = 0; jj < liczbaPktGenPrzezAlgo; jj++) { // PRZECHODZENIE
                    // PO
                    // KOLEKCJACH
                    // W
                    // CELU POBRANIA DANYCH DESC
                    pom4 = it.next();
                    pom5 = it2.next();
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

                it2 = descList.iterator();
                for (int zz = 0; zz < skok; zz++) { // PRZESUWANIE
                    // ITERATORA
                    // LISTY
                    // PIERWSZEJ W CELU JEGO
                    // ODPOWIEDNIGO UMIEJSCOWIENIA
                    it2.next();
                }

            }
            imgHistogram.put(nrSlupka, imgHistogram.get(nrSlupka) + 1);

            nrSlupka = 0;
            it = basePointsList.iterator();

            skok = (liczbaPktGenPrzezAlgo + 1) * ++licznikObrotu; // WYLICZANIE WARTOSCI
            // PRZESUNI�CIA
            // ITERATORA PIERWSZEJ LISTY

            z++;

        }
        return imgHistogram;
    }


    public void generujHistogramyDlaBazy(String pathToDataBase, int liczbaPktGenerowanychPrzezAlgorytm, int liczbaKoszykow) throws IOException {

        File folder;
        if (liczbaPktGenerowanychPrzezAlgorytm == SUFR) {
            folder = new File(pathToDataBase + "\\desc");
        } else {
            folder = new File(pathToDataBase + "\\descSIFT");
        }
        File[] listOfFiles = folder.listFiles();

        for (File fileWithDESC : listOfFiles) {

            if (fileWithDESC.isFile()) {

                ArrayList<String> lista = pktToArrayList(fileWithDESC, liczbaPktGenerowanychPrzezAlgorytm);
                HashMap histogram = histogramGenerator(liczbaKoszykow, lista, liczbaPktGenerowanychPrzezAlgorytm);

                File plik;
                if (liczbaPktGenerowanychPrzezAlgorytm == SUFR) {
                    plik = new File(pathToDataBase + "\\HistogramySurf\\Histogram_" + fileWithDESC.getName());
                } else {
                    plik = new File(pathToDataBase + "\\HistogramySift\\Histogram_" + fileWithDESC.getName());
                }
                FileWriter zapis = new FileWriter(plik, true);
                zapis.write(histogram.toString());
                zapis.close();
            }
        }
    }


    /**
     * metoda do sumowania histogramow
     * @param pathToDataBase
     * @param pathToFirstHistograms
     * @param pathTOSecendHistograms
     * @throws IOException
     */
    public void histogramSumator(String pathToDataBase, String pathToFirstHistograms, String pathTOSecendHistograms) throws IOException {

        LoaderHistograms um = new LoaderHistograms();

        File folder1 = new File(pathToFirstHistograms);
        File folder2 = new File(pathTOSecendHistograms);


        File[] listOfFilesSurf = folder1.listFiles();
        File[] listOfFilesSift = folder2.listFiles();


        for (int i = 0; i < listOfFilesSurf.length; i++) {
            if (listOfFilesSurf[i] != null && listOfFilesSurf[i].isFile()) {
                HashMap<Integer, Integer> surfMap = um.fileToHasMap(listOfFilesSurf[i]); //wczytanie bazowej mapy
                HashMap<Integer, Integer> siftMap = um.fileToHasMap(listOfFilesSift[i]);

                HashMap<Integer, Integer> sumOfHistMap = new HashMap<>();

                sumOfHistMap.putAll(surfMap);

                for (Map.Entry<Integer, Integer> entry : siftMap.entrySet()) {
                    sumOfHistMap.put(entry.getKey() + surfMap.size(), entry.getValue());
                }
                File plik = new File(pathToDataBase + "\\HistogramySurfSift\\Histogram_" + listOfFilesSurf[i].getName());
                FileWriter zapis = new FileWriter(plik, true);
                zapis.write(sumOfHistMap.toString());
                zapis.close();
            }

        }

    }

    public static void main(String[] args) throws IOException {

        GeneratorHistogramow2NEW generatorHistogramow2NEW = new GeneratorHistogramow2NEW();

        //generatorHistogramow2NEW.generujHistogramyDlaBazy(PATH_TO_RZESZOW_DATABASE,SUFR,200);
        // generatorHistogramow2NEW.generujHistogramyDlaBazy(PATH_TO_RZESZOW_DATABASE,SIFT,200);

        generatorHistogramow2NEW.histogramSumator(PATH_TO_RZESZOW_DATABASE,"H:\\MAGISTERKA\\BazyZdjec\\Rzeszow\\HistogramySurf\\", "H:\\MAGISTERKA\\BazyZdjec\\Rzeszow\\Histogramy\\" );

    }

}
