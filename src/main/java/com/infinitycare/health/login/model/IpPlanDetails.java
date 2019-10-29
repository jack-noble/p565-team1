package com.infinitycare.health.login.model;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "IPlanDetails")
public class IpPlanDetails {

    public String _id;
    public String mName;
    public String mProvider;
    public String mPrice;
    public String mDetails;

    public IpPlanDetails(String mName, String mProvider) {
        this._id = Integer.toString(mName.hashCode());
        this.mName = mName;
        this.mProvider = mProvider;
        this.mPrice = "";
        this.mDetails = "";
    }

}
