package capteur;

import observer.ObserverDeCapteurAsync;
import strategy.Diffusion;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CountDownLatch;

public class CapteurImpl implements Capteur {

    private final List<ObserverDeCapteurAsync> observerDeCapteurAsyncs;
    private int value = 0;
    private Diffusion diffusion;
    private final Set<Integer> listOfTicks;
    private CountDownLatch countDownLatch;

    public CapteurImpl(List<ObserverDeCapteurAsync> observerDeCapteurAsyncs, Diffusion diffusion) {
        this.observerDeCapteurAsyncs = observerDeCapteurAsyncs;
        this.listOfTicks = new LinkedHashSet<>();
        this.diffusion = diffusion;
        diffusion.configure(this);
    }

    @Override
    public void setDiffusion(Diffusion diffusion) {
        this.diffusion = diffusion;
        diffusion.configure(this);
    }

    @Override
    public void initializeCountDownLatch() {
        this.countDownLatch = new CountDownLatch(observerDeCapteurAsyncs.size());
    }

    @Override
    public void attach(ObserverDeCapteurAsync observerDeCapteurAsync) {
        observerDeCapteurAsyncs.add(observerDeCapteurAsync);
    }

    @Override
    public void detach(ObserverDeCapteurAsync observerDeCapteurAsync) {
        observerDeCapteurAsyncs.remove(observerDeCapteurAsync);
    }

    @Override
    public int getValue() {
        return value;
    }

    @Override
    public void tick() {
        value++;
        listOfTicks.add(value);
        diffusion.execute();
    }

    @Override
    public void lock() {
        try {
            countDownLatch.await();
            initializeCountDownLatch();

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void unLock() {
        countDownLatch.countDown();
    }

    @Override
    public CountDownLatch getCountDownLatch() {
        return countDownLatch;
    }

    @Override
    public List<ObserverDeCapteurAsync> getObserverDeCapteurAsyncs() {
        return Collections.unmodifiableList(observerDeCapteurAsyncs);
    }

    @Override
    public Set<Integer> getListOfTicks() {
        return listOfTicks;
    }

    @Override
    public Diffusion getDiffusion() {
        return diffusion;
    }

    @Override
    public String toString() {
        return "Capteur" +
                "listOfTicks=" + listOfTicks;
    }
}
