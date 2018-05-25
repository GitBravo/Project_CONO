package kr.ac.kumoh.s20130053.cono;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import static kr.ac.kumoh.s20130053.cono.MainActivity.hairshop_token;

public class FragmentHairshop extends android.support.v4.app.Fragment {

    TextView intro; // 설명
    TextView service; // 서비스
    TextView businessHour; // 영업시간
    TextView holiday; // 휴무일
    TextView phoneNumber; // 전화번호
    TextView address; // 주소
    TextView reprt; // 대표자
    TextView businessName; // 상호명
    TextView reprtAddress; // 대표자 주소

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_hairshop, container, false);

        intro = rootView.findViewById(R.id.frag_hairshop_intro);
        service = rootView.findViewById(R.id.frag_hairshop_service);
        businessHour = rootView.findViewById(R.id.frag_hairshop_businessHours);
        holiday = rootView.findViewById(R.id.frag_hairshop_holiday);
        phoneNumber = rootView.findViewById(R.id.frag_hairshop_phoneNumber);
        address = rootView.findViewById(R.id.frag_hairshop_address);
        reprt = rootView.findViewById(R.id.frag_hairshop_representative);
        businessName = rootView.findViewById(R.id.frag_hairshop_businessName);
        reprtAddress = rootView.findViewById(R.id.frag_hairshop_representativeAddress);

        new LocalDataRefresher(hairshop_token).TryInfoRefresh(intro, service, businessHour, holiday, phoneNumber, address, reprt, businessName, reprtAddress);
        return rootView;
    }
}