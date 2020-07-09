package leveque.zamble.tp6;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class ControleurTP6 implements Initializable {
private boolean estGrilleAleatoire;
private int numGrille;

	@FXML
	public Button retour;

	private TextField [][] tableTextField;
	@FXML private GridPane monGridPane;
	private MotsCroisesTP6 motsCroises;

	public ControleurTP6(boolean estGrilleAleatoire, int numGrille) {
		this.estGrilleAleatoire = estGrilleAleatoire;
		this.numGrille = numGrille;
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		this.init();

		retour.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				Stage stage = (Stage) retour.getScene().getWindow();
				stage.close();
				new MainTP6().start(new Stage());

			}
		});
	}
	@FXML
	public void init() {
		 try {
			 ConnexionBD bd = new ConnexionBD();
			 if (estGrilleAleatoire){
				 generationGrille(bd, bd.RandomGrilleNum());
			 }else{
				 generationGrille(bd, numGrille);
			 }

	         bd.close();
		 } catch (Exception e) {
			 e.printStackTrace();
		 }

		//this.motsCroises = MotsCroisesFactory.creerMotsCroises2x3();

		for (Node n : monGridPane.getChildren()) {
			if (n instanceof TextField) {
				TextField tf = (TextField) n ;
				int lig = ((int) n.getProperties().get("gridpane-row")) + 1 ;
				int col = ((int) n.getProperties().get("gridpane-column")) + 1 ;
				// Initialisation du TextField tf ayant pour coordonn�es (lig, col)
				// (cf. sections 1.3, 1.4 et 1.5)
				tf.textProperty().bindBidirectional(this.motsCroises.propositionProperty(lig, col));

				if(this.motsCroises.getDefinition(lig, col, true) != null && this.motsCroises.getDefinition(lig, col, false) != null){
					tf.setTooltip(new Tooltip(this.motsCroises.getDefinition(lig, col, true)+" / "+this.motsCroises.getDefinition(lig, col, false)));
				}else if(this.motsCroises.getDefinition(lig, col, true) != null) {
					tf.setTooltip(new Tooltip(this.motsCroises.getDefinition(lig, col, true)));
				} else if(this.motsCroises.getDefinition(lig, col, false) != null) {
					tf.setTooltip(new Tooltip(this.motsCroises.getDefinition(lig, col, false)));
				}
				tf.setOnMouseClicked((e)->{ this.clicCase(e);}) ;
				tf.addEventFilter(KeyEvent.KEY_TYPED, limitCharacter(1));
				tf.textProperty().addListener(new ChangeListener<String>() {
					@Override
					public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
						if (!t1.equals("")) {
							char ch = tf.textProperty().get().charAt(0);
							System.out.println("test " + motsCroises.getSolution(lig, col));
							if (tf.textProperty().get().charAt(0) == motsCroises.getSolution(lig, col)) {
								tf.getStyleClass().add("solution");
							}
							if ((ch >= 65 && ch <= 90) || (ch >= 97 && ch <= 122))
								tableTextField[lig-1][col].requestFocus();
						}
					}
				});
				tf.setOnKeyPressed(new EventHandler<KeyEvent>() {
					@Override
					public void handle(KeyEvent keyEvent) {
						switch (keyEvent.getCode()){
							case UP:
								choisirDirection(lig-2, col, 1);
								break;
							case DOWN:
								choisirDirection(lig, col, 1);
								break;
							case RIGHT:
								if( col < motsCroises.getLargeur() && tableTextField[lig-1][col] != null )
									tableTextField[lig-1][col].requestFocus();
								break;
							case LEFT:
								choisirDirection(lig-1, col, 2);
								break;

						}
					}
				});
			}
		}
	}

	private void choisirDirection(int lig, int col, int i) {
		if ((col-i) >= 0 && lig >= 0  && (lig < motsCroises.getHauteur() && col < motsCroises.getLargeur()) &&tableTextField[lig][col - i] != null)
			tableTextField[lig][col - i].requestFocus();
	}

	private EventHandler<KeyEvent> limitCharacter(int i) {
		return new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent keyEvent) {
				TextField tf = (TextField) keyEvent.getSource();
				if (tf.getText().length() >= i)
					keyEvent.consume();
			}
		};
	}
	@FXML
	public void clicCase(MouseEvent e) {

		if (e.getButton() == MouseButton.PRIMARY) {
			if (e.getClickCount() == 2){
			// C'est un double clic "
			TextField tf = (TextField) e.getSource();
			int lig = ((int) tf.getProperties().get("gridpane-row")) + 1 ;// n� ligne de la case (cf. boucle du 1.2)
			int col = ((int) tf.getProperties().get("gridpane-column")) + 1 ;// n� colonne de la case (cf. boucle du 1.2)
			this.motsCroises.reveler(lig, col);// demande de r�v�lation de la solution sur (lig,col)
			tf.textProperty().set(this.motsCroises.propositionProperty(lig, col).get());
		}
		}
	}

	@FXML
	public void nouvelleGrille() {
		try {
			ConnexionBD bd = new ConnexionBD();
			generationGrille(bd, bd.RandomGrilleNum());
			bd.close();
		} catch (Exception e) {
			 e.printStackTrace();
		}
	}

	private void generationGrille(ConnexionBD bd, int numeroGrille) {

		TextField modele = (TextField) monGridPane.getChildren().get(0);
		monGridPane.getChildren().clear();
		motsCroises = extraireGrille(numeroGrille);
		tableTextField = new TextField[motsCroises.getHauteur()][motsCroises.getLargeur()];
		for(int i = 1; i <= motsCroises.getHauteur(); i++) {
			for(int j = 1; j <= motsCroises.getLargeur(); j++) {
				if(!motsCroises.estCaseNoire(i, j)){
					TextField textfield = new TextField();
					textfield.setPrefHeight(modele.getPrefHeight());
					textfield.setPrefWidth(modele.getPrefWidth());
					for (Object cle : modele.getProperties().keySet()) {
						textfield.getProperties().put(cle, modele.getProperties().get(cle)) ;
					}
					monGridPane.add(textfield, j-1, i-1);
					tableTextField[i-1][j-1]= textfield;
				}
			}
		}
	}

	private MotsCroisesTP6 extraireGrille(int numGrille) {
		MotsCroisesTP6 mc = null;

		String taille = "Select largeur, hauteur FROM DB_MotsCroisés.TP5_GRILLE WHERE num_grille = ?";
		try {
			PreparedStatement preparedStatement = ConnexionBD.connecterBD().prepareStatement(taille);
			preparedStatement.setString(1, String.valueOf(numGrille));
			ResultSet resultSet = preparedStatement.executeQuery();

			while (resultSet.next()) {

				int l = resultSet.getInt("largeur");
				int h = resultSet.getInt("hauteur");
				mc = new MotsCroisesTP6(h, l);

			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		String select = "Select definition, horizontal, ligne, colonne, solution FROM DB_MotsCroisés.TP5_MOT WHERE num_grille = ?";
		try {
			PreparedStatement preparedStatement = ConnexionBD.connecterBD().prepareStatement(select);
			preparedStatement.setString(1, String.valueOf(numGrille));
			ResultSet resultSet = preparedStatement.executeQuery();

			while (resultSet.next()) {

				String def = resultSet.getString("definition");
				int hz = resultSet.getInt("horizontal");
				int l = resultSet.getInt("ligne");
				int c = resultSet.getInt("colonne");
				String s = resultSet.getString("solution");

				if(hz == 1) {
					mc.setDefinition(l, c, true, def);
					int j = c;
					for(int i = 0 ; i < s.length() ; i++) {
						mc.setSolution(l, j, s.charAt(i));
						j++;
					}
				} else {
					mc.setDefinition(l, c, false, def);
					int j = l;
					for(int i = 0 ; i < s.length() ; i++) {
						mc.setSolution(j, c, s.charAt(i));
						j++;
					}
				}
			}
			return mc;

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return mc;

	}



}
