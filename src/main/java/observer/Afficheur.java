package observer;

import proxy.Canal;

import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class Afficheur implements ObserverDeCapteur {

    private final int numberAfficheur;
    private final Set<Integer> queue;

    public Afficheur(int numberAfficheur) {
        this.numberAfficheur = numberAfficheur;
        queue = new LinkedHashSet<>();
    }

    @Override
    public void update(Canal canal) {
        try {
            Future<Integer> future = canal.getValue();
            queue.add(future.get());
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

    }

    public Set<Integer> getQueue() {
        return queue;
    }

    @Override
    public String toString() {
        return "afficheur " + numberAfficheur + " " + queue + " " + queue.size();
    }
}
