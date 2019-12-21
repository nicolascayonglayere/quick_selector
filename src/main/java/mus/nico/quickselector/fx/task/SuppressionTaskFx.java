package mus.nico.quickselector.fx.task;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javafx.concurrent.Task;

public class SuppressionTaskFx extends Task<List<Path>> {

	private Path cheminDest;
	private List<String> listeSuppr;

	private int compteur;

	public SuppressionTaskFx(Path cheminDest) {
		this.cheminDest = cheminDest;

	}

	@Override
	public List<Path> call() throws Exception {
		this.listeSuppr = new ArrayList<String>();
		this.compteur = 0;
		List<String> listInt = new ArrayList<String>();
		try (DirectoryStream<Path> stream = Files.newDirectoryStream(this.cheminDest)) {
			for (Path entry : stream) {
				System.out.println(entry);
				Files.walkFileTree(entry, new SimpleFileVisitor<Path>() {
					@Override
					public FileVisitResult visitFile(final Path dir, final BasicFileAttributes attrs)
							throws IOException {
						Files.deleteIfExists(dir);
						// updateMessage("Suppression : "+dir.toString());
						return super.visitFile(dir, attrs);
					}

					@Override
					public FileVisitResult postVisitDirectory(Path repo, IOException exc) throws IOException {
						// System.out.println("postVisitDirectory: " + dir);
						listInt.add(repo.toString());
						Files.deleteIfExists(repo);

						SuppressionTaskFx.this.compteur++;
						SuppressionTaskFx.this.updateProgress(SuppressionTaskFx.this.compteur,
								SuppressionTaskFx.this.cheminDest.toFile().listFiles().length - 1);
						return super.postVisitDirectory(repo, exc);
					}
				});
			}

			for (int i = 0; i < listInt.size(); i++) {
				if (i % 3 == 0) {
					this.listeSuppr.add(listInt.get(i));
				}
			}
			this.updateMessage("Suppression de " + listInt.size() + " albums");
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("FIN NETTOYAGE");
		return this.listeSuppr.stream().map(s -> Paths.get(s)).collect(Collectors.toList());
	}
}
