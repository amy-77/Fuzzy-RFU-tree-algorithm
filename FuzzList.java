import java.util.ArrayList;
import java.util.List;

public class FuzzList {
    List<String> fuzzyItems=new ArrayList<String>();//存的是模糊项的集合
    List<Element> list=new ArrayList<Element>();// 这里的element是自己写的构造类

   /* public static void main(String[] args) {
    FuzzList fuzzList1=new FuzzList();
        fuzzList1.getFuzzyItems().add("A.L");
        fuzzList1.getFuzzyItems().add("C.L");
        Element a=new Element(1,0.8,0.8);
        Element b=new Element(5,0.6,0.0);
        Element c=new Element(6,0.6,0.8);
        Element d=new Element(8,0.8,0.8);
        fuzzList1.getList().add(a);
        fuzzList1.getList().add(b);
        fuzzList1.getList().add(c);
        fuzzList1.getList().add(d);
        FuzzList fuzzList2=new FuzzList();
        fuzzList2.getFuzzyItems().add("C.L");
        fuzzList2.getFuzzyItems().add("B.L");
        Element a1=new Element(2,0.6,0.8);
        Element b1=new Element(3,0.6,0.6);
        Element c1=new Element(4,0.6,0.8);
        Element d1=new Element(6,0.8,0.8);
        fuzzList2.getList().add(a1);
        fuzzList2.getList().add(b1);
        fuzzList2.getList().add(c1);
        fuzzList2.getList().add(d1);
        FuzzList fuzzList3=fuzzList1.Construct(fuzzList2);
        System.out.println(fuzzList3.getFuzzyItems());
        //System.out.println(fuzzList3.getFuzzyItems());

    }  */

    /*
    public FuzzList Construct(FuzzList another) {
        FuzzList newFuzzList=new FuzzList();
        if (canJoint(fuzzyItems,another.fuzzyItems)){
            newFuzzList.setFuzzyItems(Joint(fuzzyItems,another.fuzzyItems));
            newFuzzList.setList(JointList(list,another.list));
        }
        return newFuzzList;
    }
    */

    double getSumIf(){
        double sumIf=0.0;
        for (Element e:list) {
            sumIf+=e.getIF();
        }
        return sumIf;
    }
    /*
    double getSumRf(){
    double sumRf=0.0;
        for (Element e:list) {
            sumRf+=e.getRF();
        }
        return sumRf;
    }
     */
    private List<String> Joint(List<String> fuzzyItems, List<String> fuzzyItems1) {
        List<String> newList=new ArrayList<String>();
        int i=0;
        for (;i<fuzzyItems.size();i++)
        {
            newList.add(fuzzyItems.get(i));
        }
        newList.add(fuzzyItems1.get(i-1));
        return newList;
    }

    private boolean canJoint(List<String> fuzzyItems, List<String> fuzzyItems1) {
        if (fuzzyItems.size()==fuzzyItems1.size()){
            if (fuzzyItems.size()==1){
                return true;
            }else if(fuzzyItems.size()>1){
                for (int i=0;i<fuzzyItems.size()-1;i++)
                {
                    if (!fuzzyItems.get(i).equals(fuzzyItems1.get(i)))
                    {
                        return false;
                    }
                }
            }
        }else{
            return false;
        }
        return true;
    }
  /*  List<Element> JointList(List<Element> list,List<Element> list1){
        List<Element> newList=new ArrayList<Element>();
        for (Element e:list) {
            for (Element e1:list1) {
                if (e.getTimstamp()== e1.getTimstamp()){
                    Element newE=new Element(e.getTimstamp());
                    newE.setIF(Math.min(e.getIF(),e1.getIF()));
                    newE.setRF(e1.getRF());
                    newList.add(newE);
                }
            }
        }
        return newList;


    }
      */
    public List<String> getFuzzyItems() {
        return fuzzyItems;
    }

    public void setFuzzyItems(List<String> fuzzyItems) {
        this.fuzzyItems = fuzzyItems;
    }

    public List<Element> getList() {
        return list;
    }

    public void setList(List<Element> list) {
        this.list = list;
    }
}
