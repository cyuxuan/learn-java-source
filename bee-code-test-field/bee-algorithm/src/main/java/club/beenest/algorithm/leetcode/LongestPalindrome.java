package club.beenest.algorithm.leetcode;

/**
 * 最长回文字串
 * https://leetcode.cn/leetbook/read/top-interview-questions-medium/xvn3ke/
 *
 * @author 陈玉轩
 * @since 22.11.18
 */
public class LongestPalindrome {
    public static void main(String[] args) {
        String s = "aacabdkacaa";
        String res = "";
        // 中心扩散法
//        res = centerDiffusion(s);
        // 动态规划
        res = dynamicProgramming(s);
        System.out.println(res);
    }

    /**
     * 动态规划解决最长回文子串
     *  dp[left][right] = s.charAt(left)==s.charAt(right) && dp[left+1][right-1]
     *
     * @return 最长的回文子串
     */
    public static String dynamicProgramming(String s) {
        // 记录数据长度
        int len = s.length();
        // 初始化一个数组，存储状态
        boolean[][] dp = new boolean[len][len];

        // 记录开始位置
        int start = 0;
        // 记录结束位置
        int end = 0;

        // 执行循环，刷新数据
        for (int right = 1; right < len; right++) {
            for (int left = 0; left < right; left++) {
                if (s.charAt(left) == s.charAt(right)) {
                    // 如果 左右下标相等 则是一个字母，一定是
                    // 左右下标为 (i,i+1)或者(i,i+2)，分别对应 xx , xyx 这两种情况，也判定相等
                    if ((right - left) <= 2) {
                        // 将对应值置为相等
                        dp[left][right] = true;
                    }else {
                        dp[left][right] = dp[left + 1][right - 1];
                    }

                    // 处理完再处理当前字串是否大于已记录字串
                    if (dp[left][right] && (right - left) > (end - start)) {
                        start = left;
                        end = right;
                    }
                }
            }
        }

        return s.substring(start, end + 1);
    }


    /**
     * 中心扩散法，解决最长回文字串
     * 说明：
     * 当出现 abba 这种串时，以任何一个为中心都不对，因为回文串有奇数也有偶数
     * 以第一个b为开始位置时，左右不相等，但是偶数对bb是一个回文
     * 所以需要检测，当前数字的左右是否相等，如果相等则跳过，因为相等的字符不管是偶数还是奇数个，都是回文
     *
     * @param s 待解决的字串
     * @return 返回最长的回文字串
     */
    public static String centerDiffusion(String s) {
        // 当前字符串长度
        int len = s.length();
        // 当前最长回文开始位置
        int start = 0;
        // 当前最长回文结束位置
        int end = 0;
        // 开始执行
        for (int i = 0; i < len; i++) {
            // 以i位置为中心进行扩散
            // 记录扩散的左临界点
            int left = i;
            // 记录扩散的右临界点
            int right = i;
            // 判断不是最后一位，是最后一位则不再排除右边数据，因为没有了
            while (right < len && s.charAt(right) == s.charAt(i)) {
                right++;
            }

            // 判断不是第一位，是第一位则不再排查左边数据，因为没有了
            while (left >= 0 && s.charAt(left) == s.charAt(i)) {
                left--;
            }
            // 开始不同数据的检查
            while (left >= 0 && right < len) {
//                System.out.println("统计位置：" + left + "-" + right + "全局位置：" + start + "-" + end);
                // 相等则更新标记，并继续扩散
                if (s.charAt(left) == s.charAt(right)) {
                    left--;
                    right++;
                } else {
                    // 否则退出，查找结束进入下一个循环
                    break;
                }
            }

            // 最后再判断当前记录的字串是否大于已经记录的字串
            if ((right - left - 1) > (end - start)) {
                start = left + 1;
                end = right;
            }
//            System.out.println("扩散位置：" + i + " 结果: " + s.substring(start, end));
        }
        // 返回最终结果
        return s.substring(start, end);
    }
}
