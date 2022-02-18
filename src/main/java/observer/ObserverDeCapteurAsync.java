package observer;

import capteur.Capteur;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public interface ObserverDeCapteurAsync {

    Future<Integer> update(Capteur capteur) throws ExecutionException, InterruptedException;
}
