package gear.subcommands.locus;

import java.io.PrintStream;
import java.text.DecimalFormat;

import gear.family.GenoMatrix.GenotypeMatrix;
import gear.family.pedigree.file.SNP;
import gear.family.plink.PLINKParser;
import gear.family.qc.rowqc.SampleFilter;
import gear.subcommands.CommandArguments;
import gear.subcommands.CommandImpl;
import gear.util.FileUtil;
import gear.util.Logger;
import gear.util.pop.PopStat;

public class LocusCommandImpl extends CommandImpl {
	private GenotypeMatrix pGM;
	private double[][] allelefreq;
	private double[] allelevar;
	private LocusCommandArguments locusArgs;

	@Override
	public void execute(CommandArguments cmdArgs) {
		locusArgs = (LocusCommandArguments) cmdArgs;

		PLINKParser pp = PLINKParser.parse(locusArgs);
		SampleFilter sf = new SampleFilter(pp.getPedigreeData(), cmdArgs);
		pGM = new GenotypeMatrix(sf.getSample(), pp.getMapData(), cmdArgs);

		allelefreq = PopStat.calAlleleFrequency(pGM);
		allelevar = PopStat.calGenoVariance(pGM);
		printResult();
	}

	private void printResult() {
		DecimalFormat fmt = new DecimalFormat("0.0000");
		DecimalFormat fmtp = new DecimalFormat("0.00E000");
		PrintStream LocusPrint = FileUtil.CreatePrintStream(this.locusArgs.getOutRoot() + ".locus");
		LocusPrint.println("SNP\tCHR\tBP\tRefAllele\tAltAllele\tFreq\tVar\tEVar\tnChr");

		for (int i = 0; i < pGM.getSNPList().size(); i++) {
			SNP snp = pGM.getSNPList().get(i);
			double eVar = locusArgs.isInbred() ? 4 * allelefreq[i][0] * allelefreq[i][1]
					: 2 * allelefreq[i][0] * allelefreq[i][1];
			LocusPrint.println(snp.getName() + "\t" + snp.getChromosome() + "\t" + snp.getPosition() + "\t"
					+ snp.getFirstAllele() + "\t" + snp.getSecAllele() + "\t"
					+ (allelefreq[i][0] > 0.0001 ? fmt.format(allelefreq[i][0]) : fmtp.format(allelefreq[i][0])) + "\t"
					+ (allelevar[i] > 0.0001 ? fmt.format(allelevar[i]) : fmtp.format(allelevar[i])) + "\t"
					+ (eVar > 0.001 ? fmt.format(eVar) : fmtp.format(eVar)) + "\t"
					+ (int) ((1 - allelefreq[i][2]) * pGM.getNumIndivdial() * 2));
		}
		LocusPrint.close();
		Logger.printUserLog("Save results to " + locusArgs.getOutRoot() + ".locus.");
	}
}
