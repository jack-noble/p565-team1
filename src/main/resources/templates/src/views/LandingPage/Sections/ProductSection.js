import React from "react";
// @material-ui/core components
import { makeStyles } from "@material-ui/core/styles";

// @material-ui/icons
import Chat from "@material-ui/icons/Chat";
import VerifiedUser from "@material-ui/icons/VerifiedUser";
import Fingerprint from "@material-ui/icons/Fingerprint";
// core components
import GridContainer from "components/Grid/GridContainer.js";
import GridItem from "components/Grid/GridItem.js";
import InfoArea from "components/InfoArea/InfoArea.js";

import styles from "assets/jss/material-kit-react/views/landingPageSections/productStyle.js";

const useStyles = makeStyles(styles);

export default function ProductSection() {
  const classes = useStyles();
  return (
    <div className={classes.section}>
      <GridContainer justify="center">
        <GridItem xs={12} sm={12} md={8}>
          <h2 className={classes.title}>One-stop-shop for health care</h2>
          <h5 className={classes.description}>
            We provide a patient-centric application that allows you to make informed decisions 
            regarding your health in the comfort of your own home. Some of our features include:
          </h5>
        </GridItem>
      </GridContainer>
      <div>
        <GridContainer>
          <GridItem xs={12} sm={12} md={4}>
            <InfoArea
              title="Free Chat"
              description="Live-chat with your doctors regarding questions, appointments and more.
              You can also live-chat with your healthcare representive to learn more about your insurance
              and other questions."
              icon={Chat}
              iconColor="info"
              vertical
            />
          </GridItem>
          <GridItem xs={12} sm={12} md={4}>
            <InfoArea
              title="Verified Doctors"
              description="All of the doctors on our network are verified and trusted. We do the research
              for you. Each doctor will be available for live-chat as well as any inquiries regarding your
              symptons, healthcare policy, etc."
              icon={VerifiedUser}
              iconColor="success"
              vertical
            />
          </GridItem>
          <GridItem xs={12} sm={12} md={4}>
            <InfoArea
              title="Secure"
              description="Your information security is our top priority. Any data regarding our users 
              are strictly kept within the system and affiliated doctors/insurances. Third-party authorization
              and sales of personal information is strictly forbidden."
              icon={Fingerprint}
              iconColor="danger"
              vertical
            />
          </GridItem>
        </GridContainer>
        <GridContainer justify="center">
          <GridItem xs={12} sm={12} md={8}>
            <h2 className={classes.title}>Featured plans/doctors</h2>
            <h5 className={classes.description}>
              TODO
            </h5>
          </GridItem>
        </GridContainer>
      </div>
    </div>
  );
}
