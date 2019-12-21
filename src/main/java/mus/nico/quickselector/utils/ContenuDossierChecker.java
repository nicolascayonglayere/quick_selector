package mus.nico.quickselector.utils;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;

public class ContenuDossierChecker {

	private static boolean chanson;

	public static boolean contientDossier(Path cheminDossier) throws IOException {
		return new ArrayList<>(Arrays.asList(cheminDossier.toFile().listFiles())).stream().filter(f -> f.isDirectory())
				.findFirst().isPresent();
	}

	public static boolean contientChanson(Path cheminDossier) {
		try (DirectoryStream<Path> stream = Files.newDirectoryStream(cheminDossier)) {
			stream.forEach(p -> {
				if (Files.isRegularFile(p) && ContenuDossierChecker.isChanson(p)) {
					chanson = true;
				}
			});
		} catch (IOException e) {
			e.printStackTrace();
		}
		// System.out.println("nb de chansons " + chansons.size() + " dans l'album " +
		// pAlbum);
		return chanson;
	}

	public static boolean isChanson(Path chemFichier) {
		String vChemin = chemFichier.toString();
		String ext = vChemin.substring(vChemin.lastIndexOf(".")); // récupère l'extention
		return (ext.equalsIgnoreCase(".mp3") || ext.equalsIgnoreCase(".flac") || ext.equalsIgnoreCase(".wma")
				|| ext.equalsIgnoreCase(".m3u") || ext.equalsIgnoreCase(".m4a"));
	}
}
