package net.octacomm.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * Date와 관련된 유틸리티 클래스
 */
public class DateUtil {

    public static String getCurrentDatetime() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime());
    }

    /**
     * 1970년 1월 1일부터의 millisecond를 정해진 포맷으로 수정한다.
     * 
     * @param time
     *            시간
     * @return 변환된 포맷
     */
    public static String long2Str(final long time) {
        java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss", java.util.Locale.KOREA);
        return formatter.format(new Date(time));

    }

    public static String long2DateStr(final long time) {
        java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.KOREA);
        return formatter.format(new Date(time));
    }

    /**
     * 지금 시간을 정해진 포맷으로 수정한다.
     * 
     * @return 변환된 포맷
     */
    public static String now2Str() {
        Calendar calendar = Calendar.getInstance();
        java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss", java.util.Locale.KOREA);
        return formatter.format(calendar.getTime());
    }

    public static String nowDate2Str() {
        Calendar calendar = Calendar.getInstance();
        java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.KOREA);
        return formatter.format(calendar.getTime());
    }

    /**
     * 지금 시간을 얻어온다.
     * 
     * @return 변환된 포맷
     */
    public static long now2Long() {
        Calendar calendar = Calendar.getInstance();
        Date date_ = calendar.getTime();

        return date_.getTime();
    }

    /**
     * 1970년 1월 1일부터의 초를 정해진 포맷으로 수정한다.
     * 
     * @param sec
     *            초
     * @return 변환된 포맷
     */
    public static String sec2Str(final int sec) {
        java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss", java.util.Locale.KOREA);
        return formatter.format(new Date((long) (sec) * 1000));
    }

    /**
     * 해당 시간을 뺀 시간을 구해 정해진 포맷으로 수정한다.
     * 
     * @param millisec
     *            뺄 시간
     * @return 변환된 포맷
     */
    public static String prev2Str(final int millisec) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MILLISECOND, millisec * -1);
        java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss", java.util.Locale.KOREA);
        return formatter.format(calendar.getTime());
    }

    /**
     * 해당 시간을 뺀 시간을 구해 정해진 포맷으로 수정한다.
     * 
     * @param l
     *            뺄 시간
     * @return 변환된 포맷
     */
    public static String prev2DateStr(final long l) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MILLISECOND, (int) (l * -1));
        java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat(
                "yyyy-MM-dd", java.util.Locale.KOREA);
        return formatter.format(calendar.getTime());
    }

    /**
     * string 형식의 시간을 long형으로 수정한다. (yyyy-MM-dd HH:mm:ss)
     * @param date
     * @return
     */
    public static long str2Long(final String date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", java.util.Locale.KOREA);

        try {
            Date date_ = sdf.parse(date);
            return date_.getTime();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return -1;
    }

    /**
     * 현재 시간을 리턴한다.
     * 
     * @return rettime 현재 시각
     */
    public static int getCurrentHour() {
        GregorianCalendar cal_ = new GregorianCalendar();

        return cal_.get(Calendar.HOUR_OF_DAY);
    }

    /**
     * 현재 분을 리턴한다.
     * 
     * @return rettime 현재 시각
     */
    public static int getCurrentMinute() {
        GregorianCalendar cal_ = new GregorianCalendar();

        return cal_.get(Calendar.MINUTE);
    }

    /**
     *현재 시간을 가져온다.
     */
    public static String getCurrentTime() {
        Calendar cal = Calendar.getInstance();
        StringBuffer time = new StringBuffer();

        if (cal.get(Calendar.HOUR_OF_DAY) < 10) {
            time.append("0" + cal.get(Calendar.HOUR_OF_DAY) + ":");
        } else {
            time.append(cal.get(Calendar.HOUR_OF_DAY) + ":");
        }

        if (cal.get(Calendar.MINUTE) < 10) {
            time.append("0" + cal.get(Calendar.MINUTE) + ":");
        } else {
            time.append(cal.get(Calendar.MINUTE) + ":");
        }

        if (cal.get(Calendar.SECOND) < 10) {
            time.append("0" + cal.get(Calendar.SECOND));
        } else {
            time.append(cal.get(Calendar.SECOND));
        }

        return time.toString();
    }

    /**
     *현재 날짜를 가져온다.
     */
    public static String getToday() {
        Calendar cal = Calendar.getInstance();
        StringBuffer date = new StringBuffer();

        date.append(cal.get(Calendar.YEAR) + "-");

        if (cal.get(Calendar.MONTH) < 9) {
            date.append("0" + (cal.get(Calendar.MONTH) + 1) + "-");
        } else {
            date.append((cal.get(Calendar.MONTH) + 1) + "-");
        }

        if (cal.get(Calendar.DATE) < 10) {
            date.append("0" + cal.get(Calendar.DATE));
        } else {
            date.append(cal.get(Calendar.DATE));
        }

        return date.toString();
    }

    /**
     *  입력한 날짜가 3개월 이전보다 후인지 확인
     */
    public static boolean expire1month(String passwordUpdate) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date passDate = null;
        Calendar after90days = Calendar.getInstance();
        Date after90Date = null;
        Date toDay = null;
        try {
            toDay = formatter.parse(getToday());
            passDate = formatter.parse(passwordUpdate.substring(0, 10));
            after90days.setTime(passDate);
            after90days.add(Calendar.DAY_OF_MONTH, 30); // 30일 후 
            after90Date = formatter.parse(new SimpleDateFormat("yyyy-MM-dd").format(after90days.getTime()));

            if (toDay.after(after90Date)) {
                return false;
            }            
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return true;
    }

    /**
     * 인자로 주어진 월에 속한 모든 주를 조회한다.
     * 
     * @param year 년도
     * @param month 1 ~ 12
     * @return
     */
    static public List<Calendar> getWeeks(int year, int month) {
        GregorianCalendar calendar = new GregorianCalendar(year, month - 1, 1);

        ArrayList<Calendar> ret = new ArrayList<Calendar>();

        // 1일의 주를 기준으로 한다. SUNDAY(일요일): 1
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);

        // calendar 1일이 속한 주의 시작일로 설정한다.
        int diff = Calendar.SUNDAY - dayOfWeek;

        if (0 != diff) {
            // 전달 부터 연결된 주
            calendar.add(Calendar.DAY_OF_MONTH, diff);
        }

        ret.add((Calendar) calendar.clone());

        while (true) {
            calendar.add(Calendar.DAY_OF_MONTH, 7);

            if (calendar.get(Calendar.MONTH) != month - 1) {
                break;
            }

            ret.add((Calendar) calendar.clone());
        }

        return ret;
    }

    /**
     * 인자로 주어진 일자를 시작일로 하는 한 주의 기간을 문자열로 포멧팅한다.
     * 예) 2011-11-27 ~ 2011-12-03
     * 
     * @param startDate
     * @return
     */
    static public String getWeekDisplayString(Calendar startDate) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String startDay = dateFormat.format(startDate.getTime());

        Calendar endDate = (Calendar) startDate.clone();
        endDate.add(Calendar.DAY_OF_MONTH, 6);

        String endDay = dateFormat.format(endDate.getTime());

        return String.format("%s ~ %s", startDay, endDay);
    }

    /**
     * 인자로 주어진 월에 속한 마지막일자를 조회한다.
     * 
     * @param year 년도
     * @param month 1 ~ 12
     * @return
     */
    static public int getLastDay(int year, int month) {
        GregorianCalendar calendar = new GregorianCalendar(year, month - 1, 1);

        return calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
    }

    public static void main(String[] args) {
        List<Calendar> weeks = DateUtil.getWeeks(2011, 12);

        System.out.println("Month = 12");

        for (Calendar startDayOfWeek : weeks) {
            System.out.println(getWeekDisplayString(startDayOfWeek));
        }

        for (int i = 0; i < 12; i++) {
            System.out.println("Last day of " + (i + 1) + " = " + getLastDay(2011, i + 1));
        }
    }
}
