import React from "react";
import PropTypes from "prop-types";

class User extends React.Component {
  render() {
    const { name, email, isLoading } = this.props;

    const userDetails = (
      <div>
        <h4 className="mb-0">{name}</h4>
        <span className="text-muted">{email}</span>
      </div>
    );

    const loadingMessage = <span className="d-flex m-auto">Loading...</span>;

    return (
      <p>
        {isLoading ? loadingMessage : userDetails}
      </p>
    );
  }
}

User.propTypes = {
  name: PropTypes.string,
  email: PropTypes.string,
  isLoading: PropTypes.bool
};

export default User;