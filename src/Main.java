import java.util.ArrayList;
import java.util.Collections;
import javafx.util.Pair;
import java.util.Comparator;
/**
 * Created by justin on 28/03/16.
 */
public class Main {

    public static void main(String[] args) {
        final int POP_SIZE = 10;
        final int N = 5;
        final int K = POP_SIZE/10;//number of individuals to replace each generation;
        ArrayList<Individual> pop = new ArrayList<Individual>(POP_SIZE);
        //new population
        for(int i = 0; i < POP_SIZE; ++i) {
            pop.add(new Preference(N).randomize());
        }

        System.out.println("Starting Initial Evaluation:");

        //initial evaluation
        int overallFitness = Integer.MAX_VALUE;
        Individual fittest = new Preference(N);
        for(Individual i : pop) {
            int fitness = i.evaluate();
            if(fitness < overallFitness) {
                overallFitness = fitness;
                fittest = i;
            }
        }

        System.out.println("Done Initial Evaluion.");
        System.out.println("Starting fitness = " + overallFitness);
        //genetic search
        final int MAX_GENERATIONS = Integer.MAX_VALUE;
        int generation = 0;
        while(overallFitness > 0 && generation < MAX_GENERATIONS) {
            Collections.sort(pop, new Comparator<Individual>() {
                @Override public int compare(Individual one, Individual two) {
                    return one.getFitness()  - two.getFitness();
                }
            });
            System.out.println("Fittest of generation " + generation);
            System.out.println("Fitness = " + pop.get(0).getFitness());
            System.out.println(pop.get(0).toString());
            for(int i =0; i<K; i+=2) {
                Individual one = pop.get(i);
                Individual two = pop.get(i+1);
                Pair<Individual,Individual> children = one.crossover(two);
                Individual childOne = children.getKey();
                Individual childTwo = children.getValue();
                pop.set(POP_SIZE - K -1+ i, childOne);
                pop.set(POP_SIZE - K -1+ i + 1, childTwo);
            }
            overallFitness = Integer.MAX_VALUE;
            for(Individual i : pop) {
                int fitness = i.evaluate();
                if(fitness < overallFitness) {
                    overallFitness = fitness;
                    fittest = i;
                }
            }
            ++generation;
        }
        System.out.println(fittest.toString());
    }

}
