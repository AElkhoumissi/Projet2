package ensah.genieLogiciel.cryptographieSecurite.miniProjet.steganographie;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.net.URL;
import javax.imageio.ImageIO;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JWindow;

/*
 * Réalisé par: AMINE EL KHOUMISSI
 */

public class ApplicationDeBase extends JWindow {

	//Composants graphiques de la page de chargement de l'application:
	JLabel loading,title;// Labels
	JProgressBar b;//Barre de progression
	 JPanel p;//Panneau
	   
	    public ApplicationDeBase() {
	        //Configuration du panneau
	    	p=new JPanel(){};
	        
	        this.setLayout(new GridLayout());
	        p.setLayout(null);
	        this.add(p);
	        this.setSize(850, 600);
	        
	        //Configuration de la barre de progression:
	        Dimension dimension= Toolkit.getDefaultToolkit().getScreenSize();
	        int x=(int)((dimension.getWidth()-this.getWidth())/2);
	        int y=(int)((dimension.getHeight()-this.getHeight())/2);
	        this.setLocation(x, y);
	        b=new JProgressBar(0,2000);
	        b.setBounds(50, 500, 740, 8);
	        b.setForeground(Color.black);
	        
	        //Configuration du label de chargement:
	        loading=new JLabel("chargement................");
	        loading.setForeground(Color.black);
	        loading.setFont(new Font("Times new Roman",Font.ITALIC,19));
	        loading.setBounds(50, 455, 180, 60);
	        
	        //Configuration du titre du panneau
	        title=new JLabel("Stéganographie");
	        title.setForeground(Color.black);
	        title.setFont(new Font("Times new Roman",Font.ITALIC,50));
	        title.setBounds(480, 170,350, 220);
	        
	        //Ajout des composants graphiques au panneau:
	        p.add(loading);
	        p.add(title);
	        p.add(b);
	        b.setValue(0);
	        this.add(p);
	        
	        //Affichage du contenu du panneau:
	        this.setVisible(true);
	                }
	    
	    //Méthode exécutant l'application:
	    public static void main(String[] args) throws InterruptedException 
	    {
	    	ApplicationDeBase s= new ApplicationDeBase();
	       //Implémentation de l'avancement de la barre de progression:
	       int i=0;
	       while(i<=2000)
	       {
	       s.b.setValue(i);
	       i=i+25;
	       Thread.sleep(50);
	       }
	   
	       new FenetrePrincipale().setVisible(true);
	       s.dispose();        

	    }


}
