package IPkit;
import java.awt.LayoutManager;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * Implementação de um JPanel próprio para receber
 * inputs de IP do usuário. <br />
 * Imagem ilustrativa abaixo: <br />
 * <img src="ippanelgif.gif" alt="127.0.0.1 == localhost" border="0"/>
 */
public class IPPanel extends JPanel {
	
	private IPField ip1;
	private IPField ip2;
	private IPField ip3;
	private IPField ip4;
	private String IPString;
	
	/**
	 */
	public IPPanel()
	{
		super();
		init();
	}
	
	/**
	 */
	public IPPanel(boolean isDoubleBuffered)
	{
		super(isDoubleBuffered);
		init();
		
	}

	/**
	 */
	public IPPanel(LayoutManager layout)
	{
		super(layout);
		init();
		
	}

	/**
	 */
	public IPPanel(LayoutManager layout, boolean isDoubleBuffered)
	{
		super(layout,isDoubleBuffered);
		init();
	}
	
	/**
	 */
	private void init()
	{
		this.setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
		IPString = null;
		ip1 = new IPField();
		ip2 = new IPField();
		ip3 = new IPField();
		ip4 = new IPField();
		JLabel dot1 = new JLabel(".");
		JLabel dot2 = new JLabel(".");
		JLabel dot3 = new JLabel(".");
	
		add(ip1);
		add(dot1);
		add(ip2);
		add(dot2);
		add(ip3);
		add(dot3);
		add(ip4);
	}
	
	/**
	 * Função para pegar valor de um determinado campo do IP, 
	 * retornando o valor do campo lido ou <code>-1</code> caso haja algum erro.
	 * @param ind é o Index do campo: 1,2,3 ou 4.
	 * @return um <code>int</code>
	 */
	public int getValueAt(int ind)
	{
		String aux = null;
		switch(ind)
		{
			case 1:
				aux = ip1.getText();
				if (aux == null) return -1;
				if (aux.length() == 0) return -1;
				else return Integer.parseInt(aux);
			case 2:
				aux = ip2.getText();
				if (aux == null) return -1;
				if (aux.length() == 0) return -1;
				else return Integer.parseInt(aux);
			case 3:
				aux = ip3.getText();
				if (aux == null) return -1;
				if (aux.length() == 0) return -1;
				else return Integer.parseInt(aux);
			case 4:
				aux = ip4.getText();
				if (aux == null) return -1;
				if (aux.length() == 0) return -1;
				else return Integer.parseInt(aux);
			default: return -1;
		}
	}
	
	/**
	 * Retorna uma <code>String</code> no formato
	 * "<code><b>123.123.123.123</b></code>" ou <code>null</code> caso
	 * haja algum erro.
	 * @return uma <code>String</code>
	 */
	public String getIPString()
	{
		int i1,i2,i3,i4;
		
		i1 = this.getValueAt(1);
		if (i1 == -1) return null;
		i2 = this.getValueAt(2);
		if (i2 == -1) return null;
		i3 = this.getValueAt(3);
		if (i3 == -1) return null;
		i4 = this.getValueAt(4);
		if (i4 == -1) return null;
		
		IPString = String.valueOf(i1);
		IPString += "." + i2 + "." + i3 + "." + i4;
		
		return IPString;
	}
	
	/**
	 */
	public IPField getIp1() {
		return ip1;
	}

	/**
	 */
	public IPField getIp2() {
		return ip2;
	}

	/**
	 */
	public IPField getIp3() {
		return ip3;
	}

	/**
	 */
	public IPField getIp4() {
		return ip4;
	}
}
