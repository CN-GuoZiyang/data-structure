import java.util.ArrayList;

public class Main {
    private static ArrayList<Node> nodes = new ArrayList<>();
    private static int[][] edges;
    private static ArrayList<ArrayList<Node>> results = new ArrayList<>();
    private static Stack stack = new Stack();
    private static boolean[] visit;

    public static void main(String[] args) {
        createGraph();
        searchWay();
        showResult();
    }

    private static void createGraph() {
        boolean booleans[] = new boolean[]{true, false};
        for(boolean wolf : booleans) {
            for(boolean sheep : booleans) {
                for(boolean vegetable : booleans) {
                    for(boolean farmer : booleans) {
                        if(isValid(wolf, sheep, vegetable, farmer)) {
                            Node node = new Node(wolf, sheep, vegetable, farmer);
                            node.setIndex(nodes.size());
                            nodes.add(node);
                        }
                    }
                }
            }
        }
        edges = new int[nodes.size()][nodes.size()];
        for(int i = 0; i < nodes.size(); i ++) {
            for(int j = 0; j < nodes.size(); j ++) {
                if(canConnect(nodes.get(i), nodes.get(j))) {
                    edges[i][j] = 1;
                } else {
                    edges[i][j] = 0;
                }
            }
        }
        visit = new boolean[nodes.size()];
    }

    private static void searchWay() {
        int firstNodeIndex = 0;
        int lastNodeIndex = 0;
        for(int i = 0; i < nodes.size(); i ++) {
            Node tempNode = nodes.get(i);
            if(!tempNode.getFarmer() && !tempNode.getSheep() &&
            !tempNode.getVegetable() && !tempNode.getWolf()) {
                firstNodeIndex = i;
            }
            if(tempNode.getFarmer() && tempNode.getSheep() &&
            tempNode.getVegetable() && tempNode.getWolf()) {
                lastNodeIndex = i;
            }
        }
        DFS(nodes.get(firstNodeIndex), nodes.get(lastNodeIndex));
    }

    private static void DFS(Node startNode, Node lastNode) {
        stack.push(startNode);
        //System.out.print(startNodeIndex + " ");
        visit[startNode.index] = true;
        while(!stack.isEmpty()) {
            int tempNodeIndex = stack.getTop().getIndex();
            if(tempNodeIndex == lastNode.index) {
                results.add(stack.getList());
                stack.pop();
                visit[lastNode.index] = false;
                break;
            }
            while(startNode.flag < nodes.size()) {
                if(edges[tempNodeIndex][startNode.flag] == 1 && !visit[startNode.flag]) {
                    DFS(nodes.get(startNode.flag), lastNode);
                }
                startNode.flag ++;
            }
            if(startNode.flag == nodes.size()) {
                stack.pop();
                startNode.flag = 0;
                visit[startNode.index] = false;
                break;
            }
        }
    }

    private static void showResult() {
        for(int i = 0; i < results.size(); i ++) {
            System.out.println("第" + (i+1) + "种答案");
            ArrayList<Node> result = results.get(i);
            for(int j = 0; j < result.size() - 1; j ++) {
                Node before = result.get(j);
                Node after = result.get(j + 1);
                StringBuilder builder = new StringBuilder("农夫");
                if(before.getSheep() != after.getSheep()) {
                    builder.append("带着羊");
                }else if(before.getWolf() != after.getWolf()){
                    builder.append("带着狼");
                }else if(before.getVegetable() != after.getVegetable()) {
                    builder.append("带着菜");
                }else {
                    builder.append("什么都不带");
                }
                if(after.getFarmer()) {
                    builder.append("过到对岸。");
                }else {
                    builder.append("回到原岸。");
                }
                System.out.println(builder.toString());
            }
            System.out.println();
        }
    }

    private static boolean isValid(boolean wolf, boolean sheep, boolean vegetable, boolean farmer) {
        if((farmer != sheep) && ((wolf == sheep) || (sheep == vegetable))) {
            return false;
        } else {
            return true;
        }
    }

    private static boolean canConnect(Node i, Node j) {
        int change = 0;
        if(i.getWolf() != j.getWolf()) change ++;
        if(i.getSheep() != j.getSheep()) change ++;
        if(i.getVegetable() != j.getVegetable()) change ++;
        if(i.getFarmer() != j.getFarmer() && change <= 1) {
            return true;
        }
        return false;
    }
}

class Node {
    private boolean wolf;
    private boolean sheep;
    private boolean vegetable;
    private boolean farmer;
    public int index;
    public int flag = 0;

    public Node(boolean wolf, boolean sheep, boolean vegetable, boolean farmer) {
        this.wolf = wolf;
        this.sheep = sheep;
        this.vegetable = vegetable;
        this.farmer = farmer;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public int getIndex() {
        return index;
    }

    public boolean getWolf() {
        return wolf;
    }

    public boolean getSheep() {
        return sheep;
    }

    public boolean getVegetable() {
        return vegetable;
    }

    public boolean getFarmer() {
        return farmer;
    }
}

class Stack {
    private ArrayList<Node> list;

    public Stack() {
        list = new ArrayList<>();
    }

    public void push(Node node) {
        list.add(node);
    }

    public Node pop() {
        return list.remove(list.size() - 1);
    }

    public Node getTop() {
        return list.get(list.size() - 1);
    }

    public boolean isEmpty() {
        return list.isEmpty();
    }

    @SuppressWarnings("unchecked")
    public ArrayList<Node> getList() {
        return (ArrayList<Node>)list.clone();
    }
}