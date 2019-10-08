import java.util.ArrayList;
import java.util.Random;

public class DS01_2 {
    private static int maxNumber;
    private static ArrayList<Integer> maxIndex = new ArrayList<>();
    public static void main(String[] args) {
        int[] samples = new int[100];
        Random random = new Random();
        for(int i = 0; i < 100; i ++) {  
            samples[i] = random.nextInt(999) + 1;
            System.out.print(samples[i] + " ");
        }
        System.out.println();
        findMax(samples);
        System.out.println("The max number is " + maxNumber + " and the max index is " + maxIndex.toString());
    }
    //从两侧向中间查找最大值的方法
    private static void findMax(int[] samples) {
        int leftIndex = 1;
        int rightIndex = 99;
        maxNumber = samples[0];
        maxIndex.add(0);
        while(leftIndex < rightIndex) {
            if(samples[leftIndex] == maxNumber) {
                maxIndex.add(leftIndex);
            }
            if(samples[leftIndex] > maxNumber) {
                maxNumber = samples[leftIndex];
                maxIndex.clear();
                maxIndex.add(leftIndex);
            }
            if(samples[rightIndex] == maxNumber) {
                maxIndex.add(rightIndex);
            }
            if(samples[rightIndex] > maxNumber) {
                maxNumber = samples[rightIndex];
                maxIndex.clear();
                maxIndex.add(rightIndex);
            }
            leftIndex ++;
            rightIndex --;
        }
    }
}