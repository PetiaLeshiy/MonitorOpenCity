import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import javax.net.ssl.HttpsURLConnection;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class PageContent {
    private HttpsURLConnection conn;
    private String login;
    private String pass;
    private String url;
    private String nameTour;

    public String getNameTour() {
        return nameTour;
    }




    public PageContent(String url, String login, String pass) throws IOException {
        this.url = url;
        this.login = login;
        this.pass = pass;
    }

    public PageContent(String url) {
        this.url = url;
    }

    protected String getPageContent() throws IOException {
        URL obj = new URL(url);
        this.conn = (HttpsURLConnection) obj.openConnection();
        this.conn.setRequestMethod("GET");
        this.conn.setUseCaches(false);
        this.conn.setRequestProperty("User-Agent", "Mozilla/5.0");
        this.conn.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
        this.conn.setRequestProperty("Accept-Language", "ru-RU,ru;q=0.9,en-US;q=0.8,en;q=0.7");
        BufferedReader in = new BufferedReader(new InputStreamReader(this.conn.getInputStream()));
        StringBuffer sB = new StringBuffer();

        String inputLine;
        while ((inputLine = in.readLine()) != null) {
            sB.append(inputLine);
        }

        in.close();
        return sB.toString();
    }

    protected String getConnectionParams(String html) throws IOException {
        Document doc = Jsoup.parse(html);
        Element loginform = doc.select("div.loginLogin").first();
        Elements inputElements = loginform.getElementsByTag("input");
        List<String> paramList = new ArrayList();
        String param;
        String value;
        for (Iterator var6 = inputElements.iterator(); var6.hasNext(); paramList.add(param + "=" + URLEncoder.encode(value, "UTF-8"))) {
            Element inputElement = (Element) var6.next();
            param = inputElement.attr("name");
            value = inputElement.attr("value");
            if (param.equals("username")) {
                value = this.login;
            } else if (param.equals("password")) {
                value = this.pass;
            }
        }

        StringBuilder result = new StringBuilder();
        Iterator var11 = paramList.iterator();

        while (var11.hasNext()) {
            param = (String) var11.next();
            if (result.length() == 0) {
                result.append(param);
            } else {
                result.append("&" + param);
            }
        }

        result.append("&Login=");
        return result.toString();
    }


    protected boolean sendPost(String postParams) throws IOException {
        conn = (HttpsURLConnection) new URL(this.url).openConnection();
        this.conn.setUseCaches(false);
        this.conn.setRequestMethod("POST");
        this.conn.setRequestProperty("HOST", "xn--c1acndtdamdoc1ib.xn--p1ai");
        this.conn.setRequestProperty("User-Agent", "Mozilla/5.0");
        this.conn.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
        this.conn.setRequestProperty("Accept-Language", "ru-RU,ru;q=0.9,en-US;q=0.8,en;q=0.7");
        this.conn.setRequestProperty("Connection", "keep-alive");
        this.conn.setRequestProperty("Referer", "https://xn--c1acndtdamdoc1ib.xn--p1ai/vxod.html?service=logout");
        this.conn.setRequestProperty("Contetnt-Type", "application/x-www-form-urlencoded");
        this.conn.setRequestProperty("Content-Length", Integer.toString(postParams.length()));
        this.conn.setDoOutput(true);
        this.conn.setDoInput(true);
        DataOutputStream wr = new DataOutputStream(this.conn.getOutputStream());
        wr.writeBytes(postParams);
        wr.flush();
        wr.close();
        BufferedReader reader = new BufferedReader(new InputStreamReader(this.conn.getInputStream()));
        StringBuffer lineIn = new StringBuffer();

        while (reader.ready()) {
            lineIn.append(reader.readLine());
        }

        return lineIn.indexOf("Неправильное имя пользователя или пароль.") == -1;
    }


    protected String getPostParamsTour(String pageTour) {
        Document doc = Jsoup.parse(pageTour);
        Element element = doc.select("title").first();
        try {
            this.nameTour = new String(element.getAllElements().html().getBytes(), "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        String[] date = this.url.substring(this.url.indexOf("date=") + 5, this.url.length() - 11).split("-");
        String lastChar = url.substring(url.indexOf("%") + 6, url.length() - 3);
        StringBuilder sb = new StringBuilder();

        for(int i = date.length - 1; i >= 0; --i) {
            sb.append(date[i] + "-");
        }

        sb.deleteCharAt(sb.length() - 1);
        sb.append("+" + this.url.substring(this.url.indexOf("date=") + 18, this.url.indexOf("date=") + 20));
        sb.append("%3A" + lastChar);
        String dateLine = sb.toString();
        element = doc.select("button.button").first();
        String id = element.attr("data-insert_tour");
        String type = element.attr("data-insert_tour_type");
        String check = String.format("id=%s&type=%s&date=%s", new Object[]{id, type, dateLine});
        return check;
    }


}