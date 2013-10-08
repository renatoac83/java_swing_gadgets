package LEDkit;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

/**
 * Classe que monitoria a freq��ncia
 * com que o LED pisca. <br />
 * S� ter� funcionalidade se no LED estiver <code>blink == true</code>.
 */
public class Blink extends Thread{
	
	private int time;
	private JLabel lbl;
	private ImageIcon normal,faded;
	
	/**
	 * Construtor padr�o
	 * @param lbl
	 * @param time
	 */
	public Blink(JLabel lbl,int time){
		super();
		this.lbl = lbl;
		this.time = time;
		this.normal = null;
		this.faded = null;
	}
	
	/**
	 * N�o � recomendado mexer nesta fun��o. <br />
	 * Uso interno do LED.
	 * @param normal
	 * @param faded
	 */
	public void setIcons(ImageIcon normal,ImageIcon faded)
	{
		this.normal = normal;
		this.faded = faded;
	}
	
	/**
	 * Alterna LED entre 2 tipos de icone para
	 * simular efeito de pisca-pisca.
	 */
	public void run(){
		while(true)
		{
			try
			{
				sleep(time);
				if ( (faded != null) && (normal != null))
				{
					lbl.setIcon(normal);
				}
				sleep(time);
				if ( (faded != null) && (normal != null))
				{
					lbl.setIcon(faded);
				}
			}
			catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Retorna tempo (em milisegundos) em que LED
	 * pisca.
	 * @return int
	 */
	public int getTime() {
		return time;
	}
	
	/**
	 * Pode-se alterar a freq��ncia do LED por aqui tamb�m.
	 * @param time
	 */
	public void setTime(int time) {
		this.time = time;
	}
}
