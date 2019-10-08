import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.io.FileReader;

public class Main {
    private static AOE aoe = new AOE();
    private static ArrayList<ArrayList<VertexNode>> results = new ArrayList<>();
    private static boolean[] visit = new boolean[8];
    private static Stack stack = new Stack();
    private static ArrayList<VertexNode> keyWay;

    public static void main(String[] args) {
        buildAOE();
        showAOE();
        findKeyWay();
    }

    private static void buildAOE() {
        for(int i = 0; i < 8; i ++) {
            aoe.add(new VertexNode(i));
        }
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader("data.txt"));
            String line = null;
            while((line = reader.readLine()) != null) {
                VertexNode tempNode = aoe.get(Integer.parseInt(line.split(" ")[0]));
                EdgeNode tempEdge = tempNode.getFirstEdge();
                if(tempEdge == null) {
                    tempNode.setFirstEdge(new EdgeNode(Integer.parseInt(line.split(" ")[3]), line.split(" ")[1], Integer.parseInt(line.split(" ")[2]), Integer.parseInt(line.split(" ")[0])));
                } else {
                    while(tempEdge.getNextNode() != null) {
                        tempEdge = tempEdge.getNextNode();
                    }
                    tempEdge.setNextNode(new EdgeNode(Integer.parseInt(line.split(" ")[3]), line.split(" ")[1], Integer.parseInt(line.split(" ")[2]), Integer.parseInt(line.split(" ")[0])));
                }
            }
            System.out.println("AOE网建立完成。");
        } catch (Exception e) {
            System.out.println("建立AOE网时有错误发生。");
            System.exit(1);
        } finally {
            try {
                if(reader != null) {
                    reader.close();
                }
            } catch(IOException e) {
                System.out.println("关闭流时有错误发生。");
            }
        }
    }

    private static void showAOE() {
        for(int i = 0; i < 8; i ++) {
            VertexNode tempNode = aoe.get(i);
            EdgeNode tempEdge = tempNode.getFirstEdge();
            if(tempEdge == null) {
                System.out.println(tempNode.getVertex());
                return;
            } else {
                System.out.print(tempNode.getVertex() + " ==> ");
            }
            while(tempEdge.getNextNode() != null) {
                System.out.print(tempEdge.getEvent() + "," + tempEdge.getTime() + "," + tempEdge.getVertex() + " --> ");
                tempEdge = tempEdge.getNextNode();
            }
            System.out.println(tempEdge.getEvent() + "," + tempEdge.getTime() + "," + tempEdge.getVertex());
        }
    }

    private static void findKeyWay() {
        VertexNode startNode = aoe.get(0);
        VertexNode lastNode = aoe.get(7);
        DFS(startNode, lastNode);
        // for(ArrayList<VertexNode> result : results) {
        //     for(VertexNode node : result) {
        //         System.out.print(node.getVertex() + " ");
        //     }
        //     System.out.println();
        // }
        int[] resultLength = new int[results.size()];
        for(int i = 0; i < results.size(); i ++) {
            resultLength[i] = 0;
        }
        for(int i = 0; i < results.size(); i ++) {
            ArrayList<VertexNode> result = results.get(i);
            for(int j = 0; j < result.size() - 1; j ++) {
                int target = result.get(j + 1).getVertex();
                VertexNode tempNode = result.get(j);
                EdgeNode tempEdge = tempNode.getFirstEdge();
                while(tempEdge != null) {
                    if(tempEdge.getVertex() == target) {
                        resultLength[i] += tempEdge.getTime();
                        break;
                    }
                    tempEdge = tempEdge.getNextNode();
                }
            }
        }
        // for(int i = 0; i < results.size(); i ++) {
        //     System.out.print(resultLength[i] + " ");
        // }
        int maxIndex = 0;
        for(int i = 0; i < resultLength.length; i ++) {
            if(resultLength[i] > resultLength[maxIndex]) {
                maxIndex = i;
            }
        }
        keyWay = results.get(maxIndex);
        System.out.println("该工程至少需要" + resultLength[maxIndex] + "天完成");
        System.out.println("关键工程顺序如下：");
        for(int j = 0; j < keyWay.size() - 1; j ++) {
            int target = keyWay.get(j + 1).getVertex();
            VertexNode tempNode = keyWay.get(j);
            EdgeNode tempEdge = tempNode.getFirstEdge();
            while(tempEdge != null) {
                if(tempEdge.getVertex() == target) {
                    System.out.print(tempEdge.getEvent() + " ");
                    break;
                }
                tempEdge = tempEdge.getNextNode();
            }
        }
        System.out.println();
        System.out.println("关键事件如下：");
        for(VertexNode node : keyWay) {
            System.out.print(node.getVertex() + " ");
        }
        System.out.println();
    }

    private static void DFS(VertexNode startNode, VertexNode lastNode) {
        stack.push(startNode);
        visit[startNode.getVertex()] = true;
        while(!stack.isEmpty()) {
            VertexNode tempVertex = stack.getTop();
            if(tempVertex.getVertex() == lastNode.getVertex()) {
                results.add(stack.getList());
                stack.pop();
                visit[lastNode.getVertex()] = false;
                break;
            }
            EdgeNode tempEdge = startNode.getFirstEdge();
            while(tempEdge != null) {
                if(!visit[tempEdge.getVertex()]) {
                    DFS(aoe.get(tempEdge.getVertex()), lastNode);
                }
                tempEdge = tempEdge.getNextNode();
            }
            if(tempEdge == null) {
                stack.pop();
                visit[startNode.getVertex()] = false;
                break;
            }
        }
    }
}

class VertexNode {
    private int vertex;
    private EdgeNode firstEdge;

    public VertexNode(int vertex) {
        this.vertex = vertex;
        this.firstEdge = null;
    }

    public int getVertex() {
        return vertex;
    }

    public EdgeNode getFirstEdge() {
        return firstEdge;
    }

    public void setFirstEdge(EdgeNode firstEdge) {
        this.firstEdge = firstEdge;
    }

    public void setVertex(int vertex) {
        this.vertex = vertex;
    }
}

class EdgeNode {
    private int vertex;
    private int input;
    private String event;
    private int time;
    private EdgeNode nextNode;

    public EdgeNode(int vertex, String event, int time, int input) {
        this.vertex = vertex;
        this.event = event;
        this.time = time;
        this.setInput(input);
        this.nextNode = null;
    }

    public int getInput() {
        return input;
    }

    public void setInput(int input) {
        this.input = input;
    }

    public int getVertex() {
        return vertex;
    }

    public EdgeNode getNextNode() {
        return nextNode;
    }

    public void setNextNode(EdgeNode nextNode) {
        this.nextNode = nextNode;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public void setVertex(int vertex) {
        this.vertex = vertex;
    }
}

class AOE {
    private ArrayList<VertexNode> nodes = null;

    public AOE() {
        nodes = new ArrayList<>();
    }

    public VertexNode get(int index) {
        for(int i = 0; i < nodes.size(); i ++) {
            if(index == nodes.get(i).getVertex()) {
                return nodes.get(i);
            }
        }
        return null;
    }

    public void add(VertexNode node) {
        nodes.add(node);
    }

    public ArrayList<VertexNode> getList() {
        return nodes;
    }
}

class Stack {
    private ArrayList<VertexNode> list;

    public Stack() {
        list = new ArrayList<>();
    }

    public void push(VertexNode node) {
        list.add(node);
    }

    public VertexNode pop() {
        return list.remove(list.size() - 1);
    }

    public VertexNode getTop() {
        return list.get(list.size() - 1);
    }

    public boolean isEmpty() {
        return list.isEmpty();
    }

    @SuppressWarnings("unchecked")
    public ArrayList<VertexNode> getList() {
        return (ArrayList<VertexNode>)list.clone();
    }
}