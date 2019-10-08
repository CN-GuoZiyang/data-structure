import java.io.BufferedReader;
import java.io.IOException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class Main {
    private static TreeNode redBlackTree;
    private static Scanner scanner = new Scanner(System.in);
    private static ArrayList<Integer> allElements = new ArrayList<>();
    private static int success = 0;
    private static int tempTimes = 0;
    private static int nullNode = 0;
    private static int failure = 0;

    public static void main(String[] args) {
        buildTree();
        while(true) {
            menu();
        }
    }

    private static void menu() {
    	System.out.println();
        System.out.println("1. 插入节点");
        System.out.println("2. 查找节点");
        System.out.println("3. 删除节点");
        System.out.println("4. 遍历节点");
        System.out.println("5. 性能分析");
        System.out.println("6. 退出");
        System.out.print("请输入选择：");
        scanner = new Scanner(System.in);
        int choice = scanner.nextInt();
        System.out.println();
        switch(choice) {
            case 1:
            	System.out.print("请输入要插入的数值：");
            	int value = scanner.nextInt();
            	if(searchTree(redBlackTree, value) != null) {
            		System.out.println("该记录已存在，无法插入！");
            		return;
            	}
            	insertNode(value);
            	System.out.println("插入数据成功！");
                break;
            case 2:
            	System.out.print("请输入你要查找的节点：");
            	int target = scanner.nextInt();
            	if(searchTree(redBlackTree, target) != null) {
            		System.out.println("已找到该节点！");
            	} else {
            		System.out.println("未找到该节点！");
            	}
                break;
            case 3:
            	System.out.print("请输入你要删除的节点：");
            	int deletion = scanner.nextInt();
            	TreeNode node = searchTree(redBlackTree, deletion);
            	if(node != null) {
            		deleteNode(node);
            		System.out.println("删除节点成功！");
            	} else {
            		System.out.println("待删除节点不存在！");
            	}
                break;
            case 4:
            	allElements.clear();
            	sortBST(redBlackTree);
            	System.out.println("该红黑树共有" + allElements.size() + "个元素，中序遍历如下：");
            	System.out.println(allElements.toString());
                break;
            case 5:
            	analysis();
                break;
            case 6:
                System.exit(0);
            case 7:
            	debugMode();
            	break;
            default:
                System.out.println("选择有误，请重新选择！");
                break;
        }
    }

    private static void buildTree() {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader("order.txt"));
            redBlackTree = new TreeNode(null, Integer.parseInt(reader.readLine()));
            redBlackTree.setRed(false);
            redBlackTree.setLeftChild(new TreeNode(null, null, false));
            redBlackTree.setRightChild(new TreeNode(null, null, false));
            String line = null;
            while((line = reader.readLine()) != null) {
                insertNode(Integer.parseInt(line));
            }
            System.out.println("红黑树建立完成！");
        }catch(Exception e) {
            System.out.println("创建红黑树时有错误发生！");
            e.printStackTrace();
        }finally {
            if(reader != null) {
                try {
                    reader.close();
                }catch(IOException e) {
                    System.out.println("关闭流时有错误发生！");
                    e.printStackTrace();
                }
            }
        }
    }

    private static void insertNode(int record) {
        TreeNode targetNode = insertBST(redBlackTree ,record);
        balanceTree(targetNode);
    }

    private static TreeNode insertBST(TreeNode node, int record) {
        if(record < node.getRecord().intValue()) {
            if(node.getLeftChild().getRecord() == null) {
                node.getLeftChild().setRecord(record);
                node.getLeftChild().setParent(node);
                node.getLeftChild().setRed(true);
                node.getLeftChild().setLeftChild(new TreeNode(null, null, false));
                node.getLeftChild().setRightChild(new TreeNode(null, null, false));
                return node.getLeftChild();
            } else {
                return insertBST(node.getLeftChild(), record);
            }
        } else {
            if(node.getRightChild().getRecord() == null) {
                node.getRightChild().setRecord(record);
                node.getRightChild().setParent(node);
                node.getRightChild().setRed(true);
                node.getRightChild().setLeftChild(new TreeNode(null, null, false));
                node.getRightChild().setRightChild(new TreeNode(null, null, false));
                return node.getRightChild();
            } else {
                return insertBST(node.getRightChild(), record);
            }
        }
    }

    private static void balanceTree(TreeNode targetNode) {
    	while(targetNode.getParent() != null && targetNode.getParent().isRed()) {
    		TreeNode parentNode = targetNode.getParent();
            TreeNode ancestorNode = parentNode.getParent();
            TreeNode uncleNode = null;
            if(parentNode.getRecord().intValue() > ancestorNode.getRecord().intValue()) {
                uncleNode = ancestorNode.getLeftChild();
            } else {
            	uncleNode = ancestorNode.getRightChild();
            }
            if(parentNode == ancestorNode.getLeftChild()) {
            	if(uncleNode.isRed()) {
                	parentNode.setRed(false);
                	uncleNode.setRed(false);
                	ancestorNode.setRed(true);
                	targetNode = ancestorNode;
                } else if(!uncleNode.isRed() && targetNode == parentNode.getRightChild()) {
                	targetNode = parentNode;
                	leftRotate(parentNode);
                } else if(!uncleNode.isRed() && targetNode == parentNode.getLeftChild()) {
                	parentNode.setRed(false);
                	ancestorNode.setRed(true);
                	rightRotate(ancestorNode);
                }
            } else {
            	if(uncleNode.isRed()) {
                	parentNode.setRed(false);
                	uncleNode.setRed(false);
                	ancestorNode.setRed(true);
                	targetNode = ancestorNode;
                } else if(!uncleNode.isRed() && targetNode == parentNode.getLeftChild()) {
                	targetNode = parentNode;
                	rightRotate(parentNode);
                } else if(!uncleNode.isRed() && targetNode == parentNode.getRightChild()) {
                	parentNode.setRed(false);
                	ancestorNode.setRed(true);
                	leftRotate(ancestorNode);
                }
            }
    	}
    	redBlackTree.setRed(false);
    }
    
    private static void leftRotate(TreeNode targetNode) {
    	TreeNode parentNode = targetNode.getParent();
    	TreeNode rightChild = targetNode.getRightChild();
    	TreeNode grandson = rightChild.getLeftChild();
    	if(parentNode == null) {
    		redBlackTree = rightChild;
    		rightChild.setParent(null);
    		rightChild.setLeftChild(targetNode);
    		targetNode.setParent(rightChild);
    		targetNode.setRightChild(grandson);
    		grandson.setParent(targetNode);
    	} else if(targetNode.getRecord().intValue() > parentNode.getRecord().intValue()) {
    		parentNode.setRightChild(rightChild);
    		rightChild.setParent(parentNode);
    		rightChild.setLeftChild(targetNode);
    		targetNode.setParent(rightChild);
    		targetNode.setRightChild(grandson);
    		grandson.setParent(targetNode);
    	} else {
    		parentNode.setLeftChild(rightChild);
    		rightChild.setParent(parentNode);
    		rightChild.setLeftChild(targetNode);
    		targetNode.setParent(rightChild);
    		targetNode.setRightChild(grandson);
    		grandson.setParent(targetNode);
    	}
    }
    
    private static void rightRotate(TreeNode targetNode) {
    	TreeNode parentNode = targetNode.getParent();
    	TreeNode leftChild = targetNode.getLeftChild();
    	TreeNode grandson = leftChild.getRightChild();
    	if(parentNode == null) {
    		redBlackTree = leftChild;
    		leftChild.setParent(null);
    		leftChild.setRightChild(targetNode);
    		targetNode.setParent(leftChild);
    		targetNode.setLeftChild(grandson);
    		grandson.setParent(targetNode);
    	} else if(targetNode.getRecord().intValue() > parentNode.getRecord().intValue()) {
    		parentNode.setRightChild(leftChild);
    		leftChild.setParent(parentNode);
    		leftChild.setRightChild(targetNode);
    		targetNode.setParent(leftChild);
    		targetNode.setLeftChild(grandson);
    		grandson.setParent(targetNode);
    	} else {
    		parentNode.setLeftChild(leftChild);
    		leftChild.setParent(parentNode);
    		leftChild.setRightChild(targetNode);
    		targetNode.setParent(leftChild);
    		targetNode.setLeftChild(grandson);
    		grandson.setParent(targetNode);
    	}
    }

    private static TreeNode searchTree(TreeNode node, int record) {
    	if(node.getRecord() == null) {
    		return null;
    	} else if(record == node.getRecord().intValue()) {
    		return node;
    	}
    	if(record < node.getRecord().intValue()) {
    		return searchTree(node.getLeftChild(), record);
    	} else {
    		return searchTree(node.getRightChild(), record);
    	}
    }
    
    private static void deleteNode(TreeNode targetNode) {
    	TreeNode tempNode = null;
    	if(targetNode.getLeftChild().getRecord() == null || targetNode.getRightChild().getRecord() == null) {
    		tempNode = targetNode;
    	} else {
    		tempNode = targetNode.getRightChild();
    		while(tempNode.getLeftChild().getRecord() != null) {
    			tempNode = tempNode.getLeftChild();
    		}
    	}
    	TreeNode tempNodex = null;
    	if(tempNode.getLeftChild().getRecord() != null) {
    		tempNodex = tempNode.getLeftChild();
    	} else {
    		tempNodex = tempNode.getRightChild();
    	}
    	tempNodex.setParent(tempNode.getParent());
    	if(tempNode.getParent() == null) {
    		redBlackTree = tempNode;
    		redBlackTree.setRed(false);
    	} else if(tempNode == tempNode.getParent().getLeftChild()) {
    		tempNode.getParent().setLeftChild(tempNodex);
    	} else {
    		tempNode.getParent().setRightChild(tempNodex);
    	}
    	if(tempNode != targetNode) {
    		targetNode.setRecord(tempNode.getRecord());
    	}
    	if(!tempNode.isRed()) {
    		balanceAfterDelete(tempNodex);
    	}
    }
    
    private static void balanceAfterDelete(TreeNode targetNode) {
    	while((targetNode == null || !targetNode.isRed()) && redBlackTree != targetNode) {
    		if(targetNode == targetNode.getParent().getLeftChild()) {
    			TreeNode tempNode = targetNode.getParent().getRightChild();
    			if(tempNode.isRed()) {
    				tempNode.setRed(false);
    				targetNode.getParent().setRed(true);
    				leftRotate(targetNode.getParent());
    				tempNode = targetNode.getRightChild();
    			} else {
					if((tempNode.getLeftChild() == null || !tempNode.getLeftChild().isRed()) && (tempNode.getRightChild() == null || !tempNode.getRightChild().isRed())) {
        				tempNode.setRed(true);
        				targetNode = targetNode.getParent();
        			} else if(tempNode.getRightChild() == null || !tempNode.getRightChild().isRed()) {
        				tempNode.getLeftChild().setRed(false);
        				tempNode.setRed(true);
        				rightRotate(tempNode);
        				tempNode = targetNode.getParent().getRightChild();
        			} else {
        				tempNode.setRed(targetNode.getParent().isRed());
        				targetNode.getParent().setRed(false);
        				tempNode.getRightChild().setRed(false);
        				leftRotate(targetNode.getParent());
        				targetNode = redBlackTree;
        				break;
        			}
    			}
    		} else {
				TreeNode tempNode = targetNode.getParent().getLeftChild();
    			if(tempNode.isRed()) {
    				tempNode.setRed(false);
    				targetNode.getParent().setRed(true);
    				rightRotate(targetNode.getParent());
    				tempNode = targetNode.getLeftChild();
    			} else {
					if((tempNode.getRightChild() == null || !tempNode.getRightChild().isRed()) && (tempNode.getLeftChild() == null || !tempNode.getLeftChild().isRed())) {
        				tempNode.setRed(true);
        				targetNode = targetNode.getParent();
        			} else if(tempNode.getLeftChild() == null || !tempNode.getLeftChild().isRed()) {
        				tempNode.getRightChild().setRed(false);
        				tempNode.setRed(true);
        				leftRotate(tempNode);
        				tempNode = targetNode.getParent().getLeftChild();
        			} else {
        				tempNode.setRed(targetNode.getParent().isRed());
        				targetNode.getParent().setRed(false);
        				tempNode.getLeftChild().setRed(false);
        				rightRotate(targetNode.getParent());
        				targetNode = redBlackTree;
        				break;
        			}
    			}
    		}
    	}
    	targetNode.setRed(false);
    }
    
    private static void sortBST(TreeNode node) {
    	if(node.getRecord() != null) {
    		sortBST(node.getLeftChild());
    		allElements.add(node.getRecord());
    		sortBST(node.getRightChild());
    	}
    }
    
    private static void analysis() {
    	success = 0;
    	allElements.clear();
    	sortBST(redBlackTree);
    	for(Integer ele : allElements) {
    		searchSuccess(redBlackTree, ele);
    	}
    	System.out.printf("查找成功的平均查找长度为%f\n", (success * 1.0) / allElements.size());
    	nullNode = 0;
    	failure = 0;
    	for(Integer ele : allElements) {
    		tempTimes = 0;
    		searchFailure(redBlackTree, ele);
    	}
    	System.out.printf("查找失败的平均查找长度为%f\n", (failure * 1.0) / nullNode);
    }
    
    private static TreeNode searchSuccess(TreeNode node, int record) {
    	if(node.getRecord() == null) {
    		return null;
    	} else if(record == node.getRecord().intValue()) {
    		return node;
    	}
    	if(record < node.getRecord().intValue()) {
    		success ++;
    		return searchSuccess(node.getLeftChild(), record);
    	} else {
    		success ++;
    		return searchSuccess(node.getRightChild(), record);
    	}
    }
    
    private static TreeNode searchFailure(TreeNode node, int record) {
    	if(record == node.getRecord().intValue()) {
    		tempTimes ++;
    		if(node.getLeftChild().getRecord() == null && node.getRightChild().getRecord() == null) {
    			nullNode = nullNode + 2;
    			failure += 2 * tempTimes;
    		} else if(node.getLeftChild().getRecord() == null || node.getRightChild().getRecord() == null){
    			nullNode ++;
    			failure += tempTimes;
    		}
    		return node;
    	}
    	if(record < node.getRecord().intValue()) {
    		tempTimes ++;
    		return searchFailure(node.getLeftChild(), record);
    	} else {
    		tempTimes ++;
    		return searchFailure(node.getRightChild(), record);
    	}
    }
    
    private static void debugMode() {
    	System.out.println("该模式下，将进行100次插入，100次删除，100次查找操作。（不可恢复！）");
    	Random random = new Random();
    	for(int i = 0; i < 100; i ++) {
    		int element = random.nextInt(4096);
    		if(searchTree(redBlackTree, element) != null) {
    			insertNode(element);
    		}
    	}
    	for(int i = 0; i < 100; i ++) {
    		int element = random.nextInt(4096);
    		TreeNode node = null;
    		if((node = searchTree(redBlackTree, element)) != null) {
    			deleteNode(node);
    		}
    	}
    	for(int i = 0; i < 100; i ++) {
    		searchTree(redBlackTree, random.nextInt(4096));
    	}
    	System.out.println("测试完成！");
    }
}

class TreeNode {
    private TreeNode parent;
    private TreeNode leftChild;
    private Integer record;
    private boolean isRed;
    private TreeNode rightChild;

    public TreeNode(TreeNode parent, Integer record) {
        this.parent = parent;
        this.record = record;
        this.leftChild = null;
        this.isRed = true;
        this.rightChild = null;
    }

    public TreeNode(TreeNode parent, Integer record, boolean isRed) {
        this.parent = parent;
        this.record = record;
        this.leftChild = null;
        this.isRed = isRed;
        this.rightChild = null;
    }

    public TreeNode getParent() {
        return parent;
    }

    public TreeNode getRightChild() {
        return rightChild;
    }

    public void setRightChild(TreeNode rightChild) {
        this.rightChild = rightChild;
    }

    public boolean isRed() {
        return isRed;
    }

    public void setRed(boolean isRed) {
        this.isRed = isRed;
    }

    public Integer getRecord() {
        return record;
    }

    public void setRecord(Integer record) {
        this.record = record;
    }

    public TreeNode getLeftChild() {
        return leftChild;
    }

    public void setLeftChild(TreeNode leftChild) {
        this.leftChild = leftChild;
    }

    public void setParent(TreeNode parent) {
        this.parent = parent;
    }
}
