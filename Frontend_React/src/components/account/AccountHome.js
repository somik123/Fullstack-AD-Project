import React, { useReducer, useEffect } from 'react'
import {
    Box, Container, Grid, Typography, Snackbar,
    Alert
} from '@mui/material';

import { AccountProfile } from './AccountProfile';
import { AccountProfileDetails } from './AccountProfileDetail';
import Loading from './../shared/Loading';

import AccountDataService from '../../api/account-actions';
import { accountReducer, initialState } from '../../reducers/accountReducer'
import { AccountProvider } from '../../providers/accountProvider'
import { Claims } from '../../utils/ClientCache'

const AccountHome = () => {

    const [accountContext, dispatch] = useReducer(accountReducer, initialState);

    useEffect(() => {
        const initAccount = () => {
            dispatch({ type: 'SET_LOADING', loading: true });
            AccountDataService.getUserByuserName(Claims.getUserName())
                .then((response) => {
                    const item = response.data;
                    dispatch({
                        type: 'SET_ACCOUNT',
                        account: item
                    });
                    getAllInterests();
                })
                .catch((e) => {
                    console.log(e.response);
                    inputSnackBarDetails("Something went wrong!","error");
                });

        };
        initAccount();
    }, []);

    const getAllInterests = () => {
        AccountDataService.getAllInterest()
            .then((response) => {
                const item = response.data;
                dispatch({
                    type: 'SET_INTERESTS',
                    interests: item
                });

                dispatch({ type: 'SET_LOADING', loading: false });
            })
            .catch((e) => {
                console.log(e.response);
                inputSnackBarDetails("Something went wrong!","error");
            });
    }

    const handleSnackBarClose = () => {
        accountContext.snackBarDetails.open = false;
        dispatch({ type: 'SET_SNACKBARDETAILS', snackBarDetails: accountContext.snackBarDetails });
    };

    const inputSnackBarDetails = (message, type) => {
        accountContext.snackBarDetails = {
            open: true,
            message,
            type
        };
        dispatch({ type: 'SET_SNACKBARDETAILS', snackBarDetails: accountContext.snackBarDetails });
    }

    if (accountContext.loading) return <Container><Loading /></Container>;

    return (
        <div style={{ borderTop: '0.5px solid #1976d2' }}>
            <AccountProvider value={{ accountContext, dispatch }}>
                <Box
                    component="main"
                    sx={{
                        flexGrow: 1,
                        py: 8
                    }}
                >
                    <Container maxWidth="lg">
                        <Typography
                            sx={{ mb: 3 }}
                            variant="h4">
                            Account
                        </Typography>
                        <Grid
                            container
                            spacing={3}
                        >
                            <Grid
                                item
                                lg={4}
                                md={6}
                                xs={12}
                            >
                                <AccountProfile inputSnackBarDetails={inputSnackBarDetails}/>
                            </Grid>
                            <Grid
                                item
                                lg={8}
                                md={6}
                                xs={12}
                            >
                                <AccountProfileDetails inputSnackBarDetails={inputSnackBarDetails} />
                            </Grid>
                        </Grid>
                    </Container>
                </Box>
                <Snackbar
                    anchorOrigin={{ horizontal: 'center', vertical: 'bottom' }}
                    key={`bottom, center`}
                    open={accountContext.snackBarDetails.open}
                    autoHideDuration={6000}
                    onClose={handleSnackBarClose}>
                    <Alert onClose={handleSnackBarClose} severity={accountContext.snackBarDetails.type}>
                        {accountContext.snackBarDetails.message}
                    </Alert>
                </Snackbar>
            </AccountProvider>
        </div>
    )
}

export default AccountHome;