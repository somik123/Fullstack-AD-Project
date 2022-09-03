import React from 'react';
import BookHome from './book/BookHome';
import Grid from '@mui/material/Grid';

const Book = () => {
    document.body.style.overflowY = 'scroll';
    return (
        <div style={{ borderTop: '0.5px solid #1976d2', marginBottom: '20px' }} >
        <Grid container spacing={2}>
            <Grid item xs={12} sm={12} md={12}>
                <BookHome/>
            </Grid>
        </Grid>
    </div>
      );
}

export default Book;