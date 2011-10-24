package admixture.population.scheme;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import admixture.AdmixtureConstant;
import admixture.population.AlleleFrequencyReader;
import admixture.population.GeneFlowGenerateColony;
import admixture.population.genome.DNAStirrer;
import admixture.population.genome.GeneFlow;
import admixture.population.genome.HotSpot;
import admixture.population.genome.chromosome.ChromosomeGenerator;
import admixture.population.phenotype.PhenotypeGenerator;
import admixture.population.phenotype.QualityControl;

public class AdmixedModel {
	public static void main(String[] args) {

		Parameter p = new Parameter();
		p.commandListenor(args);
		
		String unifiedunrelated = "unrelated";
		File file = new File(p.dir + unifiedunrelated);
		file.mkdir();
		String dir_unified_unrelated = p.dir + unifiedunrelated + System.getProperty("file.separator");

		String unified = "unified";
		file = new File(p.dir + unified);
		file.mkdir();
		String dir_unified = p.dir + unified + System.getProperty("file.separator");
		
		String fam = "family";
		file = new File(p.dir + fam);
		file.mkdir();
		String dir_fam = p.dir + fam + System.getProperty("file.separator");
		
		String c_c = "cc";
		file = new File(p.dir + c_c);
		file.mkdir();
		String dir_cc = p.dir + c_c + System.getProperty("file.separator");
		
		long seed = p.seed;
		int control_chr = p.control_chr;
		boolean isNullHypothesis = p.isNullHypothesis;

		// specific components
		// family
		Integer N_aff_parent = new Integer(p.affectedParent);
		int N_Fam = p.family[0];
		int N_Kid = p.kid[0];
		int N_aff_Kid = p.affectedKid[0];

		int N_case = p.cases[0];
		int N_control = p.controls[0];

		// logistic regression
		String[] f = p.genotypeFunction;
		double[] g_e = p.genotypeEffect;
		int[] chr = p.diseaseChr;
		int[] loci = p.diseaseLocus;
		double mu = p.mu;
		double c_eff = p.covariateEffect;
		double c_dev = p.covariateSD;

		double[] prevalence = p.popPrevalence;
		int N_phe = 3;

		String[] chr_file = p.AIM_file[0];
		int[] AIM_number = p.aim;
		double[] pop_proportion = p.popProportion;

		PhenotypeGenerator pg = new PhenotypeGenerator(f, g_e, chr, loci, mu, c_eff, c_dev);
		HotSpot hs = new HotSpot();

		ArrayList<DNAStirrer> DNAPool = new ArrayList<DNAStirrer>();
		ArrayList<GeneFlow> GF = new ArrayList<GeneFlow>();
		ArrayList<ChromosomeGenerator> CG = new ArrayList<ChromosomeGenerator>();
		for (int i = 0; i < chr_file.length; i++) {
			AlleleFrequencyReader afr = new AlleleFrequencyReader(chr_file[i], AIM_number[i]);
			DNAStirrer ds = new DNAStirrer(afr, 1, 10000, AdmixtureConstant.Without_Genetic_Drift, pop_proportion);
			ds.DNAStir(1);
			DNAPool.add(ds);
			GeneFlow gf = new GeneFlow(afr, 10000, pop_proportion, hs);
			gf.mating(p.generation);
			GF.add(gf);
			ChromosomeGenerator cg = new ChromosomeGenerator(afr.getAlleleFreq(), pop_proportion);
			cg.setSeed(seed + i);
			CG.add(cg);
		}

		GeneFlowGenerateColony GC = new GeneFlowGenerateColony.Builder().DNAPool(DNAPool).diesaseRate(prevalence)
				.hotSpot(hs).popProportion(pop_proportion).numPhenotype(N_phe).ChrGenerator(CG).PheGenerator(pg).GeneFlow(GF).seed(
						seed).diseaseChr(control_chr).isNullHypothesis(isNullHypothesis).build();

		for (int rep = 0; rep < p.simulation; rep++) {
			QualityControl qc = new QualityControl(N_aff_Kid, N_aff_parent, p.samplingScheme);
			QualityControl qc_c = new QualityControl(N_case, N_control, AdmixtureConstant.CaseControl);
			GeneFlowGenerateColony.setCurrFamilyID(0);
			GC.GenerateFamHab(N_Fam, N_Kid, qc);
			GC.GenerateCCHab(N_case + N_control, 1, qc_c);

			try {
				StringBuilder Gsb_u = new StringBuilder(rep + "ped.txt");
				Gsb_u.insert(0, dir_unified_unrelated);
				StringBuilder Psb_u = new StringBuilder(rep + "phe.txt");
				Psb_u.insert(0, dir_unified_unrelated);
				StringBuilder L_Gsb_u = new StringBuilder(rep + "L_ped.txt");
				L_Gsb_u.insert(0, dir_unified_unrelated);
				StringBuilder L_Psb_u = new StringBuilder(rep + "L_phe.txt");
				L_Psb_u.insert(0, dir_unified_unrelated);
				GC.printGenotype2file(Gsb_u.toString(), Psb_u.toString(), !AdmixtureConstant.printAllele,
						!AdmixtureConstant.printLinked);
				GC.printGenotype2file(L_Gsb_u.toString(), L_Psb_u.toString(), AdmixtureConstant.printAllele,
						AdmixtureConstant.printLinked);

				StringBuilder Gsb = new StringBuilder(rep + "ped.txt");
				Gsb.insert(0, dir_unified);
				StringBuilder Psb = new StringBuilder(rep + "phe.txt");
				Psb.insert(0, dir_unified);
				StringBuilder L_Gsb = new StringBuilder(rep + "L_ped.txt");
				L_Gsb.insert(0, dir_unified);
				StringBuilder L_Psb = new StringBuilder(rep + "L_phe.txt");
				L_Psb.insert(0, dir_unified);
				GC.printGenotype2file(Gsb.toString(), Psb.toString(), !AdmixtureConstant.printAllele,
						!AdmixtureConstant.printLinked);
				GC.printGenotype2file(L_Gsb.toString(), L_Psb.toString(), AdmixtureConstant.printAllele,
						AdmixtureConstant.printLinked);

				StringBuilder F_Gsb = new StringBuilder(rep + "ped.txt");
				F_Gsb.insert(0, dir_fam);
				StringBuilder F_Psb = new StringBuilder(rep + "phe.txt");
				F_Psb.insert(0, dir_fam);
				StringBuilder L_F_Gsb = new StringBuilder(rep + "L_ped.txt");
				L_F_Gsb.insert(0, dir_fam);
				StringBuilder L_F_Psb = new StringBuilder(rep + "L_phe.txt");
				L_F_Psb.insert(0, dir_fam);
				GC.printFamilyGenotype2file(F_Gsb.toString(), F_Psb.toString(), !AdmixtureConstant.printAllele,
						!AdmixtureConstant.printLinked);
				GC.printFamilyGenotype2file(L_F_Gsb.toString(), L_F_Psb.toString(), AdmixtureConstant.printAllele,
						AdmixtureConstant.printLinked);

				StringBuilder C_Gsb = new StringBuilder(rep + "ped.txt");
				C_Gsb.insert(0, dir_cc);
				StringBuilder C_Psb = new StringBuilder(rep + "phe.txt");
				C_Psb.insert(0, dir_cc);
				StringBuilder L_C_Gsb = new StringBuilder(rep + "L_ped.txt");
				L_C_Gsb.insert(0, dir_cc);
				StringBuilder L_C_Psb = new StringBuilder(rep + "L_phe.txt");
				L_C_Psb.insert(0, dir_cc);
				GC.printCCGenotype2file(C_Gsb.toString(), C_Psb.toString(), !AdmixtureConstant.printAllele,
						!AdmixtureConstant.printLinked);
				GC.printCCGenotype2file(L_C_Gsb.toString(), L_C_Psb.toString(), AdmixtureConstant.printAllele,
						AdmixtureConstant.printLinked);

			} catch (IOException e) {
				e.printStackTrace(System.err);
			}
		}
	}
}