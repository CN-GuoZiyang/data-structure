import java.util.ArrayList;

public class Main {
    static ArrayList<Integer> A1 = new ArrayList<>();
    static ArrayList<Integer> A2 = new ArrayList<>();
    static ArrayList<Integer> A3 = new ArrayList<>();
    static ArrayList<Integer> A4 = new ArrayList<>();
    static ArrayList<Integer> A5 = new ArrayList<>();
    static ArrayList<Integer> A6 = new ArrayList<>();
    static ArrayList<Integer> A7 = new ArrayList<>();
    static ArrayList<Integer> A8 = new ArrayList<>();
    static ArrayList<ArrayList<Integer>> AA = new ArrayList<>();
    static TreeNode TopNode;

    public static void InitArray(){
        A1.add(10);A1.add(15);A1.add(16);
        A2.add(9);A2.add(20);A2.add(28);
        A3.add(20);A3.add(20);A3.add(30);
        A4.add(6);A4.add(15);A4.add(25);
        A5.add(8);A5.add(15);A5.add(20);
        A6.add(9);A6.add(11);A6.add(16);
        A7.add(90);A7.add(100);A7.add(110);
        A8.add(17);A8.add(18);A8.add(20);
        AA.add(A1);AA.add(A2);AA.add(A3);AA.add(A4);AA.add(A5);AA.add(A6);AA.add(A7);AA.add(A8);
    }


    public static void InitLoserTree(){
        ArrayList<TreeNode> Tree0 = new ArrayList<>();
        ArrayList<TreeNode> Tree1 = new ArrayList<>();
        ArrayList<TreeNode> Tree2 = new ArrayList<>();
        for(int i = 0;i < 8;i += 2){
            TreeNode TmpNode = new TreeNode();
            if(AA.get(i).isEmpty()){
                AA.get(i).add(Integer.MAX_VALUE);
            }
            if(AA.get(i+1).isEmpty()){
                AA.get(i+1).add(Integer.MAX_VALUE);
            }
            TmpNode.ChangeValue(AA.get(i).get(0) > AA.get(i+1).get(0)?i:i+1);
            TmpNode.ChangeLoser(AA.get(i).get(0) > AA.get(i+1).get(0)?i+1:i);
            TreeNode LeftNode = new TreeNode();
            LeftNode.ChangeValue(i);
            LeftNode.ChangeFather(TmpNode);
            TmpNode.ChangeLeft(LeftNode);

            TreeNode RightNode = new TreeNode();
            RightNode.ChangeValue(i+1);
            RightNode.ChangeFather(TmpNode);
            TmpNode.ChangeRight(RightNode);
            Tree1.add(TmpNode);
        }
        for(int i = 0;i < 4;i += 2){
            TreeNode TmpNode = new TreeNode();
            if(AA.get(Tree1.get(i).GetLoser()).isEmpty()){
                AA.get(Tree1.get(i).GetLoser()).add(Integer.MAX_VALUE);
            }
            if(AA.get(Tree1.get(i+1).GetLoser()).isEmpty()){
                AA.get(Tree1.get(i+1).GetLoser()).add(Integer.MAX_VALUE);
            }
            TmpNode.ChangeValue(AA.get(Tree1.get(i).GetLoser()).get(0) > AA.get(Tree1.get(i+1).GetLoser()).get(0)?Tree1.get(i).GetLoser():Tree1.get(i+1).GetLoser());
            TmpNode.ChangeLoser(AA.get(Tree1.get(i).GetLoser()).get(0) > AA.get(Tree1.get(i+1).GetLoser()).get(0)?Tree1.get(i+1).GetLoser():Tree1.get(i).GetLoser());
            TmpNode.ChangeLeft(Tree1.get(i));
            TmpNode.ChangeRight(Tree1.get(i+1));
            Tree1.get(i).ChangeFather(TmpNode);
            Tree1.get(i+1).ChangeFather(TmpNode);
            Tree2.add(TmpNode);
        }
        for(int i = 0;i < 2;i += 2){
            TreeNode TmpNode = new TreeNode();
            if(AA.get(Tree2.get(i).GetLoser()).isEmpty()){
                AA.get(Tree2.get(i).GetLoser()).add(Integer.MAX_VALUE);
            }
            if(AA.get(Tree2.get(i+1).GetLoser()).isEmpty()){
                AA.get(Tree2.get(i+1).GetLoser()).add(Integer.MAX_VALUE);
            }
            TmpNode.ChangeValue(AA.get(Tree2.get(i).GetLoser()).get(0) > AA.get(Tree2.get(i+1).GetLoser()).get(0)?Tree2.get(i).GetLoser():Tree2.get(i+1).GetLoser());
            TmpNode.ChangeLoser(AA.get(Tree2.get(i).GetLoser()).get(0) > AA.get(Tree2.get(i+1).GetLoser()).get(0)?Tree2.get(i+1).GetLoser():Tree2.get(i).GetLoser());
            TmpNode.ChangeLeft(Tree2.get(i));
            TmpNode.ChangeRight(Tree2.get(i+1));
            Tree2.get(i).ChangeFather(TmpNode);
            Tree2.get(i+1).ChangeFather(TmpNode);
            TopNode = TmpNode;
        }
    }

    public static boolean FINISH(){
        for(int i = 0;i < 8;i ++){
            if(AA.get(i).get(0) != Integer.MAX_VALUE){
                return false;
            }
        }
        return true;
    }

    public static void MergeSort(){
        while(!FINISH()) {
            System.out.print(AA.get(TopNode.GetLoser()).get(0) + " ");
            AA.get(TopNode.GetLoser()).remove(0);
            InitLoserTree();
        }
        System.out.println();
    }

    public static void main(String[] args) {
        InitArray();
        InitLoserTree();
        MergeSort();
    }
}
class TreeNode
{
    int Value;
    int Loser;
    TreeNode FatherNode;
    TreeNode RightNode;
    TreeNode LeftNode;

    public void ChangeFather(TreeNode NewNode){
        this.FatherNode = NewNode;
    }

    public void ChangeLeft(TreeNode NewNode){
        this.LeftNode = NewNode;
    }

    public void ChangeRight(TreeNode NewNode){
        this.RightNode = NewNode;
    }

    public void ChangeLoser(int NewLoser){
        this.Loser = NewLoser;
    }

    public void ChangeValue(int NewValue){
        this.Value = NewValue;
    }

    public int GetLoser(){
        return this.Loser;
    }

    public int GetValue(){
        return this.Value;
    }

    public TreeNode GetFather(){
        return this.FatherNode;
    }

    public TreeNode GetRight(){
        return this.RightNode;
    }

    public TreeNode GetLeft(){
        return this.LeftNode;
    }
}