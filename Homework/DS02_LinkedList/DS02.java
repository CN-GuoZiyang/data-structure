import java.util.ArrayList;
import java.util.Scanner;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

public class DS02 {
    private static Scanner scanner = new Scanner(System.in);
    private static ProductNode headNode = null;

    public static void main(String[] args) {
        System.out.println("是否要从文件中读入链表数据(y or n)？");
        if("y".equals(scanner.next())) {
            readFromFile();
        } else {
            headNode = new ProductNode();
        }
        while(true) {
            menu();
        }
    }

    private static void readFromFile() {
        try{
            ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(new File("headnode")));
            headNode = (ProductNode)objectInputStream.readObject();
            objectInputStream.close();
        }catch(Exception e) {
            System.out.println("读入数据失败！已创建空的头节点");
            headNode = new ProductNode();
        }
    }

    private static void menu() {
        scanner = new Scanner(System.in);
        System.out.println();
        System.out.println();
        System.out.println("***********************");
        System.out.println("*1. 进货              *");
        System.out.println("*2. 提货              *");
        System.out.println("*3. 查询              *");
        System.out.println("*4. 更新              *");
        System.out.println("*5. 结束              *");
        System.out.println("***********************");
        System.out.print("请输入选择：");
        int choice = 0;
        try{
            choice = scanner.nextInt();
        }catch(Exception e) {
            System.out.println("输入有误，请重新输入！");
            return;
        }
        System.out.println();
        System.out.println();
        switch(choice) {
            case 1:
                addProduct();
                break;
            case 2:
                reduceProduct();
                break;
            case 3:
                searchProduct();
                break;
            case 4:
                updateProduct();
                break;
            case 5:
                exitSystem();
                break;
            default:
                System.out.println("输入有误，请重新输入！");
                break;
        }
    }

    private static void addProduct() {
        scanner = new Scanner(System.in);
        System.out.print("请输入进货的货物名称：");
        String name = scanner.nextLine();
        System.out.print("请输入进货的货物品牌：");
        String brand = scanner.nextLine();
        System.out.print("请输入进货的货物单价：");
        float price = scanner.nextFloat();
        System.out.print("请输入进货的货物数量：");
        int amount = scanner.nextInt();
        if(headNode.getNextNode() == null) {
            headNode.setNextNode(new ProductNode(name, brand, price, amount));
        } else {
            ProductNode tempNode = headNode;
            ProductNode targetNode = null;
            while(tempNode.getNextNode() != null) {
                tempNode = tempNode.getNextNode();
                if(name.equals(tempNode.getName())) {
                    if(brand.equals(tempNode.getBrand())) {
                        if(price == tempNode.getPrice()) {
                            targetNode = tempNode;
                            break;
                        } else {
                            System.out.println("当前货物品牌和名称已存在，单价不符，请修改单价后再添加！");
                            return;
                        }
                    } else {
                        continue;
                    }
                } else {
                    continue;
                }
            }
            if(targetNode != null) {
                targetNode.setAmount(targetNode.getAmount() + amount);
            } else {
                ProductNode previousNode = headNode;
                tempNode = headNode.getNextNode();
                while(previousNode.getNextNode() != null) {
                    if(tempNode.getPrice() >= price) {
                        previousNode.setNextNode(new ProductNode(name, brand, price, amount));
                        previousNode =  previousNode.getNextNode();
                        previousNode.setNextNode(tempNode);
                        return;
                    } else {
                        previousNode = previousNode.getNextNode();
                        tempNode = tempNode.getNextNode();
                        continue;
                    }
                }
                previousNode.setNextNode(new ProductNode(name, brand, price, amount));
            }
        }

    }

    private static void reduceProduct() {
        if(headNode.getNextNode() == null) {
            System.out.println("当前无任何货物！");
            return;
        }
        scanner = new Scanner(System.in);
        System.out.print("请输入提货的货物名称：");
        String name = scanner.nextLine();
        System.out.print("请输入提货的货物品牌：");
        String brand = scanner.nextLine();
        System.out.print("请输入提货的货物数量：");
        int amount = scanner.nextInt();
        ProductNode currentNode = headNode.getNextNode();
        ProductNode previousNode = headNode;
        ProductNode targetNode = null;
        while(currentNode != null) {
            if(name.equals(currentNode.getName())) {
                if(brand.equals(currentNode.getBrand())) {
                    targetNode = currentNode;
                    break;
                } else {
                    currentNode = currentNode.getNextNode();
                    previousNode = previousNode.getNextNode();
                    continue;
                }
            }else{
                currentNode = currentNode.getNextNode();
                previousNode = previousNode.getNextNode();
                continue;
            }
        }
        if(targetNode == null) {
            System.out.println("查无此货物！");
            return;
        } else {
            if(targetNode.getAmount() > amount) {
                targetNode.setAmount(targetNode.getAmount() - amount);
            } else if(targetNode.getAmount() == amount) {
                currentNode = currentNode.getNextNode();
                previousNode.setNextNode(currentNode);
            } else {
                System.out.println("该货物数量不足，请查询后重新操作！");
            }
        }
    }

    private static void searchProduct() {
        System.out.println("***********************");
        System.out.println("*1. 输出全部           *");
        System.out.println("*2. 按照名称           *");
        System.out.println("*3. 按照品牌           *");
        System.out.println("***********************");
        System.out.print("请输入选择：");
        int choice = scanner.nextInt();
        while(choice != 1 && choice != 2 && choice != 3) {
            System.out.println("选择有误，请重新输入：");
            choice = scanner.nextInt();
        }
        switch(choice) {
            case 1:
                outputAll();
                break;
            case 2:
                searchByName();
                break;
            case 3:
                searchByBrand();
                break;
            default:
                break;
        }
    }

    private static void outputAll() {
        ProductNode tempNode = headNode.getNextNode();
        if(tempNode == null) {
            System.out.println("当前无任何货物信息！");
            return;
        }
        while(tempNode != null) {
            System.out.printf("%8s\t%8s\t%6.2f\t%5d\n", tempNode.getName(), tempNode.getBrand(), tempNode.getPrice(), tempNode.getAmount());
            tempNode = tempNode.getNextNode();
        }
    }

    private static void searchByName() {
        scanner = new Scanner(System.in);
        System.out.print("请输入你要查询的货物名称：");
        String targetName = scanner.nextLine();
        ProductNode tempNode = headNode.getNextNode();
        System.out.println("查询结果：");
        while(tempNode != null) {
            if(tempNode.getName().equals(targetName)) {
                System.out.printf("%8s\t%8s\t%6.2f\t%5d\n", tempNode.getName(), tempNode.getBrand(), tempNode.getPrice(), tempNode.getAmount());
            }
            tempNode = tempNode.getNextNode();
        }
    }

    private static void searchByBrand() {
        scanner = new Scanner(System.in);
        System.out.print("请输入你要查询的货物品牌：");
        String targetBrand = scanner.nextLine();
        ProductNode tempNode = headNode.getNextNode();
        System.out.println("查询结果：");
        while(tempNode != null) {
            if(tempNode.getBrand().equals(targetBrand)) {
                System.out.printf("%8s\t%8s\t%6.2f\t%5d\n", tempNode.getName(), tempNode.getBrand(), tempNode.getPrice(), tempNode.getAmount());
            }
            tempNode = tempNode.getNextNode();
        }
    }

    private static void updateProduct() {
        scanner = new Scanner(System.in);
        System.out.println("请输入要更新的商品的名称：");
        String name = scanner.nextLine();
        System.out.println("请输入要更新的商品的品牌：");
        String brand = scanner.nextLine();
        ProductNode targetNode = null;
        ProductNode tempNode = headNode.getNextNode();
        ProductNode previousNode = headNode;
        while(tempNode != null) {
            if(tempNode.getName().equals(name)) {
                if(tempNode.getBrand().equals(brand)) {
                    targetNode = tempNode;
                    break;
                }
            }
            previousNode = previousNode.getNextNode();
            tempNode = tempNode.getNextNode();
        }
        if(targetNode == null) {
            System.out.println("无此商品，请核对之后在修改！");
            return;
        }
        previousNode.setNextNode(tempNode.getNextNode());       //暂时删除这一节点
        System.out.println("当前查询到的记录信息：");
        System.out.printf("%8s\t%8s\t%6.2f\t%5d\n", targetNode.getName(), targetNode.getBrand(), targetNode.getPrice(), targetNode.getAmount());
        System.out.print("请输入修改的字段名称（名称，品牌，单价，数量）：");
        String choice = scanner.nextLine();
        switch(choice){
            case "名称":
                System.out.print("请输入修改后的内容：");
                targetNode.setName(scanner.nextLine());
                break;
            case "品牌":
                System.out.print("请输入修改后的内容：");
                targetNode.setBrand(scanner.nextLine());
                break;
            case "单价":
                System.out.print("请输入修改后的内容：");
                targetNode.setPrice(scanner.nextFloat());
                break;
            case "数量":
                System.out.print("请输入修改后的内容：");
                targetNode.setAmount(scanner.nextInt());
                break;
            default:
                System.out.println("输入错误，请检查字段名称！");
                break;
        }
        insertProduct(targetNode);
    }

    private static void insertProduct(ProductNode targetNode) {
        float price = targetNode.getPrice();
        if(headNode.getNextNode() == null) {
            headNode.setNextNode(targetNode);
        } else {
            ProductNode previousNode = headNode;
            ProductNode tempNode = headNode.getNextNode();
            while(tempNode != null) {
                if(tempNode.getPrice() >= price) {
                    previousNode.setNextNode(targetNode);
                    targetNode.setNextNode(tempNode);
                    return;
                } else {
                    previousNode = previousNode.getNextNode();
                    tempNode = tempNode.getNextNode();
                    continue;
                }
            }
            previousNode.setNextNode(targetNode);
            targetNode.setNextNode(null);
        }
    }

    private static void exitSystem() {
        saveToFile();
        System.exit(0);
    }

    private static void saveToFile() {
        try{
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(new File("headnode")));
            objectOutputStream.writeObject(headNode);
            objectOutputStream.close();
        }catch(Exception e) {
            System.out.println("写入文件失败！");
            e.printStackTrace();
        }
    }
}

class ProductNode implements Serializable{
    private static final long serialVersionUID = 1L;

    private String name;
    private String brand;
    private float price;
    private int amount;
    private ProductNode nextNode;

    public ProductNode(String name, String brand, float price, int amount) {
        this.name = name;
        this.brand = brand;
        this.price = price;
        this.amount = amount;
    }

    public ProductNode() {

    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getBrand() {
        return brand;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public float getPrice() {
        return price;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getAmount() {
        return amount;
    }

    public void setNextNode(ProductNode nextNode) {
        this.nextNode = nextNode;
    }

    public ProductNode getNextNode() {
        return nextNode;
    }
}