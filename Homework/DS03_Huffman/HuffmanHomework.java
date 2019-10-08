import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.FileOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.Collections;
import java.io.File;
import java.io.FileInputStream;

public class HuffmanHomework {
    private static Scanner scanner = new Scanner(System.in);
    private static HashMap<Character, TreeNode> leafNodes = null;
    private static TreeNode topNode = null;
    private static File file = null;
    private static int binaryNumber = 0;

    public static void main(String[] args) {
        while(true) {
            menu();
        }
    }

    private static void menu() {
        System.out.println();
        System.out.println();
        System.out.println("1. 原始文件压缩");
        System.out.println("2. 压缩文件解码");
        System.out.println("3. 退出");
        System.out.print("请输入选择：");
        int choice = scanner.nextInt();
        if(choice != 1 && choice != 2 && choice != 3) {
            System.out.println("输入有误，请重新选择！");
            System.out.println();
            System.out.println();
            return;
        }
        System.out.println();
        System.out.println();
        switch(choice) {
            case 1:
                encrapt();
                break;
            case 2:
                decrapt();
                break;
            case 3:
                System.exit(0);
        }
    }

    private static void encrapt() {
        String fileContent = readFromFile();
        HashMap<Character, Double> perChance =  calculateCharChance(fileContent);
        topNode = buildHuffmanTree(perChance);
        String encraptedContent = encraptContent(fileContent);
        writeIntoFile(encraptedContent);
    }

    private static String readFromFile() {
        System.out.print("请输入加密的文件路径：");
        scanner = new Scanner(System.in);
        String fileName = scanner.nextLine();
        file = new File(fileName);
        try{
            BufferedReader reader = new BufferedReader(new FileReader(file));
            StringBuilder fileContentBuilder = new StringBuilder();
            String tempString = null;
            while((tempString = reader.readLine()) != null) {
                fileContentBuilder.append(tempString);
                fileContentBuilder.append("\n");
            }
            reader.close();
            return fileContentBuilder.toString();
        }catch(IOException exception) {
            System.out.println("读入文件失败！请检查文件路径！");
            return null;
        }
    }

    private static HashMap<Character, Double> calculateCharChance(String fileContent) {
        if(fileContent == null) {
            return null;
        }
        HashMap<Character, Double> charTimes = new HashMap<>();
        for(int i = 0; i < fileContent.length(); i ++) {
            char tempChar = fileContent.charAt(i);
            if(charTimes.containsKey(tempChar)) {
                charTimes.put(tempChar, charTimes.get(tempChar) + 1d);
            } else {
                charTimes.put(tempChar, 1d);
            }
        }
        HashMap<Character, Double> perChance = new HashMap<>();
        for(Map.Entry<Character, Double> entry : charTimes.entrySet()) {
            perChance.put(entry.getKey(), entry.getValue() / fileContent.length());
        }
        return perChance;
    }

    private static TreeNode buildHuffmanTree(HashMap<Character, Double> perChance) {
        if(perChance == null) {
            return null;
        }
        ArrayList<TreeNode> treeNodes = new ArrayList<>();
        leafNodes = new HashMap<>();
        for(Map.Entry<Character, Double> entry : perChance.entrySet()) {
            TreeNode tempNode = new TreeNode(entry.getKey(), entry.getValue());
            treeNodes.add(tempNode);
            leafNodes.put(tempNode.getCurrentChar(), tempNode);
        }
        TreeNodeComparator comparator = new TreeNodeComparator();
        while(treeNodes.size() != 1) {
            Collections.sort(treeNodes, comparator);
            TreeNode tempNode1 = treeNodes.remove(0);
            TreeNode tempNode2 = treeNodes.remove(0);
            TreeNode tempTop = new TreeNode(tempNode1.getChance() + tempNode2.getChance());
            tempTop.setLeftNode(tempNode1);
            tempTop.setRightNode(tempNode2);
            tempNode1.setParentNode(tempTop);
            tempNode2.setParentNode(tempTop);
            treeNodes.add(tempTop);
        }
        return treeNodes.get(0);
    }

    private static String encraptContent(String fileContent) {
        if(fileContent == null) {
            return null;
        }
        HashMap<Character, String> codeMap = new HashMap<>();
        for(Map.Entry<Character, TreeNode> entry : leafNodes.entrySet()) {
            TreeNode tempNode = entry.getValue();
            TreeNode tempHeadNode = null;
            String code = "";
            while(tempNode.getParentNode() != null) {
                tempHeadNode = tempNode.getParentNode();
                if(tempNode.equals(tempHeadNode.getLeftNode())) {
                    code += "0";
                } else {
                    code += "1";
                }
                tempNode = tempNode.getParentNode();
            }
            codeMap.put(entry.getValue().getCurrentChar(), new StringBuilder(code).reverse().toString());
        }
        StringBuilder encraptedContent = new StringBuilder();
        for(int i = 0; i < fileContent.length(); i ++) {
            encraptedContent.append(codeMap.get(fileContent.charAt(i)));
        }
        binaryNumber = encraptedContent.length();
        encraptedContent = encraptedContent.insert(0, toFullBinaryString(binaryNumber));
        int moreZero = 8 - encraptedContent.length() % 8;
        for(int i = 0; i < moreZero; i ++) {
            encraptedContent.append("0");
        }
        return encraptedContent.toString();
    }

    public static String toFullBinaryString(int num) {
        char[] chs = new char[Integer.SIZE];
        for (int i = 0; i < Integer.SIZE; i++) {
            chs[Integer.SIZE - 1 - i] = (char) (((num >> i) & 1) + '0');
        }
        return new String(chs);
    }

    private static void writeIntoFile(String encraptedContent) {
        if(encraptedContent == null) {
            return;
        }
        char num1 = "1".charAt(0);
        int byteLength = encraptedContent.length() / 8;
        byte[] bytes = new byte[byteLength];
        for(int i = 0; i < byteLength; i ++) {
            bytes[i] = (byte)0;
        }
        for(int i = 0; i < encraptedContent.length(); i ++) {
            char tempBinary = encraptedContent.charAt(i);
            if(tempBinary == num1) {
                switch(i % 8) {
                    case 0: bytes[i/8] = (byte)((int)bytes[i/8] | 0x80);break;
                    case 1: bytes[i/8] = (byte)((int)bytes[i/8] | 0x40);break;
                    case 2: bytes[i/8] = (byte)((int)bytes[i/8] | 0x20);break;
                    case 3: bytes[i/8] = (byte)((int)bytes[i/8] | 0x10);break;
                    case 4: bytes[i/8] = (byte)((int)bytes[i/8] | 0x8);break;
                    case 5: bytes[i/8] = (byte)((int)bytes[i/8] | 0x4);break;
                    case 6: bytes[i/8] = (byte)((int)bytes[i/8] | 0x2);break;
                    case 7: bytes[i/8] = (byte)((int)bytes[i/8] | 0x1);break;
                }
            }
        }
        File encrapyFile = new File(file.getName().split("\\.")[0] + ".encrapy");
        File keyFile = new File(file.getName().split("\\.")[0] + ".key");
        try{
            FileOutputStream outputStream = new FileOutputStream(encrapyFile);
            outputStream.write(bytes);
            outputStream.flush();
            outputStream.close();
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(keyFile));
            objectOutputStream.writeObject(topNode);
            objectOutputStream.close();
        }catch(Exception e) {
            System.out.println("加密后文件写入失败！");
        }
        System.out.println("\n密码文件为 " + encrapyFile.getName() + "\nKey文件为 " + keyFile.getName());
        System.out.println("压缩前文件大小 " + file.length() + " Byte\n压缩后文件大小 " + encrapyFile.length() + " Byte");
        System.out.printf("压缩率：%.2f%%\n", (((float)file.length() - (float)encrapyFile.length()) / (float)file.length()) * 100);
    }

    private static void decrapt() {
        String encraptedContent = readEncraptedContent();
        readKeyFile();
        decraptContent(encraptedContent);
    }

    private static String readEncraptedContent() {
        scanner = new Scanner(System.in);
        System.out.print("请输入密码文件路径：");
        String encrapyFileName = scanner.nextLine();
        byte[] bytes = null;
        try{
            InputStream inputStream = new FileInputStream(encrapyFileName);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024 * 4];
            int n = 0;
            while((n = inputStream.read(buffer)) != -1) {
                byteArrayOutputStream.write(buffer, 0, n);
            }
            bytes = byteArrayOutputStream.toByteArray();
            byteArrayOutputStream.close();
            inputStream.close();
        }catch(IOException e) {
            System.out.println("读入密码文件失败！请检查文件路径！");
            return null;
        }
        StringBuilder builder = new StringBuilder();
        for(byte tempByte : bytes) {
            builder.append(Integer.toBinaryString((tempByte & 0xFF) + 0x100).substring(1));
        }
        return builder.toString();
    }

    private static void readKeyFile() {
        scanner = new Scanner(System.in);
        System.out.print("请输入Key文件路径：");
        String keyFileName = scanner.nextLine();
        try{
            ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(keyFileName));
            topNode = (TreeNode)objectInputStream.readObject();
            objectInputStream.close();
        }catch(Exception e) {
            System.out.println("读入Key文件失败！请检查文件路径！");
            topNode = null;
            return;
        }
    }

    private static void decraptContent(String encraptedContent) {
        if(encraptedContent == null) {
            return;
        }
        StringBuilder resultBuilder = new StringBuilder();
        binaryNumber = binaryToTen(encraptedContent.substring(0, 32));
        encraptedContent = encraptedContent.substring(32);
        TreeNode tempNode = topNode;
        for(int i = 0; i < binaryNumber; i ++) {
            if(encraptedContent.charAt(i) == '0') {
                tempNode = tempNode.getLeftNode();
            } else {
                tempNode = tempNode.getRightNode();
            }
            if(tempNode.getCurrentChar() != null) {
                resultBuilder.append(tempNode.getCurrentChar());
                tempNode = topNode;
            }
        }
        System.out.print("解密完成，是否输出（y or n）？");
        String choice = scanner.next();
        if("y".equals(choice)) {
            System.out.println("\n\n" + resultBuilder.toString());
        }
        System.out.print("是否写入文件（y or n）？");
        choice = scanner.next();
        if("y".equals(choice)) {
            System.out.print("请输入待写入文件路径：");
            scanner = new Scanner(System.in);
            String resultFileName = scanner.nextLine();
            try{
                BufferedWriter writer = new BufferedWriter(new FileWriter(resultFileName));
                writer.write(resultBuilder.toString());
                writer.close();
            }catch(Exception e) {
                System.out.println("写入结果文件失败！请检查文件路径！");
            }
        }
    }

    private static int binaryToTen(String binary) {
        int res = 0;
        for(int i = 0; i < 32; i ++) {
            if(binary.charAt(i) == '1') {
                res += Math.pow(2, 31 - i);
            }
        }
        return res;
    }
}

class TreeNodeComparator implements Comparator<TreeNode> {
    @Override
    public int compare(TreeNode node1, TreeNode node2) {
        return node1.getChance() > node2.getChance() ? 1 : -1;
    }
}

class TreeNode implements Serializable{
    private static final long serialVersionUID = 1L;

    private Character currentChar = null;
    private Double chance;
    private TreeNode parentNode;
    private TreeNode leftNode;
    private TreeNode rightNode;

    public TreeNode(char currentChar, double chance) {
        this.currentChar = currentChar;
        this.chance = chance;
    }

    public TreeNode(double chance) {
        this.chance = chance;
    }

    public Character getCurrentChar() {
        return currentChar;
    }

    public Double getChance() {
        return chance;
    }

    public void setParentNode(TreeNode parentNode) {
        this.parentNode = parentNode;
    }

    public TreeNode getParentNode() {
        return parentNode;
    }

    public void setLeftNode(TreeNode leftNode) {
        this.leftNode = leftNode;
    }

    public TreeNode getLeftNode() {
        return leftNode;
    }

    public void setRightNode(TreeNode rightNode) {
        this.rightNode = rightNode;
    }

    public TreeNode getRightNode() {
        return rightNode;
    }
}