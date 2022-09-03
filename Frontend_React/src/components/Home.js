import React from 'react';
import GroupHome from './group/GroupHome';
import Grid from '@mui/material/Grid';

const Home = () => {
    document.body.style.overflowY = 'scroll';
    return (
        <div style={{ borderTop: '0.5px solid #1976d2', marginBottom: '20px' }} >
            <Grid container spacing={2}>
                <Grid item xs={12} sm={12} md={12}>
                    <GroupHome />
                </Grid>
            </Grid>
        </div>
    )
}

export default Home;