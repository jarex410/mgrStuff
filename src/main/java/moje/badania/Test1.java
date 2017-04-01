package moje.badania;

import moje.histogramy.HistogramComparator;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import static moje.GeneratorHistogramow2NEW.PATH_TO_RZESZOW_DATABASE;
import static moje.GeneratorHistogramow2NEW.PATH_TO_TEST_DATABASE;
import static moje.GeneratorKoszykowNEW.SUFR;

/**
 * Created by JaroLP on 2016-11-12.
 * <p>
 * Operacje na histogramach tylko dla SUrfa
 */
public class Test1 {

    public final static String PATH_TO_TEST_FOLDER = "H:\\MAGISTERKA\\TESTY\\TEST_BAZA_TESTOWA\\";

    /**
     * TEST LOSUJE LICZBE ZDJĘC DO ROZPOZNANIA Z BAZY I ZWRACA WYNIKI POROWNANIA W POSTACI HASH MAP.
     *
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        Test1 test1 = new Test1();
        HashMap[] wynikiOstateczne;

        wynikiOstateczne = test1.test1(200, PATH_TO_RZESZOW_DATABASE);

        File fileOut = new File(PATH_TO_TEST_FOLDER + "\\PSurfKoszykiBin.txt");
        FileWriter zapis = new FileWriter(fileOut, true);
        zapis.write(wynikiOstateczne[0].toString());
        zapis.close();

        File fileOut2 = new File(PATH_TO_TEST_FOLDER + "\\WSurfKoszykiBin.txt");
        FileWriter zapis2 = new FileWriter(fileOut2, true);
        zapis2.write(wynikiOstateczne[1].toString());
        zapis2.close();
    }

    public HashMap[] test1(int liczbaObrazowDoLosowanychDoTestow, String bazaDanych) throws IOException {

        File folder = new File(bazaDanych + "\\KoszykiWKoszykachSurfBin\\");
        File folderUczacych = new File(bazaDanych + "\\Uczace\\");

        File[] listOfFiles = folder.listFiles();


        List<String> nazwyUczacych = new ArrayList<File>(Arrays.asList(folderUczacych.listFiles())).stream().map(x -> x.getName()).collect(Collectors.toList());
        List<File> listaUczacych = new ArrayList<File>(Arrays.asList(listOfFiles)).stream().filter(x->nazwyUczacych.contains(x.getName().substring(0,12))).collect(Collectors.toList());
        File[] histogramyObrazowUczacych = listaUczacych.toArray(new File[listaUczacych.size()]);

        List<File> obrazyTestoweLista = new ArrayList<File>(Arrays.asList(listOfFiles)).stream().filter(x->!nazwyUczacych.contains(x.getName().substring(0,12))).collect(Collectors.toList());
        File[] obrazyTestoweTab = obrazyTestoweLista.toArray(new File[obrazyTestoweLista.size()]);


        Random random = new Random();

        HashMap<String, String> wyniki = new HashMap<>();
        HashMap<String, Integer> podsumowanie = new HashMap<>();

        HistogramComparator histogramComparator = new HistogramComparator();

        File[] wylosowaneObrazy = new File[liczbaObrazowDoLosowanychDoTestow];


        //losowanie obrazów narazie nie potrzebne


/*        for (int i = 0; i < liczbaObrazowDoLosowanychDoTestow; i++) {
            int pozycja = random.nextInt(listOfFiles.length);
            while (listOfFiles[pozycja] == null || nazwyUczacych.contains(listOfFiles[pozycja].getName().substring(10, 22))) {
                pozycja = random.nextInt(listOfFiles.length);
            }
            wylosowaneObrazy[i] = listOfFiles[pozycja];
            listOfFiles[pozycja] = null;
        }*/

        wylosowaneObrazy = obrazyTestoweTab; //zamiast losowania wszystkie obrazy poza testowymi

        int liczbaPoprawnych = 0;

        for (File fileToCompare : wylosowaneObrazy) {
            wyniki.putAll(histogramComparator.comparator(fileToCompare, null, histogramyObrazowUczacych, 200, SUFR));
            podsumowanie.putAll(statystyka(fileToCompare.getName(), wyniki.get(fileToCompare.getName())));
            if (podsumowanie.get(fileToCompare.getName()) == 1) {
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
     *
     * @param nazwaPorownywanego
     * @param typ
     * @return
     */
    public HashMap<String, Integer> statystyka(String nazwaPorownywanego, String typ) {
        HashMap<String, Integer> podsumowanie = new HashMap<>();

        if (nazwaPorownywanego.substring(0,2).contains(typ.substring(0,2))) {
            podsumowanie.put(nazwaPorownywanego, 1);
        } else {
            podsumowanie.put(nazwaPorownywanego, 0);
        }

        return podsumowanie;
    }

}

