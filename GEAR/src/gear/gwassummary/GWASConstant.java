package gear.gwassummary;

public class GWASConstant
{
	public static final int SNP = 0;
	public static final int CHR = 1;
	public static final int BP = 2;
	public static final int BETA = 3;
	public static final int OR = 4;
	public static final int SE = 5;
	public static final int P = 6;
	public static final int A1 = 7;
	public static final int A2 = 8;
	
	public static boolean isNASNP(String snp)
	{
		String[] naSNP = initNASNP();
		for (String naStr : naSNP)
		{
			if (naStr.equalsIgnoreCase(snp))
			{
				return true;
			}
		}
		return false;
	}

	private static String[] initNASNP()
	{
		return new String[] { ".", "-", "na" };
	}

}
