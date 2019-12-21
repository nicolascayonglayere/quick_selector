package mus.nico.quickselector.utils;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import mus.nico.quickselector.fx.model.Album;
import mus.nico.quickselector.fx.model.Artiste;

public class ConstructionListeTsAlbumsTest {

	private String chemTemoinFacile = "H:\\Electro\\Alif Tree\\French Cuisine";
	private String chemTemoinDifficile1 = "H:\\Electro\\A Hundred Effects (Jarring Effects [FX100])";
	private String chemTemoinDifficile2 = "H:\\Musique française\\Bobby Lapointe - L'Intégrale Cd1 & Cd2\\CD1";

	private Artiste artTemoinFacile;
	private Album albumArtFacile1;

	private Artiste artTemoinDifficile1;
	private Album albumArtDiff1;

	private Artiste artTemoinDifficile2;
	private Album album1ArtDiff2;
	private Album album2ArtDiff2;

	@Before
	public void setUp() {
		this.artTemoinFacile = new Artiste("Alif Tree");
		this.artTemoinFacile.setChemin(Paths.get("H:\\Electro\\Alif Tree"));
		this.albumArtFacile1 = new Album("French Cuisine", Paths.get(this.chemTemoinFacile));
		this.artTemoinFacile.addAlbum(this.albumArtFacile1);

		this.artTemoinDifficile1 = new Artiste("");
		this.artTemoinDifficile1.setChemin(Paths.get(this.chemTemoinDifficile1));
		this.albumArtDiff1 = new Album("A Hundred Effects (Jarring Effects [FX100])",
				Paths.get(this.chemTemoinDifficile1));
		this.artTemoinDifficile1.addAlbum(this.albumArtDiff1);

		this.artTemoinDifficile2 = new Artiste("Bobby Lapointe - L'Intégrale Cd1 & Cd2");
		this.artTemoinDifficile2.setChemin(Paths.get("H:\\Musique française\\Bobby Lapointe - L'Intégrale Cd1 & Cd2"));
		this.album1ArtDiff2 = new Album("CD1", Paths.get(this.chemTemoinDifficile2));
		this.album2ArtDiff2 = new Album("CD2",
				Paths.get("H:\\Musique française\\Bobby Lapointe - L'Intégrale Cd1 & Cd2\\CD2"));
		this.artTemoinDifficile2.addAlbum(this.album1ArtDiff2);
		this.artTemoinDifficile2.addAlbum(this.album2ArtDiff2);
	}

	@Test
	public void whenGivenPath_thenReturnArtiste() throws IOException {
		Assert.assertTrue(this.artTemoinFacile
				.equals(ConstructionListeTsAlbums.identificationArtiste(Paths.get(this.chemTemoinFacile))));

		System.out.println("CTRL TEST TEMOIN " + this.artTemoinDifficile1.toString());
		System.out.println("CTRL TEST "
				+ ConstructionListeTsAlbums.identificationArtiste(Paths.get(this.chemTemoinDifficile1)).toString());
		Assert.assertTrue(this.artTemoinDifficile1
				.equals(ConstructionListeTsAlbums.identificationArtiste(Paths.get(this.chemTemoinDifficile1))));

		Assert.assertTrue(this.artTemoinDifficile2
				.equals(ConstructionListeTsAlbums.identificationArtiste(Paths.get(this.chemTemoinDifficile2))));
	}

	@Test
	public void whenGivenNothing_thenReturnListAllAlbum() throws IOException {
		List<Album> mesAlbums = ConstructionListeTsAlbums.construireListe(Paths.get("H:\\"));
		System.out.println(mesAlbums.size());
//		for (int i = 0; i < mesAlbums.size(); i++) {
//			System.out.println(mesAlbums.get(i));
//		}
	}
}
