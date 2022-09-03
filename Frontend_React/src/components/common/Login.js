import React, { useState } from 'react'
import LoadingButton from '@mui/lab/LoadingButton';
import CssBaseline from '@mui/material/CssBaseline';
import TextField from '@mui/material/TextField';
import Link from '@mui/material/Link';
import Paper from '@mui/material/Paper';
import Box from '@mui/material/Box';
import Grid from '@mui/material/Grid';
import Typography from '@mui/material/Typography';
import { createTheme, ThemeProvider } from '@mui/material/styles';
import Alert from '@mui/material/Alert';
import {
  useLocation
} from "react-router-dom";
import CommonDataService from './../../api/common-actions'
import { Claims } from '../../utils/ClientCache'

const theme = createTheme();

const Login = () => {
  let location = useLocation();
  const [alertValue, setAlert] = useState({ message: '', type: 'error', open: false })
  const [loading, setLoading] = React.useState(false);

  let from = location.state?.from?.pathname || "/";

  const handleSubmit = (event) => {
    event.preventDefault();
    const data = new FormData(event.currentTarget);

    let authRequest = {
      username: data.get('username'),
      password: data.get('password'),
    }
    setLoading(true);
    CommonDataService.authenticate(authRequest)
      .then((response) => {
        Claims.setToken(response.data.token);
        Claims.setUserName(response.data.userName);
        Claims.setUserId(response.data.userId);
        Claims.setDisplayName(response.data.displayName);
        Claims.setProfileImage(response.data.photo);
        //navigate(from, { replace: true });
        window.location.href = `${from}`
      })
      .catch((e) => {
        setLoading(false);
        console.log(e.response);
        if (e.response.status == 401) setAlert({ message: 'Invalid login attempt!', type: 'error', open: true });
      });
  };

  return (
    <ThemeProvider theme={theme}>
      <Grid container component="main" sx={{ height: '100vh' }}>
        <CssBaseline />
        <Grid
          item
          xs={false}
          sm={4}
          md={7}
          sx={{
            backgroundImage: 'url(assets/covers/login_cover.jpg)',
            backgroundRepeat: 'no-repeat',
            backgroundColor: (t) =>
              t.palette.mode === 'light' ? t.palette.grey[50] : t.palette.grey[900],
            backgroundSize: 'cover',
            backgroundPosition: 'center',
          }}
        />
        <Grid item xs={12} sm={8} md={5} component={Paper} elevation={6} square>
          <Box
            sx={{
              my: 8,
              mx: 4,
              display: 'flex',
              flexDirection: 'column',
              alignItems: 'center',
            }}
          >
            <img src="/logo.png" style={{ maxWidth: "150px", maxHeight: "170px", marginBottom: '30px' }} />
            <Typography component="h1" variant="h5">
              Sign In
            </Typography>
            <Box component="form" noValidate onSubmit={handleSubmit} sx={{ mt: 1 }}>
              {
                alertValue.open ?
                  <Alert sx={{ mt: 2, mb: 2 }}
                    severity={alertValue.type}
                    onClose={() => setAlert({ open: false })}>{alertValue.message}</Alert>
                  : <></>
              }
              <TextField
                margin="normal"
                required
                fullWidth
                id="username"
                label="User Name"
                name="username"
                autoFocus
              />
              <TextField
                margin="normal"
                required
                fullWidth
                name="password"
                label="Password"
                type="password"
                id="password"
              />

              <LoadingButton
                type="submit"
                fullWidth
                variant="contained"
                sx={{ mt: 3, mb: 2 }}
                loading={loading}
                loadingPosition="end"
              >
                Sign In
              </LoadingButton>
              <Grid container>
                <Grid item>
                  <Link href="/register" variant="body2">
                    {"Don't have an account? Sign Up"}
                  </Link>
                </Grid>
              </Grid>
            </Box>
          </Box>
        </Grid>
      </Grid>
    </ThemeProvider>
  )
}

export default Login;
