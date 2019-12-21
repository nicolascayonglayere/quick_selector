package mus.nico.quickselector.fx.model;

import java.nio.file.Path;

public class Chanson {

	private String nom;
	private Path chemin;
	private Album album;

	public Chanson() {
	}

	public Chanson(String nom, Path chemin, Album album) {
		this.nom = nom;
		this.chemin = chemin;
		this.album = album;
	}

	public String getNom() {
		return this.nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public Path getChemin() {
		return this.chemin;
	}

	public void setChemin(Path chemin) {
		this.chemin = chemin;
	}

	public Album getAlbum() {
		return this.album;
	}

	public void setAlbum(Album album) {
		this.album = album;
	}

	@Override
	public String toString() {
		return "Chanson [nom=" + this.nom + ", chemin=" + this.chemin + "]";// , album=" + this.album + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((this.album == null) ? 0 : this.album.hashCode());
		result = prime * result + ((this.chemin == null) ? 0 : this.chemin.hashCode());
		result = prime * result + ((this.nom == null) ? 0 : this.nom.hashCode());
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
		Chanson other = (Chanson) obj;
		if (this.album == null) {
			if (other.album != null)
				return false;
		} else if (!this.album.equals(other.album))
			return false;
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
		return true;
	}

}
