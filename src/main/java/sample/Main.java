package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import sample.view.MainController;
import sample.view.RootLayoutController;

import java.io.IOException;


public class Main extends Application {

    private Stage primaryStage;
    private BorderPane rootLayout;
    private MainController mainController;

    @Override
    public void start(Stage primaryStage) throws Exception{
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("Optimizatior");
        primaryStage.setResizable(false);
        primaryStage.sizeToScene();
        initRootLayout();
        showLayout();
    }

    private void showLayout() throws IOException {
        try{
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("view/Main.fxml"));
            AnchorPane lruPane = loader.load();

            rootLayout.setCenter(lruPane);

            mainController = loader.getController();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initRootLayout() throws IOException {
        try{
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("view/RootLayout.fxml"));
            rootLayout = (BorderPane) loader.load();

            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);
            primaryStage.show();

            RootLayoutController rootLayoutController = loader.getController();
            rootLayoutController.setMainApp(this);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }

    public MainController getMainController(){
        return mainController;
    }
}
