import React, { useContext } from 'react';
// @mui
import { styled } from '@mui/material/styles';
import { Autocomplete, InputAdornment, TextField, CircularProgress } from '@mui/material';
import SearchIcon from '@mui/icons-material/Search';
// ----------------------------------------------------------------------
import { BookContext } from '../../providers/bookProvider'
import BookDataService from '../../api/book-actions';

const bookUrl = "https://java.team1ad.site/images/textbooks"

export default function BookSearch({ inputSnackBarDetails }) {

  const { booksContext, dispatch } = useContext(BookContext);
  const [options, setOptions] = React.useState([]);
  const [loading, setLoading] = React.useState(false);
  const [searchText, setSearchText] = React.useState('');
  const [value, setValue] = React.useState(null);
  const [inputValue, setInputValue] = React.useState('');
  const [searchResult, setSearchResult] = React.useState([]);

  const getBook = (book) => {
    if (book != null) {
      let filterbooks = searchResult.filter(t => t.title == book.title);
      console.log(filterbooks)
      dispatch({
        type: 'SET_FILTER_BOOK',
        filterBookList: filterbooks
      });
    }
    else {
      dispatch({
        type: 'SET_FILTER_BOOK',
        filterBookList: booksContext.bookList
      });
    }
  };

  const handleChange = (event) => {
    setSearchText(event.target.value)
  }

  const onKeyUp = (e) => {
    console.log(e.keyCode);
    if (e.keyCode == 13) {
      handleSearch();
    }
  }

  const handleSearch = () => {
    let searchRequest = {
      searchText
    }
    setLoading(true);
    BookDataService.searchBooks(searchRequest)
      .then((response) => {
        const items = response.data;
        if (items.length > 0) {
          const bookList = (items).map((item) => {
            item.photo = `${bookUrl}/${item.photo}`;
            return item;
          });
          setOptions(bookList);
          setSearchResult(bookList);
        }
        else {
          setOptions([]);
          dispatch({
            type: 'SET_FILTER_BOOK',
            filterBookList: booksContext.bookList
          });
        }

        setInputValue('');
        setSearchText('');
        setLoading(false);
      })
      .catch((e) => {
        console.log(e);
        setLoading(false);
        inputSnackBarDetails("Something went wrong!","error");
      });
  }

  return (
    <Autocomplete
      value={value}
      onChange={(_event, newValue) => {
        setValue(newValue);
        getBook(newValue);
      }}
      inputValue={inputValue}
      onInputChange={(_event, newInputValue) => {
        setInputValue(newInputValue);
      }}
      id="asynchronous-demo"
      sx={{ width: 420 }}
      options={options}
      getOptionLabel={(option) => option.title}
      loading={loading}
      renderInput={(params) => (
        <TextField
          {...params}
          value={searchText}
          onChange={handleChange}
          onKeyUp={onKeyUp}
          placeholder="Search book..."
          InputProps={{
            ...params.InputProps,
            startAdornment: (
              <InputAdornment position="start">
                <SearchIcon sx={{ ml: 1, width: 20, height: 20, color: 'brown' }} onClick={handleSearch} />
              </InputAdornment>
            ),
            endAdornment: (
              <React.Fragment>
                {loading ? <CircularProgress color="inherit" size={20} /> : null}
                {params.InputProps.endAdornment}
              </React.Fragment>
            ),
          }}
        />
      )}
    />
  );
}