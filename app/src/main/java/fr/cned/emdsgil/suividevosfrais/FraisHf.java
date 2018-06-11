package fr.cned.emdsgil.suividevosfrais;

import java.io.Serializable;

/**
 * Classe metier contenant la description d'un frais hors forfait
 *
 * @author Kevin Delcourt
 */
class FraisHf  implements Serializable {

	private final Float montant ;
	private final String motif ;
	private final Integer jour ;
	
	public FraisHf(Float montant, String motif, Integer jour) {
		this.montant = montant ;
		this.motif = motif ;
		this.jour = jour ;
	}

	public Float getMontant() {
		return montant;
	}

	public String getMotif() {
		return motif;
	}

	public Integer getJour() {
		return jour;
	}

	public String toString() {
		return this.montant + "!" + this.motif + "!" + this.jour;
	}

}
