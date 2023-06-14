/**
 * @author Wensheng Gan, HIT, China
 * @see AlgoRFMP_tree
 */

public class Item {
//成员变量
    String name = "-1";
    double utility = 0;
    double recency = 0d;
   double frequency = 0;

    /**
     * Constructor that takes item name
     *
     * @param name item
     */
    //构造方法
    public Item(String name) {
        this.name = name;
    }

    /**
     * Constructor that takes item name and utility
     *
     * @param name item
     * @param utility utility value of item
     * @param recency recency value of item
     */
    public Item(String name, double utility, double recency) {
        this.name = name;
        this.utility = utility;
        this.recency = recency;
    }
//    public Item(String name, double frequency, double utility) {
//        this.name = name;
//        this.utility = utility;
//        this.frequency = frequency;
//    }
    /**
     * Constructor that takes item name and utility
     *
     * @param name item
     * @param utility utility value of item
     * @param recency recency value of item
     * @param frequency frequency value of item
     */
   public Item(String name, double utility, double recency, double frequency) {
        this.name = name;
        this.utility = utility;
        this.recency = recency;
      this.frequency = frequency;
    }



    /**
     * method to get node utility
     */
    public double getUtility() {
        return utility;
    }

    /**
     * method to get node recency value
     */
    public double getRececny() {
        return recency;
    }

    /**
     * method to get node frequency value
     */
   public double getFrequency() { return frequency; }

    /**
     * method to set node utility
     */
    public void setUtility(int utility) {
        this.utility = utility;
    }

    /**
     * method to set node recency value
     */
    public void setRecency(double recency) {
        this.recency = recency;
    }

    /**
     * method to set node frequency value
     */
public void setFrequency(int frequency) { this.frequency = frequency; }

    /**
     * method to get perticular item
     */
    public String getName() {
        return name;
    }

    /**
     * override the out-perform method
     */
    @Override
    public String toString() {
        return this.name + " utility: " + this.utility + " , frequency: " + this.frequency + " , recency: " + this.recency;
    }
}
