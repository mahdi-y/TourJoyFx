package Controller;

import com.example.javafx.HelloApplication;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import utils.UserSession;

import java.io.IOException;
import java.net.URL;

public class Home {
    @FXML
    public Button ClaimsButton;
    @FXML
    public Button HomeButton;
    @FXML
    public Button Acc;
    @FXML
    public Button adminButton;
    @FXML
    public ImageView profileImageView;
    @FXML
    private Button guideButton;
    @FXML
    private Button homeButton;

    @FXML
    private Button MonumentButton;
    @FXML
    private Button SubscriptionButton;


    public void initialize() throws IOException {
        UserSession session = UserSession.getInstance();

        /*firstNameLabel.setText("Welcome " + session.getFirstname() + "!");
        System.out.println(session.getFirstname() + " " + session.getPhonenumber());*/

        if (isAdmin()) {
            adminButton.setVisible(true); // Show admin features if user is admin
        } else {
            adminButton.setVisible(false); // Hide admin features if user is not admin
        }

    }

    public void logoutUser(ActionEvent actionEvent) throws IOException {
        UserSession.getInstance().cleanUserSession();
        HelloApplication.loadFXML("/login.fxml");
    }

    private boolean isAdmin() {
        String[] roles = UserSession.getInstance().getRoles();
        for (String role : roles) {
            if ("ROLE_ADMIN".equals(role)) {
                return true;
            }
        }
        return false;
    }



    public void goToMonuments(ActionEvent actionEvent) {
        URL url = getClass().getResource("/com/test/tjv2/MonumentFront.fxml");
        if (url == null) {
            System.err.println("Cannot find MonumentDetails.fxml");
        } else {
            try {
                Parent root = FXMLLoader.load(url);
                MonumentButton.getScene().setRoot(root);
            } catch (IOException ex) {
                ex.printStackTrace(); // This prints the stack trace to help diagnose the issue
            }
        }
    }

    public void goToClaims(ActionEvent actionEvent){
        // Correct the package path in the getResource method
        URL url = getClass().getResource("/com/example/javafx/add-rec.fxml");
        if (url == null) {
            System.err.println("Cannot find add-rec.fxml");
        } else {
            try {
                Parent root = FXMLLoader.load(url);
                ClaimsButton.getScene().setRoot(root);
            } catch (IOException ex) {
                ex.printStackTrace(); // This prints the stack trace to help diagnose the issue
            }
        }
    }


    public void goToGuides(ActionEvent actionEvent) {
        URL url = getClass().getResource("/guidesFront.fxml");
        if (url == null) {
            System.err.println("Cannot find Guides.fxml");
        } else {
            try {
                Parent root = FXMLLoader.load(url);
                guideButton.getScene().setRoot(root);
            } catch (IOException ex) {
                ex.printStackTrace(); // This prints the stack trace to help diagnose the issue
            }
        }
    }

    public void goToProfile(MouseEvent actionEvent) {
        URL url = getClass().getResource("/userProfile.fxml");
        if (url == null) {
            System.err.println("Cannot find userProfile.fxml");
        } else {
            try {
                Parent root = FXMLLoader.load(url);
                guideButton.getScene().setRoot(root);
            } catch (IOException ex) {
                ex.printStackTrace(); // This prints the stack trace to help diagnose the issue
            }
        }
    }

    public void goToHome(ActionEvent actionEvent) {
        URL url = getClass().getResource("/com/test/tjv2/Home.fxml");
        if (url == null) {
            System.err.println("Cannot find Home.fxml");
        } else {
            try {
                Parent root = FXMLLoader.load(url);
                guideButton.getScene().setRoot(root);
            } catch (IOException ex) {
                ex.printStackTrace(); // This prints the stack trace to help diagnose the issue
            }
        }
    }



    public void goToAcc(ActionEvent actionEvent) {
        // Get the URL of the FXML file
        URL url = getClass().getResource("/com/example/javafx/accFront.fxml");

        // Check if the URL is null, indicating that the resource was not found
        if (url == null) {
            System.err.println("Cannot find accFront.fxml");
        } else {
            try {
                // Load the FXML file into a Parent node
                Parent root = FXMLLoader.load(url);

                // Set the loaded FXML file as the root scene
                Acc.getScene().setRoot(root);
            } catch (IOException ex) {
                // Handle any IOException that occurs during loading
                ex.printStackTrace(); // This prints the stack trace to help diagnose the issue
            }
        }
    }
    public void backoffice() throws IOException {
        HelloApplication.loadFXML("/Dashboard.fxml");
    }

    public void backoffice(ActionEvent actionEvent) throws IOException {
        backoffice();
    }

    public void goToFavs(ActionEvent actionEvent) {
        URL url = getClass().getResource("/com/example/javafx/Favorites.fxml");

        // Check if the URL is null, indicating that the resource was not found
        if (url == null) {
            System.err.println("Cannot find Favorites.fxml");
        } else {
            try {
                // Load the FXML file into a Parent node
                Parent root = FXMLLoader.load(url);

                // Set the loaded FXML file as the root scene
                Acc.getScene().setRoot(root);
            } catch (IOException ex) {
                // Handle any IOException that occurs during loading
                ex.printStackTrace(); // This prints the stack trace to help diagnose the issue
            }
        }
    }

    public void goToSubscriptions(ActionEvent actionEvent) {
        URL url = getClass().getResource("/com/test/tjv2/SubscriptionFront.fxml");
        if (url == null) {
            System.err.println("Cannot find SubscriptionFront.fxml");
        } else {
            try {
                Parent root = FXMLLoader.load(url);
                SubscriptionButton.getScene().setRoot(root);
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