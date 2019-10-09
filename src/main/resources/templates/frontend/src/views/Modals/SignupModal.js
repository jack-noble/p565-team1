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
// core components
import CustomLinearProgress from "components/CustomLinearProgress/CustomLinearProgress.js";
import SignupButton from "views/Components/Buttons/SignupButton";

import modalStyles from "assets/jss/material-kit-react/modalStyle.js";

const Transition = React.forwardRef(function Transition(props, ref) {
  return <Slide direction="down" ref={ref} {...props} />;
});

const useModalStyles = makeStyles(modalStyles);

export default function SignupModal() {
  const [modal, setModal] = React.useState(true);
  const [cardAnimation, setCardAnimation] = React.useState("cardHidden");
  const modalClasses = useModalStyles();
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