import React, { useContext } from 'react'
import {
  Box,
  Button,
  Card,
  CardContent,
  CardHeader,
  Divider,
  Grid,
  TextField,
  Autocomplete,
  Chip
} from '@mui/material';

import UpdatePasswordDialog from "./UpdatePasswordDialog";

import AccountDataService from '../../api/account-actions';

import { AccountContext } from '../../providers/accountProvider'
import { Claims } from '../../utils/ClientCache';

export const AccountProfileDetails = ({ inputSnackBarDetails }) => {
  const { accountContext, dispatch } = useContext(AccountContext);
  const [interest, setInterest] = React.useState(accountContext.account?.interest);
  const [open, setOpen] = React.useState(false);

  const handleClose = () => {
    setOpen(false);
  };

  const handleSubmit = (event) => {
    event.preventDefault();
    let user = {
      id: accountContext.account.id,
      username: accountContext.account.username,
      password: "",
      name: accountContext.account.name,
      email: accountContext.account.email,
      photo: accountContext.account.photo,
      interest
    }
    AccountDataService.updateUser(user)
      .then((_response) => {
        updateInterest(user.interest);
        //inputSnackBarDetails("Your record is succesfully updated!", 'success');
      })
      .catch((e) => {
        console.log(e.response);
        inputSnackBarDetails("Something went wrong!", 'error');
      });
  };

  const updateInterest = (userInterestList) => {
    let interestDTO = {
      userId: Claims.getUserId(),
      interests: []
    }
    userInterestList.forEach(myFilter);
    function myFilter(item, _index) {
      let id = accountContext.interests.filter(x => x.name == item)[0].id;
      interestDTO.interests.push(id);
    }
    AccountDataService.updateInterest(interestDTO)
      .then((_response) => {
        inputSnackBarDetails("Your record is succesfully updated!", 'success');
      })
      .catch((e) => {
        console.log(e.response);
        inputSnackBarDetails("Something went wrong!", 'error');
      });
  }

  const handleChange = (event, label) => {
    switch (label) {
      case "name":
        accountContext.account.name = event.target.value;
        dispatch({ type: 'SET_ACCOUNT', account: accountContext.account });
        break;
      case "email":
        accountContext.account.email = event.target.value;
        dispatch({ type: 'SET_ACCOUNT', account: accountContext.account });
        break;
      case "interest":
        console.log(event)
        break;
    }
  }

  return (
    <form
      autoComplete="off"
      onSubmit={handleSubmit}
    >
      {
        accountContext.account &&
        <Card>
          <CardHeader
            subheader="The information can be edited"
            title="Profile"
          />
          <Divider />
          <CardContent>
            <Grid
              container
              spacing={3}
            >
              <Grid
                item
                md={6}
                xs={12}
              >
                <TextField
                  fullWidth
                  disabled
                  label="User name"
                  value={accountContext.account.username}
                  variant="outlined"
                />
              </Grid>
              <Grid
                item
                md={6}
                xs={12}
              >
                <TextField
                  fullWidth
                  label="Display name"
                  name="name"
                  onChange={event => handleChange(event, 'name')}
                  required
                  value={accountContext.account.name}
                  variant="outlined"
                />
              </Grid>
              <Grid
                item
                md={6}
                xs={12}
              >
                <TextField
                  fullWidth
                  label="Email Address"
                  name="email"
                  type="email"
                  onChange={event => handleChange(event, 'email')}
                  required
                  value={accountContext.account.email}
                  variant="outlined"
                />
              </Grid>
              <Grid
                item
                md={6}
                xs={12}
              >
                <Autocomplete
                  value={interest}
                  onChange={(_event, newValue) => {
                    setInterest(newValue);
                  }}
                  multiple
                  options={accountContext.interests.map((option) => option.name)}
                  renderTags={(value, getTagProps) =>
                    value.map((option, index) => (
                      <Chip variant="outlined" label={option} {...getTagProps({ index })} />
                    ))
                  }
                  renderInput={(params) => (
                    <TextField
                      {...params}
                      variant="outlined"
                      label="Interest"
                      placeholder="Favorites"
                    />
                  )}
                />
              </Grid>
            </Grid>
          </CardContent>
          <Divider />
          <Box
            sx={{
              display: 'flex',
              justifyContent: 'flex-end',
              p: 2,
            }}
          >
            <Button sx={{ mr: 1 }}
              onClick={() => setOpen(true)}
              color="warning"
              variant="contained"
            >
              Change Password
            </Button>
            <Button
              type="submit"
              color="primary"
              variant="contained"
            >
              Save details
            </Button>
          </Box>
        </Card>
      }
      <UpdatePasswordDialog open={open} handleClose={handleClose}
        accountContext={accountContext} inputSnackBarDetails={inputSnackBarDetails} />
    </form>
  );
};

// Top 100 films as rated by IMDb users. http://www.imdb.com/chart/top
const top100Films = [
  { title: 'Machine Learning' },
  { title: 'Android' },
  { title: 'Computer Sciences' },
];