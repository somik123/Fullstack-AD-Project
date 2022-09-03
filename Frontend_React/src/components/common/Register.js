import React, { useState } from 'react'
import LoadingButton from '@mui/lab/LoadingButton';
import CssBaseline from '@mui/material/CssBaseline'
import TextField from '@mui/material/TextField'
import Link from '@mui/material/Link'
import Paper from '@mui/material/Paper'
import Box from '@mui/material/Box'
import Grid from '@mui/material/Grid'
import Typography from '@mui/material/Typography'
import { createTheme, ThemeProvider } from '@mui/material/styles'
import CommonDataService from './../../api/common-actions'
import {
  useNavigate
} from "react-router-dom"
import Alert from '@mui/material/Alert';

const theme = createTheme()

export default function Register() {
  let navigate = useNavigate()
  const [alertValue, setAlert] = useState({ message: '', type: 'error', open: false })
  const [loading, setLoading] = React.useState(false);

  const handleSubmit = (event) => {
    event.preventDefault()
    const data = new FormData(event.currentTarget)
    let user = {
      username: data.get('username'),
      password: data.get('password'),
      confirmPassword: data.get('confirmPassword'),
      name: data.get('name'),
      email: data.get('email')
    }
    if (user.username.length < 1) {
      setAlert({ message: "User name is required!", type: 'error', open: true });
    }
    else if (user.name.length < 1) {
      setAlert({ message: "Full name is required!", type: 'error', open: true });
    }
    else if (user.email.length < 1) {
      setAlert({ message: "Email is required!", type: 'error', open: true });
    }
    else if (user.password == user.confirmPassword) {
      setLoading(true);
      CommonDataService.register(user)
        .then((_response) => {
          //console.log(response.data);
          navigate('/signup_success')
        })
        .catch((e) => {
          console.log(e.response)
          setLoading(false);
          if (e.response.data.email != undefined) {
            setAlert({ message: e.response.data.email, type: 'error', open: true });
          }
          else if (e.response.data.name != undefined) {
            setAlert({ message: e.response.data.name, type: 'error', open: true });
          }
          else if (e.response.data?.length > 0) {
            setAlert({ message: e.response.data, type: 'error', open: true });
          }
          else {
            setAlert({ message: "Something went wrong!", type: 'error', open: true });
          }
        })
    }
    else {
      setAlert({ message: 'Confirm Password does not match!', type: 'error', open: true });
    }

  }

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
            backgroundImage: 'url(assets/covers/register_cover.jpg)',
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
              Sign up
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
                required={true}
                fullWidth
                name="name"
                label="Full Name"
                id="fullName"
              />
              <TextField
                margin="normal"
                required={true}
                fullWidth
                name="email"
                label="Email"
                id="email"
              />
              <TextField
                margin="normal"
                required={true}
                fullWidth
                id="username"
                label="User Name"
                name="username"
                autoFocus
              />
              <TextField
                margin="normal"
                required={true}
                fullWidth
                name="password"
                label="Password"
                type="password"
                id="password"
              />
              <TextField
                margin="normal"
                required={true}
                fullWidth
                name="confirmPassword"
                label="Confirm Password"
                type="password"
                id="confirmPassword"
              />
              <LoadingButton
                type="submit"
                fullWidth
                variant="contained"
                sx={{ mt: 3, mb: 2 }}
                loading={loading}
                loadingPosition="end"
              >
                Sign Up
              </LoadingButton>
              <Grid container>
                <Grid item>
                  <Link href="/login" variant="body2">
                    {"Already have an account? Sign In"}
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
