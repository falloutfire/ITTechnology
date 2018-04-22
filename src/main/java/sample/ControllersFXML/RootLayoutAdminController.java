package sample.ControllersFXML;

import javafx.event.ActionEvent;
import sample.Main;

public class RootLayoutAdminController {

    private Main main;

    public void handleClose(ActionEvent actionEvent) {
        System.exit(0);
    }

    public void handleAbout(ActionEvent actionEvent) {
        main.aboutWindow();
    }

    public void setMainApp(Main main) {
        this.main = main;
    }

    public void handleChangePass(ActionEvent actionEvent) {
        boolean okClicked = main.showChangePassDialog();
        if(okClicked){
            System.out.println("change complete");
        }
    }
}
