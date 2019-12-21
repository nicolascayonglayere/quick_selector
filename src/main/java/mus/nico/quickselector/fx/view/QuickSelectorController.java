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
import mus.nico.quickselector.fx.model.Album;
import mus.nico.quickselector.fx.task.Copy10AlbumsTaskFx;
import mus.nico.quickselector.fx.task.Copy99AlbumsTasksFx;
import mus.nico.quickselector.fx.task.SuppressionTaskFx;
import mus.nico.quickselector.utils.ConstructionListeTsAlbums;

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
	private RadioButton semaineTravail;
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

	private Copy10AlbumsTaskFx copy10Task;
	private Copy99AlbumsTasksFx copy99Task;
	private SuppressionTaskFx supprTask;

	private List<Album> mesAlbums;

	// @FXML
	public void init() throws IOException {
		this.mesAlbums = ConstructionListeTsAlbums.construireListe(Paths.get("H:\\"));// QuickSelectorController.this.etqSource.getText().substring(0,
																						// 3)));
		// this.annulerButton.setCancelButton(true);
		this.txtArea.clear();
		this.monTexte = "";
		// this.barreProgression = new ProgressBar(0);
		// Group
		ToggleGroup group = new ToggleGroup();
		this.suppression.setToggleGroup(group);
		this.semaineTravail.setToggleGroup(group);
		this.jourTravail.setToggleGroup(group);

	}

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
				String chemDossierDest = fileChooser
						.showDialog(QuickSelectorController.this.quickSelector.getPrimaryStage()).getPath();
				if (chemDossierDest != null) {
					QuickSelectorController.this.printLog(QuickSelectorController.this.etqDestination, chemDossierDest);
				}
			}
		});
	}

	@FXML
	private void annuler() {
		this.annulerButton.setCancelButton(true);
		this.annulerButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				QuickSelectorController.this.txtArea.clear();
				// QuickSelectorController.this.barreProgression.setProgress(0);
				// QuickSelectorController.this.copy10Task.cancel(true);
				QuickSelectorController.this.copy10Task.cancel();
			}
		});
	}

	@FXML
	private void selectAndAct() throws IOException {

		List<Boolean> options = new ArrayList<Boolean>();
		options.add(this.suppression.isSelected());
		options.add(this.jourTravail.isSelected());
		options.add(this.semaineTravail.isSelected());

		options.stream().forEach(p -> System.out.println(p));
		System.out.println(QuickSelectorController.this.etqDestination.getText());

		if (options.get(0)) {
			this.supprTask = new SuppressionTaskFx(Paths.get(this.etqDestination.getText()));
			this.txtArea.clear();
			this.monTexte = "";
			QuickSelectorController.this.statutsEtq.setTextFill(Color.BLUE);

			// Unbind text property for Label.
			QuickSelectorController.this.statutsEtq.textProperty().unbind();

			// Bind the text property of Label
			// with message property of Task
			QuickSelectorController.this.statutsEtq.textProperty().bind(this.supprTask.messageProperty());

			// this.validerButton.setDisable(true);

			// Unbind progress property
			QuickSelectorController.this.barreProgression.progressProperty().unbind();
			QuickSelectorController.this.barreProgression.setProgress(0);
			QuickSelectorController.this.barreProgression.progressProperty().bind(this.supprTask.progressProperty());
			long begin = System.currentTimeMillis();
			this.supprTask.addEventFilter(WorkerStateEvent.WORKER_STATE_SUCCEEDED, //
					new EventHandler<WorkerStateEvent>() {
						@Override
						public void handle(WorkerStateEvent event) {
							List<Path> copied = QuickSelectorController.this.supprTask.getValue();
							QuickSelectorController.this.statutsEtq.textProperty().unbind();
							// QuickSelectorController.this.statutsEtq.setText("Copied: " + copied.size());
							copied.stream().forEachOrdered(p -> {
								QuickSelectorController.this.monTexte += "\n" + p.toString();
							});
							long end = System.currentTimeMillis();
							float time = (end - begin) / 1000f;
							QuickSelectorController.this.txtArea
									.setText("ALBUMS supprimes : " + QuickSelectorController.this.monTexte + " \n "
											+ "Temps d'execution suppression : " + String.valueOf(time));
						}

					});
			// Start the Task.
			new Thread(this.supprTask).start();
		}

		if (options.get(1)) {
			this.copy10Task = new Copy10AlbumsTaskFx(this.mesAlbums, Paths.get(this.etqDestination.getText()));
			this.txtArea.clear();
			this.monTexte = "";

			QuickSelectorController.this.statutsEtq.setTextFill(Color.BLUE);

			// Unbind text property for Label.
			QuickSelectorController.this.statutsEtq.textProperty().unbind();

			// Bind the text property of Label
			// with message property of Task
			QuickSelectorController.this.statutsEtq.textProperty().bind(this.copy10Task.messageProperty());

			// this.validerButton.setDisable(true);

			// Unbind progress property
			QuickSelectorController.this.barreProgression.progressProperty().unbind();
			QuickSelectorController.this.barreProgression.setProgress(0);
			QuickSelectorController.this.barreProgression.progressProperty().bind(this.copy10Task.progressProperty());
			long begin = System.currentTimeMillis();
			this.copy10Task.addEventFilter(WorkerStateEvent.WORKER_STATE_SUCCEEDED, //
					new EventHandler<WorkerStateEvent>() {
						@Override
						public void handle(WorkerStateEvent event) {
							List<Path> copied = QuickSelectorController.this.copy10Task.getValue();
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
			new Thread(this.copy10Task).start();
		}

		if (options.get(2)) {
			this.copy99Task = new Copy99AlbumsTasksFx(this.mesAlbums, Paths.get(this.etqDestination.getText()));
			this.txtArea.clear();
			this.monTexte = "";
			this.statutsEtq.setTextFill(Color.BLUE);

			// Unbind text property for Label.
			this.statutsEtq.textProperty().unbind();

			// Bind the text property of Label
			// with message property of Task
			this.statutsEtq.textProperty().bind(this.copy99Task.messageProperty());

			// this.validerButton.setDisable(true);

			// Unbind progress property
			this.barreProgression.progressProperty().unbind();
			this.barreProgression.setProgress(0);
			this.barreProgression.progressProperty().bind(this.copy99Task.progressProperty());
			long begin = System.currentTimeMillis();
			// copy99Task.addEventFilter(WorkerStateEvent.WORKER_STATE_SUCCEEDED, //
			this.copy99Task.addEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED, //
					new EventHandler<WorkerStateEvent>() {
						@Override
						public void handle(WorkerStateEvent event) {
							List<Path> copied = QuickSelectorController.this.copy99Task.getValue();
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

			new Thread(this.copy99Task).start();
//			System.out.println("Temps d'execution semaine de travail: " + String.valueOf(time));
		}
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
