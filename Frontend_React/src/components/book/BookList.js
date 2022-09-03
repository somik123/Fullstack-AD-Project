import * as React from 'react';
// material
import { Grid } from '@mui/material';
import BookCard from './BookCard';
import Pagination from '@mui/material/Pagination';
import Stack from '@mui/material/Stack';

export default function BookList({ books, ...other }) {

  const [page, setPage] = React.useState(1);
  const handleChange = (_event, value) => {
    setPage(value);
  };
  let count = Math.ceil(books.length / 6);

  return (
    <Stack spacing={2}>
      <Grid container spacing={3} {...other}>
        {books.slice((page - 1) * 6, page * 6).map((book, index) => (
          <Grid key={index} item xs={12} sm={12} md={12}>
            <BookCard book={book} />
          </Grid>
        ))}
      </Grid>
      <div style={{ display: 'flex', justifyContent: 'center', marginTop: 15 }}>
        <Pagination count={count} page={page} onChange={handleChange} />
      </div>
    </Stack>
  );
}