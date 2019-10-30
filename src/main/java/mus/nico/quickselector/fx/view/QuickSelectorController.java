package mus.nico.quickselector.fx.view;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.stage.DirectoryChooser;
import mus.nico.quickselector.CopierMusique;
import mus.nico.quickselector.fx.QuickSelectorFx;

public class QuickSelectorController {

	@FXML
	private TextField etqSource;
	@FXML
	private TextField etqDestination;
	@FXML
	private Button sourceButton;
	@FXML
	private Button destinationButton;

	@FXML
	private Button validerButton;
	@FXML
	private Button annulerButton;
	@FXML
	private RadioButton suppression;
	@FXML
	private RadioButton listeAlea;
	@FXML
	private RadioButton jourTravail;

	private QuickSelectorFx quickSelector;

	@FXML
	private void selectDossierSource() {
		final DirectoryChooser fileChooser = new DirectoryChooser();
		fileChooser.setInitialDirectory(Paths.get("H:\\").toFile());
		this.sourceButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				QuickSelectorController.this.etqSource.clear();
				String chemDossierSource = fileChooser
						.showDialog(QuickSelectorController.this.quickSelector.getPrimaryStage()).getPath();
				if (chemDossierSource != null) {
					QuickSelectorController.this.printLog(QuickSelectorController.this.etqSource, chemDossierSource);
				}
			}
		});
	}

	@FXML
	private void selectDossierDestination() {
		final DirectoryChooser fileChooser = new DirectoryChooser();
		this.destinationButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				QuickSelectorController.this.etqDestination.clear();
				String chemDossierSource = fileChooser
						.showDialog(QuickSelectorController.this.quickSelector.getPrimaryStage()).getPath();
				if (chemDossierSource != null) {
					QuickSelectorController.this.printLog(QuickSelectorController.this.etqDestination,
							chemDossierSource);
				}
			}
		});
	}

	@FXML
	private void selectAndAct() {
		// Group
		ToggleGroup group = new ToggleGroup();
		this.suppression.setToggleGroup(group);
		this.listeAlea.setToggleGroup(group);
		this.jourTravail.setToggleGroup(group);

		List<Boolean> options = new ArrayList<Boolean>();
		options.add(this.suppression.isSelected());
		options.add(this.jourTravail.isSelected());
		options.add(this.listeAlea.isSelected());
		// MaFenetre.this.btCopier.setEnabled(false);
		CopierMusique cm = new CopierMusique(
				Paths.get(QuickSelectorController.this.etqSource.getText().substring(0, 3)),
				Paths.get(QuickSelectorController.this.etqDestination.getText()), options);
		if (options.get(0)) {
			long begin = System.currentTimeMillis();
			// MaFenetre.this.txtArea.repaint();
			// MaFenetre.this.refreshAff();
			cm.nettoyage();
			long end = System.currentTimeMillis();
			float time = (end - begin) / 1000f;
//			for (String s : cm.getListeSuppr()) {
//				monTexte += "\n " + s;
//			}

//			MaFenetre.this.txtArea.setText("DOSSIERS SUPPRIMES : " + monTexte + " \n "
//					+ "Temps d'execution nettoyage: " + String.valueOf(time));
			System.out.println("Temps d'execution nettoyage: " + String.valueOf(time));
			// MaFenetre.this.getContentPane().revalidate();
			// MaFenetre.this.getContentPane().repaint();

		}

		if (options.get(1)) {
			long begin = System.currentTimeMillis();
			// MaFenetre.this.refreshAff();
			cm.copierAlbumJournee();
			long end = System.currentTimeMillis();
			float time = (end - begin) / 1000f;
//			for (String s : cm.getListeAlbumsAjoutes()) {
//				monTexte += "\n" + s;
//			}
//			MaFenetre.this.txtArea.setText("ALBUMS AJOUTES : " + monTexte + " \n "
//					+ "Temps d'execution copie journee: " + String.valueOf(time));
			System.out.println("Temps d'execution copie journee: " + String.valueOf(time));
		}

		if (options.get(2)) {
			long begin = System.currentTimeMillis();
//			MaFenetre.this.refreshAff();
			try {
				cm.listeAleatoire();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			long end = System.currentTimeMillis();
			float time = (end - begin) / 1000f;
//			for (String s : cm.getListeAlea()) {
//				monTexte += "\n" + s;
//			}
//			MaFenetre.this.txtArea.setText("TITRES AJOUTES : " + monTexte + " \n "
//					+ "Temps d'execution liste aléatoire: " + String.valueOf(time));
			System.out.println("Temps d'execution liste aléatoire: " + String.valueOf(time));
		}
//		group.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
//			@Override
//			public void changed(ObservableValue<? extends Toggle> ov, Toggle old_toggle, Toggle new_toggle) {
//				// Has selection.
//				if (group.getSelectedToggle() != null) {
//					RadioButton button = (RadioButton) group.getSelectedToggle();
//					System.out.println("Button: " + button.getText());
//				}
//			}
//		});
	}

	private void printLog(TextField textArea, String files) {
		if (files == null || files.isEmpty()) {
			return;
		} else {
			textArea.appendText(files + "\n");
		}
	}

	public void setMainApp(QuickSelectorFx quickSelectorFx) {
		this.quickSelector = quickSelectorFx;

	}

}
