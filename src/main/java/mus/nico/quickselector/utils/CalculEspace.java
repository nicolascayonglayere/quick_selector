package mus.nico.quickselector.utils;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;

public class CalculEspace {

	public static long calcul(Path chemin) {
		long restant = chemin.toFile().getTotalSpace();
		try {
			DirectoryStream<Path> stream = Files.newDirectoryStream(chemin);
			for (Path entry : stream) {

				long size = Files.walk(entry).filter(p -> p.toFile().isFile()).mapToLong(p -> p.toFile().length())
						.sum();
				restant -= size;
				// System.out.println("taille repo " + entry + " " + size);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return restant;
	}

}
