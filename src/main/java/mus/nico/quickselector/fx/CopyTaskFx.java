package mus.nico.quickselector.fx;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import javafx.concurrent.Task;
import mus.nico.quickselector.utils.CalculEspace;
import mus.nico.quickselector.utils.ListerArbo;

public class CopyTaskFx extends Task<List<Path>> {

	private Path cheminSce;
	private Path cheminDest;
	private List<Path> mesAlbums = ListerArbo.listerAlbums();
	private boolean existence;

	public CopyTaskFx(Path cheminSce, Path cheminDest) {
		this.cheminSce = cheminSce;
		this.cheminDest = cheminDest;
	}

	@Override
	protected List<Path> call() throws Exception {

		List<String> listeAlbumsAjoutes = new ArrayList<String>();
		// this.mesAlbums = ListerArbo.listerAlbums();
		System.out.println("CTRL------------------" + this.cheminDest.toString());
		Long espaceRestant = CalculEspace.calcul(this.cheminDest);
		int compteur = 0;
		while (espaceRestant > 500000000 && compteur < 10) {
			try {
				String dossier = this.mesAlbums.get(this.tirageAleatoire(this.mesAlbums.size())).toString()
						.substring(3);
				String artiste = dossier.split("\\\\")[1];
				String genre = dossier.split("\\\\")[0];
				// System.out.println("CTRL-------artiste : " + artiste);
				for (String s : listeAlbumsAjoutes) {

					while (s.contains(artiste)) {
						dossier = this.mesAlbums.get(this.tirageAleatoire(this.mesAlbums.size())).toString()
								.substring(3);
					}
				}
				dossier = Paths.get(dossier).subpath(1, Paths.get(dossier).getNameCount()).toString();
				String dossierCopy = dossier.replaceAll("\\\\", "__");

				if (!(this.checkAlbumDestcheminSce(this.cheminDest.resolve(dossierCopy).toString()))) {
					listeAlbumsAjoutes.add(dossierCopy);
					Files.createDirectory(this.cheminDest.resolve(dossierCopy));

					System.out.println(
							"CTRL sce " + Paths.get(this.cheminSce.toString(), genre + "\\" + dossier).toString()
									+ " CTRL dest " + Paths.get(this.cheminDest.toString(), dossierCopy).toString());

					for (Path p : ListerArbo
							.listerChanson(Paths.get(this.cheminSce.toString(), genre + "\\" + dossier))) {
						Files.copy(p,
								Paths.get(this.cheminDest.resolve(dossierCopy).toString(), p.getFileName().toString()),
								StandardCopyOption.COPY_ATTRIBUTES);

					}
					this.copy(dossierCopy);
					espaceRestant = CalculEspace.calcul(this.cheminDest);
					compteur++;
					this.updateProgress(compteur, 10);
				} else {
					continue;
				}

				System.out.println("album copier : " + dossier + " espace restant " + espaceRestant);
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
		System.out.println("FIN COPIE JOURNEE");

		return listeAlbumsAjoutes.stream().map(s -> Paths.get(s)).collect(Collectors.toList());
	}

	private void copy(String nomAlbum) throws Exception {
		this.updateMessage("Copying: " + nomAlbum);
	}

	private int tirageAleatoire(int borne) {
		Random r = new Random();
		int monTirage = r.nextInt(borne);
		return monTirage;
	}

	private boolean checkAlbumDestcheminSce(String albumTest) {
		try (DirectoryStream<Path> stream = Files.newDirectoryStream(this.cheminDest)) {
			for (Path p : stream) {
				// System.out.println("CTRL existance " + albumTest + " -- " + p);
				if (albumTest.equalsIgnoreCase(p.toString())) {
					this.existence = true;
					break;
				} else {
					this.existence = false;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return this.existence;
	}

}
