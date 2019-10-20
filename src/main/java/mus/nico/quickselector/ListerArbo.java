package mus.nico.quickselector;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class ListerArbo {

	private static String repoMusique = "H:\\";
	private static Path cheminMusique = Paths.get(repoMusique);

	// public GestionFlux() { }

	protected static List<Path> listerGenre() {
		List<Path> genres = new ArrayList<Path>();
		try {
			DirectoryStream<Path> stream = Files.newDirectoryStream(cheminMusique);
			for (Path entry : stream) {
				// System.out.println(entry);
				if (!Files.isRegularFile(entry) && (!entry.startsWith("H:\\$RECYCLE.BIN"))
						&& (!entry.startsWith("H:\\Mes sélections")) && (!entry.startsWith("H:\\Playlists"))
						&& (!entry.startsWith("H:\\System Volume Information"))) {
					genres.add(entry);
				}

			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("nb de genres :" + genres.size());

		return genres;

	}

	public static List<Path> listerGroupe() {
		List<Path> groupes = new ArrayList<Path>();
		for (Path p : listerGenre()) {
			try {
				DirectoryStream<Path> stream = Files.newDirectoryStream(p);
				for (Path entry : stream) {
					// System.out.println(entry);
					groupes.add(entry);
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return groupes;
	}

	public static List<Path> listerGroupe(Path pGenre) {
		List<Path> groupes = new ArrayList<Path>();

		try {
			DirectoryStream<Path> stream = Files.newDirectoryStream(pGenre);
			for (Path entry : stream) {
				// System.out.println(entry);
				groupes.add(entry);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("nb de groupes " + groupes.size() + " du genre " + pGenre);
		return groupes;
	}

	protected static List<Path> listerAlbums() {
		List<Path> albums = new ArrayList<Path>();
		for (Path p : listerGroupe()) {
			try {
				DirectoryStream<Path> stream = Files.newDirectoryStream(p);
				for (Path entry : stream) {
					// System.out.println(entry);
					if (!Files.isRegularFile(entry)) {
						albums.add(entry);
					}
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return albums;
	}

	protected static List<Path> listerAlbum(Path pGroupe) {
		List<Path> albums = new ArrayList<Path>();

		try {
			DirectoryStream<Path> stream = Files.newDirectoryStream(pGroupe);
			for (Path entry : stream) {
				// System.out.println(entry);
				if (!Files.isRegularFile(entry)) {
					albums.add(entry);
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("nb d'album " + albums.size() + " du groupe " + pGroupe);
		return albums;
	}

	protected static List<Path> listerChanson(Path pAlbum) {
		List<Path> chansons = new ArrayList<Path>();

		try {
			DirectoryStream<Path> stream = Files.newDirectoryStream(pAlbum);
			for (Path entry : stream) {
				// System.out.println(entry);
				if (Files.isRegularFile(entry)) {
					String vChemin = entry.toString();
					String ext = vChemin.substring(vChemin.lastIndexOf(".")); // récupère l'extention
					if (ext.equalsIgnoreCase(".mp3") || ext.equalsIgnoreCase(".flac") || ext.equalsIgnoreCase(".wma")
							|| ext.equalsIgnoreCase(".m3u") || ext.equalsIgnoreCase(".m4a")) {
						chansons.add(entry);
					}
				}

			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("nb de chansons " + chansons.size() + " dans l'album " + pAlbum);
		return chansons;
	}

}
