import org.apache.http.HttpEntity;
import org.apache.http.ParseException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by luo ze on 2016/12/22.
 */
public class AlmanacUtil {
    static String url = "http://tools.2345.com/rili.htm";


    public static Almanac getAlmanac() {
        String html = pickData(url);
        Almanac almanac = analyzeHTMLByString(html);
        return almanac;
    }

    private static String pickData(String url) {
        CloseableHttpClient httpClient = HttpClients.createDefault();

        try {
            HttpGet httpGet = new HttpGet(url);
            CloseableHttpResponse response = httpClient.execute(httpGet);
            try {
                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    return EntityUtils.toString(entity, "UTF-8");
                }
            } finally {
                response.close();
            }


        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        } finally {
            try {
                httpClient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }


    private static Almanac analyzeHTMLByString(String html) {
        String solarDate,lunarDate,chineseAra,should,avoid=" ";
        Document document= Jsoup.parse(html);

        //公历时间
        //solarDate=getSolarDate();
        Element date=document.getElementById("info_all");
        solarDate=date.text();

        //农历时间
        Element eLunarDate=document.getElementById("info_nong");
        lunarDate=eLunarDate.child(0).html().substring(1,3)+eLunarDate.html().substring(11);
        //lunarDate=eLunarDate.text();
        //天干地支纪年法
        Element eChineseAra=document.getElementById("info_chang");
        chineseAra=eChineseAra.text().toString();
        //宜
        should=getSuggestion(document,"yi");
        //忌
        avoid=getSuggestion(document,"ji");
        Almanac almanac=new Almanac(solarDate,lunarDate,chineseAra,should,avoid);
        return almanac;
    }

    /*
     * 获取忌/宜
     */
    private static String getSuggestion(Document doc,String id){
        Element element=doc.getElementById(id);
        Elements elements=element.getElementsByTag("a");
        StringBuffer sb=new StringBuffer();
        for (Element e : elements) {
            sb.append(e.text()+" ");
        }
        return sb.toString();
    }

    /*
     * 获取公历时间,用yyyy年MM月dd日 EEEE格式表示。
     * @return yyyy年MM月dd日 EEEE
     */

    private static String getSolarDate() {
        Calendar calendar = Calendar.getInstance();
        Date solarDate = calendar.getTime();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日 EEEE");
        return formatter.format(solarDate);
    }

}
