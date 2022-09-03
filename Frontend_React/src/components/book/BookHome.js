import React, { useEffect, useReducer } from 'react';
// material
import { Container, Typography, Stack, Snackbar, Alert } from '@mui/material';

import BookList from './BookList';
import BookSearch from './BookSearch';
import Loading from './../shared/Loading';
// mock
//import BOOKS from '../../_mock/books';

// ----------------------------------------------------------------------
import BookDataService from '../../api/book-actions';
import { BookProvider } from '../../providers/bookProvider'
import { bookReducer, initialState } from '../../reducers/bookReducer'

const bookUrl = "https://java.team1ad.site/images/textbooks"

export default function BookHome() {
  const [booksContext, dispatch] = useReducer(bookReducer, initialState);

  useEffect(() => {
    const initBooks = async () => {
      dispatch({ type: 'SET_LOADING', loading: true });
      BookDataService.getUserBooks()
        .then((response) => {
          const items = response.data;
          const bookList = (items).map((item) => {
            item.photo = `${bookUrl}/${item.photo}`;
            return item;
          });
          dispatch({
            type: 'SET_BOOK_LIST',
            bookList
          });
          dispatch({
            type: 'SET_FILTER_BOOK',
            filterBookList: bookList
          });
          dispatch({ type: 'SET_LOADING', loading: false });
        })
        .catch((e) => {
          console.log(e.response);
          inputSnackBarDetails("Something went wrong!","error");
        });

    };
    initBooks();
  }, []);

  const handleSnackBarClose = () => {
    booksContext.snackBarDetails.open = false;
    dispatch({ type: 'SET_SNACKBARDETAILS', snackBarDetails: booksContext.snackBarDetails });
  };

  const inputSnackBarDetails = (message, type) => {
    booksContext.snackBarDetails = {
      open: true,
      message,
      type
    };
    dispatch({ type: 'SET_SNACKBARDETAILS', snackBarDetails: booksContext.snackBarDetails });
  }

  if (booksContext.loading) return <div style={{ marginBottom: '20px' }}><Container> <Loading /></Container></div>;

  return (
    <div style={{ marginBottom: '20px' }}>
      <BookProvider value={{ booksContext, dispatch }}>
        <Container>
          <Stack direction="row" alignItems="center" justifyContent="space-between" mb={2} mt={2}>
            <Typography variant="h4" gutterBottom>
              Books
            </Typography>
          </Stack>
          <Stack mb={5} direction="row" alignItems="center" justifyContent="space-between">
            <BookSearch inputSnackBarDetails={inputSnackBarDetails}/>
          </Stack>
          <BookList books={booksContext.filterBookList} />
        </Container>
        <Snackbar
          anchorOrigin={{ horizontal: 'center', vertical: 'bottom' }}
          key={`bottom, center`}
          open={booksContext.snackBarDetails.open}
          autoHideDuration={6000}
          onClose={handleSnackBarClose}>
          <Alert onClose={handleSnackBarClose} severity={booksContext.snackBarDetails.type}>
            {booksContext.snackBarDetails.message}
          </Alert>
        </Snackbar>
      </BookProvider>
    </div>
  );
}