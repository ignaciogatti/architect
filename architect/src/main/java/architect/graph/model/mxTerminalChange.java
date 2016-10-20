package architect.graph.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;

@XmlAccessorType(XmlAccessType.NONE)
public class mxTerminalChange {

	@XmlAttribute
	private String cell;
	
	@XmlAttribute
	private String terminal;
	
	@XmlAttribute
	private Integer source;
	
	public mxTerminalChange() {
		
	}
	
	public mxTerminalChange(String cell, String terminal, Integer source) {
		this.cell = cell;
		this.terminal = terminal;
		this.source = source;
	}

	public String getCell() {
		return cell;
	}

	public void setCell(String cell) {
		this.cell = cell;
	}

	public String getTerminal() {
		return terminal;
	}

	public void setTerminal(String terminal) {
		this.terminal = terminal;
	}

	public Integer getSource() {
		return source;
	}

	public void setSource(Integer source) {
		this.source = source;
	}	
	
}
