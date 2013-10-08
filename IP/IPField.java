package IPkit;
import java.awt.Dimension;

import javax.swing.JTextField;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.PlainDocument;

/**
 * Esta classe implementa um JTextField que recebe
 * valores de IP. <br />
 * <i>[PENDING: Passar classe/javadoc para o inglês(?)]</i>
 */
public class IPField extends JTextField {

	/**
	 * Ignora contrutor da superclasse pelo contrutor padrão.
	 */
	public IPField(int cols) {
        super();
    }

	/**
	 * Construtor padrão.
	 */
    public IPField() {
        super();
    }

	/**
	 * Utilizar classe IPDoc para formatar input do usuário.
	 */
    protected Document createDefaultModel() {
	      return new IPDoc();
    }

	/**
	 * Como usamos números, a convenção é que eles
	 * fiquem à direita.
	 * @return <code>JTextField.RIGHT</code>
	 */
	public int getHorizontalAlignment() {
		return JTextField.RIGHT;
	}

	/**
	 * Considerando o uso padrão da fonte <code>Dialog</code>
	 * o tamanho retornado forçadamente é o ideal para caber
	 * o número de um IP.
	 * @return <code>(new Dimension(27,20))</code>
	 */
	public Dimension getPreferredSize() {
		return (new Dimension(27,20));
	}

	/**
	 * Considerando o uso padrão da fonte <code>Dialog</code>
	 * o tamanho retornado forçadamente é o ideal para caber
	 * o número de um IP.
	 * @return <code>(new Dimension(27,20))</code>
	 */
	public Dimension getMaximumSize() {
		return (new Dimension(27,20));
	}

	/**
	 * Considerando o uso padrão da fonte <code>Dialog</code>
	 * o tamanho retornado forçadamente é o ideal para caber
	 * o número de um IP.
	 * @return <code>(new Dimension(27,20))</code>
	 */
	public Dimension getMinimumSize() {
		return (new Dimension(27,20));
	}

	/**
	 * Esta classe deve ser usada em conjunto com
	 * a classe IPField. Ela serve para formatar
	 * o input do usuário.
	 */
    public class IPDoc extends PlainDocument {

    	/** Contrutor padrão */
    	public IPDoc(){
    		super();
    	}

    	/**
    	 * Esta é a função que formata o input do 
    	 * usuário de modo que ele não pode digitar
    	 * letras ou valores maiores que 255.
    	 */
    	public void insertString(int offs, String str, AttributeSet a) throws BadLocationException
        {
			if (str.length() != 1) return; // dont accept cut & paste
			if (offs == 3) return; // dont accept more than 3 chars
			char c = str.charAt(0);
			if ( !Character.isDigit(c) ) return; //accept only digits
			// Format: Max number 255
			if (offs == 2)
			{
				String aux;
				aux = this.getText(0,2) + str;
				c = aux.charAt(0);
				int i = (int)c - 48; // 48 == '0' na tabela ASCII
				if (i > 2) return; // Max 200
				if (i == 2)
				{
					c = aux.charAt(1);
					i = (int)c - 48;
					if (i > 5) return; // Max 250
					if (i == 5)
					{
						c = aux.charAt(2);
						i = (int)c - 48;
						if (i > 5) return; // Max 255
					}
				}
			}
			super.insertString(offs,str,a);
        }
    }

}