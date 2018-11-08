/**
 * FV of a simple rock-paper-scissors game in Java in French
 * (TO DO : NameOfVariables and comment translation)
 * Author : Darya ZAVOLSKAYA, June 2018
 */
import java.util.Random;

public class ProjetFinalNFA031LastVersion {
  
  public static void main(String[] args) { 
    
    String[] outils  = {"Feuille", "Ciseaux", "Pierre"};
    String[] messages = { outils[1] + " battent "+ outils[0], outils[0] + " bat " + outils[2], outils[2] + " bat " + outils[1]};
    int[] indices = {1, 0, 2}; //tableau d'indices -> s'obtient par addition de n=0 à n=2 des outils dans [] outils: // pointe sur le tableau []outils
    
    accueil(outils, messages, indices);
  }
  
  public static void accueil(String[] outils, String[] messages, int[] indices) {
    
    for (;;) {
      Terminal.ecrireStringln("\n=== BIENVENUE DANS LE JEU : Pierre, Feuille, Ciseaux  ===");
      Terminal.ecrireString("\nMENU PRINCIPAL :" +"\n\nTaper:" +"\n  1 - Jouer" + "\n  0 - Quitter" + "\n\nVotre choix: ");
      int menu = Terminal.lireInt();
      switch(menu) { 
        case 0: System.exit(0);
        case 1:
          Terminal.ecrireStringln("\nPour commencer à jouer veuillez acheter des jetons (1 jeton = 1 €)");  
          Terminal.ecrireString("\nTaper:" +"\n  1 - 1 jeton" + "\n  2 - 2 jetons" + "\n  3 - 3 jetons" + "\n  0 - Quitter" + "\n\nVotre choix: ");
          int jetons = Terminal.lireInt(); 
          if (jetons == 0) { return; }
          if (jetons < 0 || jetons > 3) {
            Terminal.ecrireStringln("Choix invalide, veuillez recommencer!");
            jetons = Terminal.lireInt();
          }
          jeu (jetons, outils, messages, indices);
          break; 
        default:
          Terminal.ecrireStringln("Choix invalide, veuillez recommencer.");
          break;
      }
    }
  }
  
  public static void jeu (int jetons, String[] outils, String[] messages, int[] indices) {
    Terminal.ecrireStringln("\n=== Combien de tours voulez-vous jouer?  (0 - Retour au Menu Principal)"); 
    int nTours = Terminal.lireInt();
    if (nTours == 0) { accueil(outils, messages, indices); }
    if (nTours >= 15) { 
      Terminal.ecrireStringln("Jouer trop n'est pas bon pour votre santé! Veuillez choisir moins de tours"); 
      nTours = Terminal.lireInt();
    }
    Terminal.sautDeLigne();
    int scoreJoueur = 0; int scoreMachine = 0; 
    int[] gains = new int[nTours]; int[] stockJetons = new int[nTours]; int[] parties = new int[nTours];
    String[] figuresJouees = new String[nTours]; String[] figuresIa = new String[nTours]; 
    int dernierePartie = 0;
    for (int nombrePartie = 0; nombrePartie < nTours; nombrePartie ++) { //boucle du jeu pour le nombre de tours choisi au clavier
      if (jetons >  0) {
        int mise = verificationMise(jetons);
        
        int choixJoueur = proposerJoueur(outils, nombrePartie);
        figuresJouees[nombrePartie] = outils[choixJoueur];
        
        int choixMachine = choisirMachine(outils);
        figuresIa[nombrePartie] = outils[choixMachine];
        
        int resultat = resoudreTour(outils, messages, indices, nombrePartie, mise, choixJoueur, choixMachine);
        parties[nombrePartie] = resultat;
        
        int gainTour = calculeGain(resultat, true, mise);
        gains[nombrePartie] = gainTour;
        
        jetons += gainTour;
        stockJetons[nombrePartie] = jetons;
        
        Terminal.ecrireStringln("\n|| Votre score actuel : " + calculePoint(resultat, true) + " ||   || " + 
                                "IA score actuel : " + calculePoint(resultat, false) + " ||");
        Terminal.ecrireStringln("||  Votre score total : " + (scoreJoueur += calculePoint(resultat, true)) + " ||   || " + 
                                " IA score total : " + (scoreMachine += calculePoint(resultat, false)) + " ||");
        dernierePartie = nombrePartie;
      } else if (jetons == 0) {
        Terminal.ecrireStringln("\nVous n'avez plus de jetons. Au revoir! Partie terminée...");
        break;
      }
    }
    afficheHistorique(dernierePartie, gains, stockJetons, parties, figuresJouees, figuresIa);
    accueil(outils, messages, indices);
  }
  
  public static void afficheHistorique(int dernierePartie, int[] gains, int[] stockJetons, int[] parties, String[] figuresJouees, String[] figuresIa) {
    Terminal.ecrireStringln("\n==>TABLEAU FINAL DU JEU :" + "\n\nTour\tResultat\tGains\tJetons\tJoueur\tIA");
    for(int i = 0; i <= dernierePartie; i++) {
      String beauResultat = "";
      if(parties[i] == 0) {
        beauResultat = "N";
      } else if(parties[i] < 0){
        beauResultat = "D";
      } else {
        beauResultat = "V";
      }
      
      Terminal.ecrireStringln((i+1) + "\t" + beauResultat + "\t" + gains[i] + "\t" + stockJetons[i] + "\t" + figuresJouees[i] + "\t" + figuresIa[i]);
    }
  }
  
  public static int verificationMise(int jetons) {
    while (true) {
      Terminal.ecrireString("Vous avez " + jetons + " jeton(s). Combien voulez-vous miser?");
      int mise = Terminal.lireInt();
      if (mise > 0 && mise <= jetons ) {
        return mise;
      }
      Terminal.ecrireStringln("Imossible! Veuillez miser autre chose!");
    }
  }
  
  public static int calculePot(int jetons, int mise) { 
    if (mise > jetons) {
      Terminal.ecrireString("Il vous reste " + jetons + " jeton(s)... Veuillez modifier le montant de la mise!");
      mise = Terminal.lireInt();
    } 
    if (mise <= jetons && mise > 0) { 
      return mise; 
    };
    return mise;
  }
  
  public static int calculePoint(int resultat, boolean estLeJoueur) {
    if((resultat > 0 && estLeJoueur == true) || (resultat < 0 && estLeJoueur == false)) {
      return 1;
    }
    return 0;
  }
  
  public static int calculeGain(int resultat, boolean estLeJoueur, int mise) {
    if(resultat > 0 && estLeJoueur == true) {
      return mise;
    }
    if(resultat == 0 && estLeJoueur == true) {
      return 0;
    } else {
      return -mise;
    }
  }
  
  public static int proposerJoueur(String[] outils, int nombrePartie) {
    Terminal.ecrireStringln("Veuillez choisir entre Pierre, Feuille et Ciseaux: ");
    for (int i=0;i<3;i++) {  //affiche menu : choix d'outils de jeu
      Terminal.ecrireStringln(i + " - " + outils[i]);
    }
    int choixJoueur = Terminal.lireInt();
    if (choixJoueur < 0 || choixJoueur > 2) { // verification de saisie
      Terminal.ecrireStringln("\nChoix invalide, veuillez recommencer." + "\nVeuillez choisir entre Pierre, Feuille et Ciseaux: ");
      for (int i=0;i<3;i++) {
        Terminal.ecrireStringln(i + " - " + outils[i]);
      }
      choixJoueur = Terminal.lireInt();
    }    
    Terminal.ecrireStringln("\nPOUR LA PARTIE N° : " + (nombrePartie+1) + "\n*** Votre choix : " + outils[choixJoueur]); // affiche choix Joueur
    
    return choixJoueur;
  }
  
  public static int choisirMachine(String[] outils) {
    Random rand = new Random();
    int choixMachine = rand.nextInt(3); // tire un nombre entre 0 et 2
    Terminal.ecrireStringln("*** Choix de IA : " + outils[choixMachine]); // affiche choix IA
    return choixMachine;
  }
  
  public static int resoudreTour(String[] outils, String[] messages, int[] indices, int nombrePartie, int mise, int choixJoueur, int choixMachine) {    
    int resultat = resolution(indices, choixJoueur, choixMachine);
    
    if(resultat == 0) {
      Terminal.ecrireStringln("\n==>RESULTAT : Egalité dans la partie " + (nombrePartie+1) + "!");
    } else {
      int scenario = obtenirIndice(choixJoueur, choixMachine);
      Terminal.ecrireStringln(messages[scenario]);
      if (resultat == -1) {
        Terminal.ecrireStringln("\n==>RESULTAT : IA gagne la partie " + (nombrePartie+1) + "!");
      } else {
        Terminal.ecrireStringln("\n==>RESULTAT : Vous avez gagné la partie " + (nombrePartie+1) + " et " + mise + " jeton(s)!");
      }
    }
    return resultat;
  }
  
  public static int resolution(int[] indices, int choixJoueur, int choixMachine) {  //resolution du jeu
    if ( choixMachine == choixJoueur ) {  
      return 0;
    }
    int scenario = obtenirIndice(choixJoueur, choixMachine); // permet d'obtenir un indice pour [] messages
    if (choixJoueur == indices[scenario]) {
      return 1;
    } else {
      return -1;
    }
  }
  
  public static int obtenirIndice(int choixJoueur, int choixMachine) { //regle du gagnant/perdant pour [] indices
    return choixJoueur + choixMachine - 1;
  }
  
}
