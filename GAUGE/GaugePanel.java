/* Created on Jun 10, 2004
 * 
 * TODO pack this 'package' and put it on SourceForge (GaugePanelDemo (former ComponentTest) and GaugePanel+GaugeVisor+Gauge kit
 * 
 * TODO Alarms, beeps to be implemented? //Toolkit.getDefaultToolkit().beep();
 * 
 * TODO fazer um checkup do codigo de Gauge e GaugePanel para ver se está tudo ok...
 */

package Gauge;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

/**
 * Implementação de um JPanel que contém um visualizador
 * em forma de Gauge e um visor numérico do valor do Gauge. <br />
 * Imagem ilustrativa abaixo: <br />
 * <img src="gauge.png" alt="GaugePanel + GaugeVisor + Gauge" border="0"/>
 */
public class GaugePanel extends JPanel {
	
	private Gauge gd;
	private GaugeVisor gv;
	private String title = "VFAT [OFFLINE]";
	private String unit = "V";
	private double value = 0;
	private TitledBorder titlebrd;
	private JLabel label;
	private JPanel aux = new JPanel();
	private JPanel pane = new JPanel();
	private JPanel emptyspaceside = new JPanel();
	private JPanel emptyspacebelow = new JPanel();
	
	public GaugePanel(){
		super();
		gd = new Gauge();
		init();
	}
	
	public GaugePanel(double maxvalue,double minvalue,double minangle,double maxangle,int majorticks,int minorticks,int arrowtype,int arctype, String unit, String title){
		super();
		gd = new Gauge(maxvalue,minvalue,minangle,maxangle,majorticks,minorticks,arrowtype,arctype,unit);
		this.title = title;
		this.unit = unit;
		init();
	}

	private void init() {
		setLayout(new BorderLayout());

		pane.setLayout(new BoxLayout(pane, BoxLayout.PAGE_AXIS));
		titlebrd = BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLACK), title);
		titlebrd.setTitleJustification(TitledBorder.CENTER);
		pane.setBorder(titlebrd);
		
		aux.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
		gv = new GaugeVisor(6);
		aux.add(gv);
		label = new JLabel(unit);
		aux.add(label);
		
		pane.add(gd);
		pane.add(Box.createVerticalGlue());
		pane.add(aux);

		add(pane,BorderLayout.CENTER);
		add(emptyspaceside,BorderLayout.LINE_END);

		setPreferredSize(new Dimension(160,160));
		setMinimumSize(new Dimension(160,160));
		setMaximumSize(new Dimension(160,160));
		pane.setMaximumSize(new Dimension(100,150));
		pane.setMinimumSize(new Dimension(100,150));
		pane.setPreferredSize(new Dimension(100,150));
		emptyspaceside.setMaximumSize(new Dimension(80,1));
		emptyspaceside.setPreferredSize(new Dimension(80,1));
		aux.setPreferredSize(new Dimension(110,30));
		aux.setMinimumSize(new Dimension(110,30));
		aux.setMaximumSize(new Dimension(110,30));
		gd.setPreferredSize(new Dimension(110,110));
		gd.setMaximumSize(new Dimension(110,110));
	
	}

	public double getValue() {
		return value;
	}

	public void setValue(double value) {
		this.value = value;
		gv.setText(String.valueOf(value));
		gd.setValue(value);

		gd.repaint();
	}
	
	public String getTitle() {
		return title;
	}
	
	public void setTitle(String title) {
		this.title = title;
		titlebrd = BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLACK), title);
		titlebrd.setTitleJustification(TitledBorder.CENTER);
		pane.setBorder(titlebrd);
	}

	private void fixsize(){
		int proportion; // 5/14 of the size.height is due aux-panel and the rest is used for the Gauge
		Insets insets = pane.getInsets();
		Dimension size = new Dimension(getWidth() - insets.left, getHeight() );//- insets.top);
		size.width = size.height = Math.min(size.width,size.height);
		if (size.width < 110)
		{
			size.width = size.height = 110;//forçar minimum size (110,110)
			if (size.height < 150) size.height = 150;
		}
		else
		{
			proportion = Math.round((size.width * 4.0f)/15.0f);
			size.width -= proportion;
		}

		pane.setPreferredSize(size);
		
		emptyspaceside.setPreferredSize(new Dimension ((getWidth() - size.width) , 1)) ;
		emptyspaceside.setMaximumSize(new Dimension ((getWidth() - size.width) , 1)) ;
		
		if ( (gd != null) && (aux != null))
		{
			gd.setPreferredSize( new Dimension(size.width-(2*insets.left),size.width-(2*insets.left)));
			gd.setMaximumSize( new Dimension(size.width-(2*insets.left),size.width-(2*insets.left)));
			aux.setMaximumSize(new Dimension (gd.getWidth(),size.height - gd.getHeight() - (2*insets.top)));
		}
		setPreferredSize(size);
	}

	public void repaint(){
		if (pane != null) fixsize();
		super.repaint();
	}
}
