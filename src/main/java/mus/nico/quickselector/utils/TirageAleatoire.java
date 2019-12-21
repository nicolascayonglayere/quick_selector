package mus.nico.quickselector.utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import mus.nico.quickselector.fx.model.Album;
import mus.nico.quickselector.fx.model.Artiste;

public class TirageAleatoire {

	public static List<Album> tirageAleatoire(int nbElement, List<Album> maMusique) throws IOException {
		List<Album> monTirage = new ArrayList<>();
		Random rand = new Random();

		for (int i = 0; i < nbElement; i++) {
			int randomIndex = rand.nextInt(maMusique.size());
			Artiste monArtisteRemove = ConstructionListeTsAlbums
					.identificationArtiste(maMusique.get(randomIndex).getChemin());

			monTirage.add(maMusique.get(randomIndex));

			monArtisteRemove.getAlbums().stream().forEachOrdered(a -> {
				for (int j = 0; j < maMusique.size(); j++) {
					if (maMusique.get(j).getChemin().getParent().toString()
							.equalsIgnoreCase(a.getChemin().getParent().toString())) {
						maMusique.remove(j);
						break;
					}
				}
			});

//			System.out.println(
//					"ARTISTE ENLEVE " + monArtisteRemove.toString() + "- -" + monArtisteRemove.getAlbums().size());

			// System.out.println(maMusique.size());
		}
		return monTirage;
	}
}
