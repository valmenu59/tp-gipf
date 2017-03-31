package gipf;

/**
 * Représente une exception lors de l'inscription d'un joueur
 */
public class InscriptionException extends Exception {
	private static final long serialVersionUID = -1693277274726563119L;

	public InscriptionException(String message) {
		super(message);
	}
}
