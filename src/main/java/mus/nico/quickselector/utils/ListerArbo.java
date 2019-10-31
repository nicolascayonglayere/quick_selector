package mus.nico.quickselector.utils;

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

	protected static List<Path> listerGenre() {
		List<Path> genres = new ArrayList<Path>();
		try (DirectoryStream<Path> stream = Files.newDirectoryStream(cheminMusique)) {
			stream.forEach(p -> {
				if (Files.isDirectory(p) && (!p.startsWith("H:\\$RECYCLE.BIN")) && (!p.startsWith("H:\\Mes sélections"))
						&& (!p.startsWith("H:\\Playlists")) && (!p.startsWith("H:\\System Volume Information"))) {
					genres.add(p);
				}
			});
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("nb de genres :" + genres.size());

		return genres;

	}

	public static List<Path> listerGroupe() {
		List<Path> groupes = new ArrayList<Path>();
		ListerArbo.listerGenre().stream().forEachOrdered(pa -> {
			try (DirectoryStream<Path> stream = Files.newDirectoryStream(pa)) {
				stream.forEach(p -> {
					groupes.add(p);
				});
			} catch (IOException e) {
				e.printStackTrace();
			}
		});
		return groupes;
	}

	public static List<Path> listerGroupe(Path pGenre) {
		List<Path> groupes = new ArrayList<Path>();

		try (DirectoryStream<Path> stream = Files.newDirectoryStream(pGenre)) {
			stream.forEach(p -> {
				groupes.add(p);
			});
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("nb de groupes " + groupes.size() + " du genre " + pGenre);
		return groupes;
	}

	public static List<Path> listerAlbums() {
		List<Path> albums = new ArrayList<Path>();
		ListerArbo.listerGroupe().stream().forEachOrdered(pa -> {
			try (DirectoryStream<Path> stream = Files.newDirectoryStream(pa)) {
				stream.forEach(p -> {
					if (Files.isDirectory(p)) {
						albums.addAll(ListerArbo.listerCDAlbum(p));
					} else {
						albums.add(pa);
					}
				});
			} catch (IOException e) {
				e.printStackTrace();
			}
		});
		return albums;
	}

	protected static List<Path> listerCDAlbum(Path pAlbum) {
		List<Path> cdAlbum = new ArrayList<>();
		try (DirectoryStream<Path> stream = Files.newDirectoryStream(pAlbum)) {
			stream.forEach(p -> {
				if (Files.isRegularFile(p)) {
					cdAlbum.add(p.getParent());
				} else {
					cdAlbum.add(p);
				}
			});
		} catch (IOException e) {
			e.printStackTrace();
		}
		return cdAlbum;
	}

	public static List<Path> listerChanson(Path pAlbum) {
		List<Path> chansons = new ArrayList<Path>();
		try (DirectoryStream<Path> stream = Files.newDirectoryStream(pAlbum)) {
			stream.forEach(p -> {
				if (Files.isRegularFile(p)) {
					String vChemin = p.toString();
					String ext = vChemin.substring(vChemin.lastIndexOf(".")); // récupère l'extention
					if (ext.equalsIgnoreCase(".mp3") || ext.equalsIgnoreCase(".flac") || ext.equalsIgnoreCase(".wma")
							|| ext.equalsIgnoreCase(".m3u") || ext.equalsIgnoreCase(".m4a")) {
						chansons.add(p);
					}
				}
			});
		} catch (IOException e) {
			e.printStackTrace();
		}
		// System.out.println("nb de chansons " + chansons.size() + " dans l'album " +
		// pAlbum);
		return chansons;
	}

}
