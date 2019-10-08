import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.File;

public class Log {
    private static BufferedWriter bufferedWriter = null;
    private static File logFile = new File("log.txt");

    public static void log(String character, String stackState, Boolean logOn) {
        if(logOn == false) {
            return;
        }
        if(bufferedWriter == null) {
            try{
                bufferedWriter = new BufferedWriter(new FileWriter(logFile));
            } catch(Exception e) {
                System.out.println("建立输出字符流失败！");
            }
        }
        String line = "当前读取字符(串)：" + character + "\n栈内状态：" + stackState + "\n";
        try{
            bufferedWriter.write(line);
            bufferedWriter.flush();
        } catch(Exception e) {
            System.out.println("写入日志失败！日志内容：" + line);
        }
    }

    public static void log(String string, Boolean logOn) {
        if(logOn == false) {
            return;
        }
        if(bufferedWriter == null) {
            try{
                bufferedWriter = new BufferedWriter(new FileWriter(logFile));
            } catch(Exception e) {
                System.out.println("建立输出字符流失败！");
            }
        }
        try{
            bufferedWriter.write(string);
            bufferedWriter.flush();
        } catch(Exception e) {
            System.out.println("写入日志失败！日志内容：" + string);
        }
    }

    public static void closeStream() {
        if(bufferedWriter != null) {
            try{
                bufferedWriter.close();
            }catch(Exception e) {
                System.out.println("关闭输出流失败！");
            }
        }
    }
}