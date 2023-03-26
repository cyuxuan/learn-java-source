package club.beenest.algorithm.leetcode.bookintermediatelevelalgorithm.treesandgraphs;

/**
 * 岛屿数量
 * https://leetcode.cn/leetbook/read/top-interview-questions-medium/xvtsnm/
 *
 * @author 陈玉轩
 * @since 22.12.03
 */
public class NumIslands {
    //    private static char[][] grid = {
//            {'1', '1', '1', '1', '0'},
//            {'1', '1', '0', '1', '0'},
//            {'1', '1', '0', '0', '0'},
//            {'0', '0', '0', '0', '0'}
//    };
    private static char[][] grid = {
            {'1', '1', '0', '0', '0'},
            {'1', '1', '0', '0', '0'},
            {'0', '0', '1', '0', '0'},
            {'0', '0', '0', '1', '1'}
    };

    public static void main(String[] args) {
        int res = doNumIslands(grid);
        System.out.println("res-" + res);
    }

    private static int doNumIslands(char[][] tempg) {
        // 原数据外面再包一圈，防止溢出
        char[][] grids = new char[tempg.length + 2][tempg[0].length + 2];
        for (int i = 0; i < grids.length; i++) {
            for (int j = 0; j < grids[i].length; j++) {
                if (i == 0 || i == (grids.length - 1) || j == 0 || j == (grids[i].length - 1)) {
                    grids[i][j] = '0';
                } else {
                    grids[i][j] = tempg[i - 1][j - 1];
                }
            }
        }
        // 打印数据
        grid = grids;
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                System.out.print(grid[i][j] + " ");
            }
            System.out.println();
        }
        // 实际流程
        int count = 0;
        // 对每个点进行遍历，统计该点是否可以形成岛屿
        for (int i = 1; i < grids.length - 1; i++) {
            for (int j = 1; j < grids[i].length - 1; j++) {
                System.out.println("*****************************");
                if (traverse(i, j)) {
                    count++;
                }
                System.out.println("*****************************");
            }
        }
        return count;
    }

    private static boolean traverse(int x, int y) {
        // 已经访问过则返回false
        if (grid[x][y] == '0') {
            return false;
        }
        // 已经访问过的岛屿置为不同的标记
        if (grid[x][y] == '1') {
            grid[x][y] = '0';
        }
        // 不是水也没访问过则说明当前是岛屿组成部分
        traverse(x + 1, y);
        traverse(x, y + 1);
        traverse(x - 1, y);
        traverse(x, y - 1);
        // 当前位置四周全是0，则不可能再向四周延伸出去了
        return true;
    }
}
