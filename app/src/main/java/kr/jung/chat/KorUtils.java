package kr.jung.chat;

public class KorUtils {
    public static final char[] cho = {'ㄱ', 'ㄲ', 'ㄴ', 'ㄷ', 'ㄸ', 'ㄹ', 'ㅁ', 'ㅂ', 'ㅃ', 'ㅅ', 'ㅆ', 'ㅇ', 'ㅈ', 'ㅉ', 'ㅊ', 'ㅋ', 'ㅌ', 'ㅍ', 'ㅎ'};
    public static final char[] jung = {'ㅏ', 'ㅐ', 'ㅑ', 'ㅒ', 'ㅓ', 'ㅔ', 'ㅕ', 'ㅖ', 'ㅗ', 'ㅘ', 'ㅙ', 'ㅚ', 'ㅛ', 'ㅜ', 'ㅝ', 'ㅞ', 'ㅟ', 'ㅠ', 'ㅡ', 'ㅢ', 'ㅣ'};
    public static final char[] jong = {' ', 'ㄱ', 'ㄲ', 'ㄳ', 'ㄴ', 'ㄵ', 'ㄶ', 'ㄷ', 'ㄹ',
            'ㄺ', 'ㄻ', 'ㄼ', 'ㄽ', 'ㄾ', 'ㄿ', 'ㅀ', 'ㅁ', 'ㅂ', 'ㅄ', 'ㅅ', 'ㅆ', 'ㅇ', 'ㅈ', 'ㅊ', 'ㅋ', 'ㅌ', 'ㅍ', 'ㅎ'};

    public static String secedeKor(String str) {
        StringBuilder res = new StringBuilder();
        for (char c : str.toCharArray()) {
            if (c < '가' || c > '힣') {
                res.append(c);
                continue;
            }
            int ch = c - 44032;
            // 588 : 유니코드상에서 초성이 바뀌는 주기
            int c1 = (int)(ch / 588);
            int c2 = (int)(ch % 588 / jong.length);
            int c3 = (int)(ch % 588 % jong.length);
            res.append(cho[c1]).append(jung[c2]);
            if(jong[c3] != ' ') res.append(jong[c3]);
        }
        return res.toString();
    }

}
