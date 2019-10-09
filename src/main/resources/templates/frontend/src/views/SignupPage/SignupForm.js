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

export default class SignupButton extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      username: "",
      email: "",
      password: "",
      userType: "insurance",
      cardAnimaton: "cardHidden"
    };
    this.handleUsernameChange = this.handleUsernameChange.bind(this);
    this.handleEmailChange = this.handleEmailChange.bind(this);
    this.handlePasswordChange = this.handlePasswordChange.bind(this);
    this.handleSubmit = this.handleSubmit.bind(this);
  }

  handleUsernameChange = event => {
    this.setState({ username: event.target.value });
  };

  handleEmailChange = event => {
    this.setState({ email: event.target.value });
  };

  handlePasswordChange = event => {
    this.setState({ password: event.target.value });
  };

  handleSubmit = event => {
    event.preventDefault();

    const user = {
      username: this.state.username,
      email: this.state.email,
      password: this.state.password,
      userType: this.state.userType
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

  render() {
    return (
      <form>
        <CardHeader color="primary">
          <h4>Sign up with</h4>
          <div>
            <Button
              justIcon
              href="#pablo"
              target="_blank"
              color="transparent"
              onClick={e => e.preventDefault()}
            >
              <i className={"fab fa-google"} />
            </Button>
              <Button
                justIcon
                href="#pablo"
                target="_blank"
                color="transparent"
                onClick={e => e.preventDefault()}
              >
                <i className={"fab fa-facebook"} />
              </Button>
          </div>
        </CardHeader>
        <CardBody>
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
            labelText="Email..."
            id="email"
            formControlProps={{
              fullWidth: true
            }}
            inputProps={{
              type: "email",
              onChange: this.handleEmailChange,
              endAdornment: (
                <InputAdornment position="end">
                  <Email />
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
              onChange: this.handlePasswordChange,
              endAdornment: (
                <InputAdornment position="end">
                  <Icon>lock_outline</Icon>
                </InputAdornment>
              ),
              autoComplete: "off"
            }}
          />
        </CardBody>
        <Button
          onClick={this.handleSubmit}
          style={{ minWidth: "70%" }}
          color="info"
        >
          Sign up
        </Button>
      </form>
    );
  }
}
