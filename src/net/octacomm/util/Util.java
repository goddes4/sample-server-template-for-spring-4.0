package net.octacomm.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Util {

    /**
     * List 객체가 Null인지 검사한다.
     * 
     * @param list
     * @return Null이면 true, 그렇지 않으면 false
     */
    public static boolean isNull(final List<?> list) {
        if (list == null || list.size() == 0) {
            return true;
        }

        return false;
    }

    /**
     * 문자열을 MD5 해쉬 값으로 수정한다.
     * 
     * @param pwd
     *            비밀번호
     * @return MD5 해쉬 문자열
     */
    public static String stringToMD5(final String pwd) {
        MessageDigest md5 = null;
        byte[] byteHash = null;
        StringBuffer buffer_ = new StringBuffer();

        try {
            md5 = MessageDigest.getInstance("MD5");

            md5.reset();

            md5.update(pwd.getBytes());

            byteHash = md5.digest();

            for (int i = 0; i < byteHash.length; i++) {
                buffer_.append(Integer.toHexString(0xFF & byteHash[i]));
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return buffer_.toString();
    }

    /**
     * 문자열이 null이거나 사이즈가 0인지 체크한다.
     * 
     * @param str
     *            문자열
     * @return null이거나 사이즈가 0이면 true, 문자열이 존재하면 false이다.
     */
    public static boolean isNull(final String str) {
        if (str == null || str.length() == 0) {
            return true;
        }
        return false;
    }

    /**
     * 요청한 문자열에서 정규식에 해당하는 문자들을 꺼내서 String 배열로 반환한다.
     * 
     * @param str
     *            문자열
     * @return String 배열
     */
    public static String[] getStringArray(final String regex, String str) {
        if (isNull(str)) {
            return null;
        }

        ArrayList<String> matchList = new ArrayList<String>();

        // 정규식을 사용해서 해당 문자를 찾는다.
        Matcher matcher = Pattern.compile(regex).matcher(str);

        // 찾은 문자들을 저장한다.
        while (matcher.find()) {
            matchList.add(matcher.group());
        }

        String[] result = new String[matchList.size()];
        return matchList.toArray(result);
    }

    public static String parseFloat(final float f) {
        String str = String.valueOf(f);

        int startIndex = str.indexOf('E');
        if (startIndex == -1) {
            return str;
        }
        int ex = Integer.valueOf(str.substring(startIndex + 2)) - 1;

        StringBuffer sb = new StringBuffer();
        sb.append("0.");
        for (int i = 0; i < ex; i++) {
            sb.append("0");
        }
        sb.append(str.substring(0, 1));
        sb.append(str.substring(2, startIndex - 1));
        return sb.toString();
    }

    /**
     * String -> int 로 변환한다.
     * 
     * @param val
     * @return
     */
    public static int parseInt(final String val) {
        if (isNull(val)) {
            return -1;
        }
        try {
            return Integer.parseInt(val);
        } catch (Exception e) {
//            e.printStackTrace();
        }

        return -1;
    }

    /**
     * String -> int 로 변환한다.
     * 변환 실패 시 인자로 주어진 defVal를 리턴한다.
     * 
     * @param val
     * @return
     */
    public static int parseInt(final String val, int defVal) {
        try {
            return Integer.parseInt(val);
        } catch (Exception e) {
//            e.printStackTrace();
        }

        return defVal;
    }

    public static String toString(final int val, int defVal) {
        if (defVal == 10) {
            return Integer.toString(val);
        } else if (defVal == 16) {
            return Integer.toHexString(val);
        } else if (defVal == 2) {
            return Integer.toBinaryString(val);
        } else if (defVal == 8) {
            return Integer.toOctalString(val);
        }

        return null;
    }

    public static String calculateDeviceID(String mac) {
        try {
            if (mac == null || mac.length() < 4) {
                return null;
            }

            String idStr = mac.substring(mac.length() - 4);
            return String.valueOf(Integer.valueOf(idStr, 16));
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 버전 문자열을 비교한다.
     * @param o1 비교할 버전
     * @param o2 비교할 버전
     * @return o1이 o2보다 적으면 음수, 같으면 0, 크면 양수
     */
    public static int compareVersion(String o1, String o2) {
        int idx;
        String[] oa1, oa2;
        int i1, i2;
        oa1 = o1.split("\\.");
        oa2 = o2.split("\\.");

        if (oa1.length < oa2.length) {
            // 같은 자리수 까지만 비교한다.
            for (idx = 0; idx < oa1.length; idx += 1) {
                i1 = Integer.valueOf(oa1[idx]);
                i2 = Integer.valueOf(oa2[idx]);
                // "1.0.1" = "1.0.1" 
                if (i1 == i2) {
                    continue;
                } // "1.0.1" < "1.0.2" 
                else if (i1 < i2) {
                    return -1;
                }

                // "1.0.2" > "1.0.1" 
                return 1;
            }

            //나머지 자리수를 비교한다.
            // "1.0.1" > "1.0.1.?.?"
            for (; idx < oa2.length; idx += 1) {
                i2 = Integer.valueOf(oa2[idx]);
                // "1.0.1" < "1.0.1.1" 
                if (0 < i2) {
                    return -1;
                }
            }

            //나머지 자리수가 모드 '0'이면 같음.
            // "1.0.1" = "1.0.1.0.0" 
            return 0;
        } else if (oa1.length > oa2.length) {
            // 같은 자리수 까지만 비교한다.
            for (idx = 0; idx < oa2.length; idx += 1) {
                i1 = Integer.valueOf(oa1[idx]);
                i2 = Integer.valueOf(oa2[idx]);
                // "1.0.1" = "1.0.1" 
                if (i1 == i2) {
                    continue;
                } // "1.0.1" < "1.0.2" 
                else if (i1 < i2) {
                    return -1;
                }

                // "1.0.2" > "1.0.1" 
                return 1;
            }

            //나머지 자리수를 비교한다.
            // "1.0.1.?.?" > "1.0.1"
            for (; idx < oa1.length; idx += 1) {
                i1 = Integer.valueOf(oa1[idx]);
                // "1.0.1.1" < "1.0.1" 
                if (0 < i1) {
                    return 1;
                }
            }

            //나머지 자리수가 모드 '0'이면 같음.
            // "1.0.1.0.0" = "1.0.1" 
            return 0;
        } else /*if(oa1.length == oa2.length) */ {
            // 같은 자리수 까지만 비교한다.
            for (idx = 0; idx < oa1.length; idx += 1) {
                i1 = Integer.valueOf(oa1[idx]);
                i2 = Integer.valueOf(oa2[idx]);
                if (i1 == i2) {
                    continue;
                } else if (i1 < i2) {
                    return -1;
                }

                return 1;
            }
        }

        return 0;
    }

    /**
     * 인자로 주어진 문자열 str이 null이면 공백 문자열을 리턴한다.
     * @param str
     * @return
     */
    public static String checkNull(String str) {
        if (null == str) {
            return "";
        }

        return str;
    }
}
