import java.util.Scanner;

public class DS_String {
    private static String rawString;
    private static String matchString;
    private static Node headNode;

    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        inputString();
        constructLinkedList();
        if(matchString()) {
            System.out.println("字符串匹配！");
        } else {
            System.out.println("字符串不匹配！");
        }
    }

    private static void inputString() {
        System.out.println("请输入原始字符串：");
        rawString = scanner.nextLine();
        System.out.println("请输入待匹配字符串：");
        matchString = scanner.nextLine();
    }

    private static void constructLinkedList() {
        int complement = rawString.length() % 4;
        int nodeNumber = (complement == 0) ? (rawString.length() / 4) : (rawString.length() / 4 + 1);
        for(int i = 0; i < 4 - complement; i ++) {
            rawString += "\0";
        }
        headNode = new Node("");
        Node tempNode = headNode;
        for(int i = 0; i < nodeNumber; i ++) {
            tempNode.setNextNode(new Node(rawString.substring(i * 4, i * 4 + 4)));
            tempNode.getNextNode().setPreviousNode(tempNode);
            tempNode = tempNode.getNextNode();
        }
    }

    private static boolean matchString() {
        int currentMatchIndex = 0;      //待匹配字符串的移动下标
        Node currentRawNode = headNode.getNextNode();   //头匹配时头所在的节点
        int currentRawIndex = 0;        //头匹配时头所在的节点的字符串的下标
        Node currentRawMoveNode = null; //头匹配时逐个匹配的节点
        int currentRawMoveIndex = 0;    //头匹配时逐个匹配的节点字符串下标
        while(currentRawNode != null) {
            outer:for(currentRawIndex = 0; currentRawIndex < 4; currentRawIndex ++) {
                if(currentRawNode.getNodeString().charAt(currentRawIndex) == matchString.charAt(0)) {
                    currentRawMoveNode = currentRawNode;
                    currentRawMoveIndex = currentRawIndex;
                    for(currentMatchIndex = 0; currentMatchIndex < matchString.length(); currentMatchIndex ++) {
                        if(currentRawMoveIndex >= 4) {
                            currentRawMoveNode = currentRawMoveNode.getNextNode();
                            currentRawMoveIndex = 0;
                        }
                        if(currentRawMoveNode.getNodeString().charAt(currentRawMoveIndex) != matchString.charAt(currentMatchIndex)) {
                            continue outer;
                        }
                        currentRawMoveIndex ++;
                    }
                    return true;
                }
            }
            currentRawNode = currentRawNode.getNextNode();
        }
        return false;
    }
}

class Node {
    private String nodeString;
    private Node previousNode;
    private Node nextNode;

    public Node(String nodeString) {
        this.nodeString = nodeString;
    }

    public String getNodeString() {
        return nodeString;
    }

    public void setNodeString(String nodeString) {
        this.nodeString = nodeString;
    }

    public Node getPreviousNode() {
        return previousNode;
    }

    public void setPreviousNode(Node previousNode) {
        this.previousNode = previousNode;
    }

    public Node getNextNode() {
        return nextNode;
    }

    public void setNextNode(Node nextNode) {
        this.nextNode = nextNode;
    }
}