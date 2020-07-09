package leveque.zamble.tp6;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;


public class MenuTp6 {
    @FXML
    public Button Grille_a;

    @FXML
    public Button GrilleCx;

    @FXML
    public Button Quitter;

    private int numGrille;
    private boolean estGrilleAleatoire = false;

    @FXML
    public void initialize() {
        Grille_a.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                estGrilleAleatoire = true;
                ChoirGrille();
            }
        });

        GrilleCx.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {

                AfficherDialogue();
            }
        });

        Quitter.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                Stage st = (Stage) Quitter.getScene().getWindow();
                st.close();
            }
        });
    }


    public void ChoirGrille() {
        try {
            Quitter.getScene().getWindow().hide();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("VueTP6.fxml"));
            ControleurTP6 controller = new ControleurTP6(estGrilleAleatoire, numGrille);
            loader.setController(controller);
            Parent parent = loader.load();
            Scene scene = new Scene(parent);
            Stage stage = new Stage();
            stage.setTitle("TP6 Leveque-Zamble");
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void AfficherDialogue(){
        //Créer une liste de grille
        List <Integer> grille = new ArrayList<>();

        //
        grille.add(1);
        grille.add(2);
        grille.add(3);
        grille.add(4);
        grille.add(5);
        grille.add(6);
        grille.add(7);
        grille.add(8);
        grille.add(9);
        grille.add(10);
        grille.add(11);

        ChoiceDialog<Integer> dialog = new ChoiceDialog<>( 1, grille);
        dialog.setTitle("Choix d'une grille");
        dialog.setHeaderText(null);
        dialog.setContentText("Choisir une grille :");
        Optional<Integer> result = dialog.showAndWait();
        result.ifPresent(new Consumer<Integer>() {
            @Override
            public void accept(Integer numero) {
                numGrille = numero;
                ChoirGrille();
            }
        });
    }
}