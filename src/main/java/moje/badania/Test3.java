package moje.badania;

import moje.histogramy.HistogramComparator;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import static moje.GeneratorKoszykowNEW.SUFR;
import static moje.histogramy.HistogramComparator.PATH_TO_RZESZOW_DATABASE;

/**
 * Created by JaroLP on 2016-11-12.
 *
 * Operacje na histogramach tylko dla SUrfa
 */
public class Test3 {

    public final static String PATH_TO_TEST_FOLDER = "H:\\MAGISTERKA\\TESTY\\TEST3\\";

    /**
     * TEST LOSUJE LICZBE ZDJĘC DO ROZPOZNANIA Z BAZY I ZWRACA WYNIKI POROWNANIA W POSTACI HASH MAP.
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        Test3 test3 = new Test3();
        HashMap[] wynikiOstateczne;

        wynikiOstateczne = test3.test3(500, PATH_TO_RZESZOW_DATABASE);

        File fileOut = new File(PATH_TO_TEST_FOLDER + "\\test1Porownanie1JEDEN_HIST.txt");
        FileWriter zapis = new FileWriter(fileOut, true);
        zapis.write(wynikiOstateczne[0].toString());
        zapis.close();

        File fileOut2 = new File(PATH_TO_TEST_FOLDER + "\\test1SWyniki2JEDEN_HIST.txt");
        FileWriter zapis2 = new FileWriter(fileOut2, true);
        zapis2.write(wynikiOstateczne[1].toString());
        zapis2.close();

        File fileOut3 = new File(PATH_TO_TEST_FOLDER + "\\test1Porownanie3.txt");
        FileWriter zapis3 = new FileWriter(fileOut3, true);
        zapis3.write(wynikiOstateczne[2].toString());
        zapis3.close();

        File fileOut4 = new File(PATH_TO_TEST_FOLDER + "\\test1SWyniki4.txt");
        FileWriter zapis4 = new FileWriter(fileOut4, true);
        zapis4.write(wynikiOstateczne[3].toString());
        zapis4.close();
    }

    public HashMap[] test3(int liczbaObrazowDoLosowanychDoTestow, String bazaDanych) throws IOException {

        File folder = new File(bazaDanych + "\\KoszykiWKoszykachSurf\\");

        File folderSum = new File(bazaDanych + "\\KoszykiWKoszykachSift\\");

        File[] listOfHistogramSum = folderSum.listFiles();

        File folderUczacych = new File(bazaDanych + "\\Uczace\\");
        List<String> nazwyUczacych = new ArrayList<File>(Arrays.asList(folderUczacych.listFiles())).stream().map(x->x.getName()).collect(Collectors.toList());
        File[] listOfFiles = folder.listFiles();

        List<File> listaUczacych = new ArrayList<File>(Arrays.asList(listOfFiles)).stream().filter(x->nazwyUczacych.contains(x.getName().substring(10,22))).collect(Collectors.toList());
        File[] listaHistogramowPojedynczych = listaUczacych.toArray(new File[listaUczacych.size()]);


        List<File> listaUczacychSum = new ArrayList<File>(Arrays.asList(listOfHistogramSum)).stream().filter(x->nazwyUczacych.contains(x.getName().substring(10,22))).collect(Collectors.toList());
        File[] listaSumHistogramow = listaUczacychSum.toArray(new File[listaUczacych.size()]);

        Random random = new Random();

        HashMap<String, String> wyniki = new HashMap<>();
        HashMap<String, Integer> podsumowanie = new HashMap<>();

        HistogramComparator histogramComparator = new HistogramComparator();

        File[] wylosowaneObrazyJedenHis = new File[liczbaObrazowDoLosowanychDoTestow];

        File[] wylosowaneObrazySumHistogram = new File[liczbaObrazowDoLosowanychDoTestow];
        //Lista surfów
        for (int i = 0; i < liczbaObrazowDoLosowanychDoTestow; i++) {
            int pozycja = random.nextInt(listOfFiles.length);
            while(listOfFiles[pozycja] == null || nazwyUczacych.contains(listOfFiles[pozycja].getName().substring(10,22))){
                pozycja = random.nextInt(listOfFiles.length);
            }
            wylosowaneObrazyJedenHis[i] = listOfFiles[pozycja];
            wylosowaneObrazySumHistogram[i] = listOfHistogramSum[pozycja];
            listOfFiles[pozycja] = null;
            listOfHistogramSum[pozycja]=null;
        }

        int liczbaPoprawnych = 0;

        for (File fileToCompare : wylosowaneObrazyJedenHis) {
            wyniki.putAll(histogramComparator.comparator(fileToCompare, null, listOfFiles, 200, SUFR));
            podsumowanie.putAll(statystyka(fileToCompare.getName(), wyniki.get(fileToCompare.getName())));
            if(podsumowanie.get(fileToCompare.getName()) == 1){
                liczbaPoprawnych++;
            }
        }

        HashMap<String, String> wyniki2 = new HashMap<>(); //sumawane histogramy
        HashMap<String, Integer> podsumowanie2 = new HashMap<>();


        podsumowanie.put("POPRAWNYCH", liczbaPoprawnych);
        HashMap[] wynikiOstaeczne = new HashMap[4];
        wynikiOstaeczne[0] = wyniki;
        wynikiOstaeczne[1] = podsumowanie;

        for (File fileToCompare : wylosowaneObrazySumHistogram) {
            wyniki2.putAll(histogramComparator.comparator(fileToCompare, null, listOfHistogramSum, 400, 1256));
            podsumowanie2.putAll(statystyka(fileToCompare.getName(), wyniki2.get(fileToCompare.getName())));
            if(podsumowanie2.get(fileToCompare.getName()) == 1){
                liczbaPoprawnych++;
            }
        }
        podsumowanie2.put("POPRAWNYCH", liczbaPoprawnych);

        wynikiOstaeczne[2] = wyniki;
        wynikiOstaeczne[3] = podsumowanie;

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

