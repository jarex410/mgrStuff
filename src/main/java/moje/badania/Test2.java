package moje.badania;

import moje.histogramy.HistogramComparator;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Random;

import static moje.GeneratorHistogramow2NEW.SUFR;
import static moje.histogramy.HistogramComparator.PATH_TO_RZESZOW_DATABASE;

/**
 * Created by JaroLP on 2016-11-12.
 *
 * Operacje na histogramach tylko dla SUrfa
 */
public class Test2{

    public final static String PATH_TO_TEST_FOLDER = "H:\\MAGISTERKA\\TESTY\\TEST2";

    /**
     * TEST LOSUJE LICZBE ZDJĘC DO ROZPOZNANIA Z BAZY I ZWRACA WYNIKI POROWNANIA W POSTACI HASH MAP.
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        Test2 test2 = new Test2();
        HashMap[] wynikiOstateczne;

        wynikiOstateczne = test2.test2(20, PATH_TO_RZESZOW_DATABASE);

        File fileOut = new File(PATH_TO_TEST_FOLDER + "\\test2Porownanie.txt");
        FileWriter zapis = new FileWriter(fileOut, true);
        zapis.write(wynikiOstateczne[0].toString());
        zapis.close();

        File fileOut2 = new File(PATH_TO_TEST_FOLDER + "\\test2SWyniki.txt");
        FileWriter zapis2 = new FileWriter(fileOut2, true);
        zapis2.write(wynikiOstateczne[1].toString());
        zapis2.close();
    }

    public HashMap[] test2(int liczbaObrazowDoLosowanychDoTestow, String bazaDanych) throws IOException {

        File folderSurf = new File(bazaDanych + "\\HistogramySurf\\");
        File[] listOfFilesSurf = folderSurf.listFiles();

        File folderSift = new File(bazaDanych + "\\HistogramySift\\");
        File[] listOfFilesSift = folderSift.listFiles();

        Random random = new Random();

        HashMap<String, String> wyniki = new HashMap<>();
        HashMap<String, Integer> podsumowanie = new HashMap<>();

        HistogramComparator histogramComparator = new HistogramComparator();

        File[] wylosowaneObrazySufr = new File[liczbaObrazowDoLosowanychDoTestow];
        File[] wylosowaneObrazySift = new File[liczbaObrazowDoLosowanychDoTestow];

        for (int i = 0; i < liczbaObrazowDoLosowanychDoTestow; i++) {
            int pozycja = random.nextInt(listOfFilesSurf.length);
            wylosowaneObrazySufr[i] = listOfFilesSurf[pozycja];
            wylosowaneObrazySift[i] = listOfFilesSift[pozycja];
            listOfFilesSurf[pozycja] = null;
            listOfFilesSift[pozycja] = null;
        }

        int liczbaPoprawnych = 0;

        for (File fileToCompare : wylosowaneObrazySufr) {
            wyniki.putAll(histogramComparator.comparator(fileToCompare, null, listOfFilesSurf, 200, SUFR));
            podsumowanie.putAll(statystyka(fileToCompare.getName(), wyniki.get(fileToCompare.getName())));
            if(podsumowanie.get(fileToCompare.getName()) == 1){
                liczbaPoprawnych++;
            }
        }
        podsumowanie.put("POPRAWNYCH", liczbaPoprawnych);
        HashMap[] wynikiOstaeczne = new HashMap[2];
        wynikiOstaeczne[0] = wyniki;
        wynikiOstaeczne[1] = podsumowanie;

        return wynikiOstaeczne;
    }

    /**
     * TWORZY HASH MAPE KTÓREJ KLUCZEM JEST NAZWA ROZPOZNAWANEGO OBRAZU NATOMIAST KLUCZEM JEST WARTOŚĆ 1 LUB 0
     * @param nazwaPorownywanego
     * @param typ
     * @return
     */
    public HashMap<String, Integer> statystyka(String nazwaPorownywanego, String typ) {
        HashMap<String, Integer> podsumowanie = new HashMap<>();

        if (nazwaPorownywanego.contains(typ.substring(0, 13))) {
            podsumowanie.put(nazwaPorownywanego, 1);
        } else {
            podsumowanie.put(nazwaPorownywanego, 0);
        }

        return podsumowanie;
    }

}

