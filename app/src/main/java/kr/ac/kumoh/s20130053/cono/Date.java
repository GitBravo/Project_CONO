package kr.ac.kumoh.s20130053.cono;

import java.util.Calendar;

public class Date {
    // 현재 년 값 추출 메소드
    public int getCurrentYear() {
        Calendar cal = Calendar.getInstance();
        return cal.get(cal.YEAR);
    }

    // 현재 월 값 추출 메소드
    public int getCurrentMonth() {
        Calendar cal = Calendar.getInstance();
        return cal.get(cal.MONTH) + 1;
    }

    // 현재 일 값 추출 메소드
    public int getCurrentDay() {
        Calendar cal = Calendar.getInstance();
        return cal.get(cal.DATE);
    }

    public boolean isOverdue(int year, int month, int dayOfMonth){
        // 현재날짜와 인수로 입력된 날짜를 비교하여 이미 지나간 날짜면 true 를 반환한다.
        if(year < getCurrentYear()
                || (year == getCurrentYear() && month < getCurrentMonth())
                || (year == getCurrentYear() && month == getCurrentMonth() && dayOfMonth < getCurrentDay()))
            return true;
        else
            return false;
    }

    public int[] getIntegerDate(String date){
        // ex) 2018.5.31 등의 문자열 날짜를 ".", "-" 을 기준으로 분할해서 정수배열(year, month, dayOfMonth)로 반환
        String[] strings = date.split("[.]");
        int year = Integer.valueOf(strings[0]);
        int month = Integer.valueOf(strings[1]);
        int dayOfMonth = Integer.valueOf(strings[2]);
        return new int[]{year, month, dayOfMonth};
    }

    public boolean CompareLatestDate(String oldDate, String newDate){
        int []new_IntDate = getIntegerDate(newDate);
        int []old_IntDate = getIntegerDate(oldDate);
        if(old_IntDate[0] < new_IntDate[0]
                || (old_IntDate[0] == new_IntDate[0] && old_IntDate[1] < new_IntDate[1])
                || (old_IntDate[0] == new_IntDate[0] && old_IntDate[1] == new_IntDate[1] && old_IntDate[2] < new_IntDate[2]))
            return true;
        else
            return false;
    }
}
