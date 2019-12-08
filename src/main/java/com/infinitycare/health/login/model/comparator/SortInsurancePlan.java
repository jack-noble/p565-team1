package com.infinitycare.health.login.model.comparator;

import com.infinitycare.health.login.model.IpPlanDetails;

import java.util.Comparator;

public class SortInsurancePlan implements Comparator<IpPlanDetails> {

    @Override
    public int compare(IpPlanDetails plan1, IpPlanDetails plan2) {
        return Float.parseFloat(plan1.getPremium()) < Float.parseFloat(plan2.getPremium()) ? -1 : 1;
    }
}
