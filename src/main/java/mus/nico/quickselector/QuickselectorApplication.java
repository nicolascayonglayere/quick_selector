package mus.nico.quickselector;

import java.io.IOException;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import mus.nico.quickselector.fx.view.QuickSelectorController;

@SpringBootApplication
public class QuickselectorApplication extends Application {

	/**
	 * Version swing
	 * 
	 * @param args
	 */
//	public static void main(String[] args) {
//		// SpringApplication.run(QuickselectorApplication.class, args);
//		SpringApplicationBuilder builder = new SpringApplicationBuilder(QuickselectorApplication.class);
//
//		builder.headless(false);
//
//		// ConfigurableApplicationContext context = builder.run(args);
//
//		MaFenetre mf = new MaFenetre();
//		mf.setVisible(true);
//	}

	/**
	 * Version JavaFx
	 */
	private ConfigurableApplicationContext context;
	private Stage primaryStage;
	private BorderPane rootLayout;

	@Override
	public void init() throws Exception {
		SpringApplicationBuilder builder = new SpringApplicationBuilder(QuickselectorApplication.class);
		this.context = builder.run(this.getParameters().getRaw().toArray(new String[0]));

		FXMLLoader loader = new FXMLLoader(this.getClass().getResource("fx/view/RootLayout.fxml"));
		loader.setControllerFactory(this.context::getBean);
		this.rootLayout = loader.load();
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		this.primaryStage = primaryStage;
		this.primaryStage.setTitle("QuickSelector");

		this.initRootLayout();

		this.showQuickSelectorOverview();
	}

	public void initRootLayout() {
		// Show the scene containing the root layout.
		Scene scene = new Scene(this.rootLayout);
		this.primaryStage.setScene(scene);
		this.primaryStage.show();
	}

	public void showQuickSelectorOverview() throws NoSuchMethodException, SecurityException {
		try {
			// Load person overview.
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(QuickselectorApplication.class.getResource("fx/view/QuickSelectorOverView.fxml"));
			AnchorPane qsOverview = (AnchorPane) loader.load();

			// Set person overview into the center of root layout.
			this.rootLayout.setCenter(qsOverview);

			// Give the controller access to the main app.
			QuickSelectorController spc = loader.getController();

			spc.setMainApp(this);
			spc.init();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public Stage getPrimaryStage() {
		return this.primaryStage;
	}

	@Override
	public void stop() throws Exception {
		this.context.close();
	}

}
