import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.lang.String;


public class Lab1 {
    static Map<String, Integer> graphElements;
    static Map<Integer, String> graphElements2;
    static int[][] outDegreeMatrix;

    public static void main(String[] args) {
        try {
            showDirectedGraph(dealWithText(getTextFromFile("test.txt")));
            Scanner scanner = new Scanner(System.in);
            System.out.println("Please input the first word:");
            String word1 = scanner.nextLine();
            System.out.println("Please input the second word:");
            String word2 = scanner.nextLine();
            List<String> list = queryBridgeWords(word1, word2);
            if (list.isEmpty()) {
                System.out.println("No bridge words from " + "\"" + word1 + "\"" + " to " + "\"" + word2 + "\"" + "!");
            } else {
                System.out.println("The bridge word list from " + "\"" + word1 + "\"" + " to " + "\"" + word2 + "\"" + " is: " + list);
            }
        } catch (Exception e) {
            System.out.println(e.getStackTrace());

        } finally {
            System.out.println("解析结束。");
        }

        return;
    }

    //    生成出度矩阵
    public static void showMatrix(String[] strlist) {
        int count = 0;
        for (int i = 0; i < strlist.length; i++) {
            if (!graphElements.containsKey(strlist[i])) {
//                System.out.print(strlist[i] + "\t");
                graphElements.put(strlist[i], count);
                graphElements2.put(count++, strlist[i]);
            }
        }
        System.out.println();
        outDegreeMatrix = new int[count][count];
        for (int i = 0; i < strlist.length - 1; i++) {
            outDegreeMatrix[graphElements.get(strlist[i])][graphElements.get(strlist[i + 1])]++;
//            System.out.print("<" + graphElements.get(strlist[i]) + "," + graphElements.get(strlist[i + 1]) + ">" + "\t");
        }
//        System.out.println();
//        for (int i = 0; i < count; i++) {
//            System.out.println(Arrays.toString(outDegreeMatrix[i]));
//        }
    }

//    从文件读入文本
    public static  String getTextFromFile(String fileName) throws IOException{
        List<String> list = Files.readAllLines(Paths.get(fileName));
        String templist = "";
        for (int i = 0; i < list.size(); i++) {
            templist += " " + list.get(i);
        }
        templist = templist.trim();
        return templist;
    }

    //    处理文本
    public static String[] dealWithText(String templist) throws IOException {
        templist = templist.toLowerCase();
        String[] strlist = templist.split("[^a-z]+");
        return strlist;
    }

    //    生成并展示有向图
    public static void showDirectedGraph(String[] strlist) throws IOException {
        graphElements = new HashMap<String, Integer>();
        graphElements2 = new HashMap<Integer, String>();

        System.out.println(Arrays.toString(strlist));
        showMatrix(strlist);

    }

    //    查询桥接词
    public static List<String> queryBridgeWords(String word1, String word2) {
        List<String> list = new ArrayList<>();
        if (!(graphElements.containsKey(word1) && graphElements.containsKey(word2))) {
            return list;
        }

        int pre = graphElements.get(word1);
        int sub = graphElements.get(word2);
        for (int i = 0; i < outDegreeMatrix.length; i++) {
            if (outDegreeMatrix[pre][i] > 0) {
                if (outDegreeMatrix[i][sub] > 0) {
                    list.add(graphElements2.get(i));
                }
            }
        }
        return list;
    }

    //      根据bridge word生成新文本
    public static String generateNewText(String inputText) {

    }
//
//    //    计算两个单词之间的最短路径
//    String calcShortestPath(String word1, String word2) {
//
//    }
//
//    //    随机游走
//    String randomWalk() {
//
//    }
}
