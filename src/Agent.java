import java.util.ArrayList;
import java.util.Collections;
/**
 * Created by justin on 28/03/16.
 */
public class Agent {

    private int n;
    ArrayList<Integer> rankings;
    public Agent(int n) {
        this.n = n;
        rankings = new ArrayList<Integer>(n);
        for(int i = 0; i<n; ++i) {
            rankings.add(i,i);
        }
    }

    public Agent randomize() {
        Collections.shuffle(rankings);
        return this;
    }

    public String toString() {
        StringBuffer sb  = new StringBuffer();
        for(int i =0; i<n; ++i) {
            sb.append(rankings.get(i) + ">");
        }
        sb.append("\n");
        return sb.toString();
    }
}
