package ensah.genieLogiciel.cryptographieSecurite.miniProjet.steganographie;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import javax.swing.*;

/*
 * Réalisé par: AMINE EL KHOUMISSI
 */
public class FenetrePrincipale extends JFrame {
	
	private JTabbedPane tabbedPane;
	private JPanel panel1;
	private JPanel panel2;

	// Composants de la page dissimuler:
	JTextArea msg;
	BufferedImage imageSource = null, imageIntegree = null;
	JSplitPane sp = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
	JScrollPane paneauOriginal = new JScrollPane(), paneauIntegre = new JScrollPane();
	JButton ouvrir, cacher, enregistrer, modifier;

	// Composants de la page extraire:
	JTextArea message;
	JButton ouvrir1, extraire, modifier1;
	BufferedImage image = null;
	JScrollPane imagePane;

	public FenetrePrincipale() {

		// Créer le cadre de l'application:
		setTitle("STEGANOGRAPHIE");
		this.setBounds(GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds());
		setBackground(Color.white);
		this.setLayout(new GridLayout());
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		JPanel topPanel = new JPanel();
		topPanel.setLayout(new BorderLayout());
		getContentPane().add(topPanel);

		// Créer les pages des deux onglets qui existent dans l'application:
		dissimuler();
		extraire();

		// Créer un paneau de deux onglets:
		tabbedPane = new JTabbedPane();
		tabbedPane.addTab("Dissimulation", panel1);
		tabbedPane.addTab("Extraction", panel2);
		topPanel.add(tabbedPane, BorderLayout.CENTER);
		this.validate();

	}

	//Fenêtre de dissimulation des textes:
	public void dissimuler() {
		
		//Boutons:
		msg = new JTextArea();
		ouvrir = new JButton("Ouvrir");
		cacher = new JButton("Cacher");
		enregistrer = new JButton("Enregistrer");
		modifier = new JButton("Modifier");

		// panel1: c'est un panneau contenant les deux suivants:
		panel1 = new JPanel();
		panel1.setLayout(null);
		panel1.setBounds(0, 0, this.getWidth(), this.getHeight());

		// p1: panneax destiné à recevoir le texte à cacher dans une image:
		JPanel p1 = new JPanel();
		p1.setLayout(new GridLayout(1, 1));
		p1.setBounds(0, 0, this.getWidth(), 270);
		p1.setBackground(Color.lightGray);
		p1.add(new JScrollPane(msg));
		msg.setFont(new Font("Tiemes new Roman", Font.BOLD, 20));
		p1.setBorder(BorderFactory.createTitledBorder("Message à dissimuler"));
		panel1.add(p1);

		// p2: panneau destiné à recevoir l'image originelle et celle résultant de la dissimulation d'un texte:
		JPanel p2 = new JPanel();
		p2 = new JPanel(new GridLayout(1, 1));
		p2.setBounds(0, 290, this.getWidth(), 300);

		sp.setLeftComponent(paneauOriginal);
		sp.setRightComponent(paneauIntegre);
		sp.setResizeWeight(0.5d);
		paneauOriginal.setBorder(BorderFactory.createTitledBorder("Image originelle"));
		paneauIntegre.setBorder(BorderFactory.createTitledBorder("Image stéganopraphiée"));
		p2.add(sp, BorderLayout.CENTER);
		panel1.add(p2);

		// p3: panneau destiné aux boutons:
		JPanel p3 = new JPanel();
		p3.setLayout(new FlowLayout());
		p3.setBounds(0, 600, this.getWidth(), 120);
		p3.add(ouvrir);
		p3.add(cacher);
		p3.add(enregistrer);
		p3.add(modifier);
		panel1.add(p3);
 
		//Implémentation de l'événement 'ouvrir':
		ouvrir.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				java.io.File f = showFileDialog(true);
				try {
					imageSource = ImageIO.read(f);
					JLabel l = new JLabel(new ImageIcon(imageSource));
					paneauOriginal.getViewport().add(l);

				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		});

		//Implémentation de l'événement 'cacher':
		cacher.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				String mess = msg.getText();
				imageIntegree = imageSource.getSubimage(0, 0, imageSource.getWidth(), imageSource.getHeight());
				dissimulerMessage(imageIntegree, mess);
				JLabel l = new JLabel(new ImageIcon(imageIntegree));
				paneauIntegre.getViewport().add(l);

			}
		});

		//Implémentation de l'événement 'enregistrer':
		enregistrer.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (imageIntegree == null) {
					JOptionPane.showMessageDialog(panel1, "Aucun message n' été dissimulé!",
							"Aucune image à enregistrer", JOptionPane.ERROR_MESSAGE);
					return;
				}
				java.io.File f = showFileDialog(false);
				String name = f.getName();
				String ext = name.substring(name.lastIndexOf(".") + 1).toLowerCase();
				if (!ext.equals("png") && !ext.equals("bmp") && !ext.equals("dib")) {
					ext = "png";
					f = new java.io.File(f.getAbsolutePath() + ".png");
				}
				try {
					if (f.exists())
						f.delete();
					ImageIO.write(imageIntegree, ext.toUpperCase(), f);
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		});

		//Implémentation de l'événement 'modifier':
		modifier.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				msg.setText("");
				paneauOriginal.getViewport().removeAll();
				paneauIntegre.getViewport().removeAll();
				imageSource = null;
				imageIntegree = null;
				sp.setResizeWeight(0.5d);
				panel1.validate();

			}
		});

	}

	//Méthode permettant le chargement d'une image existant sur la machine et son enregistrement après dissimulation d'un texte:
	private java.io.File showFileDialog(final boolean open) {
		JFileChooser fc = new JFileChooser("Ouvrir une image");
		javax.swing.filechooser.FileFilter ff = new javax.swing.filechooser.FileFilter() {
			public boolean accept(java.io.File f) {
				String name = f.getName().toLowerCase();
				if (open)
					return f.isDirectory() || name.endsWith(".jpg") || name.endsWith(".jpeg") || name.endsWith(".png")
							|| name.endsWith(".gif") || name.endsWith(".tiff") || name.endsWith(".bmp")
							|| name.endsWith(".dib");
				return f.isDirectory() || name.endsWith(".png") || name.endsWith(".bmp");
			}

			public String getDescription() {
				if (open)
					return "Image (*.jpg, *.jpeg, *.png, *.gif, *.tiff, *.bmp, *.dib)";
				return "Image (*.png, *.bmp)";
			}
		};
		fc.setAcceptAllFileFilterUsed(false);
		fc.addChoosableFileFilter(ff);

		java.io.File f = null;
		if (open && fc.showOpenDialog(this) == fc.APPROVE_OPTION)
			f = fc.getSelectedFile();
		else if (!open && fc.showSaveDialog(this) == fc.APPROVE_OPTION)
			f = fc.getSelectedFile();
		return f;
	}

	//Implémentation de l'algorithme Least Significant Bit (LSB) pour dissimuler un texte dans une image:
	private void dissimulerMessage(BufferedImage img, String mess) {
		int longueurMessage = mess.length();

		int largeurImage = img.getWidth(), hauteurImage = img.getHeight(), tailleImage = largeurImage * hauteurImage;
		
		//Test de la capacité de l'image à supporter le texte à dissimuler:
		if (longueurMessage * 8 + 32 > tailleImage) {
			JOptionPane.showMessageDialog(panel1, "Message trop long pour l'image choisie", "Message trop long!",
					JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		//Dissimulation des bits du nombre des caractères constituant le texte à cacher:
		dissimulerInteger(img, longueurMessage, 0, 0);
		
		////Dissimulation des bits des caractères constituant le texte à cacher:
		byte b[] = mess.getBytes();
		for (int i = 0; i < b.length; i++)
			dissimulerOctet(img, b[i], i * 8 + 32, 0);
	}

	//Méthode de dissimulation des 32 bits du nombre de caractères dans les permiers 32 pixels de l'image:
	private void dissimulerInteger(BufferedImage img, int n, int debut, int stockageBit) {
		int maxX = img.getWidth(), maxY = img.getHeight(), debutX = debut / maxY, debutY = debut - debutX * maxY,
				compteur = 0;
		for (int i = debutX; i < maxX && compteur < 32; i++) {
			for (int j = debutY; j < maxY && compteur < 32; j++) {
				int rgb = img.getRGB(i, j), bit = getBitValeur(n, compteur);
				rgb = setBitValeur(rgb, stockageBit, bit);
				img.setRGB(i, j, rgb);
				compteur++;
			}
		}
	}

	//Méthode de dissimulation des 8 bits de chaque caractère dans les 8 pixels qui suivent de l'image:
	private void dissimulerOctet(BufferedImage img, byte b, int debut, int stockageBit) {
		int maxX = img.getWidth(), maxY = img.getHeight(), debutX = debut / maxY, debutY = debut - debutX * maxY,
				compteur = 0;
		for (int i = debutX; i < maxX && compteur < 8; i++) {
			for (int j = debutY; j < maxY && compteur < 8; j++) {
				int rgb = img.getRGB(i, j), bit = getBitValeur(b, compteur);
				rgb = setBitValeur(rgb, stockageBit, bit);
				img.setRGB(i, j, rgb);
				compteur++;
			}
		}
	}

	//Méthode retournant la valeur du bit ajouté au bit de poids faible de l'octet correspondant à la transparence dans un pixel
	private int getBitValeur(int n, int position) {
		int v = n & (int) Math.round(Math.pow(2, position));
		return v == 0 ? 0 : 1;
	}

	//Méthode retourant la nouvelle valeur du pixel après insertion d'un bit:
	private int setBitValeur(int n, int position, int bit) {
		int toggle = (int) Math.pow(2, position), bv = getBitValeur(n, position);
		if (bv == bit)
			return n;
		if (bv == 0 && bit == 1)
			n |= toggle;
		else if (bv == 1 && bit == 0)
			n ^= toggle;
		return n;
	}

	//Fenêtre d'extraction des textes cachés dans une image:
	public void extraire() {

		//Composants graphiques de la fenêtre 'extraire':
		image = null;
		imagePane = new JScrollPane();
		message = new JTextArea();
		ouvrir1 = new JButton("Ouvrir");
		extraire = new JButton("Extraire");
		modifier1 = new JButton("Modifier");

		// panel2: panneau correspondant à la fenêtre 'extraire:
		panel2 = new JPanel();
		panel2.setLayout(null);
		panel2.setBounds(0, 0, this.getWidth(), this.getHeight());

		// p1: panneau contenant les boutons:
		JPanel p1 = new JPanel();
		p1.setLayout(null);
		p1.setLayout(new FlowLayout());
		p1.setBounds(0, 0, this.getWidth(), 50);
		p1.add(ouvrir1);
		p1.add(extraire);
		p1.add(modifier1);
		panel2.add(p1);

		// p2: panneau destiné à recevoir l'image contenant le texte caché:
		JPanel p2 = new JPanel();
		p2.setLayout(new GridLayout(1, 1));
		p2.setBounds(0, 50, this.getWidth(), 320);
		imagePane.setBorder(BorderFactory.createTitledBorder("Image stéganographiée"));
		p2.add(imagePane, BorderLayout.CENTER);
		panel2.add(p2);

		// p3: panneau destiné à afficher le texte caché dans une image:
		JPanel p3 = new JPanel();
		p3.setLayout(new GridLayout(1, 1));
		p3.setBounds(0, 380, this.getWidth(), 270);
		p3.setBackground(Color.lightGray);
		p3.add(new JScrollPane(message));
		message.setFont(new Font("Times new Roman", Font.BOLD, 20));
		p3.setBorder(BorderFactory.createTitledBorder("Message extrait"));
		panel2.add(p3);

		//Implémentation de l'événement 'ouvrir1':
		ouvrir1.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				java.io.File f = showFileDialog1(true);
				try {
					image = ImageIO.read(f);
					JLabel l = new JLabel(new ImageIcon(image));
					imagePane.getViewport().add(l);
					panel2.validate();
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		});

		//Implémentation de l'événement 'extraire':
		extraire.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				int len = extraireInteger(image, 0, 0);
				byte b[] = new byte[len];
				for (int i = 0; i < len; i++)
					b[i] = extraireOctet(image, i * 8 + 32, 0);
				message.setText(new String(b));
			}
		});

		//Implémentation de l'événement 'modifier1':
		modifier1.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				message.setText("");
				imagePane.getViewport().removeAll();
				image = null;
				panel2.validate();
			}
		});

	}

	//Méthode permettant le chargement d'une image existant sur la machine:
	private java.io.File showFileDialog1(boolean open) {
		JFileChooser fc = new JFileChooser("Ouvrir une image");
		javax.swing.filechooser.FileFilter ff = new javax.swing.filechooser.FileFilter() {
			public boolean accept(java.io.File f) {
				String name = f.getName().toLowerCase();
				return f.isDirectory() || name.endsWith(".png") || name.endsWith(".bmp");
			}

			public String getDescription() {
				return "Image (*.png, *.bmp)";
			}
		};
		fc.setAcceptAllFileFilterUsed(false);
		fc.addChoosableFileFilter(ff);

		java.io.File f = null;
		if (open && fc.showOpenDialog(this) == fc.APPROVE_OPTION)
			f = fc.getSelectedFile();
		else if (!open && fc.showSaveDialog(this) == fc.APPROVE_OPTION)
			f = fc.getSelectedFile();
		return f;
	}

	//Méthode d'extraction des 32 bits du nombre de caractères qui sont cachés dans les permiers 32 pixels de l'image:
	private int extraireInteger(BufferedImage img, int debut, int stockageBit) {
		int maxX = img.getWidth(), maxY = img.getHeight(), debutX = debut / maxY, debutY = debut - debutX * maxY,
				compteur = 0;
		int length = 0;
		for (int i = debutX; i < maxX && compteur < 32; i++) {
			for (int j = debutY; j < maxY && compteur < 32; j++) {
				int rgb = img.getRGB(i, j), bit = getBitValeur(rgb, stockageBit);
				length = setBitValeur(length, compteur, bit);
				compteur++;
			}
		}
		return length;
	}

	//Méthode d'extraction des 8 bits de chaque caractère cachés dans les 8 pixels qui suivent de l'image:
	private byte extraireOctet(BufferedImage img, int debut, int stockageBit) {
		int maxX = img.getWidth(), maxY = img.getHeight(), debutX = debut / maxY, debutY = debut - debutX * maxY,
				compteur = 0;
		byte b = 0;
		for (int i = debutX; i < maxX && compteur < 8; i++) {
			for (int j = debutY; j < maxY && compteur < 8; j++) {
				int rgb = img.getRGB(i, j), bit = getBitValeur(rgb, stockageBit);
				b = (byte) setBitValeur(b, compteur, bit);
				compteur++;
			}
		}
		return b;
	}

}
