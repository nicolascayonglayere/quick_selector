package mus.nico.quickselector.utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.UnsupportedAudioFileException;

import org.apache.tika.Tika;

import javazoom.spi.mpeg.sampled.file.MpegAudioFileReader;
import net.samuelcampos.usbdrivedetector.USBDeviceDetectorManager;

public class CopierMusique {

	private USBDeviceDetectorManager driveDetector;
	private Path maMusqiueChemin;
	private File monUSB;

	private Path cheminSce;
	private Path cheminDest;
	private List<Path> mesAlbums;
	private List<Boolean> options;
	private List<String> listeSuppr;
	private List<String> listeAlbumsAjoutes;
	private List<String> listeAlea;

	private boolean existence;

	// private CopyTaskFx copyTaskFx;

	public CopierMusique(Path pSce, Path pDest, List<Boolean> pOptions) {
		this.setCheminSce(pSce);
		this.setCheminDest(pDest);
		this.options = pOptions;
		this.mesAlbums = ListerArbo.listerAlbums();

	}

	public CopierMusique(Path pMusiqueChemin) {
		this.driveDetector = new USBDeviceDetectorManager();
		this.monUSB = this.driveDetector.getRemovableDevices().get(0).getRootDirectory();
		this.driveDetector.getRemovableDevices().forEach(System.out::println);
		this.maMusqiueChemin = pMusiqueChemin;
	}

	private long calculEspace(File pChemin) {
		long restant = pChemin.getTotalSpace();
		try {
			DirectoryStream<Path> stream = Files.newDirectoryStream(Paths.get(pChemin.getPath()));
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

	public void copierAlbum() {
		Long espaceRestant = this.calculEspace(this.monUSB);
		Random r = new Random();
		while (espaceRestant > 500000000) {
			int alea = r.nextInt(2019);
			try {
				String dossier = this.mesAlbums.get(alea).toString().substring(3);
				Files.createDirectories(Paths.get(this.monUSB.getPath(), dossier));
				System.out.println("CTRL sce " + this.mesAlbums.get(alea).toString() + " CTRL dest "
						+ Paths.get(this.monUSB.getPath(), dossier).toString());

				// copyTask.call();
				for (Path p : ListerArbo.listerChanson(this.mesAlbums.get(alea))) {
					// System.out.println(p);
					Files.copy(p, Paths.get(this.monUSB.getPath(), p.toString().substring(3)),
							StandardCopyOption.COPY_ATTRIBUTES);
				}

				espaceRestant = this.calculEspace(this.monUSB);
				System.out.println(
						"album copier : " + this.mesAlbums.get(alea).toString() + " espace restant " + espaceRestant);
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
	}

	public void copierAlbumJournee() {

		this.listeAlbumsAjoutes = new ArrayList<String>();
		// this.mesAlbums = ListerArbo.listerAlbums();
		System.out.println("CTRL------------------" + this.cheminDest.toString());
		Long espaceRestant = this.calculEspace(this.cheminDest.toFile());
		int compteur = 0;
		while (espaceRestant > 500000000 && compteur < 10) {
			try {
				String dossier = this.mesAlbums.get(this.tirageAleatoire(this.mesAlbums.size())).toString()
						.substring(3);
				String artiste = dossier.split("\\\\")[1];
				String genre = dossier.split("\\\\")[0];
				// System.out.println("CTRL-------artiste : " + artiste);
				for (String s : this.listeAlbumsAjoutes) {

					while (s.contains(artiste)) {
						dossier = this.mesAlbums.get(this.tirageAleatoire(this.mesAlbums.size())).toString()
								.substring(3);
					}
				}
				dossier = Paths.get(dossier).subpath(1, Paths.get(dossier).getNameCount()).toString();
				String dossierCopy = dossier.replaceAll("\\\\", "__");

				if (!(this.checkAlbumDestSource(this.cheminDest.resolve(dossierCopy).toString()))) {
					this.listeAlbumsAjoutes.add(dossierCopy);
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

					espaceRestant = this.calculEspace(this.cheminDest.toFile());
					compteur++;
				} else {
					continue;
				}

				System.out.println("album copier : " + dossier + " espace restant " + espaceRestant);
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
		System.out.println("FIN COPIE JOURNEE");
	}

	public void nettoyage() {
		this.listeSuppr = new ArrayList<String>();
		List<String> listInt = new ArrayList<String>();
		try (DirectoryStream<Path> stream = Files.newDirectoryStream(this.cheminDest)) {
			for (Path entry : stream) {
				System.out.println(entry);
				Files.walkFileTree(entry, new SimpleFileVisitor<Path>() {
					@Override
					public FileVisitResult visitFile(final Path dir, final BasicFileAttributes attrs)
							throws IOException {
						Files.deleteIfExists(dir);
						return super.visitFile(dir, attrs);
					}

					@Override
					public FileVisitResult postVisitDirectory(Path repo, IOException exc) throws IOException {
						// System.out.println("postVisitDirectory: " + dir);
						listInt.add(repo.toString());
						Files.deleteIfExists(repo);
						return super.postVisitDirectory(repo, exc);
					}
				});
			}
			for (int i = 0; i < listInt.size(); i++) {
				if (i % 3 == 0) {
					this.listeSuppr.add(listInt.get(i));
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("FIN NETTOYAGE");
	}

	public void listeAleatoire() throws IOException {
		this.listeAlea = new ArrayList<String>();
		List<Path> mesTitres = new ArrayList<Path>();
		long duree = 0;
		for (Path p : this.mesAlbums) {
			for (Path t : ListerArbo.listerChanson(p)) {
				mesTitres.add(t);
			}
		}
		while (duree < 7260000000L) {
			Path monTitreSelect = mesTitres.get(this.tirageAleatoire(mesTitres.size()));
			this.listeAlea.add(monTitreSelect.toString().substring(3));
			System.out.println("titre ajoute " + monTitreSelect.toString() + " " + monTitreSelect.toFile().exists());
			duree += this.getDureeTitre(monTitreSelect.toFile());
			System.out.println("duree cumulee-----------" + duree);

		}

	}

	private int tirageAleatoire(int borne) {
		Random r = new Random();
		int monTirage = r.nextInt(borne);
		return monTirage;
	}

	private boolean checkAlbumDestSource(String albumTest) {
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

	private long getDureeTitre(File titre) throws IOException {
		long duree = 0L;
		System.out.println(Files.readAttributes(titre.toPath(), "*"));
		Tika tika = new Tika();
		try {

			String mediaType = tika.detect(titre);
			System.out.println(mediaType);
		} catch (IOException e) {
			System.err.println(e.getMessage());
		}

		try {
			MpegAudioFileReader mp = new MpegAudioFileReader();
			AudioFileFormat format = mp.getAudioFileFormat(titre);
			System.out.println(format.properties().get("duration"));
			if (format.properties().get("duration") != null) {
				duree = (long) format.properties().get("duration");
			}
			System.out.println("duree-----------------" + duree);
			return duree;
		} catch (UnsupportedAudioFileException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return 0;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return 0;
		}

	}

	public USBDeviceDetectorManager getDriveDetector() {
		return this.driveDetector;
	}

	public void setDriveDetector(USBDeviceDetectorManager driveDetector) {
		this.driveDetector = driveDetector;
	}

	public Path getMaMusqiueChemin() {
		return this.maMusqiueChemin;
	}

	public void setMaMusqiueChemin(Path maMusqiueChemin) {
		this.maMusqiueChemin = maMusqiueChemin;
	}

	public File getMonUSB() {
		return this.monUSB;
	}

	public void setMonUSB(File monUSB) {
		this.monUSB = monUSB;
	}

	public Path getCheminSce() {
		return this.cheminSce;
	}

	public void setCheminSce(Path cheminSce) {
		this.cheminSce = cheminSce;
	}

	public Path getCheminDest() {
		return this.cheminDest;
	}

	public void setCheminDest(Path cheminDest) {
		this.cheminDest = cheminDest;
	}

	public List<Boolean> getOptions() {
		return this.options;
	}

	public void setOptions(List<Boolean> options) {
		this.options = options;
	}

	public List<String> getListeSuppr() {
		return this.listeSuppr;
	}

	public void setListeSuppr(List<String> listeSuppr) {
		this.listeSuppr = listeSuppr;
	}

	public List<String> getListeAlbumsAjoutes() {
		return this.listeAlbumsAjoutes;
	}

	public void setListeAlbumsAjoutes(List<String> listeAlbumsAjoutes) {
		this.listeAlbumsAjoutes = listeAlbumsAjoutes;
	}

	public List<String> getListeAlea() {
		return this.listeAlea;
	}

	public void setListeAlea(List<String> listeAlea) {
		this.listeAlea = listeAlea;
	}

//	public CopyTaskFx getCopyTaskFx() {
//		return this.copyTaskFx;
//	}
//
//	public void setCopyTaskFx(CopyTaskFx copyTaskFx) {
//		this.copyTaskFx = copyTaskFx;
//	}

}
