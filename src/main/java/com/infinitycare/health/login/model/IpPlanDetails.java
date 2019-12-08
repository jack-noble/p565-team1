package com.infinitycare.health.login.model;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "IPlanDetails")
public class IpPlanDetails {

    public String _id;
    public String mName;
    public String mProvider;

    private String mPremium;
    private String mDeductible;
    private String mCoPayment;
    private String mAnnualOutOfPocketLimit;
    private String mLevel;

    private String mFirstNameOfCreator;
    private String mLastNameOfCreator;

    public IpPlanDetails(String mName, String mProvider) {
        this._id = Integer.toString(mName.hashCode());
        this.mName = mName;
        this.mProvider = mProvider;
        this.mPremium = "0";
        this.mDeductible = "0";
        this.mCoPayment = "0";
        this.mAnnualOutOfPocketLimit = "0";
        this.mLevel = "";
        this.mFirstNameOfCreator = "";
        this.mLastNameOfCreator = "";
    }

    @Override
    public boolean equals(Object o) {
        return this.mName.equals(((IpPlanDetails)o).getName());
    }

    @Override
    public int hashCode() {
        return this.mName.hashCode();
    }

    public String getName() {
        return this.mName;
    }

    public String getPremium() {
        return mPremium;
    }

    public String getDeductible() {
        return mDeductible;
    }

    public String getCoPayment() {
        return mCoPayment;
    }

    public String getAnnualOutOfPocketLimit() {
        return mAnnualOutOfPocketLimit;
    }

    public void setPremium(String premium) {
        mPremium = premium;
    }

    public void setDeductible(String mDeductible) {
        this.mDeductible = mDeductible;
    }

    public void setCoPayment(String mCoPayment) {
        this.mCoPayment = mCoPayment;
    }

    public void setAnnualOutOfPocketLimit(String mAnnualOutOfPocketLimit) {
        this.mAnnualOutOfPocketLimit = mAnnualOutOfPocketLimit;
    }

    public void setLevel(String mLevel) {
        this.mLevel = mLevel;
    }

    public String getLevel() {
        return mLevel;
    }

    public int getWeightOfTheLevel() {
        if("bronze".equalsIgnoreCase(mLevel)) {
            return 0;
        } else if("silver".equalsIgnoreCase(mLevel)) {
            return 1;
        } else if("gold".equalsIgnoreCase(mLevel)) {
            return 2;
        } else if("platinum".equalsIgnoreCase(mLevel)) {
            return 3;
        }
        //If the older plans does not contain a level, I'll assume it to be silver for now
        return 1;
    }

    public String getFirstNameOfCreator() {
        return mFirstNameOfCreator;
    }

    public void setFirstNameOfCreator(String firstNameOfCreator) {
        this.mFirstNameOfCreator = firstNameOfCreator;
    }

    public String getLastNameOfCreator() {
        return mLastNameOfCreator;
    }

    public void setLastNameOfCreator(String lastNameOfCreator) {
        this.mLastNameOfCreator = lastNameOfCreator;
    }
}
