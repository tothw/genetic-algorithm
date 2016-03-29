import javafx.util.Pair;
import java.util.Collections;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by justin on 28/03/16.
 */
public class Preference implements Individual{

    private int n;


    public ArrayList<Agent> men;
    public ArrayList<Agent> women;
    public ArrayList<Agent> dogs;

    private int fitness = Integer.MAX_VALUE;

    public Preference(int n) {
        this.n = n;
        men = new ArrayList<Agent>(n);
        women = new ArrayList<Agent>(n);
        dogs = new ArrayList<Agent>(n);
        for(int i = 0; i < n; ++i) {
            men.add(i, new Agent(n));
            women.add(i, new Agent(n));
            dogs.add(i, new Agent(n));
        }
    }

    @Override
    public Individual randomize() {
        for(int i = 0; i<n; ++i) {
            men.set(i, men.get(i).randomize());
            women.set(i, women.get(i).randomize());
            dogs.set(i, dogs.get(i).randomize());
        }
        return this;
    }

    @Override
    public int evaluate() {
        ArrayList<Integer> nums = new ArrayList<>(n);
        for (int i = 0; i < n; ++i) {
            nums.add(i);
        }
        ArrayList<Matching> matchings = new ArrayList<>();
        matchings = enumerateMatchings(new ArrayList<>(nums), new ArrayList<>(nums), new ArrayList<>(nums), matchings, new Matching());
        int unblockedMatchings = 0;
        for(Matching matching : matchings) {
            if(!isBlocked(matching)) {
                ++unblockedMatchings;
            }
        }
        fitness = unblockedMatchings;
        return fitness;
    }

    private boolean isBlocked(Matching matching) {
        for(int i = 0; i<n; ++i) {
            for(int j = 0; j<n; ++j) {
                for(int k = 0; k<n; ++k) {
                    if(matching.isBlocking(i,j,k)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private ArrayList<Matching> enumerateMatchings(ArrayList<Integer> men, ArrayList<Integer> women, ArrayList<Integer> dogs, ArrayList<Matching> list, Matching matching) {
        if(men.size() == 1) {
            Matching result = new Matching(matching);
            list.add(result.addTriple(men.get(0), women.get(0), dogs.get(0)));
            return list;
        }
        int man = men.get(0);
        for(int i = 0; i<women.size(); ++i) {
            for(int j = 0; j < dogs.size(); ++j){
                ArrayList<Integer> newMen = new ArrayList<>(men);
                newMen.remove(0);
                int woman = women.get(i);
                ArrayList<Integer> newWomen = new ArrayList<>(women);
                newWomen.remove(i);
                int dog = dogs.get(j);
                ArrayList<Integer> newDogs = new ArrayList<>(dogs);
                newDogs.remove(j);
                Matching newMatching = new Matching(matching).addTriple(man,woman,dog);
                enumerateMatchings(newMen,newWomen,newDogs,list,newMatching);
            }
        }
        return list;
    }

    private class Matching {

        public ArrayList<Triple> matches;

        public Matching() {
            matches = new ArrayList<>();
        }

        public Matching(Matching matching) {
            matches = new ArrayList<>(matching.matches);
        }
        public Matching addTriple(int i, int j, int k) {
            matches.add(new Triple(i,j,k));
            return this;
        }

        public boolean isBlocking(int i, int j, int k) {
            for(Triple triple : matches) {
                if (triple.man == i && triple.woman == j && triple.dog == k) {
                    return false;
                }
            }
            for(Triple triple : matches) {
                Agent man = men.get(triple.man);
                Agent woman = men.get(triple.woman);
                Agent dog = men.get(triple.dog);

                boolean manPrefers = man.rankings.get(j) > man.rankings.get(triple.woman);
                boolean womanPrefers = woman.rankings.get(k) > woman.rankings.get(triple.dog);
                boolean dogPrefers = dog.rankings.get(i) > dog.rankings.get(triple.man);
                if(manPrefers && womanPrefers && dogPrefers) {
                    return true;
                }
            }
            return false;
        }

        private class Triple{

            public int man;
            public int woman;
            public int dog;

            public Triple(int i, int j, int k){
                man = i;
                woman = j;
                dog = k;
            }

        }
    }

    @Override
    public int getFitness() {
        return fitness;
    }

    @Override
    public Pair<Individual, Individual> crossover(Individual individual) {
        Preference mate = (Preference)individual;
        Preference child1 = new Preference(n);
        Preference child2 = new Preference(n);

        int splitpoint = n/2;
        for(int i = 0; i<n; ++i) {
            if (i <= splitpoint) {
                child1.men.set(i, this.men.get(i));
                child1.women.set(i, this.women.get(i));
                child1.dogs.set(i, this.dogs.get(i));

                child2.men.set(i, mate.men.get(i));
                child2.women.set(i, mate.women.get(i));
                child2.dogs.set(i, mate.dogs.get(i));
            } else {
                child1.men.set(i, mate.men.get(i));
                child1.women.set(i, mate.women.get(i));
                child1.dogs.set(i, mate.dogs.get(i));

                child2.men.set(i,this.men.get(i));
                child2.women.set(i,this.women.get(i));
                child2.dogs.set(i,this.dogs.get(i));
            }
        }

        //mutate children
        //child 1
        //men
        if(ThreadLocalRandom.current().nextFloat() >= 0.5) {
            int i = ThreadLocalRandom.current().nextInt(0,n);
            child1.men.set(i, child1.men.get(i).randomize());
        }
        //women
        if(ThreadLocalRandom.current().nextFloat() >= 0.5) {
            int i = ThreadLocalRandom.current().nextInt(0,n);
            child1.women.set(i, child1.women.get(i).randomize());
        }
        //dogs
        if(ThreadLocalRandom.current().nextFloat() >= 0.5) {
            int i = ThreadLocalRandom.current().nextInt(0,n);
            child1.dogs.set(i, child1.dogs.get(i).randomize());
        }
        //child 2
        //men
        if(ThreadLocalRandom.current().nextFloat() >= 0.5) {
            int i = ThreadLocalRandom.current().nextInt(0,n);
            child2.men.set(i, child1.men.get(i).randomize());
        }
        //women
        if(ThreadLocalRandom.current().nextFloat() >= 0.5) {
            int i = ThreadLocalRandom.current().nextInt(0,n);
            child2.women.set(i, child1.women.get(i).randomize());
        }
        //dogs
        if(ThreadLocalRandom.current().nextFloat() >= 0.5) {
            int i = ThreadLocalRandom.current().nextInt(0,n);
            child2.dogs.set(i, child1.dogs.get(i).randomize());
        }
        return new Pair<>(child1, child2);
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("Preference System:\n");
        sb.append("Fitness = " + fitness);
        sb.append("\nMen's rankings of women 1 to " + n + ":\n");
        for(int i = 0; i< n; ++i) {
            sb.append(men.get(i).toString());
        }
        sb.append("\nWomen's rankings of dogs 1 to " + n + ":\n");
        for(int i = 0; i< n; ++i) {
            sb.append(women.get(i).toString());
        }
        sb.append("\nDog's rankings of men 1 to " + n + ":\n");
        for(int i = 0; i< n; ++i) {
            sb.append(dogs.get(i).toString());
        }
        return sb.toString();
    }
}

