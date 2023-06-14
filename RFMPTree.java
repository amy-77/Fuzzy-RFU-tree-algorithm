import java.util.*;

/**
 * This is an implementation of the Fuzzy-RFU-tree algorithm.
 *
 * @author  Yanlin Qi, HIT, China
 */

public class RFMPTree {

    /**
     * List of items in the header table
     */
    List<String> headerList = null;

    /**
     * List of pairs (item, Utility) of the header table
     */
    Map<String, TreeNode> mapItemNodes = new LinkedHashMap<>();

    /**
     * root of the tree
     */
    TreeNode root = new TreeNode();

    /**
     * Map that indicates the last node for each item using the node links
     * key: item, value: an RFMP-tree node (added by Philippe)
     */
    Map<String, TreeNode> mapItemLastNode = new LinkedHashMap<String, TreeNode>();

    public RFMPTree() { }

    /**
     * Method for adding a transaction to the IHUP-tree (for the initial
     * construction of the IHUP-Tree).
     *
     * @param transaction        reorganized transaction
     */
    public void addTransaction(List<Item> transaction) {
        TreeNode currentNode = root;
        // For each item in the transaction
        for (Item value : transaction) {
           String item = value.getName();
            // look if there is a node already in the IHUP-Tree
            TreeNode child = currentNode.getChildWithID(item);
            if (child == null) {
                currentNode = insertNewNode(currentNode, item, value.frequency, value.recency, value.utility);
            } else {
                child.count += value.frequency;
                child.recency += value.recency;
                child.nodeUtility += value.utility;
                currentNode = child;
            }
        }
    }

    /**
     * Add a transaction to the UP-Tree (for a local UP-Tree)
     *
     * @param localPath          the path to be inserted
     * @param pathUtility        the path utility
     * @param transactionRecency
     */
    public void addLocalTransaction(List<String> localPath,double pathFrequency,double transactionRecency, double pathUtility) {

        TreeNode currentlocalNode = root;

        // For each item in the transaction
        for (int i = 0; i < localPath.size(); i++) {
            String item = localPath.get(i);
            TreeNode child = currentlocalNode.getChildWithID(item);
            if (child == null) {
                // there is no node, we create a new one
                currentlocalNode = insertLocalNewNode(currentlocalNode, item, pathFrequency, transactionRecency, pathUtility);
            } else {// there is a node already, we update it
                // current node utility
                child.count += pathFrequency;
                child.recency += transactionRecency;
                child.nodeUtility += pathUtility;
                currentlocalNode = child;
            }
        }
    }

    /**
     * Insert a new node in the UP-Tree as child of a parent node
     *
     * @param currentlocalNode the parent node
     * @param item             the item in the new node
     * @param nodeRecency      the node recency of the new node
     * @param nodeUtility      the node utility of the new node
     *
     * @return the new node
     */
    private TreeNode insertNewNode(TreeNode currentlocalNode, String item, double nodeFrequency, double nodeRecency, double nodeUtility) {
        // create the new node
        TreeNode newNode = new TreeNode();
        newNode.name = item;
        newNode.recency = nodeRecency;
        newNode.nodeUtility = nodeUtility;
        newNode.count = nodeFrequency;
        newNode.parent = currentlocalNode;
        // we link the new node to its parent
        currentlocalNode.childs.add(newNode);
        // We update the header table.
        // We check if there is already a node with this id in the header table
        TreeNode localheadernode = mapItemNodes.get(item);
        // if there is not existing
        if (localheadernode == null) {

            mapItemNodes.put(item, newNode);
            mapItemLastNode.put(item, newNode);
        } else {
            // if there is existing
            // we find the last node with this id.
            // get the latest node in the tree with this item
            TreeNode lastNode = mapItemLastNode.get(item);
            // we add the new node to the node link of the last node
            lastNode.nodeLink = newNode;
            // Finally, we set the new node as the last node
            mapItemLastNode.put(item, newNode);
        }
        // we return this node as the current node for the next loop iteration
        return newNode;
    }
    private TreeNode insertLocalNewNode(TreeNode currentlocalNode, String item, double nodeFrequency,double nodeRecency, double nodeUtility) {
        // create the new node
        TreeNode newNode = new TreeNode();
        newNode.name = item;
        newNode.recency = nodeRecency;
        newNode.nodeUtility = nodeUtility;
        newNode.count = nodeFrequency;
        newNode.parent = currentlocalNode;
        // we link the new node to its parent
        currentlocalNode.childs.add(newNode);
        // We update the header table.
        // We check if there is already a node with this id in the header table
        TreeNode localheadernode = mapItemNodes.get(item);
        // if there is not existing
        if (localheadernode == null) {
            mapItemNodes.put(item, newNode);
            mapItemLastNode.put(item, newNode);
        } else {
            // if there is existing
            // we find the last node with this id.
            // get the latest node in the tree with this item
            TreeNode lastNode = mapItemLastNode.get(item);
            // we add the new node to the node link of the last node
            lastNode.nodeLink = newNode;
            // Finally, we set the new node as the last node
            mapItemLastNode.put(item, newNode);
        }
        // we return this node as the current node for the next loop iteration
        return newNode;
    }

    /**
     * Method for creating the list of items in the header table, in descending
     * order of Frequency.
     *
     * @param map the Frequency of each item (key: item, value: Frequency of path frequency)
     */
    void createHeaderList(Map<String, Double> map) {
        // create an array to store the header list with
        // all the items stored in the map received as parameter
        headerList = new ArrayList<String>(mapItemNodes.keySet());//原来这里是mapItemNodes
        headerList.sort(new Comparator<String>() {
            @Override
            public int compare(String id1, String id2) {
                // compare the frequency
                 double compare = map.get(id2) - map.get(id1);
                // if the same frequency, we check the lexical ordering!
                return  Double.compare(compare,0.0);

            }
        });
    }

}
