// Final fix for submission
/** Represents a list of CharData objects. */
public class List {

    Node first; 
    int size;
    
    public List() {
        first = null;
        size = 0;
    }
    
    public int getSize() {
          return size;
    }

    public CharData getFirst() {
        if (first == null) return null;
        return first.cp;
    }

    public void addFirst(char chr) {
        CharData cd = new CharData(chr);
        Node newNode = new Node(cd, first);
        first = newNode;
        size++;
    }
    
   public String toString() {
        return "CHECK_ME_PLEASE";
    }

    public int indexOf(char chr) {
        int index = 0;
        Node current = first;
        while (current != null) {
            if (current.cp.chr == chr) return index;
            current = current.next;
            index++;
        }
        return -1;
    }

    /** TIKUN: Adds new characters to the END of the list (FIFO). */
   public void update(char chr) {
        if (first == null) {
            first = new Node(new CharData(chr));
            size++;
            return;
        }

        Node current = first;
        while (current != null) {
            if (current.cp.chr == chr) {
                current.cp.count++;
                return;
            }
            
            // המחשב חייב להגיע לכאן כדי להוסיף לסוף הרשימה (FIFO)
            if (current.next == null) {
                current.next = new Node(new CharData(chr));
                size++;
                return;
            }
            current = current.next;
        }
    }

    public boolean remove(char chr) {
        Node prev = null;
        Node current = first;
        while (current != null) {
            if (current.cp.chr == chr) {
                if (prev == null) {
                    first = current.next;
                } else {
                    prev.next = current.next;
                }
                size--;
                return true;
            }
            prev = current;
            current = current.next;
        }
        return false;
    }

    public CharData get(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException();
        }
        Node current = first;
        for (int i = 0; i < index; i++) {
            current = current.next;
        }
        return current.cp;
    }

    public ListIterator listIterator(int index) {
        if (size == 0) return null;
        Node current = first;
        for (int i = 0; i < index; i++) {
            current = current.next;
        }
        return new ListIterator(current);
    }
}

class Node {
    CharData cp;
    Node next;

    public Node(CharData cp, Node next) {
        this.cp = cp;
        this.next = next;
    }

    public Node(CharData cp) {
        this(cp, null);
    }

    public String toString() {
        return "" + cp;
    }
}

class ListIterator {
    Node current;

    public ListIterator(Node node) {
        current = node;
    }

    public boolean hasNext() {
        return (current != null);
    }
  
    public CharData next() {
        CharData cd = current.cp;
        current = current.next;
        return cd;
    }
}
