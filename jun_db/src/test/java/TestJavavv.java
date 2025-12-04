/*
算法题：
        有效的括号

        题目描述：
        给定一个只包括 '('，')'，'{'，'}'，'['，']' 的字符串 s ，判断字符串是否有效。​

        有效字符串需满足：​
        1、左括号必须用相同类型的右括号闭合。​
        2、左括号必须以正确的顺序闭合。​
        3、每个右括号都有一个对应的相同类型的左括号。​

        示例 1：​
        输入：s = "()"​
        输出：true​

        示例 2：​
        输入：s = "()[]{}"​
        输出：true​

        示例 3：​
        输入：s = "(]"​
        输出：false​

        示例 4：​
        输入：s = "([])"​
        输出：true

        示例 5：​
        输入：s = “([])]”​
        输出：false

        示例 6：​
        输入：s = “(()[])”​
        输出：true
*/

class Main {

    public static void main(String[] args) {
        // 写完后依次输入上述示例
        String str = "([])";
        boolean isValid = isValid(str);
        System.out.println(isValid);
    }

    public static boolean isValid(String str) {
        if(str.contains("(")){
            if(str.contains(")")){
                return true;
            }else {
                return false;
            }
        }else if(str.contains("{")){
            if(str.contains("}")){
                return true;
            }else {
                return false;
            }
        }else if(str.contains("[")){
            if(str.contains("]")){
                return true;
            }else {
                return false;
            }
        }
        // TODO：请在此处完成代码
        return false;
    }
}
