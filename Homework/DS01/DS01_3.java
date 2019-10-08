import java.util.List;
import java.util.ArrayList;
import java.util.Scanner;

public class DS01_3 {

    private static int teamNumber = 0;
    private static ArrayList<Node> nodeList = new ArrayList<>();
    private static int[][] graph;
    private static Stack stack;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("请输入队伍总数：");
        teamNumber = scanner.nextInt();
        System.out.println("\n比赛结果的邻接矩阵如下：");
        graph = new int[teamNumber][teamNumber];
        for(int i = 0; i < teamNumber; i ++) {
            graph[i] = new int[teamNumber];
        }
        //随机生成比赛结果的有向图的邻接矩阵
        for(int i = 0; i < teamNumber; i ++) {
            graph[i][i] = 0;
            for(int j = i + 1; j < teamNumber; j ++) {
                int temp = (int)(Math.random() * 10) + 1;
                if(temp <= 5){
                    graph[i][j] = 0;
                    graph[j][i] = 1;
                }else{
                    graph[i][j] = 1;
                    graph[j][i] = 0;
                }
            }
        }
        for(int i = 0; i < teamNumber; i ++) {
            int[] temp = graph[i];
            for(int j = 0; j < teamNumber; j ++) {
                System.out.print(temp[j] + " ");
            }
            System.out.println();
        }


        for(int i = 0; i < teamNumber; i ++) {
            nodeList.add(new Node(i));
        }
        findAWay();
        System.out.println("\n查找到一条哈密顿路如下：");
        System.out.println(stack.toString());
        scanner.close();
    }
    //使用深度优先算法寻找一条哈密顿通路，找到即停止
    private static void findAWay() {
        int[] tempArray;
        stack = new Stack();
        stack.push(nodeList.get((int)(Math.random() * teamNumber)));
        stack.getTop().setTrue();
        while(!stack.isFull()) {
            if(stack.isEmpty()) {
                for(int i = 0; i < teamNumber; i ++) {
                    if(nodeList.get(i).getHasGet() == false) {
                        stack.push(nodeList.get(i));
                        stack.getTop().setTrue();
                        break;
                    }
                }
            }
            if(stack.isEmpty()) {
                System.out.println("\n该组数据无哈密顿通路！");
                System.exit(0);
            }
            tempArray = graph[stack.getTop().getIndex()];
            ArrayList<Node> tempList = new ArrayList<>();
            for(int i = 0; i < teamNumber; i ++) {
                if(tempArray[i] == 1 && nodeList.get(i).getHasGet() == false) {
                    tempList.add(nodeList.get(i));
                }
            }
            if(tempList.isEmpty()) {
                stack.pop();
            }else{
                stack.push(tempList.get((int)(Math.random() * tempList.size())));
                stack.getTop().setTrue();
            }
        }
    }
    //自定义的栈
    static class Stack{
        private List<Node> nodeList = new ArrayList<>();
    
        public void push(Node node) {
            nodeList.add(node);
        }
    
        public Node pop() {
            Node node = getTop();
            nodeList.remove(nodeList.size() - 1);
            return node;
        }
    
        public Node getTop() {
            return nodeList.get(nodeList.size() - 1);
        }
    
        @Override
        public String toString() {
            StringBuilder builder = new StringBuilder("[ " + nodeList.get(0).getIndex());
            for(int i = 1; i < nodeList.size(); i ++) {
                builder.append(" --> " + nodeList.get(i).getIndex());
            }
            builder.append(" ]");
            return builder.toString();
        }

        public Boolean isFull() {
            if(nodeList.size() == teamNumber) {
                return true;
            } else {
                return false;
            }
        }

        public Boolean isEmpty() {
            if(nodeList.size() == 0) {
                return true;
            } else {
                return false;
            }
        }
    }
    //节点类，等效于队伍
    static class Node{
        private int index;
        private boolean hasGet;
    
        public Node(int index) {
            this.index = index;
            hasGet = false;
        }
    
        public int getIndex() {
            return index;
        }
    
        public Boolean getHasGet() {
            return hasGet;
        }
    
        public void setTrue() {
            this.hasGet = true;
        }
    
        public void setFalse() {
            this.hasGet = false;
        }
    }
}