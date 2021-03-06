package gear.subcommands.glsmeta;

import gear.subcommands.Command;
import gear.subcommands.CommandArgumentException;
import gear.subcommands.CommandArguments;
import gear.subcommands.CommandImpl;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;

public class GLSMetaCommand extends Command {
	public GLSMetaCommand() {
		addAlias("glsmeta");
	}

	@Override
	public String getName() {
		return "glsmeta";
	}

	@Override
	public String getDescription() {
		return "Calculate Lambda deflation parameter for a pair of meta-analysis";
	}

	@SuppressWarnings("static-access")
	@Override
	public void prepareOptions(Options options) {
		options.addOption(
				OptionBuilder.withDescription(OPT_META_DESC).withLongOpt(OPT_META_LONG).hasArgs().create(OPT_META));
		options.addOption(OptionBuilder.withDescription(OPT_META_BATCH_DESC).withLongOpt(OPT_META_BATCH_LONG).hasArg()
				.create(OPT_META_BATCH));
		options.addOption(OptionBuilder.withDescription(OPT_META_GZ_DESC).withLongOpt(OPT_META_GZ_LONG).hasArgs()
				.create(OPT_META_GZ));
		options.addOption(OptionBuilder.withDescription(OPT_META_GZ_BATCH_DESC).withLongOpt(OPT_META_GZ_BATCH_LONG)
				.hasArg().create(OPT_META_GZ_BATCH));

		options.addOption(OptionBuilder.withDescription(OPT_CC_DESC).hasArgs().create(OPT_CC));
		options.addOption(
				OptionBuilder.withDescription(OPT_CC_SIZE_LONG_DESC).withLongOpt(OPT_CC_SIZE_LONG).hasArg().create());
		options.addOption(OptionBuilder.withDescription(OPT_QT_DESC).hasArgs().create(OPT_QT));
		options.addOption(
				OptionBuilder.withDescription(OPT_QT_SIZE_LONG_DESC).withLongOpt(OPT_QT_SIZE_LONG).hasArg().create());

		options.addOption(OptionBuilder.withDescription(OPT_KEY_DESC).hasArgs().create(OPT_KEY));

		options.addOption(OptionBuilder.withDescription(OPT_CM_DESC).hasArg().create(OPT_CM));
		options.addOption(OptionBuilder.withDescription(OPT_GC_DESC).create(OPT_GC));
		options.addOption(OptionBuilder.withDescription(OPT_GC_ALL_LONG_DESC).withLongOpt(OPT_GC_ALL_LONG).create());

		// options.addOption(OptionBuilder.withDescription(OPT_GC_INFLATION_ONLY_LONG_DESC).withLongOpt(OPT_GC_INFLATION_ONLY_LONG).create(OPT_GC_INFLATION_ONLY));
		options.addOption(OptionBuilder.withDescription(OPT_KEEPATGC_LONG_DESC).withLongOpt(OPT_KEEPATGC_LONG)
				.create(OPT_KEEPATGC));
		options.addOption(OptionBuilder.withDescription(OPT_FULL_SNP_ONLY_LONG_DESC).withLongOpt(OPT_FULL_SNP_ONLY_LONG)
				.create(OPT_FULL_SNP_ONLY));

		options.addOption(OptionBuilder.withDescription(OPT_KEEP_COHORT_LONG_DESC).withLongOpt(OPT_KEEP_COHORT_LONG)
				.hasArg().create(OPT_KEEP_COHORT));
		options.addOption(OptionBuilder.withDescription(OPT_REMOVE_COHORT_LONG_DESC).withLongOpt(OPT_REMOVE_COHORT_LONG)
				.hasArg().create(OPT_REMOVE_COHORT));

		options.addOption(OptionBuilder.withDescription(OPT_DIAG_COHORT_DESC).create(OPT_DIAG_COHORT));
		options.addOption(OptionBuilder.withDescription(OPT_NAIVE_DESC).create(OPT_NAIVE));

		options.addOption(OptionBuilder.withDescription(OPT_ADJUST_OVERLAPPING_LONG_DESC)
				.withLongOpt(OPT_ADJUST_OVERLAPPING_LONG).create());

		options.addOption(OptionBuilder.withDescription(OPT_COVAR_DESC).hasArg().create(OPT_COVAR));
		options.addOption(OptionBuilder.withDescription(OPT_COVAR_NUMBER_LONG_DESC).withLongOpt(OPT_COVAR_NUMBER_LONG)
				.hasArgs().create());
		options.addOption(OptionBuilder.withDescription(OPT_CENTER_LONG_DESC).withLongOpt(OPT_CENTER_LONG).create());

		options.addOption(OptionBuilder.withDescription(OPT_CHR_DESC).withLongOpt(OPT_CHR_LONG).hasArg().create());
		options.addOption(
				OptionBuilder.withDescription(OPT_NOT_CHR_DESC).withLongOpt(OPT_NOT_CHR_LONG).hasArg().create());
	}

	@Override
	public CommandArguments parse(CommandLine cmdLine) throws CommandArgumentException {
		GLSMetaCommandArguments lamD = new GLSMetaCommandArguments();
		parseSNPFilterChromosomeArguments(lamD, cmdLine);
		if (cmdLine.hasOption(OPT_META)) {
			lamD.setMetaFile(cmdLine.getOptionValues(OPT_META));
			lamD.setGZ(false);
			if (cmdLine.hasOption(OPT_QT)) {
				lamD.setQT(cmdLine.getOptionValues(OPT_QT));
			}
			if (cmdLine.hasOption(OPT_CC)) {
				lamD.setCC(cmdLine.getOptionValues(OPT_CC));
			}
		}

		// batch text meta batch
		if (cmdLine.hasOption(OPT_META_BATCH)) {
			lamD.setMetaBatch(cmdLine.getOptionValue(OPT_META_BATCH));
			lamD.setGZ(false);
			if (cmdLine.hasOption(OPT_QT_SIZE_LONG)) {
				lamD.setQTbatch(cmdLine.getOptionValue(OPT_QT_SIZE_LONG));
			}
			if (cmdLine.hasOption(OPT_CC_SIZE_LONG)) {
				lamD.setCCbatch(cmdLine.getOptionValue(OPT_CC_SIZE_LONG));
			}
		}

		if (cmdLine.hasOption(OPT_GC)) {
			lamD.setGC();
		}

		if (cmdLine.hasOption(OPT_GC_ALL_LONG)) {
			lamD.setGCALL();
		}

		// if(cmdLine.hasOption(OPT_GC_INFLATION_ONLY))
		// {
		// lamD.setGCInflationOnly();
		// }

		if (cmdLine.hasOption(OPT_KEEPATGC)) {
			lamD.setATGC();
		}

		if (cmdLine.hasOption(OPT_FULL_SNP_ONLY)) {
			lamD.setFullSNPOnly();
		}

		// manual gzip meta files
		if (cmdLine.hasOption(OPT_META_GZ)) {
			lamD.setMetaFile(cmdLine.getOptionValues(OPT_META_GZ));
			lamD.setGZ(true);
			if (cmdLine.hasOption(OPT_QT)) {
				lamD.setQT(cmdLine.getOptionValues(OPT_QT));
			}
			if (cmdLine.hasOption(OPT_CC)) {
				lamD.setCC(cmdLine.getOptionValues(OPT_CC));
			}
		}

		// batch gzip meta files
		if (cmdLine.hasOption(OPT_META_GZ_BATCH)) {
			lamD.setMetaBatch(cmdLine.getOptionValue(OPT_META_GZ_BATCH));
			lamD.setGZ(true);
			if (cmdLine.hasOption(OPT_QT_SIZE_LONG)) {
				lamD.setQTbatch(cmdLine.getOptionValue(OPT_QT_SIZE_LONG));
			}
			if (cmdLine.hasOption(OPT_CC_SIZE_LONG)) {
				lamD.setCCbatch(cmdLine.getOptionValue(OPT_CC_SIZE_LONG));
			}
		}

		if (cmdLine.hasOption(OPT_KEY)) {
			lamD.setKey(cmdLine.getOptionValues(OPT_KEY));
		}

		if (cmdLine.hasOption(OPT_CM)) {
			lamD.setCM(cmdLine.getOptionValue(OPT_CM));
		}

		if (cmdLine.hasOption(OPT_KEEP_COHORT)) {
			lamD.setKeepCohortFile(cmdLine.getOptionValue(OPT_KEEP_COHORT));
		}

		if (cmdLine.hasOption(OPT_REMOVE_COHORT)) {
			lamD.setRemoveCohortFile(cmdLine.getOptionValue(OPT_REMOVE_COHORT));
		}

		if (cmdLine.hasOption(OPT_NAIVE)) {
			lamD.setNaive();
		}

		if (cmdLine.hasOption(OPT_DIAG_COHORT)) {
			lamD.setDiag();
		}

		if (cmdLine.hasOption(OPT_ADJUST_OVERLAPPING_LONG)) {
			lamD.setAdjOverlapping();
		}

		if (cmdLine.hasOption(OPT_COVAR)) {
			lamD.setCovar(cmdLine.getOptionValue(OPT_COVAR));
		}

		if (cmdLine.hasOption(OPT_COVAR_NUMBER_LONG)) {
			lamD.setCovarNumber(cmdLine.getOptionValues(OPT_COVAR_NUMBER_LONG));
		}

		if (cmdLine.hasOption(OPT_CENTER_LONG)) {
			lamD.setCovCenter();
		}
		return lamD;
	}

	@Override
	protected CommandImpl createCommandImpl() {
		return new GLSMetaCommandImpl();
	}

	private final static String OPT_META = "m";
	private final static String OPT_META_LONG = "meta";
	private final static String OPT_META_DESC = "The summary statistic files";

	private final static String OPT_META_GZ = "mg";
	private final static String OPT_META_GZ_LONG = "meta-gz";
	private final static String OPT_META_GZ_DESC = "The summary statistic files in gz format";

	private final static String OPT_CC = "cc";
	private final static String OPT_CC_DESC = "Case-control study: #case 1, #ctrl 1, #case 2, #ctrl 2";

	private final static String OPT_QT = "qt";
	private final static String OPT_QT_DESC = "Quantitative trait: #sample size 1, #sample size 2";

	private final static String OPT_META_BATCH = "mb";
	private final static String OPT_META_BATCH_LONG = "meta-batch";
	private final static String OPT_META_BATCH_DESC = "The summary statistic batch";

	private final static String OPT_META_GZ_BATCH = "mgb";
	private final static String OPT_META_GZ_BATCH_LONG = "meta-gz-batch";
	private final static String OPT_META_GZ_BATCH_DESC = "The summary statistic files in gz format";

	private final static String OPT_CC_SIZE_LONG = "cc-size";
	private final static String OPT_CC_SIZE_LONG_DESC = "Case-control study: #case 1, #ctrl 1, #case 2, #ctrl 2";

	private final static String OPT_QT_SIZE_LONG = "qt-size";
	private final static String OPT_QT_SIZE_LONG_DESC = "Quantitative trait: #sample size 1, #sample size 2";

	private final static String OPT_KEY = "key";
	private final static String OPT_KEY_DESC = "Self defined key workds: snp, beta, se, a1, a2, chr, bp, p";

	private final static String OPT_CM = "cm";
	private final static String OPT_CM_DESC = "correlation matrix";

	private final static String OPT_GC = "gc";
	private final static String OPT_GC_DESC = "genomic control factor adjustment";

	private final static String OPT_GC_ALL_LONG = "gc-all";
	private final static String OPT_GC_ALL_LONG_DESC = "genomic control factor adjustment regardless gc > 1 or <1.";

	private final static String OPT_KEEPATGC = "atgc";
	private final static String OPT_KEEPATGC_LONG = "keep-atgc";
	private final static String OPT_KEEPATGC_LONG_DESC = "using all snps including A/T and G/C loci";

	private final static String OPT_FULL_SNP_ONLY = "fs";
	private final static String OPT_FULL_SNP_ONLY_LONG = "full-snp-only";
	private final static String OPT_FULL_SNP_ONLY_LONG_DESC = "Only the SNPs available to all cohorts will be printed";

	private final static String OPT_KEEP_COHORT = "kc";
	private final static String OPT_KEEP_COHORT_LONG = "keep-cohort";
	private final static String OPT_KEEP_COHORT_LONG_DESC = "Keep cohorts will be used for meta-analysis";

	private final static String OPT_REMOVE_COHORT = "rc";
	private final static String OPT_REMOVE_COHORT_LONG = "remove-cohort";
	private final static String OPT_REMOVE_COHORT_LONG_DESC = "Remove cohorts will be used for meta-analysis";

	private final static String OPT_DIAG_COHORT = "diag";
	private final static String OPT_DIAG_COHORT_DESC = "Prune the correlation matrix to make it positive definite";

	private final static String OPT_NAIVE = "naive";
	private final static String OPT_NAIVE_DESC = "Naive meta-analysis";

	private final static String OPT_ADJUST_OVERLAPPING_LONG = "adj-overlap";
	private final static String OPT_ADJUST_OVERLAPPING_LONG_DESC = "Only adjust for overlapping samples";

	private final static String OPT_COVAR = "covar";
	private final static String OPT_COVAR_DESC = "Specify the covariate file.";

	private final static String OPT_COVAR_NUMBER_LONG = "covar-number";
	private final static String OPT_COVAR_NUMBER_LONG_DESC = "Specify the covariate number";

	private final static String OPT_CENTER_LONG = "cov-center";
	private final static String OPT_CENTER_LONG_DESC = "Remove the mean of the covariance.";

}
