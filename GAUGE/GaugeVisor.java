package Gauge;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import javax.swing.JTextField;

/*
 * TODO  fazer fonte e quadrado preto de fundo
 * mudarem de tamanho de acordo com resize
 */

/**
 * Visualizador do valor apontado no Gauge. <br />
 * Aparência padrão: Fundo preto com números em verde. 
 */
public class GaugeVisor extends JTextField{
	
	private float fontsize = 13;
	private Font font;
	
	public GaugeVisor(){
		super();
		init();
	}
	
	public GaugeVisor(int cols){
		super(cols);
		init();
	}
	
	private void init(){
		setText("25.0"); // para ser coerente com o valor padrão do Gauge
		setOpaque(false);
		setEditable(false);
		font = new Font("Courier New",Font.PLAIN,(int)fontsize);
        setFont(font);
        setForeground(new Color(0,255,0));
	}
	
	protected void paintComponent(Graphics g) {
		//Fundo preto e numeros em verde
		g.setColor(Color.BLACK);
        g.fillRect(0,0,getWidth()-1,getHeight()-1);
        
        // Pintar o que sobrar   
        super.paintComponent(g);
    }


}
/*Junk Code
 * //		Dimension size = getParent().getSize();		
//		fontsize = 11.0f + (getWidth()/20.0f);
//		font.deriveFont(fontsize);

*/