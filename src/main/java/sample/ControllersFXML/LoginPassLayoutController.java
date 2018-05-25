package sample.ControllersFXML;

import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import sample.Main;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import java.util.Scanner;

public class LoginPassLayoutController {
    public TextField loginField;
    public PasswordField passwordField;
    private Main main;

    public void onClickEnter(){
        String login = loginField.getText();
        String pass = passwordField.getText();
        String result;
        final InputStream resourceF = this.getClass().getClassLoader().getResourceAsStream("sample/ControllersFXML/pass.txt");
        try(Scanner s = new Scanner(resourceF).useDelimiter("\\A")) {
            result = s.hasNext() ? s.next() : "";
        }
        System.out.println(result);
        //List<String> lines = Files.readAllLines(Paths.get(result/*resourceF.substring(10)*/), Charset.defaultCharset());
        //String password = lines.get(0);
        //String password = "test";
        if(Objects.equals(login, "Man") && Objects.equals(pass, "test")) {
            main.openLayoutUser();
        } else if (Objects.equals(login, "Admin") && Objects.equals(pass, result)) {
            main.openLayoutAdmin();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Ошибка входа");
            alert.setHeaderText("Неверное имя пользователя/пароль");
            alert.setContentText("Проверьте введеные вами данные.\n");
            alert.showAndWait();
        }
    }

    public void setMain(Main main) {
        this.main = main;
    }
}
