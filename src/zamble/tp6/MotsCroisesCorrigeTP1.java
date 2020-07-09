package leveque.zamble.tp6;

public class MotsCroisesCorrigeTP1 implements SpecifMotsCroises
{
	public Grille<Character> solution ;
	private Grille<Character> proposition ;
	private Grille<String> horizontal ;
	private Grille<String> vertical ;

	public MotsCroisesCorrigeTP1(int hauteur, int largeur)
	{
		solution = new Grille<Character> (hauteur, largeur) ;
		proposition = new Grille<Character> (hauteur, largeur) ;
		horizontal = new Grille<String> (hauteur, largeur) ;
		vertical = new Grille<String> (hauteur, largeur) ;
		for (int lig=1; lig<=getHauteur(); lig++)
		{
			for (int col=1; col<=getLargeur(); col++)
			{
				setCaseNoire(lig, col, true);
			}
		}
	}

	@Override
	public int getHauteur()
	{
		return solution.getHauteur() ;
	}

	@Override
	public int getLargeur()
	{
		return solution.getLargeur() ;
	}

	public boolean coordCorrectes(int lig, int col)
	{
		return 1<=lig && lig<=getHauteur()
				 && 1<=col && col<=getLargeur() ;
	}

	@Override
	public boolean estCaseNoire(int lig, int col)
	{
		assert coordCorrectes(lig, col) ;
		return (solution.getCellule(lig, col) == null) ;
	}

	@Override
	public void setCaseNoire(int lig, int col, boolean noire)
	{
		assert coordCorrectes(lig, col) ;
		if (noire)
		{
			solution.setCellule(lig, col, null) ;
		}
		else if (solution.getCellule(lig, col) == null)
		{
			solution.setCellule(lig, col,  new Character(' ')) ;
		}
	}

	@Override
	public char getSolution(int lig, int col)
	{
		assert coordCorrectes(lig, col) ;
		assert !estCaseNoire(lig, col) ;
		return solution.getCellule(lig, col) ;
	}

	@Override
	public void setSolution(int lig, int col, char sol)
	{
		assert coordCorrectes(lig, col) ;
		assert !estCaseNoire(lig, col) ;
		setSol(lig, col, sol) ;
	}

	private void setSol(int lig, int col, char sol)
	{
		assert coordCorrectes(lig, col) ;
		assert !estCaseNoire(lig, col) ;
		solution.setCellule(lig, col, sol) ;
	}

	@Override
	public char getProposition(int lig, int col)
	{
		assert coordCorrectes(lig, col) ;
		assert !estCaseNoire(lig, col) ;
		return proposition.getCellule(lig, col) ;
	}

	@Override
	public void setProposition(int lig, int col, char prop)
	{
		assert coordCorrectes(lig, col) ;
		assert !estCaseNoire(lig, col) ;
		proposition.setCellule(lig, col, prop) ;
	}

	@Override
	public String getDefinition(int lig, int col, boolean horiz)
	{
		assert coordCorrectes(lig, col) ;
		assert !estCaseNoire(lig, col) ;
		if (horiz)
		{
			return horizontal.getCellule(lig, col) ;
		}
		else
		{
			return vertical.getCellule(lig, col) ;
		}
	}

	@Override
	public void setDefinition(int lig, int col, boolean horiz, String def)
	{
		assert coordCorrectes(lig, col) ;
		assert !estCaseNoire(lig, col) ;
		if (horiz)
		{
			horizontal.setCellule(lig, col, def) ;
		}
		else
		{
			vertical.setCellule(lig, col, def) ;
		}
	}

	@Override
	public String toString()
	{
		return "Solution\n" + solution
					+ "\nProposition\n" + proposition
					+ "\nHorizontal\n" + horizontal
					+ "\nVertical\n" + vertical ;
	}

}
