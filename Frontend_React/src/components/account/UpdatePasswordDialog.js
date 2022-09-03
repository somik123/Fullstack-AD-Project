import React from 'react';
import Dialog from '@mui/material/Dialog';
import DialogActions from '@mui/material/DialogActions';
import DialogContent from '@mui/material/DialogContent';
import DialogTitle from '@mui/material/DialogTitle';
import FormControl from '@mui/material/FormControl';
import IconButton from '@mui/material/IconButton';
import OutlinedInput from '@mui/material/OutlinedInput';
import InputLabel from '@mui/material/InputLabel';
import InputAdornment from '@mui/material/InputAdornment';
import Visibility from '@mui/icons-material/Visibility';
import VisibilityOff from '@mui/icons-material/VisibilityOff';
import useMediaQuery from '@mui/material/useMediaQuery';
import { useTheme } from '@mui/material/styles';
import LoadingButton from '@mui/lab/LoadingButton';
import SaveIcon from '@mui/icons-material/Save';

import AccountDataService from '../../api/account-actions';
import { Stack } from '@mui/system';

export default function UpdatePasswordDialog({ open, handleClose, accountContext, inputSnackBarDetails }) {
    const theme = useTheme();
    const fullScreen = useMediaQuery(theme.breakpoints.down('md'));
    const [loading, setLoading] = React.useState(false);
    const [values, setValues] = React.useState({
        password: '',
        updatePassword: '',
        confirmPassword: '',
        showPassword: false,
    });

    const handleClickShowPassword = () => {
        setValues({
            ...values,
            showPassword: !values.showPassword,
        });
    };

    const handleMouseDownPassword = (event) => {
        event.preventDefault();
    };

    const handelCancel = () => {
        setValues({
            password: '',
            updatePassword: '',
            confirmPassword: '',
            showPassword: false,
        });
        handleClose();
    }

    const handleChange = (prop) => (event) => {
        setValues({ ...values, [prop]: event.target.value });
    };

    const updatePassword = () => {
        let passwordDTO = {
            userId: accountContext.account.id,
            currentPassword: values.password,
            updatePassword: values.updatePassword
        }
        if (values.confirmPassword == passwordDTO.updatePassword) {
            setLoading(true);
            AccountDataService.updatePassword(passwordDTO)
                .then((_response) => {
                    setLoading(false);
                    inputSnackBarDetails("Your password is succesfully updated!", 'success');
                    handelCancel();
                })
                .catch((e) => {
                    //console.log(e.response);
                    setLoading(false);
                    inputSnackBarDetails("something went wrong!", 'error');
                });
        }
        else {
            inputSnackBarDetails("Confirm password does not match!", 'error');
        }
    }

    return (
        <div>
            <Dialog
                fullScreen={fullScreen}
                open={open}
                aria-labelledby="responsive-dialog-title"
            >
                <DialogTitle id="responsive-dialog-title">
                    {"Update Password?"}
                </DialogTitle>
                <DialogContent>
                    <Stack>
                        <FormControl sx={{ m: 1 }} variant="outlined">
                            <InputLabel htmlFor="password">Current Password</InputLabel>
                            <OutlinedInput
                                id="password"
                                type={values.showPassword ? 'text' : 'password'}
                                value={values.password}
                                onChange={handleChange('password')}
                                endAdornment={
                                    <InputAdornment position="end">
                                        <IconButton
                                            aria-label="toggle password visibility"
                                            onClick={handleClickShowPassword}
                                            onMouseDown={handleMouseDownPassword}
                                            edge="end"
                                        >
                                            {values.showPassword ? <VisibilityOff /> : <Visibility />}
                                        </IconButton>
                                    </InputAdornment>
                                }
                                label="Current Password"
                            />
                        </FormControl>
                        <FormControl sx={{ m: 1 }} variant="outlined">
                            <InputLabel htmlFor="updatePassword">New Password</InputLabel>
                            <OutlinedInput
                                id="updatePassword"
                                type={values.showPassword ? 'text' : 'password'}
                                value={values.updatePassword}
                                onChange={handleChange('updatePassword')}
                                endAdornment={
                                    <InputAdornment position="end">
                                        <IconButton
                                            aria-label="toggle password visibility"
                                            onClick={handleClickShowPassword}
                                            onMouseDown={handleMouseDownPassword}
                                            edge="end"
                                        >
                                            {values.showPassword ? <VisibilityOff /> : <Visibility />}
                                        </IconButton>
                                    </InputAdornment>
                                }
                                label="New Password"
                            />
                        </FormControl>
                        <FormControl sx={{ m: 1 }} variant="outlined">
                            <InputLabel htmlFor="confirmPassword">Confirm Password</InputLabel>
                            <OutlinedInput
                                id="confirmPassword"
                                type={values.showPassword ? 'text' : 'password'}
                                value={values.confirmPassword}
                                onChange={handleChange('confirmPassword')}
                                endAdornment={
                                    <InputAdornment position="end">
                                        <IconButton
                                            aria-label="toggle password visibility"
                                            onClick={handleClickShowPassword}
                                            onMouseDown={handleMouseDownPassword}
                                            edge="end"
                                        >
                                            {values.showPassword ? <VisibilityOff /> : <Visibility />}
                                        </IconButton>
                                    </InputAdornment>
                                }
                                label="Confirm Password"
                            />
                        </FormControl>
                    </Stack>
                </DialogContent>
                <DialogActions>
                    <LoadingButton loading={loading} variant="contained" onClick={handelCancel}>
                        Cancel
                    </LoadingButton>
                    <LoadingButton color="success" variant="contained" endIcon={<SaveIcon />}
                        onClick={updatePassword} loading={loading} loadingPosition="end">
                        Save
                    </LoadingButton>
                </DialogActions>
            </Dialog>
        </div>
    );
}
