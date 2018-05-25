package kr.ac.kumoh.s20130053.cono;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import static kr.ac.kumoh.s20130053.cono.MainActivity.db;

public class NetworkThread extends Thread {
    private String query;

    // 네이버 오픈 API 사용을 위한 client ID 와 secret 값
    private static final String client_id = "VNdwpgEstF171cXkYbcm";
    private static final String client_secret = "1ktPNV31Yw";

    NetworkThread(String query) {
        this.query = query;
    }

    @Override
    public void run() {
        try {
                Map<String, Object> item = new HashMap<>();//firebase 문서 name 생성
                String Encode_query = URLEncoder.encode(this.query, "UTF-8");
                String apiURL = "https://openapi.naver.com/v1/search/local.xml?query=" + Encode_query
                        + "&display=10"
                        + "&start=1"
                        + "&sort=sim"; // XML 결과
                URL url = new URL(apiURL);
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("GET");
                con.setRequestProperty("X-Naver-Client-Id", client_id);
                con.setRequestProperty("X-Naver-Client-Secret", client_secret);

                // 데이터를 읽어온다.
                InputStream is = con.getInputStream();

                // DOM  파서 생성
                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                DocumentBuilder builder = factory.newDocumentBuilder();
                final Document document = builder.parse(is);

                Element root = document.getDocumentElement();
                NodeList item_list = root.getElementsByTagName("item");

                for (int i = 0; i < item_list.getLength(); i++) {
                    Element item_tag = (Element) item_list.item(i);
                    NodeList title_list = item_tag.getElementsByTagName("title");
                    Element title_tag = (Element) title_list.item(0);
                    item.put("name", title_tag.getTextContent());
                    db.collection("Hairshop").add(item); // 1번
                }
        } catch (Exception e) {
            // Toast.makeText(getApplicationContext(), "[Exception]", Toast.LENGTH_LONG).show(); // 예외처리 요망★
        }
    }
}
