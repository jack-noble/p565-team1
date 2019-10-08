import React from 'react';
import { Link } from "react-router-dom";
import Axios from "axios";
// material-ui components
import { makeStyles } from "@material-ui/core/styles";
import Slide from "@material-ui/core/Slide";
import Dialog from "@material-ui/core/Dialog";
import DialogTitle from "@material-ui/core/DialogTitle";
import DialogContent from "@material-ui/core/DialogContent";
import DialogActions from "@material-ui/core/DialogActions";
import IconButton from "@material-ui/core/IconButton";
import InputAdornment from "@material-ui/core/InputAdornment";
import Icon from "@material-ui/core/Icon";
// @material-ui/icons
import Close from "@material-ui/icons/Close";
import Email from "@material-ui/icons/Email";
import LocalHospital from "@material-ui/icons/LocalHospital";
import People from "@material-ui/icons/People";
import Note from "@material-ui/icons/Note";
// core components
import Button from "components/CustomButtons/Button.js";
import Card from "components/Card/Card.js";
import CardBody from "components/Card/CardBody.js";
import CardHeader from "components/Card/CardHeader.js";
import CustomInput from "components/CustomInput/CustomInput.js";
import GridContainer from "components/Grid/GridContainer.js";
import GridItem from "components/Grid/GridItem.js";
import CustomLinearProgress from "components/CustomLinearProgress/CustomLinearProgress.js";
import SignupButton from "views/Components/Buttons/SignupButton";
import InfoArea from "components/InfoArea/InfoArea.js";

import modalStyles from "assets/jss/material-kit-react/modalStyle.js";
import loginStyles from "assets/jss/material-kit-react/views/loginPage.js";

const Transition = React.forwardRef(function Transition(props, ref) {
  return <Slide direction="down" ref={ref} {...props} />;
});

const useModalStyles = makeStyles(modalStyles);
const useLoginStyles = makeStyles(loginStyles);

export default function SignupForm() {
  const [modal, setModal] = React.useState(true);
  const [cardAnimaton, setCardAnimation] = React.useState("cardHidden");
  const modalClasses = useModalStyles();
  const loginClasses = useLoginStyles();
  setTimeout(function() {
    setCardAnimation("");
  }, 700);
  
  return (
    <div>
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
          <Link to="/">
          <IconButton
            className={modalClasses.modalCloseButton}
            key="close"
            aria-label="Close"
            color="inherit"
            onClick={() => setModal(false)}
          >
            <Close className={modalClasses.modalClose} />
          </IconButton>
          </Link>
          <h4 className={modalClasses.modalTitle}>Sign Up</h4>
        </DialogTitle>
        <DialogContent
          id="modal-slide-description"
          className={modalClasses.modalBody}
        >
          <SignupButton/>
        </DialogContent>
        <CustomLinearProgress
            variant="determinate"
            color="primary"
            value={100}
          />
      </Dialog>
    </div>
  );
}