import React from "react";
import ReactDOM from "react-dom";
import { createBrowserHistory } from "history";
import { Router, Route, Switch } from "react-router-dom";

import "assets/scss/material-kit-react.scss?v=1.8.0";

// pages for this product
import Components from "views/Components/Components.js";
import LandingPage from "views/LandingPage/LandingPage.js";
import ProfilePage from "views/ProfilePage/ProfilePage.js";
import Login from "views/LoginPage/LoginPage.js";
import SignupSelect from "views/Modals/SignupSelect.js";
import SignupForm from "views/Modals/SignupForm.js";

var hist = createBrowserHistory();

ReactDOM.render(
  <Router history={hist}>
    <Switch>
      <Route path="/components" component={Components} />
      <Route path="/profile-page" component={ProfilePage} />
      <Route path="/login" component={Login}/>
      <Route path="/signup/" component={SignupForm}/>
      <Route path="/signup" component={SignupSelect}/>
      <Route path="/" component={LandingPage} />
    </Switch>
  </Router>,
  document.getElementById("root")
);
