import capteur.Capteur;
import capteur.CapteurImpl;
import observer.Afficheur;
import observer.ObserverDeCapteurAsync;
import org.apache.log4j.Logger;
import proxy.Canal;
import strategy.Diffusion;
import strategy.DiffusionAtomique;
import strategy.DiffusionParEpoque;
import strategy.DiffusionSequentielle;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Main {

    private static final Logger LOGGER = Logger.getLogger(Main.class);

    Random random = new Random();
    static Diffusion diffusionAtomique = new DiffusionAtomique();
    static Diffusion diffusionSequentielle = new DiffusionSequentielle();
    static Diffusion diffusionParEpoque = new DiffusionParEpoque();
    static List<ObserverDeCapteurAsync> observerDeCapteurAsyncs = new ArrayList<>();
    static ScheduledExecutorService scheduledExecutorService;

    static Canal canal1;
    static Canal canal2;
    static Canal canal3;
    static Canal canal4;
    static Afficheur afficheur1;
    static Afficheur afficheur2;
    static Afficheur afficheur3;
    static Afficheur afficheur4;

    static List<Afficheur> afficheurs;
    static Capteur capteur;

    public static void main(String[] args){

        setup();
        diffusionAtomique();
        setup();
        diffusionSequentielle();
        setup();
        diffusionParEpoque();

    }


    public static void setup() {

        scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        capteur = new CapteurImpl(observerDeCapteurAsyncs, diffusionAtomique);

        // afficheurs
        afficheur1 = new Afficheur(1);
        afficheur2 = new Afficheur(2);
        afficheur3 = new Afficheur(3);
        afficheur4 = new Afficheur(4);
        afficheurs = Stream.of(afficheur1, afficheur2, afficheur3, afficheur4).collect(Collectors.toList());

        // canal
        canal1 = new Canal(capteur, afficheur1);
        canal2 = new Canal(capteur, afficheur2);
        canal3 = new Canal(capteur, afficheur3);
        canal4 = new Canal(capteur, afficheur4);

        capteur.attach(canal1);
        capteur.attach(canal2);
        capteur.attach(canal3);
        capteur.attach(canal4);

        capteur.initializeCountDownLatch();
    }

    public static void diffusionAtomique() {

        LOGGER.info("--- DIFFUSION ATOMIQUE ---");
        for (int i = 0; i < 50; i++) {
            capteur.tick();
        }

        LOGGER.info(capteur);
        afficheurs.forEach(LOGGER::info);

    }

    public static void diffusionSequentielle() {

        LOGGER.info("--- DIFFUSION SEQUENTIELLE ---");
        capteur.setDiffusion(diffusionSequentielle);

        for (int i = 0; i < 3000; i++) {
            capteur.tick();
        }

        afficheurs.forEach(LOGGER::info);
    }

    public static void diffusionParEpoque() {

        LOGGER.info("--- DIFFUSION PAR EPOQUE ---");
        capteur.setDiffusion(diffusionParEpoque);

        for (int i = 1; i < 50; i++) {
            capteur.tick();
        }

        LOGGER.info(capteur);
        afficheurs.forEach(LOGGER::info);
    }
}
