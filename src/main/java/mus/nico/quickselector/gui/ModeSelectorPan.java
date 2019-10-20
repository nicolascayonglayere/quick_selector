package mus.nico.quickselector.gui;

import java.awt.Color;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JPanel;

public class ModeSelectorPan extends JPanel {

	private static final long serialVersionUID = 1L;

	private JPanel panDelete;
	private JCheckBox cbDelete;

	private JPanel panChoix;
	private JCheckBox cbTravail;
	private JCheckBox cbListeAlea;

	private boolean nettoyage;
	private boolean travail;
	private boolean listeAlea;

	public ModeSelectorPan() {
		this.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		this.setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
		this.initUI();
	}

	private void initUI() {
		this.cbDelete = new JCheckBox("Nettoyer le repertoire");
		this.cbDelete.setSelected(false);
		this.cbDelete.addActionListener(new DeleteAction());

		this.panDelete = new JPanel();
		this.panDelete.setBorder(BorderFactory.createTitledBorder("NETTOYAGE"));
		this.panDelete.add(this.cbDelete);

		this.cbTravail = new JCheckBox("Jour de travail");
		this.cbTravail.setSelected(false);
		this.cbTravail.addActionListener(new SelectTravailAction());

		this.cbListeAlea = new JCheckBox("Liste aléatoire");
		this.cbListeAlea.setSelected(false);
		this.cbListeAlea.addActionListener(new SelectListeAction());

		this.panChoix = new JPanel();
		this.panChoix.setBorder(BorderFactory.createTitledBorder("OPTIONS"));
		this.panChoix.add(this.cbTravail);
		this.panChoix.add(this.cbListeAlea);

		this.add(this.panDelete);
		this.add(this.panChoix);
	}

	public boolean isNettoyage() {
		return this.nettoyage;
	}

	public void setNettoyage(boolean nettoyage) {
		this.nettoyage = nettoyage;
	}

	public boolean isTravail() {
		return this.travail;
	}

	public void setTravail(boolean travail) {
		this.travail = travail;
	}

	public boolean isListeAlea() {
		return this.listeAlea;
	}

	public void setListeAlea(boolean listeAlea) {
		this.listeAlea = listeAlea;
	}

	private class DeleteAction extends AbstractAction {
		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			if (ModeSelectorPan.this.cbDelete.isSelected()) {
				ModeSelectorPan.this.setNettoyage(true);
			}
		}
	}

	private class SelectTravailAction extends AbstractAction {
		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			if (ModeSelectorPan.this.cbTravail.isSelected()) {
				ModeSelectorPan.this.setTravail(true);
			}
		}
	}

	private class SelectListeAction extends AbstractAction {
		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			if (ModeSelectorPan.this.cbListeAlea.isSelected()) {
				ModeSelectorPan.this.setListeAlea(true);
			}
		}
	}

}
