package Controller;

import com.example.javafx.HelloApplication;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

public class Dashboard {
    @FXML
    private Button gotoGuideManagement;

    @FXML
    private Button frontoffice;
    @FXML
    private Button acc;

    @FXML
    private Button transport;
    @FXML
    private Button user;
    @FXML
    private Button claims;

    @FXML
    private Button gotoMonumentManagement;





    public void gotoGuideManagement(javafx.event.ActionEvent actionEvent) {
        URL url = getClass().getResource("/AddGuide.fxml");
        if (url == null) {
            System.err.println("Cannot find Guides.fxml");
        } else {
            try {
                Parent root = FXMLLoader.load(url);
                gotoGuideManagement.getScene().setRoot(root);
            } catch (IOException ex) {
                ex.printStackTrace(); // This prints the stack trace to help diagnose the issue
            }
        }
    }



    public void gotoFrontOffice(ActionEvent actionEvent) {
        URL url = getClass().getResource("/com/test/tjv2/Home.fxml");
        if (url == null) {
            System.err.println("Cannot find Monument.fxml");
        } else {
            try {
                Parent root = FXMLLoader.load(url);
                frontoffice.getScene().setRoot(root);
            } catch (IOException ex) {
                ex.printStackTrace(); // This prints the stack trace to help diagnose the issue
            }
        }
    }

    public void gotoUserManagment(ActionEvent actionEvent) {
        URL url = getClass().getResource("/usersList.fxml");
        if (url == null) {
            System.err.println("Cannot find Monument.fxml");
        } else {
            try {
                Parent root = FXMLLoader.load(url);
                user.getScene().setRoot(root);
            } catch (IOException ex) {
                ex.printStackTrace(); // This prints the stack trace to help diagnose the issue
            }
        }
    }


    public void goToTransport(ActionEvent actionEvent) {
        URL url = getClass().getResource("/com/example/javafx/Subscription.fxml");
        if (url == null) {
            System.err.println("Cannot find subscription.fxml");
        } else {
            try {
                Parent root = FXMLLoader.load(url);
                transport.getScene().setRoot(root);
            } catch (IOException ex) {
                ex.printStackTrace(); // This prints the stack trace to help diagnose the issue
            }
        }
    }

    public void goToAccManagement(ActionEvent actionEvent) {
        URL url = getClass().getResource("/com/example/javafx/Accomodation.fxml");
        if (url == null) {
            System.err.println("Cannot find Acc.fxml");
        } else {
            try {
                Parent root = FXMLLoader.load(url);
                acc.getScene().setRoot(root);
            } catch (IOException ex) {
                ex.printStackTrace(); // This prints the stack trace to help diagnose the issue
            }
        }
    }

    public void gotoMonumentManagement(ActionEvent actionEvent) {
        URL url = getClass().getResource("/com/test/tjv2/Monument.fxml");
        if (url == null) {
            System.err.println("Cannot find Monument.fxml");
        } else {
            try {
                Parent root = FXMLLoader.load(url);
                gotoMonumentManagement.getScene().setRoot(root);
            } catch (IOException ex) {
                ex.printStackTrace(); // This prints the stack trace to help diagnose the issue
            }
        }
    }

    public void goToClaimsManagement(ActionEvent actionEvent) {
        URL url = getClass().getResource("/com/example/javafx/back.fxml");
        if (url == null) {
            System.err.println("Cannot find Monument.fxml");
        } else {
            try {
                Parent root = FXMLLoader.load(url);
                claims.getScene().setRoot(root);
            } catch (IOException ex) {
                ex.printStackTrace(); // This prints the stack trace to help diagnose the issue
            }
        }
    }

    private Stage getPrimaryStage() {
        return HelloApplication.getPrimaryStage();
    }

    public void minimizeWindow(javafx.event.ActionEvent actionEvent) {
        getPrimaryStage().setIconified(true);
    }

    public void expandWindow(javafx.event.ActionEvent actionEvent) {
        Stage stage = getPrimaryStage();
        stage.setMaximized(!stage.isMaximized());
    }

    public void closeWindow(javafx.event.ActionEvent actionEvent) {
        getPrimaryStage().close();
    }




}
