import { Link as RouterLink } from 'react-router-dom';
// @mui
import { styled } from '@mui/material/styles';
import { Button, Typography, Container, Box } from '@mui/material';

const ContentStyle = styled('div')(({ theme }) => ({
    maxWidth: 480,
    margin: 'auto',
    minHeight: '100vh',
    display: 'flex',
    justifyContent: 'center',
    flexDirection: 'column',
    padding: theme.spacing(12, 0)
  }));

  export default function SignupSuccess() {
    return (
        <Container>
          <ContentStyle sx={{ textAlign: 'center', alignItems: 'center' }}>
            <Typography variant="h3" paragraph>
              Congratulation!
            </Typography>
  
            <Typography sx={{ color: 'text.secondary' }}>
            You have signed up successfully!
            </Typography>
  
            <Box
              component="img"
              src="/assets/check.png"
              sx={{ height: 260, mx: 'auto', my: { xs: 5, sm: 10 } }}
            />
  
            <Button to="/login" size="large" variant="outlined" color="success" component={RouterLink}>
                Click here to Login
            </Button>
          </ContentStyle>
        </Container>
    );
  }