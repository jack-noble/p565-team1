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
import InfoArea from "components/InfoArea/InfoArea.js";

import modalStyles from "assets/jss/material-kit-react/modalStyle.js";
import loginStyles from "assets/jss/material-kit-react/views/loginPage.js";

export default class SignupButton extends React.Component {
    constructor (props) {
        super(props);
        this.state = {
            email: '',
            password: '',
            cardAnimaton: 'cardHidden',
        };
    };
  
  handleEmailChange = event => {
    this.setState({ email: event.target.value });
  }

  handlePasswordChange = event => {
    this.setState({ password: event.target.value });
  }

  handleSubmit = event => {
    event.preventDefault();

    const user = {
        email: this.state.email,
        password: this.state.password
    };

    try {
        const response = Axios.post('https://infinity-care.herokuapp.com/Signup', { user });
        console.log('👉 Returned data:', response);
        console.log('👉 You tried to log:', user);
    } catch (e) {
        console.log(`😱 Axios request failed: ${e}`);
    }
  }

  render() {    
    return (
        <div style={{padding: 0}}>
            <GridContainer justify="center">
                <GridItem xs={12} sm={12} md={4}>
                <Card>
                    <form >
                    <CardHeader color="primary" >
                        <h4>Sign up with</h4>
                        <div >
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
                        <CustomInput onInput={console.log('You added to email', this.email)}
                        labelText="Email..."
                        id="email"
                        formControlProps={{
                            fullWidth: true
                        }}
                        inputProps={{
                            type: "email",
                            endAdornment: (
                            <InputAdornment position="end">
                                <Email/>
                            </InputAdornment>
                            )
                        }}
                        />
                        <CustomInput onChange={this.handlePasswordChange}
                        labelText="Password"
                        id="pass"
                        formControlProps={{
                            fullWidth: true
                        }}
                        inputProps={{
                            type: "password",
                            endAdornment: (
                            <InputAdornment position="end">
                                <Icon>
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
                <Link to="/">
                <Button style={{minWidth: "70%"}} onClick={this.handleSubmit} color="info">
                    Sign up
                </Button>
                </Link>
            </GridContainer>
        </div>
    )
  }
}