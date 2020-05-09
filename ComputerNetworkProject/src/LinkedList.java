import java.util.ArrayList;

class LinkedList<T> {
    private int numberOfNodes = 0; 
    private ListNode<T> front = null;
    
    public boolean isEmpty() {
        return (front == null);
    }
    
    // Not: ListNode objeleri bir süre sonra JVM tarafından garbage collectore gonderliecektir. 
    public void makeEmpty() {
        front = null;
        numberOfNodes = 0;
    }
    
    public int size() {
        return numberOfNodes;
    }
    
    public void addFront( T element ) {
        front = new ListNode<T>( element, front );
        numberOfNodes++;
    }
    
    // Ilk node daki datanin referansini dondurecektir. Bos ise null degeri dondurmesi beklenir.
    public T peek() {
        if (isEmpty()) 
            return null;
        
        return front.getData();
    }
    
    // Linked listin ilk nodunu siler ve dondurur. Linkede list Bos ise sadece null dondurur
    @SuppressWarnings("unchecked")
    public T removeFront() {
        T tempData;
        
        if (isEmpty()) 
            return null;
        
        tempData = front.getData();
        front = front.getNext();
        numberOfNodes--;
        return tempData;
    }
    
    @SuppressWarnings("unchecked")
    public void removeEnd(T element) {
        ListNode<T> node=front;
        while(node.getNext() != null)
        {
            node = node.getNext();
        }
        node.setNext(new ListNode<T>((T)element, null));
    }
    
    // T tipinde nesneler ile dolu arraylist dondurur.
    @SuppressWarnings("unchecked")
    public ArrayList<T> getArray() {
        
        ArrayList<T> shapeArray=new ArrayList<T>();
        
        ListNode<T> node=front;
        while (node!=null)
        {
            shapeArray.add(node.getData());
            node = node.getNext();
        }
        
        return shapeArray;
    }
}