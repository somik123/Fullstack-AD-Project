export const initialState = {
    loading: false,
    index: 0,
    photo: ''
};

export const tabReducer = (state = initialState, action) => {
    switch (action.type) {
        case 'SET_INDEX':
            return { ...state, index: action.index };
        case 'SET_PHOTO':
            return { ...state, photo: action.photo };
        case 'SET_LOADING':
            return { ...state, loading: action.loading };
        default:
            return state;
    }
}