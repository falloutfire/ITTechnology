package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;
import sample.ControllersFXML.*;

import java.io.IOException;


public class Main extends Application {

    private Stage primaryStage;
    private BorderPane rootLayout;
    private MainController mainController;

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("Information Systems and Technology");
        primaryStage.setResizable(false);
        primaryStage.sizeToScene();
        showLoginPassLayout();
    }

    public void openLayoutUser(){
        initRootLayoutUser();
        showLayoutUser();
    }

    public void openLayoutAdmin(){
        initRootLayoutAdmin();
        showLayoutAdmin();
    }

    private void showLoginPassLayout() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(Main.class.getResource("view/LoginPassLayout.fxml"));
        AnchorPane loginPane = loader.load();
        Scene scene = new Scene(loginPane);
        primaryStage.setScene(scene);
        primaryStage.show();

        LoginPassLayoutController loginPassLayoutController = loader.getController();
        loginPassLayoutController.setMain(this);
    }

    private void showLayoutUser(){
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("view/Main.fxml"));
            AnchorPane mainPane = loader.load();

            rootLayout.setCenter(mainPane);

            mainController = loader.getController();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initRootLayoutUser(){
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("view/RootLayoutUser.fxml"));
            rootLayout = loader.load();

            Scene scene = new Scene(rootLayout);

            Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();
            primaryStage.setX((primScreenBounds.getWidth() - primaryStage.getWidth()) / 3);
            primaryStage.setY((primScreenBounds.getHeight() - primaryStage.getHeight()) / 4);

            primaryStage.setScene(scene);
            primaryStage.show();

            RootLayoutUserController rootLayoutUserController = loader.getController();
            rootLayoutUserController.setMainApp(this);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showLayoutAdmin() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("view/DataBaseLayout.fxml"));
            AnchorPane mainPane = loader.load();

            rootLayout.setCenter(mainPane);

            DataBaseLayoutController dataBaseLayoutController = loader.getController();
            dataBaseLayoutController.setMain(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initRootLayoutAdmin() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("view/RootLayoutAdmin.fxml"));
            rootLayout = loader.load();

            Scene scene = new Scene(rootLayout);

            Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();
            primaryStage.setX((primScreenBounds.getWidth() - primaryStage.getWidth()) / 3);
            primaryStage.setY((primScreenBounds.getHeight() - primaryStage.getHeight()) / 4);

            primaryStage.setScene(scene);
            primaryStage.show();

            RootLayoutAdminController rootLayoutAdminController = loader.getController();
            rootLayoutAdminController.setMainApp(this);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean showMaterialAddDialog() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("view/MaterialAddLayout.fxml"));
            AnchorPane pane = loader.load();

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Add Material");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(pane);
            dialogStage.setScene(scene);

            MaterialAddLayoutController controller = loader.getController();
            controller.setDialogStage(dialogStage);

            dialogStage.showAndWait();
            return controller.isOkClicked();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean showChangePassDialog() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("view/ChangePasswordLayout.fxml"));
            AnchorPane pane = loader.load();

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Смена пароля");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(pane);
            dialogStage.setScene(scene);

            ChangePasswordLayoutController controller = loader.getController();
            controller.setDialogStage(dialogStage);

            dialogStage.showAndWait();
            return controller.isOkClicked();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

    }

    public static void main(String[] args) {
        launch(args);
    }

    public MainController getMainController() {
        return mainController;
    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public void aboutWindow() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information Systems and Technology");
        alert.setHeaderText("О программе");
        alert.setContentText("Авторы: Илья Лихачев, Илья Родионов\nГруппа 455");

        alert.showAndWait();
    }
}
