package methodInvocation;

import observer.ObserverDeCapteur;
import proxy.Canal;

import java.util.concurrent.Callable;

public class Update implements Callable<Integer> {

    ObserverDeCapteur afficheur;
    Canal canal;

    public Update(ObserverDeCapteur afficheur, Canal canal) {
        this.afficheur = afficheur;
        this.canal = canal;
    }

    @Override
    public Integer call() {
        afficheur.update(canal);
        return null;
    }
}
