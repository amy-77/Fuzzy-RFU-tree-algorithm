public class Element {
    Integer timstamp;
    Double IF;
    String fuzzyitem;

    public Element(Integer timstamp, Double IF, String fuzzyitem) {
        this.timstamp = timstamp;
        this.IF = IF;
        this.fuzzyitem = fuzzyitem;
    }
    public Element() {
    }

    public Element(Integer timstamp, Double IF) {
        this.timstamp = timstamp;
        this.IF = IF;
    }

    public Element(Integer timstamp) {
        this.timstamp = timstamp;
    }

    public Integer getTimstamp() {
        return timstamp;
    }

    public void setTimstamp(Integer timstamp) {
        this.timstamp = timstamp;
    }

    public Double getIF() {
        return IF;
    }

    public void setIF(Double IF) {
        this.IF = IF;
    }

    public String getFuzzyitem() {
        return fuzzyitem;
    }

    public void setFuzzyitem(String fuzzyitem) {
        this.fuzzyitem = fuzzyitem;
    }
    public void show(){
        System.out.println("tid:"+timstamp+",模糊项:"+fuzzyitem+",隶属度:"+IF);
    }


}
