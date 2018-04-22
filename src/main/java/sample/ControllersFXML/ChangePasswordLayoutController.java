package sample.ControllersFXML;

import javafx.event.ActionEvent;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

public class ChangePasswordLayoutController {
    public TextField newPassField;
    public TextField confirmPassField;
    private Stage dialogStage;
    private boolean okClicked = false;

    public void onClickEnter(ActionEvent actionEvent) {
        String newPass = newPassField.getText();
        String confirm = confirmPassField.getText();
        final String resourceF = getClass().getResource("pass.txt").toExternalForm().substring(6);
        File pass = new File(resourceF);
        if (newPass.equals(confirm)) {
            try {
                PrintWriter passWrite = new PrintWriter(pass);
                passWrite.println(newPass);
                passWrite.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        okClicked = true;
        dialogStage.close();
    }

    public void onClickExit(ActionEvent actionEvent) {
        dialogStage.close();
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public boolean isOkClicked() {
        return okClicked;
    }
}
