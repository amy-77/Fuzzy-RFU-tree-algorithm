import java.util.List;

/**
 * @author Wensheng Gan, HIT, China
 * @see AlgoRFMP_tree
 */
public class Itemset {

    private String[] itemset;
    double utility = 0d;
    double recency = 0d;
    double frequency = 0d;


    /**
     * Constructor
     *
     * @param itemset the itemset
     */
    //构造方法
    public Itemset(String[] itemset) {
        this.itemset = itemset;
    }

    /**
     * Get the exact utility of this itemset
     *
     * @return the exact utility
     */
    //getter setter 方法
    public double getExactUtility() {
        return utility;
    }

    /**
     * Get the exact recency of this itemset
     *
     * @return the exact recency
     */
    public double getExactRecency() {
        return recency;
    }

    /**
     * Get the exact frequency of this itemset
     *
     * @return the exact frequency
     */
    public double getExactFrequency() { return frequency; }

    /**
     * Increase the utility of this itemset.
     *
     * @param utility the amount of utility to be added (int).
     */


    //以下这些都是成员方法 (类中自定义的方法) 不加static
    public void increaseUtility(double utility) {
        this.utility += utility;
    }

    /**
     * Increase the recency of this itemset.
     *
     * @param recency the amount of recency to be added (double).
     */
    public void increaseRecency(double recency) {
        this.recency += recency;
    }

    /**
     * Increase the frequency of this itemset.
     *
     * @param frequency the amount of frequency to be added.
     */

    public void increaseFrequency(double frequency) {
        // this.frequency = Double.min(frequency,this.frequency);
        this.frequency+= frequency;
    }




    public double minFrequency(List frequencys) {

    double minValue = (double) frequencys.get(0);
        for (int i = 0; i < frequencys.size(); i++) {
            if (i == frequencys.size() - 1) {
                continue;
            }
            double next = (double) frequencys.get(i + 1);
            if (minValue > next) {
                minValue = next;
            }
        }
       return minValue;
    }


    /**
     * get itemset its position
     *
     * @param pos the position of itemset in transaction
     *
     * @return the item of itemset
     */
    public String get(int pos) {
        return itemset[pos];
    }

    /**
     * return itemset its length
     *
     * @return the length of itemset
     */
    public int size() {
        return itemset.length;
    }
    public String[] getItemset()
    {
        return itemset;
    }

}
