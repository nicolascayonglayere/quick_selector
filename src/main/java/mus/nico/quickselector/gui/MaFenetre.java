package mus.nico.quickselector.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import mus.nico.quickselector.CopierMusique;

public class MaFenetre extends JFrame {

	private static final long serialVersionUID = 1L;

	// private JMenuItem quit = new JMenuItem("quitter");
	private NavigationPan monPanNav;
	private ModeSelectorPan monPanSelect;
	private JPanel panBouton;
	private JButton btCopier;
	private JButton btAnnuler;
	private JTextArea txtArea = new JTextArea();

	public MaFenetre() {
		// --Param de la fenetre
		this.setTitle("QuickSelector");
		this.setForeground(Color.BLACK);
		this.setBackground(Color.WHITE);
		this.setSize(new Dimension(800, 900));
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);

		this.monPanNav = new NavigationPan();
		this.monPanSelect = new ModeSelectorPan();
		// this.txtArea
		this.txtArea.setEnabled(false);

		this.btCopier = new JButton("COPIER");
		this.btCopier.addActionListener(new CopierAction());

		this.btAnnuler = new JButton("ANNULER");
		// --action a ajouter

		this.panBouton = new JPanel();
		this.panBouton.setLayout(new BoxLayout(this.panBouton, BoxLayout.PAGE_AXIS));
		this.panBouton.add(this.btCopier);
		this.panBouton.add(this.btAnnuler);

		this.monPanSelect.add(this.panBouton, BorderLayout.EAST);

		this.getContentPane().add(this.monPanNav, BorderLayout.NORTH);
		this.getContentPane().add(this.txtArea);
		this.getContentPane().add(this.monPanSelect, BorderLayout.SOUTH);
		// this.addWindowListener(new WindowAdapter() {
		// @Override
		// public void windowClosing(WindowEvent e) {
		// MaFenetre.this.quit.doClick();
		// }
		// });
		// --Essai Look&Feel
		try {
			// On force à utiliser le « look and feel » du système
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			// Ici on force tous les composants de notre fenêtre (this) à se redessiner avec
			// le « look and feel » du système
			SwingUtilities.updateComponentTreeUI(this);
		} catch (InstantiationException e1) {
			// logger.error(e.getMessage());
			e1.getMessage();
		} catch (ClassNotFoundException e2) {
			// logger.error(e.getMessage());
			e2.getMessage();
		} catch (UnsupportedLookAndFeelException e3) {
			// logger.error(e.getMessage());
			e3.getMessage();
		} catch (IllegalAccessException e4) {
			// logger.error(e.getMessage());
			e4.getMessage();
		}
	}

	private void refreshAff() {
		this.getContentPane().remove(this.txtArea);
		this.monPanSelect.setNettoyage(false);
		this.monPanSelect.setTravail(false);
		this.getContentPane().add(this.txtArea);
		this.getContentPane().revalidate();
		this.getContentPane().repaint();
	}

	private class CopierAction extends AbstractAction {
		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			List<Boolean> options = new ArrayList<Boolean>();
			options.add(MaFenetre.this.monPanSelect.isNettoyage());
			options.add(MaFenetre.this.monPanSelect.isTravail());
			options.add(MaFenetre.this.monPanSelect.isListeAlea());
			MaFenetre.this.btCopier.setEnabled(false);
			CopierMusique cm = new CopierMusique(
					Paths.get(MaFenetre.this.monPanNav.getEtqDossierSceText().substring(0, 3)),
					Paths.get(MaFenetre.this.monPanNav.getEtqDossierDestText()), options);
			String monTexte = "";

			if (options.get(0)) {
				long begin = System.currentTimeMillis();
				// MaFenetre.this.txtArea.repaint();
				MaFenetre.this.refreshAff();
				cm.nettoyage();
				long end = System.currentTimeMillis();
				float time = (end - begin) / 1000f;
				for (String s : cm.getListeSuppr()) {
					monTexte += "\n " + s;
				}

				MaFenetre.this.txtArea.setText("DOSSIERS SUPPRIMES : " + monTexte + " \n "
						+ "Temps d'execution nettoyage: " + String.valueOf(time));
				System.out.println("Temps d'execution nettoyage: " + String.valueOf(time));
				// MaFenetre.this.getContentPane().revalidate();
				// MaFenetre.this.getContentPane().repaint();

			}

			if (options.get(1)) {
				long begin = System.currentTimeMillis();
				MaFenetre.this.refreshAff();
				cm.copierAlbumJournee();
				long end = System.currentTimeMillis();
				float time = (end - begin) / 1000f;
				for (String s : cm.getListeAlbumsAjoutes()) {
					monTexte += "\n" + s;
				}
				MaFenetre.this.txtArea.setText("ALBUMS AJOUTES : " + monTexte + " \n "
						+ "Temps d'execution copie journee: " + String.valueOf(time));
				System.out.println("Temps d'execution copie journee: " + String.valueOf(time));
			}

			if (options.get(2)) {
				long begin = System.currentTimeMillis();
				MaFenetre.this.refreshAff();
				try {
					cm.listeAleatoire();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				long end = System.currentTimeMillis();
				float time = (end - begin) / 1000f;
				for (String s : cm.getListeAlea()) {
					monTexte += "\n" + s;
				}
				MaFenetre.this.txtArea.setText("TITRES AJOUTES : " + monTexte + " \n "
						+ "Temps d'execution liste aléatoire: " + String.valueOf(time));
				System.out.println("Temps d'execution liste aléatoire: " + String.valueOf(time));
			}

			MaFenetre.this.btCopier.setEnabled(true);
			// MaFenetre.this.monPanSelect.
		}
	}
}
