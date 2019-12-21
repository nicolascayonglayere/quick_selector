package mus.nico.quickselector.fx;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import mus.nico.quickselector.fx.model.Album;
import mus.nico.quickselector.fx.task.Copy99AlbumsTasksFx;
import mus.nico.quickselector.utils.ConstructionListeTsAlbums;

public class Copy99AlbumsTasksFxITTest {

	private String cheminSce = "H:\\";
	private String cheminDest = "I:\\temp";

	private Copy99AlbumsTasksFx copyTask;

	@Before
	public void setUp() throws IOException {
		List<Album> mesAlbums = ConstructionListeTsAlbums.construireListe(Paths.get(this.cheminSce));
		this.copyTask = new Copy99AlbumsTasksFx(mesAlbums, Paths.get(this.cheminDest));
	}

	@Test
	public void whenGivenNothing_thenCopyAndReturnListPath() throws Exception {
		List<Path> mesAlbumsCopies = this.copyTask.call();

		Assert.assertTrue(mesAlbumsCopies.size() == 99);
	}

}
