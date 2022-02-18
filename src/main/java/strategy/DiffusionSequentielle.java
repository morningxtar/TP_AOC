package strategy;

import capteur.Capteur;

import java.util.concurrent.ExecutionException;

public class DiffusionSequentielle implements Diffusion {

    private Capteur capteur;
    int valeur = 0;
    boolean firstExecution = true;

    @Override
    public void configure(Capteur capteur) {
        this.capteur = capteur;
    }

    @Override
    public void execute() {

        boolean canExecute;
        if (capteur.getCountDownLatch().getCount() == 0 || firstExecution) {
            canExecute = true;
            firstExecution = false;
            capteur.initializeCountDownLatch();
        } else {
            canExecute = false;
        }

        try {
            if (canExecute) {
                valeur = capteur.getValue();
                for (int i = 0; i < capteur.getObserverDeCapteurAsyncs().size(); i++) {
                    capteur.getObserverDeCapteurAsyncs().get(i).update(capteur);
                }
            }

        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int reader() {

        int value = valeur;
        capteur.unLock();
        return value;
    }
}

