package moje.histogramy;

import com.sun.xml.internal.bind.v2.TODO;
import moje.GeneratorHistogramow2NEW;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import static moje.GeneratorKoszykowNEW.PATH_TO_RZESZOW_DATABASE;
import static moje.GeneratorKoszykowNEW.SUFR;

/**
 * Created by JaroLP on 2015-10-31.
 * <p>
 * Klasa porównujaca histogramy;
 * Na HashMapach
 */
public class HammingComparator {


    public final static String PATH_TO_RZESZOW_DATABASE = "H:\\MAGISTERKA\\BazyZdjec\\Rzeszow";
    public final static String PATH_FOR_COMPARING_FILES = "H:\\MAGISTERKA\\TESTY";

    public void porownuj(String pathToDataBase, int liczbaPktGenerowanychPrzezAlgorytm, int liczbaPktBranychPodUwage) throws IOException {

        File folder;
        if (liczbaPktGenerowanychPrzezAlgorytm == SUFR) {
            folder = new File(pathToDataBase + "\\BinarSurf");
        } else {
            folder = new File(pathToDataBase + "\\BinarSIft");
        }
        File[] listOfFiles = folder.listFiles();

        /** TO DO
         * DODAC LOSOWANIE PLIKOW
         */

        for (File fileWithDESC : listOfFiles) {

            if (fileWithDESC.isFile()) {
                hammingComparator(fileWithDESC, listOfFiles, liczbaPktGenerowanychPrzezAlgorytm,liczbaPktBranychPodUwage);
            }
        }
    }


        public HashMap hammingComparator(File fileToCompare, File[] learningFiles, int liczbaPktGenPrzezAlgo, int liczbaPktBranychPodUwage) throws IOException {

        HashMap<String, String> result = new HashMap<>();
        GeneratorHistogramow2NEW gen = new GeneratorHistogramow2NEW();
        int suma;

        List<String> descPor = gen.pktToArrayList(fileToCompare, liczbaPktGenPrzezAlgo,false);

        int skok = 0;
        int licznikObrotu = 0; // ZMIENNE POMOCNA PRZY
        int z = 0;
        String pom6, pom7, pom4, pom5;
        String typ = "";



        for (File file : learningFiles) {
            if (file != null && file.isFile()) {

                List<String> descBazowki = gen.pktToArrayList(file, liczbaPktGenPrzezAlgo,false);

                Iterator<String> itPor = descPor.iterator();
                Iterator<String> itBaz = descBazowki.iterator();// DESKRYPTORY
                suma = 0;


                while (z < descPor.size() / (liczbaPktGenPrzezAlgo + 1) && z < liczbaPktBranychPodUwage) { // PETELKA
                    // PRZECHODZ�CA
                    // PO
                    // KOLEKCJACH
                    double min1 = 1000; // Najblizsi sasiedzi
                    // String wsp1Max = "";
                    // String wsp2Max = "";


                    for (int kk = 1; kk < descBazowki.size() / (liczbaPktGenPrzezAlgo + 1); kk++) // PETLA
                    // KTORA
                    // OBSLUGUJE
                    // SPRAWDZANIE PKTx1 z listy
                    // 1 z PKTx1....xn z listy 2

                    {
                        pom6 = itPor.next(); // WSPOLRZEDNE PKT
                        pom7 = itBaz.next();


                        for (int jj = 0; jj < liczbaPktGenPrzezAlgo; jj++) { // PRZECHODZENIE
                            // PO
                            // KOLEKCJACH
                            // W
                            // CELU POBRANIA DANYCH DESC
                            pom4 = itPor.next();
                            pom5 = itBaz.next();
                            // System.out.println("POM4  +  POM 6"+pom4 + "/t"+pom5);

                            //HAMMING
                            suma += Integer.parseInt(pom4)!=Integer.parseInt(pom5)?1:0;     //HAMMING

                        }

                        if (suma < min1) {
                            min1 = suma;
                            typ = file.getName();
                        }

                        suma = 0;

                        itPor = descPor.iterator();
                        for (int zz = 0; zz < skok; zz++) { // PRZESUWANIE
                            // ITERATORA
                            // LISTY
                            // PIERWSZEJ W CELU JEGO
                            // ODPOWIEDNIGO UMIEJSCOWIENIA
                            itPor.next();
                        }
                    }


                    itBaz = descBazowki.iterator();

                    skok = (liczbaPktGenPrzezAlgo + 1) * ++licznikObrotu; // WYLICZANIE WARTOSCI
                    // PRZESUNI�CIA
                    // ITERATORA PIERWSZEJ LISTY

                    z++;

                }

                result.put(fileToCompare.getName(),typ);
                typ = "";
            }
        }
        return result;
    }

    public static void main(String[] args) throws IOException {
        HammingComparator hammingComparator = new HammingComparator();
        File file = new File("H:\\MAGISTERKA\\BazyZdjec\\Rzeszow\\BinarSurf\\00_00_00.jpg.DESC");
        hammingComparator.porownuj(PATH_TO_RZESZOW_DATABASE,SUFR,200);

    }
}