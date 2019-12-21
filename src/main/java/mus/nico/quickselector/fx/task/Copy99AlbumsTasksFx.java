package mus.nico.quickselector.fx.task;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javafx.concurrent.Task;
import mus.nico.quickselector.fx.model.Album;
import mus.nico.quickselector.utils.CalculEspace;
import mus.nico.quickselector.utils.ContenuDossierChecker;
import mus.nico.quickselector.utils.TirageAleatoire;

public class Copy99AlbumsTasksFx extends Task<List<Path>> {

	private Path cheminDest;
	private List<Album> mesAlbums;

	public Copy99AlbumsTasksFx(List<Album> mesAlbums, Path cheminDest) throws IOException {
		this.mesAlbums = mesAlbums;
		this.cheminDest = cheminDest;
		// System.out.println("CTRL CONSTRUCTION TACHE ------------------------------
		// ");
	}

	@Override
	public List<Path> call() throws Exception {
		System.out.println("DEMARRAGE TASK ------------------------ ");
		List<String> listeAlbumsAjoutes = new ArrayList<String>();

		List<Album> monTirage = TirageAleatoire.tirageAleatoire(99, this.mesAlbums);
		for (int i = 0; i < monTirage.size(); i++) {
			if (CalculEspace.calcul(this.cheminDest) > 500000000) {
				String dossierCopy = this.constructionDossierDestination(monTirage.get(i));
				listeAlbumsAjoutes.add(dossierCopy);
				Files.createDirectory(this.cheminDest.resolve(dossierCopy));
				Files.newDirectoryStream(monTirage.get(i).getChemin()).forEach(p -> {
					try {
						if (ContenuDossierChecker.isChanson(p)) {
							Files.copy(p, this.cheminDest.resolve(dossierCopy + "\\" + p.getFileName()),
									StandardCopyOption.COPY_ATTRIBUTES);
						}
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				});
				this.copy(dossierCopy);
				this.updateProgress(i, monTirage.size() - 1);

			} else {
				break;
			}

		}
		System.out.println("FIN COPIE JOURNEE");

		return listeAlbumsAjoutes.stream().map(s -> Paths.get(s)).collect(Collectors.toList());
	}

	private void copy(String nomAlbum) throws Exception {
		this.updateMessage("Copying: " + nomAlbum);
	}

	private String constructionDossierDestination(Album album) {
		String dossierCopy;
		if (album.getChemin().getParent().getNameCount() > 2) {
			dossierCopy = album.getChemin().getParent().getParent().getFileName() + "__"
					+ album.getChemin().getParent().getFileName() + "_" + album.getNom();
		} else if (album.getChemin().getParent().getNameCount() == 2) {
			dossierCopy = album.getChemin().getParent().getFileName() + "__" + album.getNom();
		} else {
			dossierCopy = "__" + album.getNom();
		}
		return dossierCopy;
	}
}
