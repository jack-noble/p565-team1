import React from "react";

import API from "../../utils/API";
import User from "./User";

class RenderUser extends React.Component {
  constructor(props) {
    super(props);

    this.state = {
      isLoading: true,
      name: null,
      email: null
    };
  }

  render() {
    const { isLoading, name,  email } = this.state;

    return (
      <User isLoading={isLoading} name={name} email={email} />
    );
  }

  async componentDidMount() {
    // Load async data.
    let userData = await API.get('/', {
      params: {
        results: 1,
        inc: 'name,email,picture'
      }
    });

        // Parse the results for ease of use.
    userData = userData.data.results[0];

    // Update state with new data and re-render our component.
    const name = `${userData.name.first} ${userData.name.last}`;
    const email = userData.email;

    this.setState({
      ...this.state, ...{
        isLoading: false,
        name,
        email
      }
    });
  }
}

export default RenderUser;