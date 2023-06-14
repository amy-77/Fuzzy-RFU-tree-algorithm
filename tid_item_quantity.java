public class tid_item_quantity{
    String tid ;
    String item ;
    String quantity ;

    public tid_item_quantity(String tid, String item, String quantity) {
        this.tid = tid;
        this.item = item;
        this.quantity = quantity;
    }

    public tid_item_quantity() {
    }

    public tid_item_quantity(String tid, String item) {
        this.tid = tid;
        this.item = item;
    }

    public String getTid() {
        return tid;
    }

    public void setTid(String tid) {
        this.tid = tid;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }
}
