import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Yanlin Qi, HIT, China
 * @see AlgoRFMP_tree
 * @see RFMPTree
 */

public class TreeNode {

    String name = String.valueOf(-1);


    double recency;
    double count ;
    double nodeUtility;

    /**
     *  link to the parent node of that node
     */
    TreeNode parent = null;
    /**
     *  link to children nodes of that node
     */
    List<TreeNode> childs = new ArrayList<TreeNode>();
    /**
     * link to next node with the same item id (successive node).
     */
    TreeNode nodeLink = null;

    /**
     * Default constructor
     */
    public TreeNode() { }

    /**
     * method to get child node Return the immediate child of this node having a
     * given ID(item itself). If there is no such child, return null;
     */
    TreeNode getChildWithID(String name) {
        for (TreeNode child : childs) {
            if (child.name.equals(name)) {
                return child;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return "(i=" + name + " re=" + String.format("%.4f", recency) + " count=" + String.format("%.4f", count)+ " nu=" +  String.format("%.4f", nodeUtility) + ")";
    }

}
