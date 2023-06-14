
/**
 * This is an implementation of the Fuzzy-RFU-tree algorithm.
 *
 * @author  Yanlin Qi, HIT, China
 */
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class FFIMiner {

    static List<Map<String,Double>> FFLs=new ArrayList<Map<String, Double>>();
    static Map<Integer,Map<String,Double>> tranactions=new HashMap<Integer, Map<String, Double>>();
    static Map<Integer,Map<String,Double>> fuzzyTranactions=new HashMap<Integer, Map<String, Double>>();
    static Map<String,Map<String,Double>> ScarMap=new HashMap<String,Map<String,Double>>();
    static Map<String, Double> maxScarMap=new HashMap<String, Double>();
    static List<FuzzList> FLs=new ArrayList<FuzzList>();


    static Map<String,Double> init(String input) throws IOException {
        loadTranstions(input);
      return getMaxScar();
    }
    static void loadTranstions(String input) throws IOException {
        String trans_database_input = input;
        String thisline;
        BufferedReader br = new BufferedReader(new FileReader(trans_database_input));
        int tid=0;
        List<Double> count = new ArrayList<Double>();
        double max = 11.0;
        double min = 1.0;
        while ((thisline = br.readLine()) !=null) {
            tid++;
            String[] itemAndQual = thisline.split(" ");
            for (String itemAndQualOne : itemAndQual) {
                String[] tmp = itemAndQualOne.split(",");
                if (tmp.length == 2) {
                    count.add(Double.parseDouble(tmp[1]));
                }
            }
        }
        max = Collections.max(count);
        min = Collections.min(count);
        BufferedReader br1 = new BufferedReader(new FileReader(trans_database_input));
        int tid1=0;
        while ((thisline = br1.readLine()) !=null) {
            tid1++;
            Map<String, Double> tranOne = new HashMap<String, Double>();
            Map<String, Double> fuzzTranOne = new LinkedHashMap<String, Double>();//LinkedHashMap有序

            String[] itemAndQual = thisline.split(" ");
            for (String itemAndQualOne : itemAndQual) {
                String[] tmp = itemAndQualOne.split(",");
                if (tmp.length==2 && tmp[1] != null) {
                    tranOne.put(tmp[0], Double.parseDouble(tmp[1]));
                    Map<String, Double> temp = fuzzyTriangular(tmp[0], 1.5*max, 0.5*min, 3, Double.parseDouble(tmp[1]));//1.5*max, 0.5*min,

                    for (String key : temp.keySet()) {
                        fuzzTranOne.put(key, temp.get(key));
                    }

                    if (ScarMap.isEmpty() || null == ScarMap.get(tmp[0])) {
                        ScarMap.put(tmp[0], temp);
                    } else {
                        Map<String, Double> map = ScarMap.get(tmp[0]);
                        for (String key : temp.keySet()) {
                            double sup = map.get(key);
                            sup += temp.get(key);
                            map.put(key, sup);
                        }
                    }
                }
                tranactions.put(tid1, tranOne);
                fuzzyTranactions.put(tid1, fuzzTranOne);
            }

        }

        br.close();
    }

    static Map<String,Double> getMaxScar(){
        Map<String,Double> res = new HashMap<String, Double>();

        for (String item: ScarMap.keySet()) {
            Double maxScar = -1.0;
            String maxScarFuzz = "";
            for (String fuzzItem : ScarMap.get(item).keySet()) {
                double tmp = ScarMap.get(item).get(fuzzItem);
                if (tmp > maxScar){
                    maxScar = tmp;
                    maxScarFuzz = fuzzItem;
                }
            }
                res.put(maxScarFuzz, maxScar);
        }
        res = sortMap(res);
        return res;
    }
    static FuzzList geneList(){
        ArrayList<Map.Entry<String, Double>> list = new ArrayList<Map.Entry<String, Double>>(getMaxScar().entrySet());//??为什么list直接被赋值了？
        HashMap<String,Double> itemAndfuzzy =new HashMap<>();
        HashMap<Integer,HashMap<String,Double>> newtrans=new HashMap<>();
        FuzzList fuzzList = new FuzzList();
        for (int i = 0; i < list.size(); i++) {
            String key=list.get(i).getKey();
            fuzzList.getFuzzyItems().add(key);
            for (Integer ts:fuzzyTranactions.keySet()) {
                Element element=new Element(ts);
                Map<String,Double> fuzzValue = fuzzyTranactions.get(ts);
                if(fuzzValue.containsKey(key) && Double.compare(fuzzValue.get(key),0.0)==1){
                    element.setIF(fuzzValue.get(key));
                    element.setFuzzyitem(key);
                    itemAndfuzzy.put(key,element.IF);
                    fuzzList.getList().add(element);
                }
                newtrans.put(ts,itemAndfuzzy);
            }
          FLs.add(fuzzList);


        }

        return fuzzList;
    }
    static Map<String,Double> fuzzyTriangular(String item, double max, double min, int regions, double x){
        Map<String,Double> res=new LinkedHashMap<String,Double>();
        double[] low=new double[regions];
        double[] mid=new double[regions];
        double[] high=new double[regions];
        double fuzzyValue = 0.0;
        double s=(max-min)/(regions-1);
        for (int i=0;i<regions;i++){
            mid[i]=min+s*i;
            low[i]=mid[i]-s;
            high[i]=mid[i]+s;
            if (i == 0) {
                fuzzyValue=fuzzyCore2(low[i], mid[i], high[i], x);//代入三角隶属函数计算值
                res.put(item + ".L",fuzzyValue);
            } else if (i == 1) {
                fuzzyValue=fuzzyCore(low[i], mid[i], high[i], x);
                res.put(item + ".M",fuzzyValue);
            } else if (i == 2) {
                fuzzyValue=fuzzyCore1(low[i], mid[i], high[i], x);
                res.put(item + ".H",fuzzyValue);
            }
        }
        return res;
    }
    static double fuzzyCore(double low, double mid, double high, double x){
        double res=0.0;
        if (x<=low){
            res=0.0;
        }else if (x<=mid){
            res=(float)((x-low)/(mid-low));
        }else if (x<=high){
            res=(float)((high-x)/(high-mid));
        }else {
            res=0.0;
        }
        return res;
    }

    static double fuzzyCore1(double low, double mid, double high, double x){
        double res=0.0;
        if (x<=low){
            res=0.0;
        }else if (x <= mid){
            res=(float)((x-low)/(mid-low));
        }else {
            res= 1.0;
        }
        return res;
    }

    static double fuzzyCore2(double low, double mid, double high, double x){
        double res=0.0;
        if (x<=mid){
            res = 1.0;
        }else if (x <= high){
            res =(float)((high-x)/(high-mid));
        }else {
            res= 0.0;
        }
        return res;
    }


    public static Map sortMap(Map oldMap) {
        ArrayList<Map.Entry<String, Double>> list = new ArrayList<Map.Entry<String, Double>>(oldMap.entrySet());
        Collections.sort(list, new Comparator<Map.Entry<String, Double>>() {
            @Override
            public int compare(Map.Entry<String, Double> arg0,
                               Map.Entry<String, Double> arg1) {
                int res=Double.compare(arg0.getValue(),arg1.getValue());
                if(res>0)
                {
                    return 1;
                }else if (res<0){
                    return -1;
                }else {
                    return 0;
                }

            }
        });
        Map newMap = new LinkedHashMap();
        for (int i = 0; i < list.size(); i++) {
            newMap.put(list.get(i).getKey(), list.get(i).getValue());
        }
        return newMap;
    }
}
