
package Gauge;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.*;
import java.text.DecimalFormat;

/**
 * Implementação de um visor tipo Gauge. <br />
 * Pode-se customizá-lo em vários aspectos, vide
 * construtor com mais parâmentros da classe.
 */
public class Gauge extends JPanel{
	
	//Constantes para tipos de ponteiros
	/**
	 */
	public static final int ARROW_SIMPLE_WITH_NO_POINTER = 1;
	public static final int ARROW_SIMPLE_WITH_NARROW_POINTER = 2;
	public static final int ARROW_SIMPLE_WITH_OPEN_POINTER = 3;
	public static final int ARROW_TRIANGLE_EMPTY = 4;
	public static final int ARROW_TRIANGLE_FILLED = 5;
	
	//Constantes para tipos de arcos
	public static final int ARC_BOTH = 1;
	public static final int ARC_ONLY_LOWER = 2;
	public static final int ARC_ONLY_UPPER = 3;
	public static final int ARC_NONE = 4;
	
	//Atributos (com valores padrão)
	private double minvalue = 0; 
	private	double maxvalue = 100;
	private	double greenmaxvalue = 60;
	private	double yellowmaxvalue = 80;
	private	double value = 25;
	private int majorticks = 1;
	private int minorticks = 4;
	private double minangle = 0.00;// em graus //OBS: 0.00 == LESTE, cresce em sentido anti-horario 
	private double maxangle = 180.00;// em graus
	private int arrowtype = ARROW_SIMPLE_WITH_NO_POINTER;
	private int arctype = ARC_BOTH;
	private String unit = " ";
	private boolean colored = true;
	private int fontsize = 12;
	
	//Construtores
	public Gauge(){
		super();
		init();
	}
	
	public Gauge(double maxvalue,double minvalue){
		super();
		this.maxvalue = maxvalue;
		this.minvalue = minvalue;
		init();
	}

	public Gauge(double maxvalue,double minvalue,double minangle,double maxangle){ 
		super();
		this.maxvalue = maxvalue;
		this.minvalue = minvalue;
		this.maxangle = maxangle;
		this.minangle = minangle;
		init();
	}

	public Gauge(double maxvalue,double minvalue,double minangle,double maxangle,int majorticks,int minorticks,int arrowtype,int arctype,String unit){ 
		super();
		this.maxvalue = maxvalue;
		this.minvalue = minvalue;
		this.maxangle = maxangle;
		this.minangle = minangle;
		this.majorticks = majorticks;
		this.minorticks = minorticks;
		this.arrowtype = arrowtype;
		this.arctype = arctype;
		this.unit = unit;
		init();
	}

	private void init(){
		//Ajustar tamanhos
		setMinimumSize(new Dimension(100,100));
		//Para manter a forma quadrada
		Dimension size = getPreferredSize();
		size.width = size.height = Math.max(size.width,size.height);
		if (size.width < 100) size.width = size.height = 100;//forçar minimum size (100,100)
		setPreferredSize(size);
		
		//Fazer tamanho da fonte proporcional ao tamanho do Gauge
		fontsize = 7 + ((size.width - 100)/25);

		//Somar dois aos majorticks (é número mínimo de majorticks na tela)
		majorticks += 2;

		//Checar alguns valores
		if (majorticks < 0) majorticks = 0;
		if (minorticks < 0) minorticks = 0;
		if ((arctype < 1) || (arctype > 4)) arctype = ARC_BOTH;
		if ((arrowtype < 1) || (arrowtype > 5)) arrowtype = ARROW_SIMPLE_WITH_NO_POINTER;
		
		//Ajustar maxangle e minangle
		if (minangle > 360.00)
		{
			while (minangle > 360.00) minangle -= 360.00;
		}
		if (maxangle > 360.00)
		{
			while (maxangle > 360.00) maxangle -= 360.00;
		}
		if (minangle < -360.00)
		{
			while (minangle < -360.00) minangle += 360.00;
		}
		if (maxangle < -360.00)
		{
			while (maxangle < -360.00) maxangle += 360.00;
		}
	}

	//Acelerar performance na pintura
    public boolean isOpaque() {
        return true;
    }
	
    private void fixsize(){
    	//Fixsize serve para ajustar tamanho do widget conforme janela muda de tamanho
		Dimension size = getSize();

		size.width = size.height = Math.max(size.width,size.height);
		if (size.width < 100) size.width = size.height = 100;//forçar minimum size (100,100)

		setPreferredSize(size);
		setSize(size);

		//Fazer tamanho da fonte proporcional ao tamanho do Gauge
		fontsize = 7 + ((size.width - 100)/25);

    }
    
    protected void paintBorder(Graphics g){
    	super.paintBorder(g);
    	g.drawRoundRect(0,0,getWidth()-1,getHeight()-1,getWidth()/8,getHeight()/8);
    }
 
    protected void paintComponent(Graphics g) {
		fixsize();
		super.paintComponent(g);

		Graphics2D g2 = (Graphics2D)g.create();//copia objeto Graphics para não mudar suas configurações

		//Desenhar fundo (background)
		GradientPaint gp = new GradientPaint(0,0,Color.WHITE,getWidth(),getHeight(),new Color(110,110,110));
        g2.setPaint(gp);
        g2.fillRoundRect(0,0,getWidth()-1,getHeight()-1,getWidth()/8,getHeight()/8);
		g2.setPaint(g.getColor());
    	
		//Let's paint as if it were a Canvas!
		int x = 0;
		int y = 0;
		int w = getWidth();
	    int h = getHeight();

		double CenterX = x+w/2;
		double CenterY = y+h/2;
		double RaioMaior = w/3;
		double RaioMenor = w/4;
		double RaioCentral = w/18;

        double minangleRad = Math.toRadians(minangle);
		double maxangleRad = Math.toRadians(maxangle);
		double angle;
		double deltaangle;
		double tickvalue;
		int totalticks;
		int adjustposX=0,adjustposY=0;

        //Desenhar circulo central
        g2.fillArc(x+(w/2)-(w/18),y+(h/2)-(h/18), w/9, h/9, 0, 360);
  
        //Desenhar RaioMaior e RaioMenor
        g2.setStroke(new BasicStroke(2.5f));
        if (arctype < 4 )
        {
        	if ((arctype == ARC_BOTH) || (arctype == ARC_ONLY_LOWER) )
        		g2.draw(new Arc2D.Double(x+RaioMenor,y+RaioMenor, 2*RaioMenor, 2*RaioMenor, minangle, maxangle-minangle, Arc2D.OPEN));//raio menor
        	if ((arctype == ARC_BOTH) || (arctype == ARC_ONLY_UPPER) )
        		g2.draw(new Arc2D.Double(x+((w-2*RaioMaior)/2),y+((w-2*RaioMaior)/2), 2*RaioMaior, 2*RaioMaior, minangle, maxangle-minangle, Arc2D.OPEN));//raio maior
        }
       
        // Desenhar faixas verde,amarela e vermelha
        if (colored)
        {
            double anglegreen,angleyellow;
            g2.setStroke(new BasicStroke((float)(RaioMaior-RaioMenor),BasicStroke.CAP_BUTT,BasicStroke.JOIN_ROUND ));

            g2.setColor(Color.GREEN);
    		anglegreen = minangle + ( ((greenmaxvalue - minvalue)/(maxvalue-minvalue)) * (maxangle - minangle) );
    		g2.draw(new Arc2D.Double(x+((w-2* (RaioMenor + ((RaioMaior-RaioMenor)/2)) )/2),y+((w-2* (RaioMenor + ((RaioMaior-RaioMenor)/2)) )/2), 2* (RaioMenor + ((RaioMaior-RaioMenor)/2)), 2* (RaioMenor + ((RaioMaior-RaioMenor)/2)), minangle, anglegreen-minangle, Arc2D.OPEN));

    		g2.setColor(Color.YELLOW);
    		angleyellow = minangle + ( ((yellowmaxvalue - minvalue)/(maxvalue-minvalue)) * (maxangle - minangle) );
    		g2.draw(new Arc2D.Double(x+((w-2* (RaioMenor + ((RaioMaior-RaioMenor)/2)) )/2),y+((w-2* (RaioMenor + ((RaioMaior-RaioMenor)/2)) )/2), 2* (RaioMenor + ((RaioMaior-RaioMenor)/2)), 2* (RaioMenor + ((RaioMaior-RaioMenor)/2)), anglegreen, angleyellow-anglegreen, Arc2D.OPEN));

    		g2.setColor(Color.RED);
    		angle = minangle + ( ((maxvalue - minvalue)/(maxvalue-minvalue)) * (maxangle - minangle) );
    		g2.draw(new Arc2D.Double(x+((w-2* (RaioMenor + ((RaioMaior-RaioMenor)/2)) )/2),y+((w-2* (RaioMenor + ((RaioMaior-RaioMenor)/2)) )/2), 2* (RaioMenor + ((RaioMaior-RaioMenor)/2)), 2* (RaioMenor + ((RaioMaior-RaioMenor)/2)), angleyellow, angle-angleyellow, Arc2D.OPEN));

        }
		g2.setColor(Color.BLACK);
		g2.setStroke(new BasicStroke(1.0f));

        //Desenhar tracejados e seus valores numéricos
        DecimalFormat df = new DecimalFormat();
        df.setMaximumIntegerDigits(3);
        df.setMaximumFractionDigits(2);
		totalticks = majorticks + (minorticks*(majorticks-1));
		deltaangle = ((maxangleRad-minangleRad)/(majorticks-1));
		if (minorticks != 0) deltaangle = deltaangle / (minorticks+1);

		g2.setFont(new Font("Arial",Font.BOLD,fontsize));
		for(int i=0; i < totalticks ; i++)
		{
			angle = minangleRad + (i*deltaangle);
			//Arredondar valores para que apareça 90 graus ao inves de 90.0000001 (foge do 'if' abaixo)
			angle = Math.toDegrees(angle);
			angle = Double.parseDouble(df.format(angle));
			angle = Math.toRadians(angle);
			if ((i % (minorticks+1)) == 0) 
			{
				tickvalue = minvalue + i*((maxvalue-minvalue)/(totalticks-1));
				tickvalue = Double.parseDouble(df.format(tickvalue));
				g2.draw(new Line2D.Double(CenterX + (RaioMenor * Math.cos(angle)),CenterY - (RaioMenor * Math.sin(angle)),CenterX + (RaioMaior * Math.cos(angle)),CenterY - (RaioMaior * Math.sin(angle)) ));
				if ( ((angle >= 0) && (angle < Math.PI/2)) || ((angle < -3*Math.PI/2) && (angle > -2*Math.PI)) )
				{
					adjustposX = g2.getFont().getSize()/10;
	            	adjustposY = -g2.getFont().getSize()/10;
	            	if (angle == 0)
	            	{
	            		adjustposX = adjustposX + g2.getFont().getSize()/10;
            			adjustposY = (-1) * adjustposY + g2.getFont().getSize()/2;        		
	            	}
				}
				else if ( ((angle >= Math.PI/2) && (angle <= Math.PI)) || ((angle <= -Math.PI) && (angle >= -3*Math.PI/2)) )
				{
	            	adjustposX = (-5) * (g2.getFont().getSize()/2);
	            	if ((angle == Math.PI/2) || (angle == -3*Math.PI/2)) adjustposX = adjustposX/2;
	            	adjustposY = -g2.getFont().getSize()/10;
	            	if ((angle == Math.PI) || (angle == -Math.PI))
	            	{
	            		adjustposX = adjustposX - g2.getFont().getSize()/4;
	            		adjustposY = (-1) * adjustposY + g2.getFont().getSize()/2;
	            	}
				}
				else if ( ((angle > Math.PI) && (angle <= 3*Math.PI/2)) || ((angle > -Math.PI ) && (angle <= -Math.PI/2)) )
				{
	            	adjustposX = (-5) * (g2.getFont().getSize()/2);
	            	if ((angle == 3*Math.PI/2) || (angle == -Math.PI/2)) adjustposX = adjustposX/2;
	            	adjustposY = g2.getFont().getSize();
				}
				else if ( ((angle > 3*Math.PI/2) && (angle <= 2*Math.PI)) || ((angle > -Math.PI/2) && (angle < 0)) )
				{
	            	adjustposX = g2.getFont().getSize()/10;
	            	adjustposY = g2.getFont().getSize();
				}
				g2.drawString(Double.toString(tickvalue), (int)(CenterX+(RaioMaior*Math.cos(angle))+adjustposX), (int)(CenterY-(RaioMaior*Math.sin(angle))+adjustposY) );
			}
			else
			{
				if (arctype != ARC_ONLY_UPPER) g2.draw(new Line2D.Double(CenterX + (RaioMenor * Math.cos(angle)),CenterY - (RaioMenor * Math.sin(angle)),CenterX + ( (RaioMenor + ((RaioMaior-RaioMenor)/2)) * Math.cos(angle)),CenterY - ( (RaioMenor + ((RaioMaior-RaioMenor)/2)) * Math.sin(angle)) ));
				else g2.draw(new Line2D.Double(CenterX + (RaioMaior * Math.cos(angle)),CenterY - (RaioMaior * Math.sin(angle)),CenterX + ( (RaioMenor + ((RaioMaior-RaioMenor)/2)) * Math.cos(angle)),CenterY - ( (RaioMenor + ((RaioMaior-RaioMenor)/2)) * Math.sin(angle)) ));
			}
		}

		//Desenhar o sistema de unidades
		g2.drawString(unit,(int)(CenterX - ( g2.getFontMetrics().stringWidth(unit)/2 ) ),(int)(CenterY - (RaioMenor/2)));
		g2.setFont(getFont());
		
		//Desenhar ponteiro
		angle = minangleRad + ( ((value - minvalue)/(maxvalue-minvalue)) * (maxangleRad - minangleRad) );
		if (arrowtype <= 3)
		{
				g2.draw(new Line2D.Double(CenterX,CenterY,CenterX + ( RaioMaior * Math.cos(angle) ),CenterY - ( RaioMaior * Math.sin(angle) ) ));
				if (arrowtype > 1)
				{
					double arrowsize = RaioMaior/5;
					double angleaux1,angleaux2;
					angleaux1 = angle + (3*(Math.PI/4));
					angleaux2 = angleaux1 + (Math.PI/2);

					if (arrowtype == 2)
					{
						angleaux1 = angleaux1 + (Math.PI/12);
						angleaux2 = angleaux2 - (Math.PI/6);
					}
					
					g2.draw(new Line2D.Double(CenterX + ( RaioMaior * Math.cos(angle) ),CenterY - ( RaioMaior * Math.sin(angle) ),(CenterX + ( RaioMaior * Math.cos(angle) )) + ( arrowsize * Math.cos(angleaux1)),(CenterY - ( RaioMaior * Math.sin(angle) )) - ( arrowsize * Math.sin(angleaux1))  ));
					g2.draw(new Line2D.Double(CenterX + ( RaioMaior * Math.cos(angle) ),CenterY - ( RaioMaior * Math.sin(angle) ),(CenterX + ( RaioMaior * Math.cos(angle) )) + ( arrowsize * Math.cos(angleaux2)),(CenterY - ( RaioMaior * Math.sin(angle) )) - ( arrowsize * Math.sin(angleaux2))  ));
				}
		}
		else
		{
			double aX = CenterX + ( RaioMaior * Math.cos(angle) );
			double aY = CenterY - ( RaioMaior * Math.sin(angle) );
			double bX = CenterX + ( RaioCentral * Math.cos(angle-(Math.PI/2)) );
			double bY = CenterY - ( RaioCentral * Math.sin(angle-(Math.PI/2)) );
			double cX = CenterX + ( RaioCentral * Math.cos(angle+(Math.PI/2)) );
			double cY = CenterY - ( RaioCentral * Math.sin(angle+(Math.PI/2)) );

			Polygon poly = new Polygon();
			poly.addPoint((int)aX,(int)aY);
			poly.addPoint((int)bX,(int)bY);
			poly.addPoint((int)cX,(int)cY);

			if (arrowtype == 5) g2.fill(poly);
			else g2.draw(poly);
		}

		g2.dispose();//clean up
	}
	
	public int getArctype() {
		return arctype;
	}
	public void setArctype(int arctype) {
		this.arctype = arctype;
	}
	public int getArrowtype() {
		return arrowtype;
	}
	public void setArrowtype(int arrowtype) {
		this.arrowtype = arrowtype;
	}
	public boolean isColored() {
		return colored;
	}
	public void setColored(boolean colored) {
		this.colored = colored;
	}
	public int getFontsize() {
		return fontsize;
	}
	public void setFontsize(int fontsize) {
		this.fontsize = fontsize;
	}
	public double getGreenmaxvalue() {
		return greenmaxvalue;
	}
	public void setGreenmaxvalue(double greenmaxvalue) {
		this.greenmaxvalue = greenmaxvalue;
	}
	public int getMajorticks() {
		return majorticks;
	}
	public void setMajorticks(int majorticks) {
		this.majorticks = majorticks;
	}
	public double getMaxangle() {
		return maxangle;
	}
	public void setMaxangle(double maxangle) {
		this.maxangle = maxangle;
	}
	public double getMaxvalue() {
		return maxvalue;
	}
	public void setMaxvalue(double maxvalue) {
		this.maxvalue = maxvalue;
	}
	public double getMinangle() {
		return minangle;
	}
	public void setMinangle(double minangle) {
		this.minangle = minangle;
	}
	public int getMinorticks() {
		return minorticks;
	}
	public void setMinorticks(int minorticks) {
		this.minorticks = minorticks;
	}
	public double getMinvalue() {
		return minvalue;
	}
	public void setMinvalue(double minvalue) {
		this.minvalue = minvalue;
	}
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	public double getValue() {
		return value;
	}
	public void setValue(double value) {
		//Checar se valor passado é válido
		if ( (value >= minvalue) && (value <= maxvalue) )
		{
			this.value = value;
			if (colored)
			{
				if ( (value > greenmaxvalue) && (value <= yellowmaxvalue) )	setToolTipText("<html> <font color=yellow><b>" + value + "</b></font> <i>" + unit + "</i> ");
				else if ( value > yellowmaxvalue ) setToolTipText("<html> <font color=red><b>" + value + "</b></font> <i>" + unit + "</i> ");
				else setToolTipText("<html> <font color=green><b>" + value + "</b></font> <i>" + unit + "</i> ");
			}
			else setToolTipText("<html> <b>" + value + "</b> <i>" + unit + "</i>");
		}
	}
	public double getYellowmaxvalue() {
		return yellowmaxvalue;
	}
	public void setYellowmaxvalue(double yellowmaxvalue) {
		this.yellowmaxvalue = yellowmaxvalue;
	}
}
