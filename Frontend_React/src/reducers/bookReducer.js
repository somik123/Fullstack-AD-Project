export const initialState = {
    loading: false,
    snackBarDetails: {
        open: false,
        message: '',
        type: 'error',
    },
    bookList: [],
    filterBookList: [],
    book: null
};

export const bookReducer = (state = initialState, action) => {
    switch (action.type) {
        case 'SET_LOADING':
            return { ...state, loading: action.loading };
        case 'SET_SNACKBARDETAILS':
            return { ...state, snackBarDetails: action.snackBarDetails };
        case 'SET_BOOK_LIST':
            return { ...state, bookList: action.bookList };
        case 'SET_FILTER_BOOK':
            return { ...state, filterBookList: action.filterBookList };
        case 'SET_BOOK':
            return { ...state, book: action.book };
        default:
            return state;
    }
}