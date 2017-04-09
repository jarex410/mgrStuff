package moje;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import static moje.GeneratorHistogramow2NEW.PATH_TO_HOME_DATABASE;
import static moje.GeneratorHistogramow2NEW.PATH_TO_OXFORD_DATABASE;
import static moje.GeneratorHistogramow2NEW.PATH_TO_ZuBuDu_DATABASE;

/**
 * Created by JaroLP on 2015-10-30.
 * <p>
 * <p>
 * KLASA DZIALA POPRAWNIE
 */
public class GeneratorKoszykowNEW {


    public final static String PATH_TO_RZESZOW_DATABASE = "H:\\MAGISTERKA\\BazyZdjec\\Rzeszow";

    public final static String PATH_TO_RZESZOW_DATABASE_DESC = PATH_TO_RZESZOW_DATABASE + "\\desc";

    public final static int SUFR = 64;

    public final static int SIFT = 128;

    public final static int BRIEF = 520;


    /**
     * Metoda generuje koszyki dla odpowiedniej bazy zdjec
     *
     * @param pathToDataBase
     * @param numberOfBaskets
     * @param listaDESCGenerowanychPrzezAlgorytm
     * @throws IOException
     */

    public void generujKoszyki(String pathToDataBase, int numberOfBaskets, int listaDESCGenerowanychPrzezAlgorytm, boolean binarny) throws IOException {

        List<String> calaBazaPkt = new ArrayList<>();
        List<String> pktBazowe = new ArrayList<>();

        Random r = new Random();
        int randomik = 0;
        File folder = null;

        if (listaDESCGenerowanychPrzezAlgorytm == SUFR && !binarny) {
            folder = new File(pathToDataBase + "\\desc");
        } else if (listaDESCGenerowanychPrzezAlgorytm == SIFT && !binarny) {
            folder = new File(pathToDataBase + "\\descSIFT");
        } else if (listaDESCGenerowanychPrzezAlgorytm == SUFR && binarny) {
            folder = new File(pathToDataBase + "\\BinarSurf");
        } else if (listaDESCGenerowanychPrzezAlgorytm == SIFT && binarny) {
            folder = new File(pathToDataBase + "\\BinarSift");
        }
        if (listaDESCGenerowanychPrzezAlgorytm == BRIEF) {
            folder = new File(pathToDataBase + "\\BRIEF");
        }
        File[] listOfFiles = folder.listFiles();

        File folderUczacych = new File(pathToDataBase + "\\Uczace\\");

        List<String> nazwyUczacych = new ArrayList<File>(Arrays.asList(folderUczacych.listFiles())).stream().map(x -> x.getName()).collect(Collectors.toList());
        List<File> listaUczacych = new ArrayList<File>(Arrays.asList(listOfFiles)).stream().filter(x -> nazwyUczacych.contains(x.getName().replace(".txt", ""))).collect(Collectors.toList());
        File[] plikiUczace = listaUczacych.toArray(new File[listaUczacych.size()]);

        //zbiera pkt z okreslonej listy obrazow i losuje koszyki
        //w tym przypadku 100 losowych obrazow z bazy

        if (listaDESCGenerowanychPrzezAlgorytm == BRIEF) {
            for (File file : plikiUczace) {
                if (file.isFile()) {
                    Scanner scanner = new Scanner(file).useDelimiter(",").useDelimiter("]");
                    while (scanner.hasNext()) {
                      //  for (int ii = 0; ii < listaDESCGenerowanychPrzezAlgorytm; ii++) {
                            String next = scanner.next().replace("[","");
                            if (next.trim().contains("0") || next.trim().contains("1")) {
                                calaBazaPkt.add(next + "\n");
                            }
                       // }
                    }
                }
            }
        } else {
            for (File file : plikiUczace) {
                if (file.isFile()) {
                    Scanner scanner = new Scanner(file);
                    while (scanner.hasNextLine()) {
                        calaBazaPkt.add(scanner.nextLine() + "\n");
                        for (int ii = 0; ii < listaDESCGenerowanychPrzezAlgorytm; ii++) {
                            calaBazaPkt.add(scanner.nextLine() + "\n");
                        }
                    }
                }
            }
        }

        //generuje koszyki

        if(listaDESCGenerowanychPrzezAlgorytm==BRIEF){
            for (int i = 0; i < numberOfBaskets; i++) {
                randomik = r.nextInt(calaBazaPkt.size());
                pktBazowe.add(calaBazaPkt.get(randomik));
            }
        } else {
            for (int i = 0; i < numberOfBaskets; i++) {
                randomik = r.nextInt(calaBazaPkt.size() / (listaDESCGenerowanychPrzezAlgorytm + 1));
                pktBazowe.add(calaBazaPkt.get(randomik * (listaDESCGenerowanychPrzezAlgorytm + 1)));
                for (int k = 1; k < listaDESCGenerowanychPrzezAlgorytm + 1; k++) {
                    pktBazowe.add(calaBazaPkt.get(randomik * (listaDESCGenerowanychPrzezAlgorytm + 1) + k));
                }
            }
        }
        File fileOut = null;
        if (listaDESCGenerowanychPrzezAlgorytm == SUFR) {
            if (binarny) {
                fileOut = new File(pathToDataBase + "\\" + numberOfBaskets + "\\baskets\\SrodkiPrzedzialowSurfBin.txt");
            } else {
                fileOut = new File(pathToDataBase + "\\" + numberOfBaskets + "\\baskets\\SrodkiPrzedzialowSurf.txt");
            }
        } else if (listaDESCGenerowanychPrzezAlgorytm == SIFT) {
            if (binarny) {
                fileOut = new File(pathToDataBase + "\\" + numberOfBaskets + "\\SrodkiPrzedzialowSiftBin.txt");
            } else {
                fileOut = new File(pathToDataBase + "\\" + numberOfBaskets + "\\SrodkiPrzedzialowSift.txt");
            }
        } else if (listaDESCGenerowanychPrzezAlgorytm == BRIEF) {
            fileOut = new File(pathToDataBase + "\\" + numberOfBaskets + "\\SrodkiPrzedzialowBRIEF.txt");
        }
        FileWriter zapis = new FileWriter(fileOut, true);
        zapis.write(pktBazowe.toString().replaceAll(",", ""));
        zapis.close();
    }

    /**
     * dla testu
     *
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {

        GeneratorKoszykowNEW generatorKoszykowNEW = new GeneratorKoszykowNEW();

/*        generatorKoszykowNEW.generujKoszyki(PATH_TO_TEST_DATABASE, 200, SUFR);
        generatorKoszykowNEW.generujKoszyki(PATH_TO_TEST_DATABASE, 200, SIFT);*/

      generatorKoszykowNEW.generujKoszyki(PATH_TO_OXFORD_DATABASE, 500, BRIEF, false);
    }

}
