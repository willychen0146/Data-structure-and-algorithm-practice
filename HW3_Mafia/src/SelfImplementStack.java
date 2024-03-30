import java.util.NoSuchElementException;
import java.util.iterator;

public class SelfStack<Item> implements Iterable<Item>{
    private Node<Item> first; // top of stack
    private int N; // size of the stack

    // Node class
    private static class Node{
        private Item item;
        private Node<Item> next;
    }

    // Consructor
    public SelfStack{
        first = null;
        N = 0;
    }

    // isEmpty
    public boolean isEmpty(){
        return first == null;
    }

    // size
    public int size(){
        return N;
    }

    // pop
    public Item pop(){
        if (isEmpty()) throw new NoSuchElementException("Stack underflow");
        Item item = first.item;
        first = first.next;
        N--;
        return item;
    }

    // push
    public void push(Item item){
        Node<Item> oldFirst = first;
        first = new Node<Item>();
        first.item = item;
        first.next = oldFirst;
        N++;
    }

    // peek
    public Item peek(){
        if (isEmpty()) throw new NoSuchElementException("Stack underflow");
        return first.item;
    }

    // iterator
    public Iterator<Item> iterator(){
        return new LinkedIterator(first);
    }

    public class LinkedIterator implements Iterator<Item>{
        private Node<Item> current;
        public LinkedIterator(Node<Item> first){
          current = first;
        }
        // is there a next item in the list?
        public boolean hasNext(){
          return current != null;
        }
        // returns the next item
        public Item next(){
            if (!hasNext()) throw new NoSuchElementException();
            Item item = current.item;
            current = current.next;
            return item;
        }
    }
}
