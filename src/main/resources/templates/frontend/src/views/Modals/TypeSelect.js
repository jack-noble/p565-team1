import React from 'react';
import { Link } from "react-router-dom";
// material-ui components
import { makeStyles } from "@material-ui/core/styles";
import Slide from "@material-ui/core/Slide";
import Dialog from "@material-ui/core/Dialog";
import DialogTitle from "@material-ui/core/DialogTitle";
import DialogContent from "@material-ui/core/DialogContent";
import IconButton from "@material-ui/core/IconButton";
// @material-ui/icons
import Close from "@material-ui/icons/Close";
import LocalHospital from "@material-ui/icons/LocalHospital";
import People from "@material-ui/icons/People";
import Note from "@material-ui/icons/Note";
// core components
import Button from "components/CustomButtons/Button.js";
import GridContainer from "components/Grid/GridContainer.js";
import GridItem from "components/Grid/GridItem.js";
import CustomLinearProgress from "components/CustomLinearProgress/CustomLinearProgress.js";
import InfoArea from "components/InfoArea/InfoArea.js";

import modalStyles from "assets/jss/material-kit-react/modalStyle.js";
import productStyles from "assets/jss/material-kit-react/views/landingPageSections/productStyle.js";

const Transition = React.forwardRef(function Transition(props, ref) {
  return <Slide direction="down" ref={ref} {...props} />;
});

const useModalStyles = makeStyles(modalStyles);
const useProductStyles = makeStyles(productStyles);

export default function TypeSelect() {
  const [modal, setModal] = React.useState(false);
  const modalClasses = useModalStyles();
  const productClasses = useProductStyles();

  return (
    <div>
      <Button color="primary" simple onClick={() => setModal(true)}>
        Sign up
      </Button>
      <Dialog
        modalClasses={{
          root: modalClasses.center,
          paper: modalClasses.modal
        }}
        open={modal}
        TransitionComponent={Transition}
        keepMounted
        onClose={() => setModal(false)}
        aria-labelledby="modal-slide-title"
        aria-describedby="modal-slide-description"
      >
        <DialogTitle
          id="classic-modal-slide-title"
          disableTypography
          className={modalClasses.modalHeader}
        >
          <IconButton
            className={modalClasses.modalCloseButton}
            key="close"
            aria-label="Close"
            color="inherit"
            onClick={() => setModal(false)}
          >
            <Close className={modalClasses.modalClose} />
          </IconButton>
          <h4 className={modalClasses.modalTitle}>Step 1</h4>
        </DialogTitle>
        <DialogContent
          id="modal-slide-description"
          className={modalClasses.modalBody}
        >
            <div className={productClasses.section} style={{padding: 0}}>
              <GridContainer justify="center">
                  <GridItem xs={12} sm={12} md={8}>
                  <h2 className={productClasses.title}>Are you a(n)...</h2>
                  </GridItem>
              </GridContainer>
              <GridContainer>
                <GridItem xs={12} sm={12} md={4}>
                  <Link to={"patient/signup"}>
                    <InfoArea
                    title="Patient"
                    icon={People}
                    iconColor="info"
                    vertical
                    />
                  </Link>
                </GridItem>
                <GridItem xs={12} sm={12} md={4}>
                  <Link to={"doctor/signup"}>
                    <InfoArea
                    title="Doctor"
                    icon={LocalHospital}
                    iconColor="danger"
                    vertical
                    />
                  </Link>
                </GridItem>
                <GridItem xs={12} sm={12} md={4}>
                  <Link to={"insurance/signup"}>
                    <InfoArea
                    title="Insurance Company"
                    icon={Note}
                    iconColor="warning"
                    vertical
                    />
                  </Link>
                </GridItem>
              </GridContainer>
            </div>
            <CustomLinearProgress
              variant="determinate"
              color="primary"
              value={50}
            />
        </DialogContent>
      </Dialog>
    </div>
  );
}