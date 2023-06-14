import java.io.*;

/**
 * Example of how to use the RFMP-growth algorithm from the source code.
 *
 * @author Yanlin Qi, HIT, China
 */
public class MainTest_fuzzy_RFU_tree{

    public static void main(String[] arg) throws IOException {

        String output = ".//fuzzyoutput_tree.txt";
        String output1 = ".//fuzzyoutput_tree1.txt";
        String output3 = ".//all_output_fuzzy_tree.txt";
        double decay = 0.01;
        BufferedWriter writer3 = new BufferedWriter(new FileWriter(output3));
        Boolean use_Util_Rate = false;
        Double[] minUtil_ratios = {100.0};
        Integer[] times = {120};
         Double[] minSups_initial = {0.8};
        Double[] minRes_intial = {0.01};
        Integer[] minUtil = {20};
        Double[] steps_minSup = {5.0};
        String[] paths = {".//newdataset1/paper_db.txt"};
        String[] paths_price = {".//newdataset1/price.txt"};
        String[] paths_time = {".//newdataset1/paper_time.txt"};
        String[] datasets = {"paper_db"};
        int i=0;
        String dataset = datasets[i];
        String input = paths[i];
        String utility_table_input = paths_price[i];
        String time_table_input = paths_time[i];
        int timeCurrent = times[i];
        double step= steps_minSup[i];
        double minRecency = minRes_intial[i];
        double minfuzFval = minSups_initial[i];
        double min_utility_ratio = minUtil_ratios[i];
        int  minUtility = minUtil[i];
             Algo_fuzzy_RFU_tree algo = new Algo_fuzzy_RFU_tree();
            algo.runAlgorithm(dataset, writer3, input, utility_table_input, time_table_input, output, output1, use_Util_Rate, minfuzFval, minUtility, min_utility_ratio,decay, minRecency, timeCurrent);
            algo.printStats(minfuzFval, minUtility, minRecency);
            algo.writeout_all_minSup();
        }
}
