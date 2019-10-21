import React from "react";
import Axios from "axios";
// material-ui components
import InputAdornment from "@material-ui/core/InputAdornment";
import Icon from "@material-ui/core/Icon";
// @material-ui/icons
import Email from "@material-ui/icons/Email";
import People from "@material-ui/icons/People";
// core components
import Button from "components/CustomButtons/Button.js";
import Card from "components/Card/Card.js";
import CardBody from "components/Card/CardBody.js";
import CardHeader from "components/Card/CardHeader.js";
import CustomInput from "components/CustomInput/CustomInput.js";
import GridContainer from "components/Grid/GridContainer.js";
import GridItem from "components/Grid/GridItem.js";
import CardFooter from "components/Card/CardFooter";
import TypeSelect from "views/Modals/TypeSelect.js";
import FacebookLogin from 'react-facebook-login/dist/facebook-login-render-props'

export default class LoginForm extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      username: "",
      password: "",
      cardAnimaton: "cardHidden"
    };
    this.handleUsernameChange = this.handleUsernameChange.bind(this);
    this.handlePasswordChange = this.handlePasswordChange.bind(this);
    this.handleSubmit = this.handleSubmit.bind(this);
  }

  handleUsernameChange = event => {
    this.setState({ username: event.target.value });
  };

  handlePasswordChange = event => {
    this.setState({ password: event.target.value });
  };

  handleSubmit = event => {
    event.preventDefault();

    const user = {
      username: this.state.username,
      password: this.state.password,
    };
    console.log(user);

    try {
      const response = Axios.post(
        "https://infinity-care.herokuapp.com/signup/insurance",
        { user }
      );
      console.log("👉 Returned data:", response);
      console.log("👉 You tried to log:", user);
    } catch (e) {
      console.log(`😱 Axios request failed: ${e}`);
    }
  };

  responseFacebook(response) {
    console.log(response);
  }

  render() {
    return (
        <form>
        <CardHeader color="primary">
          <h4>Log in with</h4>
          <div>
            <FacebookLogin
              appId="523513645103749"
              autoLoad={false}
              fields="name,email,picture"
              callback={this.responseFacebook}
              render={renderProps => ( 
                <Button
                  justIcon
                  target="_blank"
                  color="transparent"
                  onClick={renderProps.onClick}
                >
                  <i className={"fab fa-facebook"} />
                </Button>
              )}
            />
          </div>
        </CardHeader>
        <CardBody>
          <div style={{display: 'flex', justifyContent: 'center', alignItems: 'center', marginBottom: 10}}>
            <p style={{display: 'flex', justifyContent: 'center', margin: 0}}>Don't have an account?</p>
            <TypeSelect />
          </div>
          <CustomInput
            labelText="Username..."
            id="first"
            formControlProps={{
              fullWidth: true
            }}
            inputProps={{
              type: "text",
              onChange: this.handleUsernameChange,
              endAdornment: (
                <InputAdornment position="end">
                  <People />
                </InputAdornment>
              )
            }}
          />
          <CustomInput
            labelText="Password"
            id="password"
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
          <div style={{display: 'flex', justifyContent: 'right', alignItems: 'center', marginBottom: 10}}>
            <Button color="primary" simple>
              Forgot password?
            </Button>
          </div>
          <small style={{display: 'flex', justifyContent: 'center'}}>I agree to the Terms and Conditions &amp; Privacy Policy</small>
        </CardBody>
        <CardFooter style={{display: 'flex', justifyContent: 'center', margin: 0}}>
          <Button color="primary" size="lg">
            Sign In
          </Button>
        </CardFooter>
      </form>
    );
  }
}
