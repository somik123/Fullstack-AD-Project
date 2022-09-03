export const initialState = {
    loading: false,
    snackBarDetails: {
        open: false,
        message: '',
        type: 'error',
    },
    eventList: [],
    event: null,
};

export const eventReducer = (state = initialState, action) => {
    switch (action.type) {
        case 'SET_LOADING':
            return { ...state, loading: action.loading };
        case 'SET_SNACKBARDETAILS':
            return { ...state, snackBarDetails: action.snackBarDetails };
        case 'SET_EVENT_LIST':
            return { ...state, eventList: action.eventList };
        case 'SET_EVENT':
            return { ...state, event: action.event };
        default:
            return state;
    }
}