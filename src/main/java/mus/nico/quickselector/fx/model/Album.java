package mus.nico.quickselector.fx.model;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class Album {

	private String nom;
	// private Artiste artiste;
	private List<Chanson> titres;
	private Path chemin;
	private int poids;

	public Album() {
	}

	public Album(String nom, Path chemin) {
		// this.artiste = artiste;
		this.nom = nom;
		this.chemin = chemin;
	}

	public String getNom() {
		return this.nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

//	public Artiste getArtiste() {
//		return this.artiste;
//	}
//
//	public void setArtiste(Artiste artiste) {
//		this.artiste = artiste;
//	}

	public List<Chanson> getTitres() {
		return this.titres;
	}

	public void setTitres(List<Chanson> titres) {
		this.titres = titres;
	}

	public void addChanson(Chanson chanson) {
		if (this.titres == null) {
			this.titres = new ArrayList<>();
		}
		this.titres.add(chanson);
	}

	public Path getChemin() {
		return this.chemin;
	}

	public void setChemin(Path chemin) {
		this.chemin = chemin;
	}

	public int getPoids() {
		return this.poids;
	}

	public void setPoids(int poids) {
		this.poids = poids;
	}

	@Override
	public String toString() {
		return "Album [nom=" + this.nom + ", titres=" + this.titres + ", chemin=" + this.chemin + ", poids="
				+ this.poids + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((this.chemin == null) ? 0 : this.chemin.hashCode());
		result = prime * result + ((this.nom == null) ? 0 : this.nom.hashCode());
		result = prime * result + this.poids;
		result = prime * result + ((this.titres == null) ? 0 : this.titres.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (this.getClass() != obj.getClass())
			return false;
		Album other = (Album) obj;
		if (this.chemin == null) {
			if (other.chemin != null)
				return false;
		} else if (!this.chemin.equals(other.chemin))
			return false;
		if (this.nom == null) {
			if (other.nom != null)
				return false;
		} else if (!this.nom.equals(other.nom))
			return false;
		if (this.poids != other.poids)
			return false;
		if (this.titres == null) {
			if (other.titres != null)
				return false;
		} else if (!this.titres.equals(other.titres))
			return false;
		return true;
	}

}
