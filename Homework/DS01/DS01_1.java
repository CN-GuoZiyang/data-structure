public class DS01_1 {
    public static void main(String[] args) {
        String[] samples = {"PAB", "5C", "PABC", "CXY", "CRSI", "7", "B899", "B9"}; //待排序数据集
        quickSort(samples, 0, samples.length - 1);
        for (int i = 0; i < samples.length; i ++) {
            System.out.print(samples[i] + " ");
        }
    }
    //快速排序方法
    private static void quickSort(String[] samples, int start, int end) {
        if (start >= end) {
            return;
        }
        int left = start;
        int right = end;
        String temp = samples[left];
        while (left < right) {
            while (myCompare(samples[right], temp) == 1 && left < right) {
                right --;
            }
            if (left < right) {
                samples[left] = samples[right];
                left ++;
            }
            while (myCompare(samples[left], temp) == 2 && left < right) {
                left ++;
            }
            if (left < right)
            {
                samples[right] = samples[left];
                right --;
            }
        }
        samples[left] = temp;
        quickSort(samples, start, left - 1);
        quickSort(samples, left + 1, end);
    }
    //按照给定顺序比较两个字符串的方法
    private static int myCompare(String a, String b) {
        int tempa = 0, tempb = 0;
        String dic = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        while (a.length() != 0 && b.length() != 0) {
            if (a.charAt(0) != b.charAt(0)) {
                for (int i = 0; i < dic.length(); i ++) {
                    if (a.charAt(0) == dic.charAt(i)) {
                        tempa = i;
                    }
                    if (b.charAt(0) == dic.charAt(i)) {
                        tempb = i;
                    }
                }
                if (tempa > tempb) {
                    return 1;
                } else if (tempa < tempb) {
                    return 2;
                }
            } else {
                a = a.substring(1);
                b = b.substring(1);
            }
        }
        if (a.length() == 0) {
            return 1;
        } else if (b.length() == 0) {
            return 2;
        }
        return 0;
    }
}