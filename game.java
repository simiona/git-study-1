import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Pattern;

/**
 * 推箱子游戏的开始
 */
public class game {
    /**
     * @param args main函数的参量
     */
    public static void main(String[] args) {
        ArrayList<ArrayList<Integer>> numarr = new ArrayList<>();
        ArrayList<Character> chararr = new ArrayList<>();
        readmap(numarr, chararr);

        /* 创建一个新的棋盘 */
        sandbox board = new sandbox(numarr);

        /* 逐步输入 */
        for (int i = 0; i < chararr.size(); i++) {
            board.move(chararr.get(i));
        }

        /* 整体输出 */
        System.out.println(printout(board));

    }

    /**
     * 
     * @param numarr  用嵌套数组来表示参差不弃的沙盘 当只有012时，归入numarr里
     * @param chararr 从终端输入的操作的集合,用字符数组来储存操作指令
     */
    public static void readmap(ArrayList<ArrayList<Integer>> numarr, ArrayList<Character> chararr) {
        Scanner sc = new Scanner(System.in);

        /*
         * 使用Pattern与matcher函数来完成正则表达式
         * 从输入段输入，当一列nextline行没有012时，归入chararr数组里
         */
        Pattern pattern = Pattern.compile("[012]");

        String input;

        /*
         * 例子：
         * 1 1 1 1
         * 1 0 2 1
         * 1 0 0 1
         * 1 1 1 1
         * h j l k k p q
         */

        while (sc.hasNextLine()) {
            input = sc.nextLine();

            /* 去掉input中可能有的空格，为toCharArray做准备 */
            input = input.replace(" ", "");

            /* 正则表达式，判断是否有非0 1 2的数 */
            if (!pattern.matcher(input).find()) {
                char[] charArray = input.toCharArray();
                for (char c : charArray) {
                    chararr.add(c);
                }
                break;
            }

            else {
                String[] tokens = input.split("");
                ArrayList<Integer> row = new ArrayList<>();
                /* 把tokens字符串中的各个字符挨个替换成int类型 */
                for (String token : tokens) {
                    row.add(Integer.parseInt(token));
                }
                numarr.add(row);
            }

        }
        sc.close();
    }

    /**
     * 
     * @param board 根据沙盘情况输出
     * @return result 是固定格式：⻆⾊的移动次数 ⽆效指令数 碰墙的次数 ⻆⾊最后的坐标。
     */
    public static String printout(sandbox board) {
        String result = board.getCountValidOperation() + " " + board.getCountInvalidOperation() + " "
                + board.getCountHit() + " " + "(" + board.getPlayer_coordx() + "," + board.getPlayer_coordy() + ")";
        return result;
    }

}

/**
 * 
 * @apiNote This class is used internally by the {@link Game} class and is not
 *          intended to be used directly by clients of the API.
 */
class sandbox {
    /*
     * 
     * @param Null 代表沙盘里是空的等同于输入0
     * 
     * @param Border 代表沙盘里的墙代表输入1
     * 
     * @param Player 代表沙盘里是角色的代表输入2
     * 
     * @param CountHit 记录了玩家撞墙的次数
     * 
     * @param CountInvalidOperation 记录了玩家做过的非有效操作次数
     * 
     * @param CountValidOperation 记录了玩家做过的有效操作次数
     * 
     * @param player_coordx 记录玩家所在坐标（x轴方向）
     * 
     * @param player_coordy 记录玩家所在坐标（y轴方向）
     */
    private static final int Null = 0;
    private static final int Border = 1;
    private static final int Player = 2;
    private ArrayList<ArrayList<Integer>> map;
    private int CountHit = 0;
    private int CountInvalidOperation = 0;
    private int CountValidOperation = 0;
    private int player_coordx;
    private int player_coordy;

    /*
     * 这个sandbox函数是用来初始构造map的，
     * 并且寻找player的初始位置
     */
    /**
     * 
     * @param map 玩家在main函数里初始化的地图
     */
    public sandbox(ArrayList<ArrayList<Integer>> map) {
        this.map = map;
        for (int i = 0; i < map.size(); i++) {
            ArrayList<Integer> innerList = map.get(i);
            for (int j = 0; j < innerList.size(); j++) {
                if (innerList.get(j) == Player) {
                    this.player_coordx = i;
                    this.player_coordy = j;
                }
            }
        }
    }

    public int getCountHit() {
        return CountHit;
    }

    public void setCountHit(int countHit) {
        CountHit = countHit;
    }

    public int getCountInvalidOperation() {
        return CountInvalidOperation;
    }

    public void setCountInvalidOperation(int countInvalidOperation) {
        CountInvalidOperation = countInvalidOperation;
    }

    public int getCountValidOperation() {
        return CountValidOperation;
    }

    public void setCountValidOperation(int countValidOperation) {
        CountValidOperation = countValidOperation;
    }

    public int getPlayer_coordx() {
        return player_coordx;
    }

    public void setPlayer_coordx(int player_coordx) {
        this.player_coordx = player_coordx;
    }

    public int getPlayer_coordy() {
        return player_coordy;
    }

    public void setPlayer_coordy(int player_coordy) {
        this.player_coordy = player_coordy;
    }

    /**
     * @param action 玩家在chararr中输入的操作
     */
    public void move(char action) {
        switch (action) {
            case 'h':
                go(0, -1);
                break;
            case 'j':
                go(1, 0);
                break;
            case 'k':
                go(-1, 0);
                break;
            case 'l':
                go(0, 1);
                break;
            case 'q':
                break;
            default:
                this.CountInvalidOperation++;
        }
    }

    /* go函数根据move后的输出换人的位置与棋盘信息 */
    /**
     * 
     * @param x 根据move函数中通过action的选择，规定x的值
     * @param y 根据move函数中通过action的选择，规定y的值
     */
    public void go(int x, int y) {
        if (map.get(player_coordx + x).get(player_coordy + y) == Border) {
            this.CountHit++;
        } else {
            map.get(player_coordx).set(player_coordy, Null);
            map.get(player_coordx + x).set(player_coordy + y, Player);
            player_coordx += x;
            player_coordy += y;
            CountValidOperation++;
        }
    }

}
