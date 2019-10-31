package mus.nico.quickselector.gui;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;

import mus.nico.quickselector.utils.ListerArbo;
import net.samuelcampos.usbdrivedetector.USBDeviceDetectorManager;

public class NavigationPan extends JPanel {

	private static final long serialVersionUID = 1L;
	private JLabel etqSource;
	private JLabel etqDestination;
	private JPanel panSource;
	private JPanel panDestination;
	private JLabel etqDossierSce;
	private JLabel etqDossierDest;
	private JButton nav, navDest;

	public NavigationPan() {
		this.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		this.initUI();
	}

	public String getEtqDossierSceText() {
		return this.etqDossierSce.getText();
	}

	public String getEtqDossierDestText() {
		return this.etqDossierDest.getText();
	}

	private void initUI() {
		USBDeviceDetectorManager driveDetector = new USBDeviceDetectorManager();
		// -- Panneau "source" --
		this.etqSource = new JLabel("Dossier Source");
		this.etqSource.setVisible(true);

		this.etqDossierSce = new JLabel();
		this.etqDossierSce.setText("H:\\musique");
		this.etqDossierSce.setVisible(true);

		this.nav = this.createBoutonNavSource();

		this.panSource = new JPanel();
		// this.panSource.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		this.panSource.setLayout(new BoxLayout(this.panSource, BoxLayout.LINE_AXIS));
		this.panSource.add(this.etqSource);
		this.panSource.add(this.nav);
		this.panSource.add(this.etqDossierSce);

		// --Panneau "destination--
		this.etqDestination = new JLabel("Dossier de destination");
		this.etqSource.setVisible(true);

		System.out.println("CTRL-----------" + driveDetector.getRemovableDevices().size());

		try {
			if (driveDetector != null && driveDetector.getRemovableDevices().size() > 0) {
				List<Path> arboDest = ListerArbo.listerGroupe(
						Paths.get(driveDetector.getRemovableDevices().get(0).getRootDirectory().getPath()));
				for (Path p : arboDest) {
					// try {
					Files.walkFileTree(p, new SimpleFileVisitor<Path>() {
						@Override
						public FileVisitResult preVisitDirectory(final Path dir, final BasicFileAttributes attrs)
								throws IOException {
							FileVisitResult result;
							if (dir.endsWith("musique".toLowerCase())) {
								result = FileVisitResult.CONTINUE;
								NavigationPan.this.etqDossierDest = new JLabel(
										// driveDetector.getRemovableDevices().get(0).getDeviceName() + " "+
										dir.toString());

							} else {
								result = FileVisitResult.CONTINUE;
								NavigationPan.this.etqDossierDest = new JLabel();
								// NavigationPan.this.etqDossierDest = new JLabel();
							}

							System.out.println("Répertoire : " + dir);
							return result;
						}
					});
					// } catch (IOException e) {
					// // TODO Auto-generated catch block
					// e.printStackTrace();
					// }
					// if (p.toString().endsWith("musique".toLowerCase())) {
					// this.etqDossierDest = new JLabel(
					// driveDetector.getRemovableDevices().get(0).getDeviceName() + " " +
					// p.toString());
					// break;
					// } else {
					// this.etqDossierDest = new
					// JLabel(driveDetector.getRemovableDevices().get(0).getDeviceName() + " "
					// + driveDetector.getRemovableDevices().get(0).getRootDirectory());
					// }
				}

			} else {
				this.etqDossierDest = new JLabel();
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} // catch (IllegalArgumentException e1) {
			// // TODO Auto-generated catch block
			// e1.printStackTrace();
			// }
		this.etqDossierDest.setVisible(true);

		this.navDest = this.createBoutonNavDest();

		this.panDestination = new JPanel();
		// this.panDestination.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		this.panDestination.setLayout(new BoxLayout(this.panDestination, BoxLayout.LINE_AXIS));
		this.panDestination.add(this.etqDestination);
		this.panDestination.add(this.navDest);
		this.panDestination.add(this.etqDossierDest);

		// --Ajout des 2 panneaux precedents
		this.add(this.panSource);
		this.add(this.panDestination);

	}

	private JButton createBoutonNavSource() {
		JButton nav = new JButton("SELECT DOSSIER");
		nav.addActionListener(new OpenFileSourceAction());
		return nav;
	}

	private JButton createBoutonNavDest() {
		JButton nav = new JButton("SELECT DOSSIER ");
		nav.addActionListener(new OpenFileDestAction());
		return nav;
	}

	private class OpenFileSourceAction extends AbstractAction {
		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			JFileChooser fileChooser = new JFileChooser();
			fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			// fileChooser.setCurrentDirectory(new File("H:\\musique"));

			int ret = fileChooser.showDialog(NavigationPan.this.panSource, "Open file");

			if (ret == JFileChooser.APPROVE_OPTION) {

				File file = fileChooser.getSelectedFile();
				String text = file.getPath();
				// String text = NavigationPan.this.readFile(file);

				NavigationPan.this.etqDossierSce.setText(text);

			}
		}
	}

	private class OpenFileDestAction extends AbstractAction {
		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			JFileChooser fileChooser = new JFileChooser();
			fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			int ret = fileChooser.showDialog(NavigationPan.this.panDestination, "Open file");

			if (ret == JFileChooser.APPROVE_OPTION) {

				File file = fileChooser.getSelectedFile();
				file.getName();

				NavigationPan.this.etqDossierDest.setText(file.getPath());

			}
		}
	}

}
