package architect.model;

import java.util.ArrayList;
import java.util.List;

import architect.engine.architecture.TacticChange;

/**
 *  Clase utilizada para albergar una alternativa candidata utilizada en el proceso de negociacion
 */

public class NegotiationChoice {
	
	// Arquitectura resultante
	Architecture architecture;
	
	// Lista de cambios
	List<TacticChange> changes;
	
	// Costo de la alternativa para el agente que la genero
	Double cost;
	
	public NegotiationChoice(){
		architecture = null;
		changes = new ArrayList<TacticChange>();
		cost = null;
	}
	
	public NegotiationChoice(Architecture architecture, List<TacticChange> changes, Double cost){
		this.architecture = architecture;
		this.changes = changes;
		this.cost = cost;
	}
	
	public Architecture getArchitecture() {
		return architecture;
	}
	
	public void setArchitecture(Architecture architecture) {
		this.architecture = architecture;
	}
	
	public List<TacticChange> getChanges() {
		return changes;
	}
	
	public void setChanges(List<TacticChange> changes) {
		this.changes = changes;
	}
	
	public Double getCost() {
		return cost;
	}
	
	public void setCost(Double cost) {
		this.cost = cost;
	}
	
}
