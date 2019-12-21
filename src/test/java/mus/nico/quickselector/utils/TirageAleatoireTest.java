package mus.nico.quickselector.utils;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import mus.nico.quickselector.fx.model.Album;

public class TirageAleatoireTest {

	private List<Album> maMusiqueTemoin;

	@Before
	public void setUp() throws IOException {
		this.maMusiqueTemoin = new ArrayList<>();

		this.maMusiqueTemoin = ConstructionListeTsAlbums.construireListe(Paths.get("H:\\"));
	}

	@Test
	public void whenGiven10ElementAndListAlbum_thenReturnListAlbum() throws IOException {
		List<Album> listeTest = TirageAleatoire.tirageAleatoire(10, this.maMusiqueTemoin);
		listeTest.stream().forEach(a -> System.out.println(a.toString()));
		Assert.assertTrue(listeTest.size() == 10);
	}

	@Test
	public void whenGiven99ElementAndListAlbum_thenReturnListAlbum() throws IOException {
		List<Album> listeTest = TirageAleatoire.tirageAleatoire(99, this.maMusiqueTemoin);
		Assert.assertTrue(listeTest.size() == 99);
	}

}
