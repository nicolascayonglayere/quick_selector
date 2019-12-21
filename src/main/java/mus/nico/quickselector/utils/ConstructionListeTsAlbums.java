package mus.nico.quickselector.utils;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import mus.nico.quickselector.fx.model.Album;
import mus.nico.quickselector.fx.model.Artiste;

public class ConstructionListeTsAlbums {

	public static List<Album> construireListe(Path cheminSource) throws IOException {
		List<Album> mesAlbums = new ArrayList<>();
		try (DirectoryStream<Path> stream = Files.newDirectoryStream(cheminSource)) {
			stream.forEach(p -> {
				if (Files.isDirectory(p) && (!p.startsWith("H:\\$RECYCLE.BIN")) && (!p.startsWith("H:\\Mes sélections"))
						&& (!p.startsWith("H:\\Playlists")) && (!p.startsWith("H:\\System Volume Information"))) {
					try {
						Files.walk(p, FileVisitOption.FOLLOW_LINKS).forEach(pa -> {
							try {
								if (pa.toFile().isDirectory() // && pa.getNameCount() > 2
										&& !ContenuDossierChecker.contientDossier(pa)
										&& ContenuDossierChecker.contientChanson(pa)) {
									// System.out.println(pa);
									mesAlbums.add(new Album(pa.getFileName().toString(), pa));
								}
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}

						});
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			});
		} catch (IOException e) {
			e.printStackTrace();
		}

//		ListerArbo.listerAlbums().stream().forEachOrdered(p -> {
//			try {
//				// System.out.println(p);
//				Files.newDirectoryStream(p).forEach(pa -> {
//					if (pa.toFile().isDirectory() && pa.getNameCount() > 2) {
//						mesAlbums.add(new Album(pa.getFileName().toString(), pa));
//					} else {
//						mesAlbums.add(new Album(p.getFileName().toString(), p));
//
//					}
//				});
//
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		});
//
//		Set<Album> set = new HashSet<>();
//		set.addAll(mesAlbums);
//		List<Album> albums = new ArrayList<>(set);

//		mesAlbums.stream().forEachOrdered(a -> {
//
//			ListerArbo.listerChanson(a.getChemin()).stream().forEach(p -> {
//				a.addChanson(new Chanson(p.getFileName().toString(), p, a));
//				poids += p.toFile().length();
//			});
////			try {
////				Files.newDirectoryStream(a.getChemin()).forEach(p -> {
////					a.addChanson(new Chanson(p.getFileName().toString(), p, a));
////					poids += p.toFile().length();
////				});
////			} catch (IOException e) {
////				// TODO Auto-generated catch block
////				e.printStackTrace();
////			}
//			a.setPoids(poids);
//			System.out.println(a.toString());
//			poids = 0;
//		});

		return mesAlbums;
	}

	public static Artiste identificationArtiste(Path cheminAlbum) throws IOException {
		Artiste monArtiste = new Artiste();
		List<Album> albumArtiste = new ArrayList<>();

		if (cheminAlbum.getNameCount() > 2) {
			monArtiste.setNom(cheminAlbum.getName(1).toString());
			monArtiste.setChemin(Paths.get(cheminAlbum.getRoot().toString(), cheminAlbum.subpath(0, 2).toString()));
			try {
				Files.walk(monArtiste.getChemin(), FileVisitOption.FOLLOW_LINKS).forEach(pa -> {
					try {
						if (pa.toFile().isDirectory() // && pa.getNameCount() > 2
								&& !ContenuDossierChecker.contientDossier(pa)) {
							// System.out.println(pa);
							albumArtiste.add(new Album(pa.getFileName().toString(), pa));
						}
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				});
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			// System.out.println(cheminAlbum.resolveSibling(cheminAlbum.getName(2)));
//			Files.newDirectoryStream(monArtiste.getChemin()).forEach(p -> {
//				if (p.toFile().isDirectory()) {
//					albumArtiste.add(new Album(p.getFileName().toString(), p));
//				}
//			});
			monArtiste.setAlbums(albumArtiste);
		} else {
			monArtiste.setNom("");
			monArtiste.setChemin(cheminAlbum);
			monArtiste.addAlbum(new Album(cheminAlbum.getFileName().toString(), cheminAlbum));
//			monArtiste.setAlbums(
//					new ArrayList<>(Arrays.asList(new Album(cheminAlbum.getFileName().toString(), cheminAlbum))));
		}

//		if (cheminAlbum.getNameCount() > 2) {
//			//monArtiste.setNom(cheminAlbum.getParent().getFileName().toString());
////			if(!ContenuDossierChecker.contientFichiers(cheminAlbum.getParent())) {
////				
////			}
//			monArtiste.setChemin(cheminAlbum.getParent());
//
//			Files.newDirectoryStream(cheminAlbum.getParent()).forEach(p -> {
//				if (p.toFile().isDirectory()) {
//					albumArtiste.add(new Album(p.getFileName().toString(), p));
//				}
//			});
//			monArtiste.setAlbums(albumArtiste);
//		} else {
//			monArtiste.setNom("");
//			monArtiste.setChemin(cheminAlbum);
//			monArtiste.setAlbums(
//					new ArrayList<>(Arrays.asList(new Album(cheminAlbum.getFileName().toString(), cheminAlbum))));
//		}

		// System.out.println(monArtiste.toString());
		return monArtiste;
	}

}
