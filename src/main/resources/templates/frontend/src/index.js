import React from "react";
import ReactDOM from "react-dom";
import { createBrowserHistory } from "history";
import { Router, Route, Switch } from "react-router-dom";

import "assets/scss/material-kit-react.scss?v=1.8.0";

// pages for this product
import Components from "views/Components/Components.js";
import LandingPage from "views/LandingPage/LandingPage.js";
import Login from "views/LoginPage/LoginPage.js";
import DoctorSignup from "views/SignupPage/DoctorSignupPage.js";
import InsuranceSignup from "views/SignupPage/InsuranceSignupPage.js";
import PatientSignup from "views/SignupPage/PatientSignupPage.js";
import DoctorDashboard from "views/Dashboards/DoctorDashboard";
import InsuranceDashboard from "views/Dashboards/InsuranceDashboard";
import PatientDashboard from "views/Dashboards/PatientDashboard";

var hist = createBrowserHistory();

ReactDOM.render(
  <Router history={hist}>
    <Switch>
      <Route path="/components" component={Components} />
      <Route path="/doctor/signup" component={DoctorSignup}/>
      <Route path="/insurance/signup" component={InsuranceSignup}/>
      <Route path="/patient/signup" component={PatientSignup}/>
      <Route path="/doctor" component={DoctorDashboard} />
      <Route path="/insurance" component={InsuranceDashboard} />
      <Route path="/patient" component={PatientDashboard} />
      <Route path="/signin" component={Login}/>
      <Route path="/" component={LandingPage} />
    </Switch>
  </Router>,
  document.getElementById("root")
);
