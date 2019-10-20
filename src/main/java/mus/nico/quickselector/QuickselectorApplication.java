package mus.nico.quickselector;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

import mus.nico.quickselector.gui.MaFenetre;

@SpringBootApplication
public class QuickselectorApplication {

	// private static String repoMusique = "H:\\";
	// private static Path cheminMusique = Paths.get(repoMusique);

	public static void main(String[] args) {
		// SpringApplication.run(QuickselectorApplication.class, args);
		SpringApplicationBuilder builder = new SpringApplicationBuilder(QuickselectorApplication.class);

		builder.headless(false);

		// ConfigurableApplicationContext context = builder.run(args);

		// for (Path p : ListerArbo.listerGenre()) {
		// // ListerArbo.listerGroupe(p);
		// for (Path pA : ListerArbo.listerGroupe(p)) {
		// // ListerArbo.listerAlbum(pA);
		// for (Path pT : ListerArbo.listerAlbum(pA)) {
		// ListerArbo.listerChanson(pT);
		// }
		// }
		// }
		// CopierMusique cm = new CopierMusique(cheminMusique);
		// cm.copierAlbum();
		MaFenetre mf = new MaFenetre();
		mf.setVisible(true);

		// new USBDeviceDetectorManager();

	}

}
