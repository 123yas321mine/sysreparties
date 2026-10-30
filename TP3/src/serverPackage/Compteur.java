package serverPackage;

/**
 * Compteur global partage entre tous les threads ClientProcess_6_1.
 * Ressource critique : l'acces est synchronise.
 */
public class Compteur {

    private static int total = 0;

    // synchronized (static) : un seul thread a la fois peut executer cette methode.
    // Sans cela, "total++" (lire, ajouter 1, ecrire) peut perdre des incrementations.
    // L'affichage est dans la methode pour que les valeurs sortent dans l'ordre.
    public static synchronized int incrementer() {
        total++;
        System.out.println("[compteur global] opérations traitées : " + total);
        return total;
    }
}