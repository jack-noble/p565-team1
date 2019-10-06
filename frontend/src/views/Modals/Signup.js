import React from 'react';
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
// core components
import Button from "components/CustomButtons/Button.js";
import Card from "components/Card/Card.js";
import CardBody from "components/Card/CardBody.js";
import CardHeader from "components/Card/CardHeader.js";
import CustomInput from "components/CustomInput/CustomInput.js";
import GridContainer from "components/Grid/GridContainer.js";
import GridItem from "components/Grid/GridItem.js";

import modalStyles from "assets/jss/material-kit-react/modalStyle.js";

const Transition = React.forwardRef(function Transition(props, ref) {
  return <Slide direction="down" ref={ref} {...props} />;
});

const useStyles = makeStyles(modalStyles);

export default function Modal() {
  const [modal, setModal] = React.useState(false);
  const classes = useStyles();
  const [cardAnimaton, setCardAnimation] = React.useState("cardHidden");
  setTimeout(function() {
    setCardAnimation("");
  }, 700);
  return (
    <div>
        <Button color="primary" size="sm" simple onClick={() => setModal(true)}>
          Sign up
        </Button>
      <Dialog
        classes={{
          root: classes.center,
          paper: classes.modal
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
          className={classes.modalHeader}
        >
          <IconButton
            className={classes.modalCloseButton}
            key="close"
            aria-label="Close"
            color="inherit"
            onClick={() => setModal(false)}
          >
            <Close className={classes.modalClose} />
          </IconButton>
          <h4 className={classes.modalTitle}>Sign Up</h4>
        </DialogTitle>
        <DialogContent
          id="modal-slide-description"
          className={classes.modalBody}
        >
        <div className={classes.container}>
            <GridContainer justify="center">
                <GridItem xs={12} sm={12} md={4}>
                <Card className={classes[cardAnimaton]}>
                    <form className={classes.form}>
                    <CardHeader color="primary" className={classes.cardHeader}>
                        <h4>Sign up with</h4>
                        <div className={classes.socialLine}>
                        <Button
                            justIcon
                            href="#pablo"
                            target="_blank"
                            color="transparent"
                            onClick={e => e.preventDefault()}
                        >
                            <i className={"fab fa-google"} />
                        </Button>
                        </div>
                    </CardHeader>
                    <CardBody>
                        <CustomInput
                        labelText="Email..."
                        id="email"
                        formControlProps={{
                            fullWidth: true
                        }}
                        inputProps={{
                            type: "email",
                            endAdornment: (
                            <InputAdornment position="end">
                                <Email className={classes.inputIconsColor} />
                            </InputAdornment>
                            )
                        }}
                        />
                        <CustomInput
                        labelText="Password"
                        id="pass"
                        formControlProps={{
                            fullWidth: true
                        }}
                        inputProps={{
                            type: "password",
                            endAdornment: (
                            <InputAdornment position="end">
                                <Icon className={classes.inputIconsColor}>
                                lock_outline
                                </Icon>
                            </InputAdornment>
                            ),
                            autoComplete: "off"
                        }}
                        />
                    </CardBody>
                    </form>
                </Card>
                </GridItem>
            </GridContainer>
        </div>
        </DialogContent>
        <DialogActions
          className={classes.modalFooter + " " + classes.modalFooterCenter}
        >
          <Button onClick={() => setModal(false)} color="success">
            Sign
          </Button>
        </DialogActions>
      </Dialog>
    </div>
  );
}