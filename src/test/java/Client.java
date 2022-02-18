import capteur.Capteur;
import capteur.CapteurImpl;
import observer.Afficheur;
import observer.ObserverDeCapteurAsync;
import org.apache.log4j.Logger;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import proxy.Canal;
import strategy.Diffusion;
import strategy.DiffusionAtomique;
import strategy.DiffusionParEpoque;
import strategy.DiffusionSequentielle;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class Client {

    private static final Logger LOGGER = Logger.getLogger(Client.class);

    Random random = new Random();
    Diffusion diffusionAtomique = new DiffusionAtomique();
    Diffusion diffusionSequentielle = new DiffusionSequentielle();
    Diffusion diffusionParEpoque = new DiffusionParEpoque();
    List<ObserverDeCapteurAsync> observerDeCapteurAsyncs = new ArrayList<>();
    ScheduledExecutorService scheduledExecutorService;

    Canal canal1;
    Canal canal2;
    Canal canal3;
    Canal canal4;
    Afficheur afficheur1;
    Afficheur afficheur2;
    Afficheur afficheur3;
    Afficheur afficheur4;

    List<Afficheur> afficheurs;
    Capteur capteur;

    @BeforeEach
    public void setup() {

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

    @Test
    @DisplayName("diffusion atomique")
    public void diffusionAtomique() {

        LOGGER.info("--- DIFFUSION ATOMIQUE ---");
        for (int i = 0; i < 50; i++) {
            capteur.tick();
        }

        LOGGER.info(capteur);
        afficheurs.forEach(afficheur -> {
            assertThat(capteur.getListOfTicks(), CoreMatchers.is(afficheur.getQueue()));
            LOGGER.info(afficheur);
        });

    }

    @Test
    @DisplayName("diffusion séquentielle")
    public void diffusionSequentielle() {

        LOGGER.info("--- DIFFUSION SEQUENTIELLE ---");
        capteur.setDiffusion(diffusionSequentielle);

        for (int i = 0; i < 3000; i++) {
            capteur.tick();
        }

        Set<Integer> results1 = afficheur1.getQueue();
        Set<Integer> results2 = afficheur2.getQueue();
        Set<Integer> results3 = afficheur3.getQueue();
        Set<Integer> results4 = afficheur4.getQueue();

        assertTrue(results1.size() == results2.size() && results1.size() == results3.size() && results1.size() == results4.size());

        afficheurs.forEach(afficheur -> {
            LOGGER.info(afficheur);
            assertThat(results1, CoreMatchers.is(afficheur.getQueue()));
        });
    }

    @Test
    @DisplayName("diffusion par époque")
    public void diffusionParEpoque() {

        LOGGER.info("--- DIFFUSION PAR EPOQUE ---");
        capteur.setDiffusion(diffusionParEpoque);

        for (int i = 1; i < 50; i++) {
            capteur.tick();
        }

        LOGGER.info(capteur);
        afficheurs.forEach(afficheur -> {
            assertTrue(capteur.getListOfTicks().containsAll(afficheur.getQueue()), "Values of afficheur is contain in capteur");
            LOGGER.info(afficheur);
        });
    }
}
