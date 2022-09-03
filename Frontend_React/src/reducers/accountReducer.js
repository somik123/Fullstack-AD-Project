export const initialState = {
    loading: false,
    snackBarDetails: {
        open: false,
        message: '',
        type: 'error',
    },
    account: null,
    interests: []
};

export const accountReducer = (state = initialState, action) => {
    switch (action.type) {
        case 'SET_LOADING':
            return { ...state, loading: action.loading };
        case 'SET_SNACKBARDETAILS':
            return { ...state, snackBarDetails: action.snackBarDetails };
        case 'SET_ACCOUNT':
            return { ...state, account: action.account };
        case 'SET_INTERESTS':
            return { ...state, interests: action.interests };
        default:
            return state;
    }
}