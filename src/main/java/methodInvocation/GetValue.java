package methodInvocation;

import capteur.Capteur;

import java.util.concurrent.Callable;

public class GetValue implements Callable<Integer> {

    private final Capteur capteur;

    public GetValue(Capteur capteur) {
        this.capteur = capteur;
    }

    @Override
    public Integer call() {
        return capteur.getDiffusion().reader();
    }
}
