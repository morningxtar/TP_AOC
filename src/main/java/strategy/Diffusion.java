package strategy;

import capteur.Capteur;

public interface Diffusion {

    void configure(Capteur capteur);

    void execute();

    int reader();
}
