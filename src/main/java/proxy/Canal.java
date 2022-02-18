package proxy;

import capteur.Capteur;
import capteur.CapteurAsync;
import methodInvocation.GetValue;
import methodInvocation.Update;
import observer.ObserverDeCapteur;
import observer.ObserverDeCapteurAsync;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

// proxy
public class Canal implements CapteurAsync, ObserverDeCapteurAsync {

    public Capteur getCapteur() {
        return capteur;
    }

    private final Capteur capteur;
    ExecutorService executorService;
    private final ObserverDeCapteur afficheur;

    public Canal(Capteur capteur, ObserverDeCapteur observerDeCapteur) {
        this.executorService = Executors.newCachedThreadPool();
        this.capteur = capteur;
        this.afficheur = observerDeCapteur;
    }

    @Override
    public Future<Integer> getValue() {
        GetValue getValue = new GetValue(capteur);
        return executorService.submit(getValue);
    }

    @Override
    public Future<Integer> update(Capteur capteur) {
        Update update = new Update(afficheur, this);
        return executorService.submit(update);
    }

}
