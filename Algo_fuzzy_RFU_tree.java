import java.io.*;
import java.util.*;

/**
 * This is an implementation of the fuzzy-RFU-tree algorithm.
 * "Mining Valuable Fuzzy Patterns via the RFM Model", Yanlin Qi et al.
 * published at 2022 IEEE International Conference on Data Mining Workshops (ICDMW)
  * @author Yanlin Qi, HIT, China
 */
public class Algo_fuzzy_RFU_tree{

    /**
     * the maximum memory usage
     */
    private double maxMemory = 0;
    /**
     * the time the algorithm started
     */
    private long startTimestamp = 0;
    private long startTimestamp1 = 0;
    private long endTimestamp3 = 0;
    private long endTimestamp4 = 0;
    private long endTimestamp5 = 0;
    /**
     * the time the algorithm terminated
     */
    private long endTimestamp1 = 0;
    /**
     * the time the algorithm terminated
     */
    private long endTimestamp2 = 0;

    /**
     * the number of RHUIs generated
     */
    private int huiCount = 0;
    /**
     * the number of PRHUIs generated
     */
    private int pRHUIsCount;
    private double totalRevenue = 0.0;
    private BufferedWriter writer3;
    private String dataset;
    private double minRecencyThreshold = 0d;
    private double minUtilityThreshold=0.0;
    private double minFrequencyThreshold=0.0;
    private double 	minUtilityRatio=0.0;
    private double 	 totalUtility1 = 0d;
    /**
     * the number of transactions
     */
    private int transCurrent = 0;
    /**
     * delay recency rate
     */
    double decayThreshold = 0.05;
    /**
     * time current
     */
    //double timeCurrent = 115;//125
    /**
     * writer to write the output file
     */
    private BufferedWriter writer = null;
    private BufferedWriter writer1 = null;
    /**
     * Structure to store the potential RHUIs
     */
    private List<Itemset> pRHUIs = new ArrayList<Itemset>();
   // private  Itemset   Itemset_val =null;

    /**
     * item-utility
     */

    List fuzzydatabase = new ArrayList();
    HashMap<String, Double> itemsUtil = new HashMap<String,Double>();
    /**
     * item-time
     */
    HashMap<Integer, Double> tidTime = new HashMap<Integer,Double>();//项对应的时间
    /**
     * item-tids
     */
    Map<String, TreeSet<Integer>> mapItemTIDS = new HashMap<String, TreeSet<Integer>>();
    /**
     * To activate debug mode
     */
    private final boolean DEBUG = false;
    /**
     * the path of input file
     */

    private String pathInput = null;
    private Object maxF,minF,maxR,minR,maxU,minU;
    private int traverse_num = 0;
    private int recursive_num = 0;
    Map<String, Double> mapItemToTWU = new HashMap<String, Double>();
    // We create a map to store the Frequency value of each item
    Map<String, Double> mapItemToFrequency = new HashMap<String, Double>();
    // We create a map to store the Recency value of each item
    Map<String, Double> mapItemToRecency = new HashMap<String, Double>();

    /**
     * Method to run the algorithm
     *
     * @param input             path for reading the input file
     * @param utilityTableInput path for item-utility file
     * @param timeTableInput    path for item-time file
     * @param output            path for writing the output file
     * @param minFrequencyRate  the minimum FrequencyRate to calculate minimum Frequency
     * @param minUtility        the minimum utility
     * @param decay             the user defined decay
     * @param minRecency        the minimum recency
     *
     * @throws IOException exception if error while reading or writing the file
     */
    public void runAlgorithm(String dataset, BufferedWriter writer3, String input, String utilityTableInput, String timeTableInput, String output, String output1,
                             boolean useRate, double minFrequencyRate, int minUtility, double minUtilityRatio, double decay,double minRecency,double timeCurrent) throws IOException {

        checkMemory();
        startTimestamp = System.currentTimeMillis();
        pathInput = input;
        decayThreshold = decay;
        maxMemory = 0;
        writer = new BufferedWriter(new FileWriter(output));
        writer1 = new BufferedWriter(new FileWriter(output1));

        this.writer3=writer3;
        this.dataset=dataset;
        this.minUtilityThreshold=minUtility;
        this.minFrequencyThreshold=minFrequencyRate;
        this.minRecencyThreshold=minRecency;
        this.minUtilityRatio=minUtilityRatio;
        this.totalUtility1=totalUtility1;

        // We create a map to store the TWU of each item


        // Map item and its utility
        Load_UtilityTable(utilityTableInput);
        // Map tranctions and its time
        Load_TimeTable(timeTableInput);
        BufferedReader myInput = null;
        String thisLine;
        FFIMiner ffi = new FFIMiner();
        Map<String, Double> ItemSup = ffi.init(input);
        Map<Integer, Map<String, Double>> fuzzyTranactions = ffi.fuzzyTranactions;
        endTimestamp3 = System.currentTimeMillis();

        String true_item;
        String true_level;
        Set<String> fuzzyitems = ItemSup.keySet();
        List<String> true_items = new ArrayList<String>();
        Map<String,String> mapItemToLevel =new HashMap<String,String>();
        Map<Integer,Double> MapTidToTWU =new HashMap<Integer,Double>();
        Map<Integer,Double> MapTidToRecency=new HashMap<Integer, Double>();



        int tid1 = 0;
        try {
            startTimestamp1 = System.currentTimeMillis();
            myInput = new BufferedReader(new InputStreamReader(new FileInputStream(new File(input))));
            myInput.mark((int) new File(input).length() + 1);
            for (String fuzzyitem : fuzzyitems) {
                String[] itemAndlevel = fuzzyitem.split("\\.");
                true_item = itemAndlevel[0];
                true_level = itemAndlevel[1];
                mapItemToLevel.put(true_item,true_level);
            }


            while ((thisLine = myInput.readLine()) != null) {
                Double transaction_utility = 0d;
                Double SumRecency = 0d, currentRecency = 0d;
                Double twu = 0d;
                String[] ItemAndQual = thisLine.split(" ");
                for (String itemAndQualOne : ItemAndQual) {
                    String[] tmp1 = itemAndQualOne.split(",");
                    String item = tmp1[0];
                    double count = Double.parseDouble(tmp1[1]);
                    transaction_utility += itemsUtil.get(item) * count;
                }
                for (String itemAndQualOne : ItemAndQual) {
                    String[] tmp = itemAndQualOne.split(",");
                    if (tmp.length == 2) {
                        String item = tmp[0];
                        String level = mapItemToLevel.get(item);
                        String fuzzyitem = item + "." + level;
                        if(fuzzyitems.contains(fuzzyitem)) {
                            if (fuzzyTranactions.get(tid1 + 1).get(fuzzyitem) != 0) {
                                mapItemToFrequency.put(item, ItemSup.get(fuzzyitem));

                                twu = mapItemToTWU.get(item);
                                twu = (twu == null) ? transaction_utility : twu + transaction_utility;
                                mapItemToTWU.put(item, twu);

                                SumRecency = mapItemToRecency.get(item);
                                currentRecency = Math.pow((1 - decayThreshold), (timeCurrent - tidTime.get(tid1))/1000.0);
                                SumRecency = (SumRecency == null) ? currentRecency : SumRecency + currentRecency;
                                mapItemToRecency.put(item, SumRecency);

                                TreeSet<Integer> Tids = mapItemTIDS.get(item);
                                if (Tids == null) {
                                    Tids = new TreeSet<Integer>();
                                }
                                Tids.add(tid1);
                                mapItemTIDS.put(item, Tids);
                            }
                        }

                    }
                }
                MapTidToTWU.put(tid1,transaction_utility);
                MapTidToRecency.put(tid1,currentRecency);
                tid1++;
            }

            int length = mapItemToFrequency.size();
            Object[] obj = mapItemToFrequency.values().toArray();
            Arrays.sort(obj);
             maxF=obj[length-1];
             minF=obj[0];

            Object[] obj1 = mapItemToRecency.values().toArray();
            Arrays.sort(obj1);
             maxR=obj1[length-1];
             minR=obj1[0];

            Object[] obj2 = mapItemToTWU.values().toArray();
            Arrays.sort(obj2);
            maxU=obj2[length-1];
            minU=obj2[0];

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (myInput != null) {
                myInput.close();
            }
        }


        // second database scan generate revised transaction and build the initial global IHUP-Tree
        try {
            RFMPTree tree = new RFMPTree();
            myInput = new BufferedReader(new InputStreamReader(new FileInputStream(new File(input))));
            int tid2 = 0;
            List<Item>  RFT1P=new ArrayList<Item>();
            ArrayList<String>  RFT1item=new ArrayList<String>();
            while ((thisLine = myInput.readLine()) != null) {
                List<Item> revisedTransaction = new ArrayList<Item>();
                String[] ItemAndQual = thisLine.split(" ");
                Map<String, Double> FitemAndFvalue = fuzzyTranactions.get(tid2 + 1);
                double transaction_utility = 0d;

                for (String itemAndQualOne : ItemAndQual) {
                    String[] tmp = itemAndQualOne.split(",");
                    String item = tmp[0];
                    double count = Double.parseDouble(tmp[1]);
                   String level=mapItemToLevel.get(item);
                   String fuzzy_item=item+"."+level;
                   if(FitemAndFvalue.get(fuzzy_item)!=0) {
                           if (mapItemToTWU.get(item) >= minUtilityThreshold && mapItemToRecency.get(item) >= minRecencyThreshold && mapItemToFrequency.get(item) >= minFrequencyThreshold) {
                               transaction_utility += itemsUtil.get(item) * count;
                                }
                          }
                     }
                     for (String itemAndQualOne : ItemAndQual) {
                            String[] tmp = itemAndQualOne.split(",");
                            String item = tmp[0];
                            String fuzzyitem = item + "." + mapItemToLevel.get(item);
                            double fuzzyFvalue = FitemAndFvalue.get(fuzzyitem);
                             if (fuzzyFvalue != 0) {
                                if (mapItemToTWU.get(item) >= minUtilityThreshold && mapItemToRecency.get(item) >= minRecencyThreshold && mapItemToFrequency.get(item) >= minFrequencyThreshold) {
                                    double currentRecency = MapTidToRecency.get(tid2);
                                     Item element = new Item(item, transaction_utility, currentRecency, fuzzyFvalue);
                                     revisedTransaction.add(element);
                                     RFT1item.add(item);
                                     RFT1item = getSingle(RFT1item);
                                   }
                              }
                      }
                       revisedTransaction.sort(new Comparator<Item>() {
                            @Override
                            public int compare(Item o1, Item o2) {
                                // return compareItemsAsc(o1.name, o2.name, mapItemToTWU);
                                 return compareItemsDesc(o1.name, o2.name, mapItemToFrequency);
                            }
                        });

                        tree.addTransaction(revisedTransaction);
                        tid2++;
                    }
                    tree.createHeaderList(mapItemToFrequency);

            checkMemory();

                    if (DEBUG) {
                        System.out.println("GLOBAL TREE" +
                                "\n mapITEM-FRE : " + mapItemToFrequency +
                                "\n mapITEM-TWU : " + mapItemToTWU +
                                "\n mapITEM-REC : " + mapItemToRecency +
                                "\n" + tree.toString());
                    }
                    //********************************//
                    // We create the header table for the global IHUP-Tree，// Mine tree with UP-Growth with 2 strategies DLU and DLN

            endTimestamp1 = System.currentTimeMillis();
            ihup(tree, new String[0]);
                    // check the memory usage again and close the file.
                    checkMemory();

         } catch(Exception e){
                    e.printStackTrace();
                } finally{
                if (myInput != null) {
                    myInput.close();
                }
            }


        pRHUIsCount = pRHUIs.size();
        checkMemory();
        endTimestamp4 = System.currentTimeMillis();



        try {
            myInput = new BufferedReader(new InputStreamReader(new FileInputStream(new File(input))));
            // Third database scan to calculate the exact utility of each PRHUIs and output RFM-patterns.
            int tid3 = 0;

           while ((thisLine = myInput.readLine()) != null) {
                List<Item> revisedTransaction = new ArrayList<Item>(); //revisedTransaction，一行代表一项的值，有几行说明这个事件中有几项
                List<String> revisedTransactionIds = new ArrayList<>();//里面装的是项。
                double transactionRecency = Math.pow((1 - decayThreshold), (timeCurrent - tidTime.get(tid3))/1000.0);
                String[] ItemAndQual = thisLine.split(" ");
                for (String itemAndQualOne : ItemAndQual) {
                    String[] tmp = itemAndQualOne.split(",");
                    if(mapItemToLevel.keySet().contains(tmp[0])) {
                        String item = tmp[0];
                        String level = mapItemToLevel.get(item);
                        String fuzzyitem =item+"."+level;
                        double fuzzyFval = fuzzyTranactions.get(tid3+1).get(fuzzyitem);
                        if(fuzzyFval!=0){
                          if (mapItemToFrequency.get(item) >= minFrequencyThreshold && mapItemToTWU.get(item) >= minUtilityThreshold && mapItemToRecency.get(item) >= minRecencyThreshold) {
                                double utility = itemsUtil.get(tmp[0]) * Double.parseDouble(tmp[1]);
                                Item element = new Item(item, utility, transactionRecency, fuzzyFval);
                                revisedTransaction.add(element);
                            }
                        }
                    }
                  }
                revisedTransaction.sort(new Comparator<Item>() {
                   @Override
                   public int compare(Item o1, Item o2) {
                      // return compareItemsAsc(o1.name, o2.name, mapItemToTWU);
                        return compareItemsDesc(o1.name, o2.name, mapItemToFrequency);
                   }
               });


                 for(Item element : revisedTransaction){
                      String item = element.name;
                      revisedTransactionIds.add(item);
                   }

               for (Itemset itemset : pRHUIs) {
                    if (itemset.size() > revisedTransaction.size()) {
                        continue;
                    }
                    updateExactUtility(revisedTransaction, revisedTransactionIds, itemset);
                }
                tid3++;
            }
            checkMemory();
        } catch (Exception e) {
            e.printStackTrace();
        }
        endTimestamp5 = System.currentTimeMillis();

        int number=0;
        for (Itemset itemset : pRHUIs) {
            ArrayList<String> buffer = new ArrayList<String>();
            for (int i = 0; i < itemset.size(); i++) {
                buffer.add(itemset.get(i));
            }
            if (itemset.getExactUtility() >= minUtilityThreshold && itemset.getExactFrequency() >= minFrequencyRate && itemset.getExactRecency() >= minRecency ) {
                 number++;
                 writeOut(itemset);
            }
        }

        // check the memory usage again
        checkMemory();

        // record end time
        endTimestamp2 = System.currentTimeMillis();

        pRHUIs.clear();
        // CLOSE OUTPUT FILE
        writer.close();
        writer1.close();
    }



    public static ArrayList getSingle(ArrayList list){
        ArrayList newList = new ArrayList();
        Iterator it = list.iterator();
        while(it.hasNext()){
            Object obj = it.next();
            if(!newList.contains(obj)){
                newList.add(obj);
            }
        }
        return newList;
    }









    private void Load_TimeTable(String timeTableInput) {
        try {
            String thisline;
            BufferedReader br = new BufferedReader(new FileReader(timeTableInput));
            while ((thisline = br.readLine()) != null) {
                String[] tmp = thisline.split(",");
                int tid = Integer.parseInt(tmp[0]);
                Double time = Double.parseDouble(tmp[1]);
                tidTime.put(tid, time);
            }
            br.close();
        } catch (Exception e) {
            System.out.println("Error about loading the time table (in ConnectionTextFile.java): " + e.toString());
        }
    }

    /**
     * Loading Utility Table
     *
     * @param utility_table_input the path of utiltiy_table file
     */
    public void Load_UtilityTable(String utility_table_input) {
        try {
            String thisline;
            BufferedReader br = new BufferedReader(new FileReader(utility_table_input)); // �_�n
            while ((thisline = br.readLine()) != null) {
                String[] tmp = thisline.split(",");
                String item = tmp[0];
                Double profit = Double.parseDouble(tmp[1]);
                itemsUtil.put(item, profit);
            }
            br.close();
        } catch (Exception e) {
            System.out.println("Eity table (in rror about loading the utilConnectionTextFile.java): " + e.toString());
        }
    }

    /**
     * compare item with descending order
     *
     * @param item1 item A
     * @param item2 item B
     * @param map item-utility
     *
     * @return o1-o2 ascending | o2-o1 descending
     */
    private int compareItemsDesc(String item1, String item2, Map<String, Double> map) {
        double compare = map.get(item2) - map.get(item1);
        // if the same, use the lexical order otherwise use the TWU
        return Double.compare(compare, 0.0);
    }

    private int compareItemsAsc(String item1, String item2, Map<String, Double> map) {
        double compare = map.get(item1) - map.get(item2);
        // if the same, use the lexical order otherwise use the TWU
        return Double.compare(compare, 0.0);
    }


    /**
     * Mine UP-Tree recursively
     *
     * @param tree       IHUPTree to mine
     * @param prefix     the prefix itemset
     */
    private void ihup(RFMPTree tree, String[] prefix) throws IOException {
        for (int i = tree.headerList.size() - 1; i >= 0; i--) {
            String item = tree.headerList.get(i);
            // ===== CALCULATE SUM OF ITEM NODE UTILITY =====
            TreeNode pathCPB = tree.mapItemNodes.get(item);
            double pathCPBFrequency = 0.0;
            double path_minif=0.0;
            double pathCPBRecency = 0.0;
            double pathCPBUtility = 0.0;
            while(pathCPB != null) {
                pathCPBFrequency += pathCPB.count;
                pathCPBRecency += pathCPB.recency;
                pathCPBUtility += pathCPB.nodeUtility;
                pathCPB = pathCPB.nodeLink;
               }
            if (pathCPBFrequency >= minFrequencyThreshold && pathCPBRecency >= minRecencyThreshold && pathCPBUtility >= minUtilityThreshold) {
                String[] newPrefix = new String[prefix.length + 1];
                System.arraycopy(prefix, 0, newPrefix, 0, prefix.length);
                newPrefix[prefix.length] = item;
                Itemset Itemset_val=new Itemset(newPrefix);
                Itemset_val.utility = pathCPBUtility;
                Itemset_val.frequency = pathCPBFrequency;
                Itemset_val.recency = pathCPBRecency;
                savePHUI(newPrefix, Itemset_val.recency);
                writeOut1(Itemset_val);
                traverse_num += 1; //
            // ===== CREATE THE LOCAL TREE =====
            RFMPTree localTree = createLocalTree(tree, item);
            if (DEBUG) {
                System.out.println("\nLOCAL TREE for projection by:" + ((prefix == null) ? "" : Arrays.toString(prefix) + ",") + item + "\n" + localTree.toString());
            }
            if (localTree.headerList.size() > 0) {
                recursive_num += 1;
                ihup(localTree, newPrefix);
             }
            }
        }
    }

    /**
     * createLocalTree
     *
     * @param tree the construct tree
     * @param item current item (local root)
     *
     * @return a local tree (the root is parameter 'item')
     */
    private RFMPTree createLocalTree(RFMPTree tree, String item) {

        // It is a subdatabase which consists of the set of prefix paths
        List<List<TreeNode>> prefixPaths = new ArrayList<List<TreeNode>>();
        TreeNode path = tree.mapItemNodes.get(item);

        // map to store path utility of local items in CPB
        final Map<String, Double> itemPathUtility = new HashMap<String, Double>();
        // map to store path recency of local items in CPB
        final Map<String, Double> itemPathRecency = new HashMap<String, Double>();
        // map to store path frequency of local items in CPB
        final Map<String, Double> itemPathFrequency= new HashMap<String, Double>();


        while (path != null) {
            // get the node utility,frequency,recency of the item
            double nodeutility = path.nodeUtility;
            double nodeFrequency = path.count;//
            double nodeRecency = path.recency;
            // if the path is not just the root node
            if (!path.parent.name.equals("-1")) {
                // create the prefix-path
                List<TreeNode> prefixPath = new ArrayList<TreeNode>();
                // add this node.
                prefixPath.add(path); // NOTE: we add it just to keep its utility,
                // actually it should not be part of the prefixPath
                TreeNode parentnode = path.parent;
                while (!parentnode.name.equals("-1")) {
                    prefixPath.add(parentnode);
                    Double pu = itemPathUtility.get(parentnode.name);
                    pu = (pu == null) ? nodeutility : pu + nodeutility;
                    itemPathUtility.put(parentnode.name, pu);
                    //*****************************
                    // pr - path recency
                    Double pr = itemPathRecency.get(parentnode.name);
                    pr = (pr == null) ? nodeRecency : pr + nodeRecency;
                    itemPathRecency.put(parentnode.name, pr);
                    //*****************************
                    // pf - path frequency
                    Double pf = itemPathFrequency.get(parentnode.name);
                    pf = (pf == null) ? nodeFrequency : pf + nodeFrequency;
                    itemPathFrequency.put(parentnode.name, pf);

                    parentnode = parentnode.parent;
                }
                // add the path to the list of prefix-paths
                prefixPaths.add(prefixPath);
            }
            // We will look for the next prefix-path
            path = path.nodeLink;
        }

        if (DEBUG) {
            System.out.println("\n\n\nPREFIXPATHS:");
            for (List<TreeNode> prefixPath : prefixPaths) {
                for (TreeNode node : prefixPath) {
                    System.out.println("    " + node);
                }
                System.out.println("    --");
            }
        }


        // Calculate the Utility of each item in the prefixpath
        RFMPTree localTree = new RFMPTree();
        for (List<TreeNode> prefixPath : prefixPaths) {
            double pathUtility = prefixPath.get(0).nodeUtility;
            double pathFrequency = prefixPath.get(0).count;
            double pathRecency = prefixPath.get(0).recency;
            List<String> localPath = new ArrayList<String>();
            // for each node in the prefixpath,
            // except the first one, we count the frequency
            for (int j = 1; j < prefixPath.size(); j++) {
                TreeNode node = prefixPath.get(j);//ArrayList<TreeNode>
                if (itemPathFrequency.get(node.name) >= minFrequencyThreshold && itemPathUtility.get(node.name) >= minUtilityThreshold && itemPathRecency.get(node.name) >= minRecencyThreshold) {
                    localPath.add(node.name);
                }
            }

            if (DEBUG) {
                System.out.println("  path utility " + pathUtility);
            }

            // we reorganize local path in decending order of path frequency
            localPath.sort(new Comparator<String>() {
                @Override
                public int compare(String o1, String  o2) {
                    // compare the frequency of the items
                       //return compareItemsAsc(o1, o2, mapItemToTWU);
                         return compareItemsDesc(o1, o2, itemPathFrequency);
                }
            });

            localTree.addLocalTransaction(localPath, pathFrequency, pathRecency, pathUtility);
        }

        // We create the local header table for the tree item - CPB
        localTree.createHeaderList(itemPathFrequency);
       //System.out.println(localTree.root);
       //System.out.println(localTree.headerList);
        return localTree;

    }


    /**
     * Save a PHUI in the list of PRHUIs
     *
     * @param itemset the itemset array
     */
    private void savePHUI(String[] itemset,double recency) {
        Itemset itemsetObj = new Itemset(itemset);
      //  System.out.println(itemsetObj.getItemset());
        //按照字典序排序
        itemsetObj.recency=recency;
       // Arrays.sort(itemset);
        pRHUIs.add(itemsetObj);


    }
    //重新计算正确的 Mvalue
    public void updateExactUtility(List<Item> transaction, List<String> revisedTransactionIds, Itemset itemset) {
        double utility = 0.0;
       // double recency = 0.0;
        double frequency=0.0;
       // List frequencys=new ArrayList();
        List<Double> frequencys = new ArrayList<>();
        if (ContainsAll(revisedTransactionIds,itemset.getItemset())){//
            for (int i = 0; i < itemset.size(); i++) {
                String itemI = itemset.get(i);//ItemI是项集中的每个项
                int index = revisedTransactionIds.indexOf(itemI);//得到在revisedTransactionIds中项为4的索引
                utility += transaction.get(index).utility;
                frequency = transaction.get(index).frequency;
                frequencys.add(frequency);
                //frequency = Double.min(transaction.get(index).frequency,frequency);
            }
          //  recency += transaction.get(0).recency;

        }
        itemset.increaseUtility(utility);
      //  itemset.increaseRecency(recency);
        double minValue;
        if (frequencys.size()!=0) {
            minValue = frequencys.get(0);
            for (int i = 0; i < frequencys.size(); i++) {
                if (i == frequencys.size() - 1) {
                    continue;
                }
                double next = frequencys.get(i + 1);
                if (minValue > next) {
                    minValue = next;
                }
            }
        } else {
            minValue = 0d;
        }
        itemset.increaseFrequency(minValue);
      //  itemset.increaseFrequency(itemset.minFrequency(frequencys));
    }
    private boolean ContainsAll(List<String> revisedTransactionIds, String[] itemset) {
        boolean res=true;
        for (String transId:itemset) {
            if (!revisedTransactionIds.contains(transId)){
                res = false;
            }
        }
        return res;
    }


    /**
     * Write a HUI to the output file
     *
     * @param HUI itemset which users need
     *
     * @throws IOException
     */
    private void writeOut(Itemset HUI) throws IOException {
        huiCount++;
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < HUI.size(); i++) {
            buffer.append(HUI.get(i));
            buffer.append(' ');
        }
        buffer.append("," +"  #Util: "+ HUI.getExactUtility());
        buffer.append("  #Freq: " + String.format("%.3f",HUI.getExactFrequency()));
        buffer.append("  #Re: " + String.format("%.3f", HUI.getExactRecency()));
        writer.write(buffer.toString());
        writer.newLine();
    }

    private void writeOut1(Itemset HUI) throws IOException {
        // increase the number of high utility itemsets found
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < HUI.size(); i++) {
            buffer.append(HUI.get(i));
            buffer.append(' ');

        }
   //     buffer.append("," + HUI.getExactUtility());
        buffer.append("," +"  #Util: "+ HUI.getExactUtility());
        buffer.append("  #Freq: " + String.format("%.3f",HUI.getExactFrequency()));
        buffer.append("  #Re: " + String.format("%.3f", HUI.getExactRecency()));
        writer1.write(buffer.toString());
        writer1.newLine();
    }

    /**
     * Method to check the memory usage and keep the maximum memory usage.
     */
    private void checkMemory() {
        // get the current memory usage
        double currentMemory = (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / 1024d / 1024d;
        // if higher than the maximum until now  replace the maximum with the current memory usage
        if (currentMemory > maxMemory) {
            maxMemory = currentMemory;
        }
    }

    public void writeout_all_minRe() throws IOException{
        java.text.DecimalFormat df = new java.text.DecimalFormat("#.00");
        StringBuilder buffer3 = new StringBuilder();
        buffer3.append(dataset);
        buffer3.append(" ");
        buffer3.append(minRecencyThreshold);
        buffer3.append(" ");
        buffer3.append((endTimestamp2 - startTimestamp)/1000.0 );
        buffer3.append(" ");
        buffer3.append(huiCount);
        buffer3.append(" ");
        buffer3.append(pRHUIsCount);
        // write to file
        writer3.write(buffer3.toString());
        writer3.newLine();
        writer3.flush();

    }

    public void writeout_all_minSup() throws IOException{
        java.text.DecimalFormat df = new java.text.DecimalFormat("#.00");
        StringBuilder buffer3 = new StringBuilder();
        buffer3.append(dataset);
        buffer3.append(" ");
        buffer3.append(minFrequencyThreshold);
        buffer3.append(" ");
        buffer3.append((endTimestamp2 - startTimestamp)/1000.0 );
        buffer3.append(" ");
        buffer3.append(huiCount);
        buffer3.append(" ");
        buffer3.append(pRHUIsCount);
        writer3.write(buffer3.toString());
        writer3.newLine();
        writer3.flush();

    }
    public void writeout_all_minUtil() throws IOException{
        java.text.DecimalFormat df = new java.text.DecimalFormat("#.00");
        StringBuilder buffer3 = new StringBuilder();
        buffer3.append(dataset);
        buffer3.append(" ");
        buffer3.append(minUtilityThreshold);
        buffer3.append(" ");
        buffer3.append((endTimestamp2 - startTimestamp)/1000.0 );
        buffer3.append(" ");
        buffer3.append(huiCount);
        buffer3.append(" ");
        buffer3.append(pRHUIsCount);
        writer3.write(buffer3.toString());
        writer3.newLine();
        writer3.flush();

    }




    /**
     * Print statistics about the latest execution to System.out.
     */
    public void printStats(double min_support,int minUtility, double minRecency) {
        java.text.DecimalFormat df = new java.text.DecimalFormat("#.00");
        System.out.println("=============  fuzzy-RFU-tree !!! =============");
        System.out.println("dataset: "+ dataset);
        System.out.println("minFval: "+ minFrequencyThreshold+", "+"minRval: "+ minRecencyThreshold +", "+"minUval: "+ minUtilityThreshold);
        System.out.println("Fval: "+String.format("%.1f", minF)+"~"+String.format("%.1f", maxF)+" , "+"Rval: "+String.format("%.1f", minR)+"~"+String.format("%.1f", maxR)+" , "+"Uval: "+String.format("%.1f", minU)+"~"+String.format("%.1f", maxU));
        System.out.println(" Total time: " + (endTimestamp2 - startTimestamp) / 1000.0 + " s");
        System.out.println(" Memory: " + df.format(maxMemory) + " MB");
        System.out.println(" true_count: " + huiCount);
        System.out.println(" candidates: " + pRHUIsCount);
        System.out.println(" fuzzy time: "+ (endTimestamp3 - startTimestamp)/ 1000.0 + " s" );
        System.out.println(" Preprocessing time: "+ (endTimestamp1 - startTimestamp)/ 1000.0 + " s" );
        System.out.println(" mining time: "+ (endTimestamp4 - endTimestamp1)/ 1000.0 + " s" );
        System.out.println("Phase-II: "+ (endTimestamp5 - endTimestamp4)/ 1000.0 + " s" );

    }

}


