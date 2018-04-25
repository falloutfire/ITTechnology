package sample.ControllersFXML;

import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import sample.Main;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;

public class LoginPassLayoutController {
    public TextField loginField;
    public PasswordField passwordField;
    private Main main;

    public void onClickEnter() throws IOException {
        String login = loginField.getText();
        String pass = passwordField.getText();
        final String resourceF = getClass().getResource("pass.txt").toExternalForm();
        List<String> lines = Files.readAllLines(Paths.get(resourceF.substring(6)), Charset.defaultCharset());
        String password = lines.get(0);

        if(Objects.equals(login, "Man") && Objects.equals(pass, "test")) {
            main.openLayoutUser();
        } else if (Objects.equals(login, "Admin") && Objects.equals(pass, password)) {
            main.openLayoutAdmin();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Ошибка входа");
            alert.setHeaderText("Неверныое имя пользователя/пароль");
            alert.setContentText("Проверьте введеные вами данные.\n");
            alert.showAndWait();
        }
    }

    public void setMain(Main main) {
        this.main = main;
    }
}
