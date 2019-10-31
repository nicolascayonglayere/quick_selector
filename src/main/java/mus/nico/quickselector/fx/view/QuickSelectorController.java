package mus.nico.quickselector.fx.view;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.paint.Color;
import javafx.stage.DirectoryChooser;
import mus.nico.quickselector.QuickselectorApplication;
import mus.nico.quickselector.fx.CopyTaskFx;
import mus.nico.quickselector.utils.CopierMusique;

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

	@FXML
	private TextArea txtArea;
	@FXML
	private ProgressBar barreProgression;
	@FXML
	private Label statutsEtq;

	private QuickselectorApplication quickSelector;

	private String monTexte;

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
		this.txtArea.clear();
		this.monTexte = "";
		// this.barreProgression = new ProgressBar(0);
		// Group
		ToggleGroup group = new ToggleGroup();
		this.suppression.setToggleGroup(group);
		this.listeAlea.setToggleGroup(group);
		this.jourTravail.setToggleGroup(group);

		List<Boolean> options = new ArrayList<Boolean>();
		options.add(this.suppression.isSelected());
		options.add(this.jourTravail.isSelected());
		options.add(this.listeAlea.isSelected());

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
			for (String s : cm.getListeSuppr()) {
				this.monTexte += "\n " + s;
			}

			QuickSelectorController.this.txtArea.setText("DOSSIERS SUPPRIMES : " + this.monTexte + " \n "
					+ "Temps d'execution nettoyage: " + String.valueOf(time));
			System.out.println("Temps d'execution nettoyage: " + String.valueOf(time));
			// MaFenetre.this.getContentPane().revalidate();
			// MaFenetre.this.getContentPane().repaint();

		}

		if (options.get(1)) {
			CopyTaskFx copyTask = new CopyTaskFx(
					Paths.get(QuickSelectorController.this.etqSource.getText().substring(0, 3)),
					Paths.get(QuickSelectorController.this.etqDestination.getText()));
			QuickSelectorController.this.statutsEtq.setTextFill(Color.BLUE);

			// Unbind text property for Label.
			QuickSelectorController.this.statutsEtq.textProperty().unbind();

			// Bind the text property of Label
			// with message property of Task
			QuickSelectorController.this.statutsEtq.textProperty().bind(copyTask.messageProperty());

			// this.validerButton.setDisable(true);

			// Unbind progress property
			QuickSelectorController.this.barreProgression.progressProperty().unbind();
			QuickSelectorController.this.barreProgression.setProgress(0);
			QuickSelectorController.this.barreProgression.progressProperty().bind(copyTask.progressProperty());
			long begin = System.currentTimeMillis();
			copyTask.addEventFilter(WorkerStateEvent.WORKER_STATE_SUCCEEDED, //
					new EventHandler<WorkerStateEvent>() {
						@Override
						public void handle(WorkerStateEvent event) {
							List<Path> copied = copyTask.getValue();
							QuickSelectorController.this.statutsEtq.textProperty().unbind();
							// QuickSelectorController.this.statutsEtq.setText("Copied: " + copied.size());
							copied.stream().forEachOrdered(p -> {
								QuickSelectorController.this.monTexte += "\n" + p.toString();
							});
							long end = System.currentTimeMillis();
							float time = (end - begin) / 1000f;
							QuickSelectorController.this.txtArea
									.setText("ALBUMS AJOUTES : " + QuickSelectorController.this.monTexte + " \n "
											+ "Temps d'execution copie journee: " + String.valueOf(time));
						}

					});
			// Start the Task.
			new Thread(copyTask).start();
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
			for (String s : cm.getListeAlea()) {
				this.monTexte += "\n" + s;
			}
			QuickSelectorController.this.txtArea.setText("TITRES AJOUTES : " + this.monTexte + " \n "
					+ "Temps d'execution liste aléatoire: " + String.valueOf(time));
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

	public void setMainApp(QuickselectorApplication quickselectorApplication) {
		this.quickSelector = quickselectorApplication;

	}

}
