import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Main {
    private static Heap heap = new Heap();

    public static void main(String[] args) {
        readFromFile();
        while(!heap.isEmpty()){
            Node tempNode = heap.deleteMax();
            System.out.print(tempNode.getIndex() + " ");
        }
        System.out.println();
    }

    private static void readFromFile() {
        try {
            BufferedReader reader = new BufferedReader(new FileReader("task.dat"));
            String tempString = null;
            while((tempString = reader.readLine()) != null) {
                heap.Insert(new Node(Integer.valueOf(tempString.split(" ")[0]), Integer.valueOf(tempString.split(" ")[1])));
            }
            reader.close();
        } catch(IOException e) {
            System.out.println("无法读取文件：task.dat");
        }
    }
}

class Node {
    private int index;
    private int priority;

    public Node() {}

    public Node(int index, int priority) {
        this.index = index;
        this.priority = priority;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }
}

class Heap {
    //示例代码的n即为elements的个数
    private int MAXSIZE = 200;
    private Node[] elements = new Node[MAXSIZE];
    private int length = 0;

    public boolean isEmpty() {
        return length == 0;
    }

    public boolean isFull() {
        return length == MAXSIZE - 1;
    }

    public int getSize() {
        return length;
    }

    public void Insert(Node node) {
        int i = 0;
        if(!isFull()) {
            i = length + 1;
            while((i != 1) && (node.getPriority() > elements[i / 2].getPriority())) {
                elements[i] = elements[i / 2];
                i = i / 2;
            }
            elements[i] = node;
            length ++;
        }
    }

    public Node deleteMax() {
        int parent = 1, child = 2;
        Node ele, tmp;
        if(!isEmpty()) {
            ele = elements[1];
            tmp = elements[length --];
            while(child <= length) {
                if((child < length) && (elements[child].getPriority() < elements[child + 1].getPriority())) {
                    child ++;
                }
                if(tmp.getPriority() >= elements[child].getPriority()) {
                    break;
                }
                elements[parent] = elements[child];
                parent = child;
                child *= 2;
            }
            elements[parent] = tmp;
            return ele;
        }
        return null;
    }
}