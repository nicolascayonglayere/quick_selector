package mus.nico.quickselector.fx.model;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class Artiste {

	private String nom;
	private List<Album> albums;
	private Path chemin;

	public Artiste() {
	}

	public Artiste(String nom) {
		this.nom = nom;
	}

	public String getNom() {
		return this.nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public List<Album> getAlbums() {
		return this.albums;
	}

	public void setAlbums(List<Album> albums) {
		this.albums = albums;
	}

	public void addAlbum(Album album) {
		if (this.albums == null) {
			this.albums = new ArrayList<>();
		}
		this.albums.add(album);
	}

	public Path getChemin() {
		return this.chemin;
	}

	public void setChemin(Path chemin) {
		this.chemin = chemin;
	}

	@Override
	public String toString() {
		return "Artiste [nom=" + this.nom + ", albums=" + this.albums + ", chemin=" + this.chemin + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((this.albums == null) ? 0 : this.albums.hashCode());
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
		Artiste other = (Artiste) obj;
		if (this.albums == null) {
			if (other.albums != null)
				return false;
		} else if (!this.albums.equals(other.albums))
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
