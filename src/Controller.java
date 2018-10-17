



import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javax.net.ssl.HttpsURLConnection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Controller {
    @FXML
    private TextField tfLogin;
    @FXML
    private TextField tfPassword;
    @FXML
    private Button btnConnect;
    @FXML
    private TextField tfConnect;
    @FXML
    private TextField tfURL1;
    @FXML
    private Button btnStart1;
    @FXML
    private Button btnStop1;
    @FXML
    private TextField tfStatus1;
    @FXML
    private TextField tfURL2;
    @FXML
    private Button btnStart2;
    @FXML
    private Button btnStop2;
    @FXML
    private TextField tfStatus2;
    @FXML
    private TextField tfURL3;
    @FXML
    private Button btnStart3;
    @FXML
    private Button btnStop3;
    @FXML
    private TextField tfStatus3;
    @FXML
    private TextField tfURL4;
    @FXML
    private Button btnStart4;
    @FXML
    private Button btnStop4;
    @FXML
    private TextField tfStatus4;
    @FXML
    private TextField tfURL5;
    @FXML
    private Button btnStart5;
    @FXML
    private Button btnStop5;
    @FXML
    private TextField tfStatus5;


    private HttpsURLConnection conn;
    private String login;
    private String pass;
    private HttpUrlConnectionExample object1;
    private HttpUrlConnectionExample object2;
    private HttpUrlConnectionExample object3;
    private HttpUrlConnectionExample object4;
    private HttpUrlConnectionExample object5;

    public Controller() {
    }

    @FXML
    private void connect() throws IOException {
        this.login = this.tfLogin.getText();
        this.pass = this.tfPassword.getText();
        String url = "https://xn--c1acndtdamdoc1ib.xn--p1ai/vxod.html";
        CookieHandler.setDefault(new CookieManager());
        this.tfLogin.clear();
        this.tfLogin.setPromptText("Login");
        this.tfPassword.clear();
        this.tfPassword.setPromptText("Password");

        PageContent connect = new PageContent(url, login, pass);
        try {
            String page = connect.getPageContent();
            String postParams = connect.getConnectionParams(page);
            boolean connection = connect.sendPost(postParams);
            if(connection) {
                this.tfConnect.setText("Подключение успешно!");
            }

            if(!connection) {
                this.tfConnect.setText("Неправильное имя пользователя или пароль.");
            }
        } catch (IOException var5) {
            var5.printStackTrace();
        }

    }


    private TextField getTextFieldURL(int id) {
        return id == 1?this.tfURL1:(id == 2?this.tfURL2:(id == 3?this.tfURL3:(id == 4?this.tfURL4:(id == 5?this.tfURL5:null))));
    }

    private TextField getTextFieldSTATUS(int id) {
        return id == 1?this.tfStatus1:(id == 2?this.tfStatus2:(id == 3?this.tfStatus3:(id == 4?this.tfStatus4:(id == 5?this.tfStatus5:null))));
    }

    private void setHttpUrlConnectionExample(int id, HttpUrlConnectionExample object) {
        if(id == 1) {
            this.object1 = object;
        }

        if(id == 2) {
            this.object2 = object;
        }

        if(id == 3) {
            this.object3 = object;
        }

        if(id == 4) {
            this.object4 = object;
        }

        if(id == 5) {
            this.object5 = object;
        }

    }

    private HttpUrlConnectionExample getHttpUrlConnectionExample(int id) {
        return id == 1?this.object1:(id == 2?this.object2:(id == 3?this.object3:(id == 4?this.object4:(id == 5?this.object5:null))));
    }


    @FXML
    private void startMonitor(ActionEvent event) {
        Button button = (Button)event.getSource();
        String nameButton = button.getId();
        int id = Integer.parseInt(nameButton.split("Start")[1]);
        TextField tfURL = this.getTextFieldURL(id);
        TextField tfStatus = this.getTextFieldSTATUS(id);

        try {
            HttpUrlConnectionExample object = new HttpUrlConnectionExample(tfURL, tfStatus);
            this.setHttpUrlConnectionExample(id, object);
            object.setDaemon(true);
            object.start();
        } catch (IOException var8) {
            var8.printStackTrace();
        }

    }

    @FXML
    private void stopMonitor(ActionEvent event) {
        Button button = (Button)event.getSource();
        String nameButton = button.getId();
        int id = Integer.parseInt(nameButton.split("Stop")[1]);
        HttpUrlConnectionExample object = this.getHttpUrlConnectionExample(id);
        if (object == null) {return;}
        object.interrupt();
        object.gettFURL().clear();
        object.gettFURL().setPromptText("URL");
        object.gettF().clear();
        object.gettF().setPromptText("Статус");
        object.gettF().setStyle("");
        object.gettFURL().setEditable(true);
    }
}
