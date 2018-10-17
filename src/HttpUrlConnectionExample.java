

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import javafx.scene.control.TextField;
import javax.net.ssl.HttpsURLConnection;


public class HttpUrlConnectionExample extends Thread {


    private String urlTour;
    private boolean open;

    private TextField tF;
    private TextField tFURL;
    private PageContent scanTour;

    public TextField gettF() {
        return this.tF;
    }

    public TextField gettFURL() {
        return this.tFURL;
    }

    public HttpUrlConnectionExample(TextField urlTourTF, TextField textField) throws IOException {
        this.tFURL = urlTourTF;
        this.urlTour = urlTourTF.getText();
        this.tF = textField;
        this.scanTour = new PageContent(urlTour);
    }

    public void run() {
        SimpleDateFormat dateTour  = new SimpleDateFormat("MM:dd HH:mm");
        String pageTour;
        String postParamsTour = null;
        try {
            pageTour = this.scanTour.getPageContent();
            postParamsTour = this.scanTour.getPostParamsTour(pageTour);
            this.tFURL.setText(scanTour.getNameTour());
            this.tFURL.setEditable(false);
            
        } catch (IOException e) {
            e.printStackTrace();
        }

        while(open != true) {
            try {
                if(!this.isInterrupted()) {

                    this.open = this.openTour(postParamsTour);
                    this.tF.setText(dateTour.format(new Date()) + " " + String.valueOf(this.open));
                    if(this.open) {
                        this.tF.setStyle("-fx-background-color: green;");
                    }

                    sleep((long)((int)(240000.0D + Math.random() * 100.0D)));
                    continue;
                }
            } catch (InterruptedException var3) {

            } catch (IOException var4) {
                var4.printStackTrace();
            }

            return;
        }
    }



    private boolean openTour(String postParamsTour) throws IOException {

        boolean test = getJson(postParamsTour).indexOf("success_quantity") != -1;
        return test;
    }


    private static String getJson(String postParamsTour) throws IOException {
        String urlJson = "https://xn--c1acndtdamdoc1ib.xn--p1ai/assets/action/insert_tour.php";
        URL objJSON = new URL(urlJson);
        HttpURLConnection connJSON = (HttpURLConnection)objJSON.openConnection();
        connJSON.setRequestMethod("POST");
        connJSON.setRequestProperty("HOST", "xn--c1acndtdamdoc1ib.xn--p1ai");
        connJSON.setRequestProperty("User-Agent", "Mozilla/5.0");
        connJSON.setRequestProperty("Accept", "*/*");
        connJSON.setRequestProperty("Accept-Language", "ru-RU,ru;q=0.9,en-US;q=0.8,en;q=0.7");
        connJSON.setRequestProperty("Contetnt-Type", "application/x-www-form-urlencoded");
        connJSON.setRequestProperty("Content-Length", String.valueOf(postParamsTour.length()));
        connJSON.setDoOutput(true);
        connJSON.setDoInput(true);
        DataOutputStream wr = new DataOutputStream(connJSON.getOutputStream());
        wr.writeBytes(postParamsTour);
        wr.flush();
        wr.close();

        BufferedReader reader = new BufferedReader(new InputStreamReader(connJSON.getInputStream()));
        StringBuffer response = new StringBuffer();

        String inputLine;
        while((inputLine = reader.readLine()) != null) {
            response.append(inputLine);
        }

        reader.close();
        return response.toString();
    }}



