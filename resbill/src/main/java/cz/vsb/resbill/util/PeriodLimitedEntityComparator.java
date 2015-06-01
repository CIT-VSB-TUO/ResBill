package cz.vsb.resbill.util;

import java.util.Comparator;

import org.apache.commons.lang3.builder.CompareToBuilder;

import cz.vsb.resbill.model.PeriodLimitedEntity;

public class PeriodLimitedEntityComparator implements Comparator<PeriodLimitedEntity> {

	public static PeriodLimitedEntityComparator INSTANCE = new PeriodLimitedEntityComparator();

	private PeriodLimitedEntityComparator() {
	}

	@Override
	public int compare(PeriodLimitedEntity o1, PeriodLimitedEntity o2) {
		CompareToBuilder cmpBuilder = new CompareToBuilder();
		cmpBuilder.append(o1.getPeriod(), o2.getPeriod());
		return cmpBuilder.toComparison();
	}

}
