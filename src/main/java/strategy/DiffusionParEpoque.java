package strategy;

import capteur.Capteur;

import java.util.concurrent.ExecutionException;

public class DiffusionParEpoque implements Diffusion {

    private Capteur capteur;

    @Override
    public void configure(Capteur capteur) {
        this.capteur = capteur;
    }

    @Override
    public void execute() {
        capteur.getObserverDeCapteurAsyncs().forEach(observerDeCapteurAsync -> {
            try {
                observerDeCapteurAsync.update(capteur);
            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public int reader() {
        return capteur.getValue();
    }
}
