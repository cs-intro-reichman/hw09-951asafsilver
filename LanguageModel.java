import java.util.HashMap;
import java.util.Random;

public class LanguageModel {

    // מפת הנתונים: מקשרת בין חלון (String) לרשימת תווים והסתברויות (List)
    HashMap<String, List> CharDataMap;
    
    // אורך החלון שבו המודל משתמש
    int windowLength;
    
    // מחולל מספרים אקראיים
    private Random randomGenerator;

    /** בונה מודל שפה עם אורך חלון וזרע (seed) קבוע לבדיקות */
    public LanguageModel(int windowLength, int seed) {
        this.windowLength = windowLength;
        randomGenerator = new Random(seed);
        CharDataMap = new HashMap<String, List>();
    }

    /** בונה מודל שפה עם אורך חלון וזרע אקראי */
    public LanguageModel(int windowLength) {
        this.windowLength = windowLength;
        randomGenerator = new Random();
        CharDataMap = new HashMap<String, List>();
    }

    /** מאמן את המודל על בסיס טקסט מקובץ (הקורפוס) */
    public void train(String fileName) {
        String window = "";
        char c;
        In in = new In(fileName);
        
        // קריאת תווים ראשונים ליצירת החלון הראשוני
        for (int i = 0; i < windowLength; i++) {
            if (!in.isEmpty()) {
                window += in.readChar();
            }
        }
        
        // מעבר על כל שאר הטקסט ועדכון המפה
        while (!in.isEmpty()) {
            c = in.readChar();
            List probs = CharDataMap.get(window);
            if (probs == null) {
                probs = new List();
                CharDataMap.put(window, probs);
            }
            probs.update(c); // הוספה לסוף הרשימה בזכות התיקון ב-List.java
            window = window.substring(1) + c;
        }
        
        // חישוב הסתברויות לכל רשימה במפה
        for (List probs : CharDataMap.values()) {
            calculateProbabilities(probs);
        }
    }

    /** מחשבת ומעדכנת את השדות p (הסתברות) ו-cp (הסתברות מצטברת) לכל תו ברשימה */
    public void calculateProbabilities(List probs) {
        int totalCount = 0;
        // ספירת סך כל המופעים ברשימה
        for (int i = 0; i < probs.getSize(); i++) {
            totalCount += probs.get(i).count;
        }
        
        double cumulativeProb = 0.0;
        Node current = probs.first;
        while (current != null) {
            // הסתברות רגילה
            current.cp.p = (double) current.cp.count / totalCount;
            // הסתברות מצטברת
            cumulativeProb += current.cp.p;
            current.cp.cp = cumulativeProb;
            current = current.next;
        }
    }

    /** בוחר תו אקראי מתוך הרשימה בשיטת מונטה-קרלו */
    public char getRandomChar(List probs) {
        double r = randomGenerator.nextDouble();
        Node current = probs.first;
        while (current != null) {
            if (current.cp.cp > r) {
                return current.cp.chr;
            }
            current = current.next;
        }
        // למקרה קצה (דיוק חישובי), מחזיר את התו האחרון
        return probs.get(probs.getSize() - 1).chr;
    }

    /** מייצר טקסט אקראי באורך מוגדר על בסיס מודל השפה */
    public String generate(String initialText, int textLength) {
        if (initialText.length() < windowLength) {
            return initialText;
        }
        StringBuilder generatedText = new StringBuilder(initialText);
        while (generatedText.length() < textLength) {
            String window = generatedText.substring(generatedText.length() - windowLength);
            List probs = CharDataMap.get(window);
            if (probs == null) {
                break;
            }
            generatedText.append(getRandomChar(probs));
        }
        return generatedText.toString();
    }

    public String toString() {
        StringBuilder str = new StringBuilder();
        for (String key : CharDataMap.keySet()) {
            List keyProbs = CharDataMap.get(key);
            str.append(key + " : " + keyProbs + "\n");
        }
        return str.toString();
    }

    public static void main(String[] args) {
        int windowLength = Integer.parseInt(args[0]);
        String initialText = args[1];
        int generatedTextLength = Integer.parseInt(args[2]);
        Boolean randomGeneration = args[3].equals("random");
        String fileName = args[4];
        LanguageModel lm;
        if (randomGeneration) {
            lm = new LanguageModel(windowLength);
        } else {
            lm = new LanguageModel(windowLength, 20); // זרע קבוע לבדיקות
        }
        lm.train(fileName);
        System.out.println(lm.generate(initialText, generatedTextLength));
    }
}