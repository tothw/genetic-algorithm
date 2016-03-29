import javafx.util.Pair;

/**
 * Created by justin on 28/03/16.
 */
public interface Individual {

     int getFitness();
     int evaluate();
     Individual randomize();
     Pair<Individual,Individual> crossover(Individual individual);
}
