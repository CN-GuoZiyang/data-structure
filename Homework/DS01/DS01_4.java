import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.io.File;

public class DS01_4 {
    private static int schoolNumber;
    private static int projectNumber;
    private static int manProjectNumber;
    private static int womanProjectNumber;
    private static ArrayList<School> schoolList;
    private static ArrayList<Project> projectList;

    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.print("是否从文件读取(y or n)？");
        if("y".equals(scanner.next())) {
            readFromFile();
        } else {
            initialList();
        }
        while(true){
            menu();
        }
    }
    //初始化所有的List
    private static void initialList() {
        System.out.print("请输入学院数量：");
        schoolNumber = scanner.nextInt();
        System.out.print("请输入男子项目数量：");
        manProjectNumber = scanner.nextInt();
        System.out.print("请输入女子项目数量：");
        womanProjectNumber = scanner.nextInt();
        projectNumber = manProjectNumber + womanProjectNumber;

        schoolList = new ArrayList<School>(schoolNumber);
        for(int i = 0; i < schoolNumber; i ++) {
            schoolList.add(new School(i));
        }
        projectList = new ArrayList<Project>(projectNumber);
        for(int i = 0; i < projectNumber; i ++) {
            Project project = new Project(i, i < manProjectNumber?true:false, i % 2 == 0?true:false);
            projectList.add(project);
        }
    }
    //输出菜单栏并根据选择调用相应的方法
    private static void menu() {
        System.out.println();
        System.out.println();
        System.out.println("**********************************");
        System.out.println("* 1.输入比赛成绩                 *");
        System.out.println("* 2.学院总分统计                 *");
        System.out.println("* 3.排序输出成绩                 *");
        System.out.println("* 4.按照序号查询                 *");
        System.out.println("* 5.退出统计程序                 *");
        System.out.println("**********************************");
        System.out.print("请输出你的选择：");
        int choice = scanner.nextInt();
        System.out.println();
        System.out.println();
        switch (choice) {
            case 1:
                inputScore();
                break;
            case 2:
                schoolScoreInfo();
                break;
            case 3:
                outputScore();
                break;
            case 4:
                searchInNumber();
                break;
            case 5:
                System.out.print("确认退出吗（y or n）？");
                if("y".equals(scanner.next())) {
                    System.out.print("是否保存到文件(y or n)？");
                    if("y".equals(scanner.next())){
                        writeToFile();
                    }
                    System.exit(0);
                } else {
                    break;
                }
            default:
                System.out.println("输入错误，请重新输入！");
                break;
        }
    }
    //按照项目读入前三名或前五名
    private static void inputScore() {
        System.out.print("请输入项目编号：");
        int projectLabel = 0;
        while(true){
            projectLabel = scanner.nextInt();
            if(projectLabel >= projectNumber) {
                System.out.println("输入有误（无此项目），请重新输入。");
            } else {
                break;
            }
        }
        Project targetProject = projectList.get(projectLabel);
        System.out.println("该项目是" + (targetProject.getIsMaleProject()?"男子":"女子") + "项目");
        System.out.println("请输入" + (targetProject.getIsThreeProject()?"前三名":"前五名") + "的学院代号");
        for (int i = 0; i < (targetProject.getIsThreeProject()?3:5); i ++) {
            int schoolNum = -1;
            System.out.print("第" + (i+1) + "名：");
            while(true) {
                schoolNum = scanner.nextInt();
                if(schoolNum >= schoolNumber) {
                    System.out.println("输入数据有误（无此院系代号），请重新输入此条字段。");
                    continue;
                }else{
                    break;
                }
            }
            School school = schoolList.get(schoolNum);
            if(targetProject.getIsMaleProject()) {
                if(targetProject.getIsThreeProject()) {
                    switch(i){
                        case 0: school.addMaleScore(5); school.addTotalScore(5); break;
                        case 1: school.addMaleScore(3); school.addTotalScore(3); break;
                        case 2: school.addMaleScore(2); school.addTotalScore(2); break;
                    }
                } else {
                    switch(i){
                        case 0: school.addMaleScore(7); school.addTotalScore(7); break;
                        case 1: school.addMaleScore(5); school.addTotalScore(5); break;
                        case 2: school.addMaleScore(3); school.addTotalScore(3); break;
                        case 3: school.addMaleScore(2); school.addTotalScore(2); break;
                        case 4: school.addMaleScore(1); school.addTotalScore(1); break;
                    }
                }
            } else {
                if(targetProject.getIsThreeProject()) {
                    switch(i){
                        case 0: school.addFemaleScore(5); school.addTotalScore(5); break;
                        case 1: school.addFemaleScore(3); school.addTotalScore(3); break;
                        case 2: school.addFemaleScore(2); school.addTotalScore(2); break;
                    }
                } else {
                    switch(i){
                        case 0: school.addFemaleScore(7); school.addTotalScore(7); break;
                        case 1: school.addFemaleScore(5); school.addTotalScore(5); break;
                        case 2: school.addFemaleScore(3); school.addTotalScore(3); break;
                        case 3: school.addFemaleScore(2); school.addTotalScore(2); break;
                        case 4: school.addFemaleScore(1); school.addTotalScore(1); break;
                    }
                }
            }
            targetProject.addWinnerSchools(school);
        }
    }
    //输出学院总分
    private static void schoolScoreInfo() {
        ComparatorById comparatorById = new ComparatorById();
        Collections.sort(schoolList, comparatorById);
        System.out.printf("%8s\t%5s\n", "学院代号", "学院总分");
        for(School school : schoolList){
            System.out.printf("%8d\t%5d\t\n", school.getSchoolLabel(), school.getTotalScore());
        }
    }
    //输出排序菜单
    private static void outputScore() {
        System.out.println("1. 学院编号");
        System.out.println("2. 学院总分");
        System.out.println("3. 男子总分");
        System.out.println("4. 女子总分");
        System.out.print("请选择排序依据：");
        int sortChoice = scanner.nextInt();
        switch(sortChoice){
            case 1:
                schoolScoreInfo();
                break;
            case 2:
                ComparatorByTotal comparatorByTotal = new ComparatorByTotal();
                Collections.sort(schoolList, comparatorByTotal);
                System.out.printf("%8s\t%5s\n", "学院代号", "学院总分");
                for(School school : schoolList){
                    System.out.printf("%8d\t%5d\t\n", school.getSchoolLabel(), school.getTotalScore());
                }
                break;
            case 3:
                ComparatorByMale comparatorByMale = new ComparatorByMale();
                Collections.sort(schoolList, comparatorByMale);
                System.out.printf("%8s\t%5s\n", "学院代号", "男子总分");
                for(School school : schoolList){
                    System.out.printf("%8d\t%5d\t\n", school.getSchoolLabel(), school.getMaleScore());
                }
                break;
            case 4:
                ComparatorByMale comparatorByFemale = new ComparatorByMale();
                Collections.sort(schoolList, comparatorByFemale);
                System.out.printf("%8s\t%5s\n", "学院代号", "女子总分");
                for(School school : schoolList){
                    System.out.printf("%8d\t%5d\t\n", school.getSchoolLabel(), school.getFemaleScore());
                }
                break;
            default:
                System.out.println("输入选择有误！");
                break;
        }
    }
    //按照编号查询的菜单
    private static void searchInNumber() {
        System.out.println("1. 按照学院编号查询");
        System.out.println("2. 按照项目编号查询");
        int searchChoice = scanner.nextInt();
        switch(searchChoice) {
            case 1:
                searchBySchool();
                break;
            case 2:
                searchByProject();
                break;
            default:
                System.out.println("输入选择有误！");
        }
    }
    //按照学院编号查询
    private static void searchBySchool() {
        System.out.print("请输入待查询的学院编号：");
        int schoolNum = -1;
        while(true) {
            schoolNum = scanner.nextInt();
            if(schoolNum >= schoolNumber) {
                System.out.println("输入数据有误（无此院系代号），请重新输入此条字段。");
                continue;
            }else{
                break;
            }
        }
        School school = null;
        for(School tempSchool : schoolList) {
            if(tempSchool.getSchoolLabel() == schoolNum) {
                school = tempSchool;
            }
        }
        Map<Integer, Integer> winnerInfoMap = new HashMap<>();
        for(Project project : projectList) {
            if(project.getWinnerSchools().contains(school)) {
                winnerInfoMap.put(project.getProjectLabel(), project.getWinnerSchools().indexOf(school));
            }
        }
        System.out.printf("%8s\t%5s\n", "项目代号", "位次");
        for(Map.Entry<Integer, Integer> entry : winnerInfoMap.entrySet()) {
            System.out.printf("%8d\t%5d\t\n", entry.getKey(), entry.getValue() + 1);
        }
    }
    //按照项目编号查询
    private static void searchByProject() {
        System.out.print("请输入待查询的项目编号：");
        int projectNum = -1;
        while(true) {
            projectNum = scanner.nextInt();
            if(projectNum >= projectNumber) {
                System.out.println("输入数据有误（无此项目代号），请重新输入此条字段。");
                continue;
            }else{
                break;
            }
        }
        Project project = projectList.get(projectNum);
        System.out.printf("%8s\t%5s\n", "项目代号", "学院代号");
        System.out.printf("%8s\t", projectNum);
        for (School school : project.getWinnerSchools()) {
            System.out.printf(school.getSchoolLabel() + " ");
        }
        System.out.println();
    }
    //序列化将对象存入文件
    private static void writeToFile() {
        try{
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(new File("schoolList")));
            objectOutputStream.writeObject(schoolList);
            objectOutputStream.close();
            objectOutputStream = new ObjectOutputStream(new FileOutputStream(new File("projectList")));
            objectOutputStream.writeObject(projectList);
            objectOutputStream.close();
        }catch(Exception exception) {
            System.out.println("写入文件失败！");
        }
    }
    //反序列化将对象从文件读出
    @SuppressWarnings("unchecked")
    private static void readFromFile() {
        try{
            ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(new File("schoolList")));
            schoolList = (ArrayList<School>)objectInputStream.readObject();
            objectInputStream.close();
            objectInputStream = new ObjectInputStream(new FileInputStream(new File("projectList")));
            projectList = (ArrayList<Project>)objectInputStream.readObject();
            objectInputStream.close();
            schoolNumber = schoolList.size();
            projectNumber = projectList.size();
            for(Project project : projectList) {
                if(!project.getIsMaleProject()) {
                    manProjectNumber = project.getProjectLabel();
                    break;
                }
            }
            womanProjectNumber = projectNumber - manProjectNumber;
        }catch(Exception exception) {
            System.out.println("读入文件失败！开始手工输入。");
            initialList();
        }
    }
}
//按照学院编号的排序器
class ComparatorById implements Comparator<School>{
    @Override
    public int compare(School s1, School s2) {
        if(s1.getSchoolLabel() > s2.getSchoolLabel()) {
            return 1;
        } else {
            return -1;
        }
    }
}
//按照总分的排序器
class ComparatorByTotal implements Comparator<School>{
    @Override
    public int compare(School s1, School s2) {
        if(s1.getTotalScore() < s2.getTotalScore()) {
            return 1;
        } else {
            return -1;
        }
    }
}
//按照男子总分的排序器
class ComparatorByMale implements Comparator<School>{
    @Override
    public int compare(School s1, School s2) {
        if(s1.getMaleScore() < s2.getMaleScore()) {
            return 1;
        } else {
            return -1;
        }
    }
}
//按照女子总分的排序器
class ComparatorByFemale implements Comparator<School>{
    @Override
    public int compare(School s1, School s2) {
        if(s1.getFemaleScore() < s2.getFemaleScore()) {
            return 1;
        } else {
            return -1;
        }
    }
}
//学院类
class School implements Serializable{
    private static final long serialVersionUID = 1;

    private int schoolLabel;
    private int totalScore;
    private int maleScore;
    private int femaleScore;

    public School(int schoolLabel) {
        this.schoolLabel = schoolLabel;
        this.totalScore = 0;
        this.maleScore = 0;
        this.femaleScore = 0;
    }

    public int getSchoolLabel() {
        return schoolLabel;
    }

    public int getFemaleScore() {
        return femaleScore;
    }

    public void addFemaleScore(int femaleScore) {
        this.femaleScore += femaleScore;
    }

    public int getMaleScore() {
        return maleScore;
    }

    public void addMaleScore(int maleScore) {
        this.maleScore += maleScore;
    }

    public int getTotalScore() {
        return totalScore;
    }

    public void addTotalScore(int totalScore) {
        this.totalScore += totalScore;
    }
}
//项目类
class Project implements Serializable{
    private static final long serialVersionUID = 1;

    private int projectLabel;
    private boolean isMaleProject;
    private boolean isThreeProject;
    private ArrayList<School> winnerSchools;

    public Project(int projectLabel, boolean isMaleProject, boolean isThreeProject) {
        this.projectLabel = projectLabel;
        this.isMaleProject = isMaleProject;
        this.isThreeProject = isThreeProject;
        this.winnerSchools = new ArrayList<School>();
    }

    public void addWinnerSchools(School school) {
        winnerSchools.add(school);
    }

    public void clearWinnerSchools() {
        winnerSchools.clear();
    }

    public int getProjectLabel() {
        return projectLabel;
    }

    public boolean getIsMaleProject() {
        return isMaleProject;
    }

    public boolean getIsThreeProject() {
        return isThreeProject;
    }

    public ArrayList<School> getWinnerSchools() {
        return winnerSchools;
    }
}