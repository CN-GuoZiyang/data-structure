import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.util.ArrayList;
import java.util.Random;

public class Main extends JFrame {
    private static JPanel mainPanel;
    public static MazeLabel[][] mazeLabels;       //25*20
    private JButton startBtn;
    private JButton primStart;
    private JButton findWayBtn;
    private JButton refresh;
    private String introductionStr;
    private JLabel introduction;

    public Main() {
        mainPanel = new JPanel();
        mazeLabels = new MazeLabel[20][25];
        startBtn = new JButton("深度优先算法开始");
        primStart = new JButton("随机Prim算法开始");
        refresh = new JButton("刷新");
        findWayBtn = new JButton("寻找一条路");
        initFrame();
        initPanel();
        initTable();
        initComp();
        setVisible(true);
    }

    private void initFrame() {
        setSize(900, 622);
        setLocationRelativeTo(null);
        setTitle("Maze");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
    }

    private void initPanel() {
        mainPanel.setLayout(null);
        setContentPane(mainPanel);
    }

    private void initTable() {
        for(int row = 0; row < 20; row ++){
            for(int col = 0; col < 25; col ++){
                mazeLabels[row][col] = new MazeLabel();
                mazeLabels[row][col].setBounds(150 + col * 30, row * 30, 30, 30);
                mazeLabels[row][col].row = row;
                mazeLabels[row][col].col = col;
                mainPanel.add(mazeLabels[row][col]);
            }
        }
    }

    private void initComp(){
        startBtn.setBounds(12, 20, 135, 40);
        startBtn.addActionListener(e -> {
            MazeTools.generateNewMaze();
        });
        mainPanel.add(startBtn);
        primStart.setBounds(12, 80, 135, 40);
        primStart.addActionListener(e -> {
            PrimGenerate.primGenerate();
        });
        mainPanel.add(primStart);
        findWayBtn.setBounds(12, 140, 135, 40);
        findWayBtn.addActionListener(e -> {
            GetWay.getWay();    
        });
        mainPanel.add(findWayBtn);
        refresh.setBounds(12, 200, 135, 40);
        refresh.addActionListener(e -> {
            for(int i = 0; i < 20; i ++) {
                for(int j = 0; j < 25; j ++) {
                    mazeLabels[i][j].repaint();
                }
            }
        });
        mainPanel.add(refresh);
        introductionStr = new String("<html><body><center>深度优先算法<br>" +
                "又称为递归回溯算法<br>" +
                "生成的迷宫主路较为明显<br>" +
                "难度较小<br><br>" +
                "随机Prim算法<br>" +
                "生成的迷宫岔路较多<br>" +
                "难度较大</center></html></body>");
        introduction = new JLabel(introductionStr);
        introduction.setBounds(0, 430, 150, 200);
        introduction.setHorizontalAlignment(SwingConstants.CENTER);
        introduction.setVerticalAlignment(SwingConstants.CENTER);
        mainPanel.add(introduction);
    }

    public static void main(String[] args) {
        new Main();
    }
}

class MazeLabel extends JLabel{
    private boolean upAlready;
    private boolean downAlready;
    private boolean leftAlready;
    private boolean rightAlready;
    private boolean already;
    private boolean isCurrent;
    private boolean isTarget;
    public boolean upWay;
    public boolean downWay;
    public boolean leftWay;
    public boolean rightWay;
    public int row;
    public int col;

    public MazeLabel(){
        initAll();
    }

    public void initAll() {
        already = false;
        upAlready = false;
        downAlready = false;
        leftAlready = false;
        rightAlready = false;
        isCurrent = false;
        isTarget = false;
        upWay = false;
        downWay = false;
        leftWay = false;
        rightWay = false;
        repaint();
    }
    @Override
    protected void paintComponent(Graphics g){
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D)g;
        g2.clearRect(0, 0, 30, 30);
        g2.setStroke(new BasicStroke(3));
        g2.setColor(Color.GREEN);
        if(!upAlready){
            g2.drawLine(1, 1, 29, 1);
        }
        if(!downAlready){
            g2.drawLine(1, 29, 29, 29);
        }
        if(!leftAlready){
            g2.drawLine(1, 1, 1, 29);
        }
        if(!rightAlready){
            g2.drawLine(29, 1, 29, 29);
        }
        if(isTarget){
            Ellipse2D.Double circle = new Ellipse2D.Double(5, 5, 20, 20);
            Paint p = g2.getPaint();
            g2.setPaint(Color.YELLOW);
            g2.fill(circle);
            g2.setPaint(p);
        }
        if(isCurrent){
            Ellipse2D.Double circle = new Ellipse2D.Double(5, 5, 20, 20);
            Paint p = g2.getPaint();
            g2.setPaint(Color.RED);
            g2.fill(circle);
            g2.setPaint(p);
        }
        g2.setStroke(new BasicStroke(3));
        g2.setColor(Color.BLUE);
        if(upWay) {
            g2.drawLine(15, 1, 15, 15);
        }
        if(downWay) {
            g2.drawLine(15, 29, 15, 15);
        }
        if(leftWay) {
            g2.drawLine(1, 15, 15, 15);
        }
        if(rightWay) {
            g2.drawLine(29, 15, 15, 15);
        }
    }


    public boolean isUpAlready() {
        return upAlready;
    }

    public void setUpAlready() {
        this.upAlready = true;
    }

    public boolean isDownAlready() {
        return downAlready;
    }

    public void setDownAlready() {
        this.downAlready = true;
    }

    public boolean isLeftAlready() {
        return leftAlready;
    }

    public void setLeftAlready() {
        this.leftAlready = true;
    }

    public boolean isRightAlready() {
        return rightAlready;
    }

    public void setRightAlready() {
        this.rightAlready = true;
    }

    public boolean getAlready() {
        return already;
    }

    public void setAlready() {
        this.already = true;
    }

    public void setTarget() {
        this.isTarget = true;
    }

    public void setCurrent() {
        this.isCurrent = true;
        repaint();
    }

    public void disableCurrent(){
        this.isCurrent = false;
        repaint();
    }

    public boolean isWin() {
        return isCurrent && isTarget;
    }
}

class MazeTools{
    private static int row;
    private static int col;
    private static int top;
    private static Random random;

    public static void generateNewMaze(){
        for(int row = 0; row < 20; row ++){
            for(int col = 0; col < 25; col ++){
                Main.mazeLabels[row][col].initAll();
            }
        }
        top = 0;
        row = 0;
        col = 0;
        random = new Random();
        ArrayList<MazeLabel> mazeLabels = new ArrayList<>();
        mazeLabels.add(Main.mazeLabels[0][0]);
        while(top >= 0){
            int[] val = new int[]{-1, -1, -1, -1};
            int times = 0;
            boolean flag = false;
            MazeLabel current = mazeLabels.get(top);
            row = current.row;
            col = current.col;
            current.setAlready();
            out:while(times < 4){
                int dir = random.nextInt(4);
                if(val[dir] == dir){
                    continue;
                }else{
                    val[dir] = dir;
                }
                switch (dir){
                    case 0:
                        if(row >= 1 && Main.mazeLabels[row - 1][col].getAlready() == false){
                            current.setUpAlready();
                            Main.mazeLabels[row - 1][col].setDownAlready();
                            mazeLabels.add(Main.mazeLabels[row - 1][col]);
                            top++;
                            flag = true;
                            break out;
                        }
                        break;
                    case 1:
                        if(row <= 18 && Main.mazeLabels[row + 1][col].getAlready() == false){
                            current.setDownAlready();
                            Main.mazeLabels[row + 1][col].setUpAlready();
                            mazeLabels.add(Main.mazeLabels[row + 1][col]);
                            top++;
                            flag = true;
                            break out;
                        }
                        break;
                    case 2:
                        if(col >= 1 && Main.mazeLabels[row][col - 1].getAlready() == false){
                            current.setLeftAlready();
                            Main.mazeLabels[row][col - 1].setRightAlready();
                            mazeLabels.add(Main.mazeLabels[row][col - 1]);
                            top++;
                            flag = true;
                            break out;
                        }
                        break;
                    case 3:
                        if(col <= 23 && Main.mazeLabels[row][col + 1].getAlready() == false){
                            current.setRightAlready();
                            Main.mazeLabels[row][col + 1].setLeftAlready();
                            mazeLabels.add(Main.mazeLabels[row][col + 1]);
                            top++;
                            flag = true;
                            break out;
                        }
                        break;
                }
                times ++;
            }
            if(!flag){
                mazeLabels.remove(top);
                top --;
            }
        }
        Main.mazeLabels[19][24].setTarget();
        Main.mazeLabels[19][24].repaint();
        Main.mazeLabels[0][0].setCurrent();
        Main.mazeLabels[0][0].repaint();
    }
}

class PrimGenerate {
    private static int row;
    private static int col;
    private static Random random;

    public static void primGenerate(){
        for(int row = 0; row < 20; row ++){
            for(int col = 0; col < 25; col ++){
                Main.mazeLabels[row][col].initAll();
            }
        }

        row = 0;
        col = 0;
        random = new Random();
        ArrayList<String> wallStack = new ArrayList<>();
        Main.mazeLabels[0][0].setAlready();
        wallStack.add("0,0,1,0");
        wallStack.add("0,0,0,1");
        while(!wallStack.isEmpty()){
            int randomNum = random.nextInt(wallStack.size());
            String tempStr = wallStack.get(randomNum);
            int x1 = Integer.parseInt(tempStr.split(",")[0]);
            int y1 = Integer.parseInt(tempStr.split(",")[1]);
            int x2 = Integer.parseInt(tempStr.split(",")[2]);
            int y2 = Integer.parseInt(tempStr.split(",")[3]);
            if(Main.mazeLabels[x2][y2].getAlready() ^ Main.mazeLabels[x1][y1].getAlready()){
                if(x2 < x1){
                    Main.mazeLabels[x1][y1].setUpAlready();
                    Main.mazeLabels[x2][y2].setDownAlready();
                }
                if(x2 > x1){
                    Main.mazeLabels[x1][y1].setDownAlready();
                    Main.mazeLabels[x2][y2].setUpAlready();
                }
                if(y2 < y1){
                    Main.mazeLabels[x1][y1].setLeftAlready();
                    Main.mazeLabels[x2][y2].setRightAlready();
                }
                if(y2 > y1){
                    Main.mazeLabels[x1][y1].setRightAlready();
                    Main.mazeLabels[x2][y2].setLeftAlready();
                }
                row = x2;
                col = y2;
                Main.mazeLabels[row][col].setAlready();
                if(row >= 1) {
                    wallStack.add(row + "," + col + "," + String.valueOf(row - 1) + "," + col);
                }
                if(row <= 18){
                    wallStack.add(row + "," + col + "," + String.valueOf(row + 1) + "," + col);
                }
                if(col >= 1){
                    wallStack.add(row + "," + col + "," + row + "," + String.valueOf(col - 1));
                }
                if(col <= 23){
                    wallStack.add(row + "," + col + "," + row + "," + String.valueOf(col + 1));
                }
            }else{
                wallStack.remove(randomNum);
            }
        }
        Main.mazeLabels[19][24].setTarget();
        Main.mazeLabels[19][24].repaint();
        Main.mazeLabels[0][0].setCurrent();
        Main.mazeLabels[0][0].repaint();
    }
}

class GetWay {
    private static boolean[][] visit;
    private static Stack stack;

    public static void getWay(){
        visit = new boolean[20][25];
        stack = new Stack();
        DFS();
        showRes();
        for(int i = 0; i < 20; i ++) {
            for(int j = 0; j < 25; j ++) {
                Main.mazeLabels[i][j].repaint();
            }
        }
    }

    private static void DFS(){
        stack.push(Main.mazeLabels[0][0]);
        visit[0][0] = true;
        while(!stack.isEmpty()) {
            MazeLabel tempLabel = stack.pop();
            int tempRow = tempLabel.row;
            int tempCol = tempLabel.col;
            if(tempRow == 19 && tempCol == 24) {
                stack.push(tempLabel);
                break;
            }
            for(int i = 0; i < 4; i ++) {
                if(i == 0) {
                    if(Main.mazeLabels[tempRow][tempCol].isUpAlready() && !visit[tempRow - 1][tempCol]) {
                        stack.push(tempLabel);
                        stack.push(Main.mazeLabels[tempRow - 1][tempCol]);
                        visit[tempRow][tempCol] = true;
                        visit[tempRow - 1][tempCol] = true;
                    }
                } else if(i == 1) {
                    if(Main.mazeLabels[tempRow][tempCol].isDownAlready() && !visit[tempRow + 1][tempCol]) {
                        stack.push(tempLabel);
                        stack.push(Main.mazeLabels[tempRow + 1][tempCol]);
                        visit[tempRow][tempCol] = true;
                        visit[tempRow + 1][tempCol] = true;
                    }
                } else if(i == 2) {
                    if(Main.mazeLabels[tempRow][tempCol].isLeftAlready() && !visit[tempRow][tempCol - 1]) {
                        stack.push(tempLabel);                        
                        stack.push(Main.mazeLabels[tempRow][tempCol - 1]);
                        visit[tempRow][tempCol] = true;
                        visit[tempRow][tempCol - 1] = true;
                    }
                } else {
                    if(Main.mazeLabels[tempRow][tempCol].isRightAlready() && !visit[tempRow][tempCol + 1]) {
                        stack.push(tempLabel);                        
                        stack.push(Main.mazeLabels[tempRow][tempCol + 1]);
                        visit[tempRow][tempCol] = true;
                        visit[tempRow][tempCol + 1] = true;
                    }
                }
            }
        }
    }

    private static void showRes() {
        ArrayList<MazeLabel> resList = stack.getList();
        ArrayList<MazeLabel> deleteList = new ArrayList<>();
        for(int i = 0; i < resList.size() - 2; i ++) {
            if(resList.get(i) == resList.get(i + 2)) {
                deleteList.add(resList.get(i + 1));
            }
        }
        for(int i = deleteList.size() - 1; i >= 0; i --) {
            int index = resList.indexOf(deleteList.get(i));
            resList.remove(index + 1);
            resList.remove(index);
        }
        for(int i = 0; i < resList.size() - 1; i ++) {
            MazeLabel before = resList.get(i);
            MazeLabel after = resList.get(i + 1);
            if(after.row - before.row == 1) {
                before.downWay = true;
                after.upWay = true;
            } else if(before.row - after.row == 1) {
                before.upWay = true;
                after.downWay = true;
            } else if(after.col - before.col == 1) {
                before.rightWay = true;
                after.leftWay = true;
            } else if(before.col - after.col == 1) {
                before.leftWay = true;
                after.rightWay = true;
            }
        }
    }
}

class Stack {
    private ArrayList<MazeLabel> list;

    public Stack() {
        list = new ArrayList<>();
    }

    public void push(MazeLabel element) {
        list.add(element);
    }

    public MazeLabel pop() {
        return list.remove(list.size() - 1);
    }

    public MazeLabel getTop() {
        return list.get(list.size() - 1);
    }

    public boolean isEmpty() {
        return list.isEmpty();
    }

    public ArrayList<MazeLabel> getList() {
        return list;
    }
}