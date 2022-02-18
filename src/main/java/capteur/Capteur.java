package capteur;

import observer.ObserverDeCapteurAsync;
import strategy.Diffusion;

import java.util.List;
import java.util.Set;
import java.util.concurrent.CountDownLatch;

public interface Capteur {
    void attach(ObserverDeCapteurAsync observerDeCapteurAsync);

    void detach(ObserverDeCapteurAsync observerDeCapteurAsync);

    int getValue();

    void tick();

    /**
     * Block le thread courant
     * Sert à bloquer le capteur
     */
    void lock();

    /**
     * débloque le thread quand tous les afficheurs ont lu
     */
    void unLock();

    Diffusion getDiffusion();

    List<ObserverDeCapteurAsync> getObserverDeCapteurAsyncs();

    /**
     * @return CountDownLatch
     * Intervient dans le unlock
     */
    CountDownLatch getCountDownLatch();

    /**
     * initialise CountDownLatch au nombre d'afficheurs
     */
    void initializeCountDownLatch();

    Set<Integer> getListOfTicks();

    void setDiffusion(Diffusion diffusion);
}
