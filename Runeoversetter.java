import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;

class Runeoversetter {
    public static void main(String[] args) {
        boolean filFunnet = false;

        // Lager scanner for lesing av fil
        String filnavn = "middelalderruner.txt";
        Scanner lesFil = new Scanner(System.in);

        try {
            lesFil = new Scanner(new File(filnavn));
            filFunnet = true;
        } catch (FileNotFoundException e) {
            System.out.println("Fant ikke filen \"" + filnavn + "\".");
        }

        // Går gjennom input fra bruker
        if (filFunnet) {
            System.out.println("Skriv inn teksten du vil oversette til runer:");

            Scanner lesInput = new Scanner(System.in);
            String input = lesInput.nextLine().toUpperCase();
            char[] bokstaver = input.toCharArray();
            String runer = "";

            for (int i = 0; i < bokstaver.length; i++) {
                String denne = Character.toString(bokstaver[i]);
                String neste = "";
                if ((i+1) < bokstaver.length) {
                    neste = Character.toString(bokstaver[i+1]);
                }

                if (erTall(denne) || denne.equals(" ")) {
                    runer += denne;
                } else {
                    try {lesFil = new Scanner(new File(filnavn));}
                    catch (FileNotFoundException e) {}
                    runer += oversett(denne, neste, lesFil);
                }
            }

            System.out.println("Ferdig! Her er resultatet:");
            System.out.println(runer);
        }
    }

    public static String oversett(String denne, String neste, Scanner lesFil) {
        String rune = "";

        while (lesFil.hasNext()) {
            String linje = lesFil.nextLine();
            if (linje.equals("")) {
                linje = lesFil.nextLine();
            }
            String[] linjebiter = linje.split(" ");
            String latinskBokstav = linjebiter[0];
            String runeBokstav = linjebiter[1];

            if (denne.equals(latinskBokstav)) {
                String nesteRune = lesFil.nextLine().split(" ")[1];
                if ((denne += neste).equals(nesteRune)) {
                    rune = nesteRune;
                } else {
                    rune = runeBokstav;
                }
            }
        }

        return rune;
    }

    public static boolean erTall(String i) {
        try {
            int tall = Integer.parseInt(i);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }
}
