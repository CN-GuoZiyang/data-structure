import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Scanner;

public class Main {
    private static int C;
    private static double [][] Lengths = new double[C][C];
    private static ArrayList<Point> Islands = new ArrayList<>();

    public static int Find(int[] father, int v){
        int f = v;
        while(father[f] > -1){
            f = father[f];
        }
        return f;
    }

    public static void Kraskal(){
        ArrayList<Edge> edges = new ArrayList<>();
        int[] Father = new int[C];
        int bnf, edf;
        boolean[] Visual = new boolean[C];
        for(int i = 0;i < C;i ++){
            Visual[i] = false;
        }

        double ans = 0;
        for(int i = 0;i < C;i++){
            Father[i] = -1;
        }
        for(int i = 0;i < C;i ++){
            for(int j = i + 1;j < C;j ++){
                if(Lengths[i][j] <= 1000 && Lengths[i][j] >= 10){
                    Edge tmp = new Edge(i, j, Lengths[i][j]);
                    edges.add(tmp);
                    Visual[i] = true;
                    Visual[j] = true;
                }
            }
        }

        edges.sort(new Comparator<Edge>(){
                @Override
                public int compare(Edge arg0, Edge arg1) {
                    double tmp = arg0.Getwei();
                    double tmp2 = arg1.Getwei();
                    if(tmp > tmp2){
                        return 1;
                    }else if(tmp == tmp2){
                        return 0;
                    }else{
                        return -1;
                    }
            }
        });

        for(int i = 0;i < edges.size();i ++){
            bnf = Find(Father, edges.get(i).Getbeg());
            edf = Find(Father, edges.get(i).Geted());
            if(bnf != edf){
                Father[bnf] = edf;
                ans += edges.get(i).Getwei();
            }
        }

        DecimalFormat format = new DecimalFormat("0.0");


        int flag = 0;
        for(int i = 0; i < C; i++) {
            if(!Visual[i]) {
                 flag = 1;
                 break;
            }
        }
        if(flag == 1){
            System.out.println("oh!");
        }else
        {
            System.out.println(format.format(ans * 100));
        }
    }

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int T = in.nextInt();
        while(T != 0){
            T--;
            Islands = new ArrayList<>();
            C = in.nextInt();
            Lengths = new double[C][C];

            for(int i = 0;i < C;i ++){
                for(int j = 0;j < C;j++){
                    Lengths[i][j] = Double.MAX_VALUE;
                }
            }

            for(int i = 0;i < C;i ++){
                int a = in.nextInt();
                int b = in.nextInt();
                Point tmp = new Point(a, b);
                Islands.add(tmp);
            }

            for(int i = 0;i < C;i ++){
                for(int j = 0;j < C;j ++){
                    if(i == j){
                        continue;
                    }
                    Lengths[i][j] = Math.sqrt(Math.pow(Islands.get(i).GetX()-Islands.get(j).GetX(),2) + Math.pow(Islands.get(i).GetY()-Islands.get(j).GetY(),2));
                }
            }
            Kraskal();
        }
    }
}

class Point
{
    private int x;
    private int y;

    public Point(int X, int Y){
        this.x = X;
        this.y = Y;
    }

    public void ChangeX(int X){
        this.x = X;
    }

    public void ChangeY(int Y){
        this.y = Y;
    }

    public int GetX(){
        return this.x;
    }

    public int GetY(){
        return this.y;
    }
}

class Edge
{
    private int beg;
    private int ed;
    private double weight;

    public Edge(){

    }

    public Edge(int bg, int fr, double we){
        this.beg = bg;
        this.ed = fr;
        this.weight = we;
    }

    public void Setbeg(int newbg){
        this.beg = newbg;
    }

    public void Seted(int newed){
        this.ed = newed;
    }

    public void Setwei(double newwei){
        this.weight = newwei;
    }

    public int Getbeg(){
        return this.beg;
    }
    public double Getwei(){
        return this.weight;
    }
    public int Geted(){
        return this.ed;
    }

    public void change(Edge tmp){
        this.beg = tmp.Getbeg();
        this.ed = tmp.Geted();
        this.weight = tmp.Getwei();
    }
}
