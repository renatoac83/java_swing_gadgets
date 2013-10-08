package LEDkit;

import java.awt.BasicStroke;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

/**
 * Esta classe simula um LED que
 * pode ter 3 cores (RED/GREEN/OFF).<br />
 * <img src="ledimg.jpg" alt="LED" /> 
 */
public class LED extends JLabel{
	
	//constantes
	/**
	 * Constante para mudar LED para desligado
	 */
	public static final int OFF = 0; 
	/**
	 * Constante para mudar LED para  vermelho
	 */
	public static final int RED = 1; 
	/**
	 * Constante para mudar LED para verde
	 */
	public static final int GREEN = 2; 
	
	//atributos
	private int estado;
	private ImageIcon red,green,off,redfade,greenfade;
	private boolean blink;
	private Blink b;
	
	//construtor
	/**
	 * Construtor padrão. <br />
	 * LED não pisca (<code>blink == false</code>).
	*/
	public LED(){
		super();
		b = new Blink(this,500);
		blink = false;
		estado = OFF;
		off = createImageIcon("nolight.jpg");
		red = createImageIcon("redlight.jpg");
		green = createImageIcon("greenlight.jpg");
		redfade = createImageIcon("redlightfade.jpg");
		greenfade = createImageIcon("greenlightfade.jpg");
		this.setIcon(off);
	}

	/**
	 * Construtor alternativo. <br />
	 * LED pisca com freqüência definida em <code>time</code> (milisegundos).
	 */
	public LED(int time){
		super();
		b = new Blink(this,time);
		blink = true;
		estado = OFF;
		off = createImageIcon("nolight.jpg");
		red = createImageIcon("redlight.jpg");
		green = createImageIcon("greenlight.jpg");
		redfade = createImageIcon("redlightfade.jpg");
		greenfade = createImageIcon("greenlightfade.jpg");
		this.setIcon(off);
	}

	//Para achar imagens corretamente
	protected static ImageIcon createImageIcon(String path) {
		java.net.URL imgURL = LED.class.getResource(path);
		if (imgURL != null) {
			return new ImageIcon(imgURL);
		} else {
			System.err.println("Couldn't find file: " + path);
			return null;
		}
	}
	
	/**
	 * Retorna estado atual do LED (ver constantes)
	 * @return int
	 */
	public int getState()
	{
		return estado;
	}
	
	/**
	 * Muda estado do LED
	 * @param estado (ver estados possíveis nas constantes)
	 */
	public void setState(int estado)
	{
		this.estado = estado;
		repaint();
	}

	/**
	 * Alterar entre LED estático e LED piscante.
	 * @param blinking
	 */
	public void setBlinking(boolean blinking){
		blink = blinking;
		if (blink)
		{
			if (b.isAlive()) return;
			b.start();
		}
		else
		{
			if (b.isAlive()) b.stop();
		}
	}
	
	/**
	 * Ver qual é o tipo de LED usado (estático ou piscante).
	 * @return boolean
	 */
	public boolean isBlinking(){
		return blink;
	}
	
	protected void paintBorder (Graphics g){
		super.paintBorder(g);

		Graphics2D g2 = (Graphics2D) g.create();
		g2.setStroke ( new BasicStroke(2.0f) );
		g2.drawRect(0,0,getWidth(),getHeight());
	}

	protected void paintComponent (Graphics g)
	{
		super.paintComponent(g);

		switch(estado)
		{
			case OFF:
				if (!blink)	setIcon(off);
				else b.setIcons(off,off);
				break;
			case RED:
				if (!blink) setIcon(red);
				else b.setIcons(red,redfade);
				break;
			case GREEN:
				if (!blink) setIcon(green);
				else b.setIcons(green,greenfade);
				break;
			default: break;
		}
		
	}

}
