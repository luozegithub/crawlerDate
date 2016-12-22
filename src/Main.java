
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.junit.Test;

import java.io.IOException;

public class Main {

    public static void main(String[] args) {
        // 创建默认的客户端实例
       HttpClient httpClient=new DefaultHttpClient();
       String result=null;
       HttpGet httpGet=new HttpGet("http://www.baidu.com");

        try {
            HttpResponse httpResponse=httpClient.execute(httpGet);
            System.out.printf("---luo"+httpResponse.getStatusLine()+"ze----");

            HttpEntity entity = httpResponse.getEntity();
            if(entity != null){

                //响应内容
                //System.out.println(EntityUtils.toString(entity));
                result=EntityUtils.toString(entity,"UTF-8");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            httpClient.getConnectionManager().shutdown();
        }

        System.out.println(result);

    }
    @Test
    public void test(){
        Almanac almanac=AlmanacUtil.getAlmanac();
        System.err.println(almanac);

    }
}

