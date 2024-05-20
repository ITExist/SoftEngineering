import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.lang.String;


public class Lab1 {
    static final int INT_MAX = Integer.MAX_VALUE; //int最大整数
    static Map<String, Integer> graphElements = new HashMap<String, Integer>(); //单词->数组下标映射
    static Map<Integer, String> graphElements2 = new HashMap<Integer, String>(); //数组下标->单词映射
    static Map<Integer, List<Integer>> outDegreeGraph = new HashMap<Integer, List<Integer>>(); //出度图
    static int[][] outDegreeMatrix; //出度矩阵

    public static void main(String[] args) {
        try {
            Initializes(getTextFromFile("test.txt")); //从文件路径读入数据并进行处理，生成出度矩阵、出度图和哈希表
            Scanner scanner = new Scanner(System.in);
            System.out.println("Please select function:");
            System.out.println("1.Search bridge words 2.Generate new text 3.Calculate the shortest path 4.Random walk");
            int chioce = scanner.nextInt();
            scanner.nextLine(); //读入缓冲区的换行符
            switch (chioce) {
                case 1: //查询桥接词
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
                    break;
                case 2: //根据输入文本生成新文本
                    System.out.println("Please enter text:");
                    String outputText = generateNewText(scanner.nextLine());
                    System.out.println("The new text is: " + "\n" + outputText);
                    break;
                case 3: //计算最短路径
                    System.out.println("Please input the first word:");
                    String word1_3 = scanner.nextLine();
                    System.out.println("Please input the second word:");
                    String word2_3 = scanner.nextLine();
                    System.out.println(calcShortestPath(word1_3, word2_3));
                    break;
                case 4: //随机游走，并将生成的文本生成txt文件
                    System.out.println("Text generation...");
                    String randText = randomWalk();
                    String outputFilePath = "func6outputText.txt";
                    try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFilePath))) {
                        writer.write(randText);
                        System.out.println("Write file successfully!");
                    } catch (IOException e) {
                        System.out.println("Write file failure!");
                    }
                    break;
                default:
                    System.out.println("Input error!");
            }

        } catch (Exception e) {
            System.out.println(e.getStackTrace());
        } finally {
            System.out.println("Parsing is over, thank you for your use!");
        }
    }

    //    生成出度矩阵、出度图及两个映射
    public static void Initializes(String[] strlist) {
        int count = 0;
        for (int i = 0; i < strlist.length; i++) { //生成单词和数组下标的互相映射和出度图
            if (!graphElements.containsKey(strlist[i])) {
//                System.out.print(strlist[i] + "\t");
                graphElements.put(strlist[i], count);
                graphElements2.put(count, strlist[i]);
                outDegreeGraph.put(count++, new ArrayList<Integer>());
            }
        }
        System.out.println();
        outDegreeMatrix = new int[count][count];
        for (int i = 0; i < strlist.length - 1; i++) { //遍历文本生成出度矩阵
            outDegreeMatrix[graphElements.get(strlist[i])][graphElements.get(strlist[i + 1])]++;
            outDegreeGraph.get(graphElements.get(strlist[i])).add(graphElements.get(strlist[i + 1]));
//            System.out.print("<" + graphElements.get(strlist[i]) + "," + graphElements.get(strlist[i + 1]) + ">" + "\t");
        }
        System.out.println(Arrays.toString(strlist));
//        Iterator<Map.Entry<Integer, List<Integer>>> iterator = outDegreeGraph.entrySet().iterator();
//        while (iterator.hasNext()) {
//            Map.Entry<Integer, List<Integer>> entry = iterator.next();
//            System.out.println("key:" + entry.getKey() + ",vaule:" + entry.getValue());
//        }
//        System.out.println();
//        for (int i = 0; i < count; i++) {
//            System.out.println(Arrays.toString(outDegreeMatrix[i]));
//        }
    }

    //    从文件读入并处理文本
    public static String[] getTextFromFile(String fileName) throws IOException {
        List<String> list = Files.readAllLines(Paths.get(fileName));
        String templist = "";
        for (int i = 0; i < list.size(); i++) {
            templist += " " + list.get(i);
        }
        templist = templist.trim();
        templist = templist.toLowerCase();
        return templist.split("[^a-z]+");
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
    public static String generateNewText(String inputText) throws IOException {
        String[] textList = inputText.split("[^a-zA-Z]+");
        String retStr = "";
        for (int i = 0; i < textList.length - 1; i++) {
            retStr += textList[i] + " ";
            //求出单词的出度词序列，随机选择一个插入原文本
            List<String> bridgeStr = queryBridgeWords(textList[i].toLowerCase(), textList[i + 1].toLowerCase());
            if (!bridgeStr.isEmpty()) {
                int randomInt = new Random().nextInt(bridgeStr.size());
                retStr += bridgeStr.get(randomInt) + " ";
            }
        }
        retStr += textList[textList.length - 1];
        retStr = retStr.trim();
        return retStr;
    }

    //    Dijkstra算法
    public static int[][] dijkstra(int index) {
        //好多，懒得写了，经典的算法，本质是贪心
        Map<Integer, String> S_Gather = new HashMap<>();
        int n = graphElements.size();
        int[] path = new int[n];
        int[] pathLen = new int[n];
        for (int i = 0; i < n; i++) { //初始化两个数组
            path[i] = index;
            if (outDegreeMatrix[index][i] != 0) {
                pathLen[i] = outDegreeMatrix[index][i];
            } else {
                pathLen[i] = INT_MAX;
            }
        }
        System.out.println("path: " + Arrays.toString(path));
        System.out.println("pathlen: " + Arrays.toString(pathLen));
        S_Gather.put(index, null);
        for (int i = 1; i < n; i++) {
            int minIndex = index;
            for (int j = 0; j < n; j++) { //选出未被选择过的路径最短的点
                if ((!S_Gather.containsKey(j)) && pathLen[j] < pathLen[minIndex]) {
                    minIndex = j;
                }
            }
            S_Gather.put(minIndex, null);
            for (int j = 0; j < n; j++) { //更新其余未被选择点的最短路径
                if (!S_Gather.containsKey(j)) {
                    if (outDegreeMatrix[minIndex][j] != 0) {
                        if (pathLen[minIndex] + outDegreeMatrix[minIndex][j] < pathLen[j]) {
                            pathLen[j] = pathLen[minIndex] + outDegreeMatrix[minIndex][j];
                            path[j] = minIndex;
                        }
                    }
                }
            }
        }
        System.out.println(Arrays.deepToString(new int[][]{path, pathLen}));
        return new int[][]{path, pathLen};
    }

    //    计算两个单词之间的最短路径
    public static String calcShortestPath(String word1, String word2) {
        if (!graphElements.containsKey(word1)) {
            return new String("The word isn't in the Graph!");
        }
        int m = graphElements.get(word1);
        int n;
        int[][] path = dijkstra(m);
        if (!graphElements.containsKey(word2)) {
            n = new Random().nextInt(graphElements.size());
        } else {
            n = graphElements.get(word2);
        }
        if (path[1][n] == INT_MAX) {
            return new String("The \"" + word1 + "\" to \"" + graphElements2.get(n) + "\" is unreachable!");
        }
        String ret = "->" + graphElements2.get(n);
        int tmp = path[0][n];
        while (tmp != m) {
            ret = "->" + graphElements2.get(tmp) + ret;
            tmp = path[0][tmp];
        }
        ret = graphElements2.get(m) + ret;
        return ret;
    }

    //    随机游走
    public static String randomWalk() throws InterruptedException {
        Map<String, String> passedPath = new HashMap<>(); //维护哈希表记录游走过的边
        String ret = "";
        Random rand = new Random();
        int index = rand.nextInt(graphElements.size());
        ret += graphElements2.get(index) + " ";
        System.out.print(graphElements2.get(index) + " ");
        while (!outDegreeGraph.get(index).isEmpty()) { //单词出度为0时结束游走
            Thread.sleep(1000);
            //从出度表中随机选择下一个单词
            int nextIndex = outDegreeGraph.get(index).get(rand.nextInt(outDegreeGraph.get(index).size()));
            String tmp = "" + index + nextIndex;
            ret += graphElements2.get(nextIndex) + " ";
            System.out.print(graphElements2.get(nextIndex) + " ");
            if (passedPath.containsKey(tmp)) { //路径重复时结束游走
                break;
            }
            passedPath.put(tmp, null); //将该边加入哈希表
            index = nextIndex;
        }
        System.out.println();
        return ret.trim();
    }
}
