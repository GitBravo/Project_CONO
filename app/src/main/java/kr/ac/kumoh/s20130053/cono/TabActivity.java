package kr.ac.kumoh.s20130053.cono;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import static kr.ac.kumoh.s20130053.cono.MainActivity.hairshop_name;
import static kr.ac.kumoh.s20130053.cono.SignInActivity.mAuth;

public class TabActivity extends AppCompatActivity implements View.OnClickListener {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;
    private NavigationView navigationView;

    @Override
    public void onBackPressed() {
        // 드로워레이아웃이 열려있는 상태에서 Back 키 누르면 자동 닫힘
        mDrawerLayout = findViewById(R.id.drawer);
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navi); // 네비게이션 드로워가 TabActivity 보다 높은 레이어에 위치하기 때문에 activity_tab 을 인플레이션 하지 않음

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (hairshop_name != null)
            toolbar.setTitle(hairshop_name); // 선택한 미용실명을 툴바의 텍스트뷰로 출력
        else {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }

        // 플로팅 액션버튼
        FloatingActionButton fab = findViewById(R.id.frag_counseling_write); // 상담 작성 버튼액션
        fab.setOnClickListener(this);

        // 여기서부터 네비게이션 드로워레이아웃 및 토글버튼 초기화 코드
        mDrawerLayout = findViewById(R.id.drawer);
        mToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.drawer_open, R.string.drawer_close);
        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // 네이게이션 뷰 초기화
        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.nav_log)
                    startActivity(new Intent(TabActivity.this, NaviLogActivity.class));
                else if (id == R.id.nav_reservation)
                    startActivity(new Intent(TabActivity.this, NaviResActivity.class));
                else if (id == R.id.nav_counseling)
                    startActivity(new Intent(TabActivity.this, NaviMyResActivity.class));
                else if (id == R.id.nav_modify_info)
                    startActivity(new Intent(TabActivity.this, NaviModifyActivity.class));
                return false;
            }
        });

        // 네비게이션 뷰의 헤더 부분 텍스트 변경
        View headerView = navigationView.getHeaderView(0);
        TextView navHairshopName = headerView.findViewById(R.id.nav_header_textview1); // 현재 헤어샵명 출력
        navHairshopName.setText(hairshop_name);
        navHairshopName = headerView.findViewById(R.id.nav_header_textview2); // 현재 계정명(이메일) 출력
        navHairshopName.setText(mAuth.getCurrentUser().getEmail());

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = findViewById(R.id.tabs);

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.frag_counseling_write:
                startActivity(new Intent(this, CounselingWriteActivity.class));
                break;
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_tab, menu); // 우측 상단 옵션 구현부분
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //버튼 액션 구현부분
        if (id == R.id.opt_hairshop_change) {
            // 헤어샵 변경
            startActivity(new Intent(this, MainActivity.class));
            finish();
            return true;
        } else if (id == R.id.opt_logout) {
            // 로그아웃
            mAuth.signOut();
            startActivity(new Intent(this, SignInActivity.class));
            finish();
            return true;
        } else if (mToggle.onOptionsItemSelected(item))
            // 네비게이션 뷰 토글버튼 동작
            return true;

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    // 이 클래스는 프래그먼트 각각을 만들어내는 메소드 PlaceholderFragment 를 갖고있다.
    // 또한, 프래그먼트를 통해 만들어진 View 를 실제 메모리상에 올려주는 onCreateView 를 통해 인플레이션 해준다.
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */

        // .java 파일 없이 임시 객체를 생성해내는 메소드
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment(); // 프래그먼트 생성
            Bundle args = new Bundle(); // 번들 : 값을 주고받기 위한 객체
            args.putInt(ARG_SECTION_NUMBER, sectionNumber); // 번들 객체를 통해 섹션 번호를 받아옴
            fragment.setArguments(args); // 생성된 프래그먼트에 번들을 부착
            return fragment; // 프래그먼트 반환
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_hairshop, container, false);
//            TextView textView = rootView.findViewById(R.id.section_label);
//            textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
            return rootView;
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        // 이 메소드는 프래그먼트를 반환해주는 메소드로서 여기서 프래그먼트란
        // 실제로 보여지는 여러 개의 화면들 각각을 의미한다.
        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            switch (position) {
                case 0:
                    return new FragmentHairshop();
                case 1:
                    return new FragmentDesigner();
                case 2:
                    return new FragmentCounseling();
            }
            return PlaceholderFragment.newInstance(position + 1); // 만약 찾는 프래그먼트가 없다면 임시로 생성해서 메소드 호출한 뒤 반환
        }

        @Override
        public int getCount() {
            // 총 몇 개의 페이지가 있는지 반환해준다.
            // 리턴 숫자를 바꿔주면 실제로 Tab Bar 에 생성되는 개수가 바뀐다.
            return 3;
        }
    }

}
