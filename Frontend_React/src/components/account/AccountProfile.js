import React, { useContext } from 'react'
import {
  Avatar,
  Box,
  Button,
  Card,
  CardActions,
  CardContent,
  Divider,
  Typography
} from '@mui/material';
import {
  useNavigate
} from "react-router-dom";
import UploadImageDialog from "./UploadImageDialog";

import { AccountContext } from '../../providers/accountProvider'
import { Claims } from '../../utils/ClientCache'

const user = {
  avatar: '/assets/face2.jpg',
  city: 'Los Angeles',
  country: 'USA',
  jobTitle: 'Senior Developer',
  name: 'Katarina Smith',
  timezone: 'GTM-7'
};

export const AccountProfile = ({ inputSnackBarDetails }) => {

  const { accountContext, dispatch } = useContext(AccountContext);
  const [open, setOpen] = React.useState(false);
  let navigate = useNavigate();

  const logoutHandle = async () => {
    Claims.clearAll();
    navigate("/");
  }

  const handleImageUpload = () => {
    setOpen(true);
  }

  const handleClose = () => {
    setOpen(false);
  };

  return (
    <>
      {
        accountContext.account &&
        <Card>
          <CardContent>
            <Box
              sx={{
                alignItems: 'center',
                display: 'flex',
                flexDirection: 'column'
              }}
            >
              <Avatar
                src={accountContext.account.photo}
                sx={{
                  height: 100,
                  mb: 2,
                  width: 100,
                }}
              />
              <Typography
                color="textPrimary"
                gutterBottom
                variant="h5"
              >
                {accountContext.account.name}
              </Typography>

              <Button color="error" variant='outlined' onClick={() => { logoutHandle() }}>
                Sign out
              </Button>
            </Box>
          </CardContent>
          <Divider />
          <CardActions>
            <Button
              color="primary"
              fullWidth
              variant="text" onClick={() => handleImageUpload()}
            >
              Upload picture
            </Button>
          </CardActions>
          <UploadImageDialog open={open} handleClose={handleClose} inputSnackBarDetails={inputSnackBarDetails}
            dispatch={dispatch} accountContext={accountContext} />
        </Card>
      }

    </>
  );
}  