public class MyListTester {
    public static void main(String[] args) {
        List l = new List();
        String test = "committee_"; // המילה לבדיקה מההנחיות
        
        System.out.println("Building list for: " + test);
        for (int i = 0; i < test.length(); i++) {
            l.update(test.charAt(i));
        }
        
        System.out.println("Resulting List: " + l.toString());
        System.out.println("Expected size: 7, Actual size: " + l.getSize());
        
        // בדיקת הסרה
        System.out.println("Removing 'm'...");
        l.remove('m');
        System.out.println("After removal: " + l.toString());
    }
}

