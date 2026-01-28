public class TestOrder {
    public static void main(String[] args) {
        List list = new List();
        list.update('a');
        list.update('b');
        list.update('c');
        
        System.out.println("Result: " + list.toString());
        
        // הבדיקה מצפה לסדר הוספה: a ראשון, b שני, c שלישי
        if (list.toString().equals("(a 1) (b 1) (c 1)")) {
            System.out.println("SUCCESS: List is FIFO (Correct Order)");
        } else {
            System.out.println("FAILURE: List is Reversed! (Actual: " + list.toString() + ")");
        }
    }
}