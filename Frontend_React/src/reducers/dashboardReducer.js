export const initialState = {
    loading: false,
    snackBarDetails: {
        open: false,
        message: '',
        type: 'error',
    },
    activeGroupList: [],
    groupList: [],
    totalMessageSend: 0,
    totalPublicGroup: 0,
    totalEvents: 0,
    totalBookGenre: 0,
    participantRate: []
};

export const dashboardReducer = (state = initialState, action) => {
    switch (action.type) {
        case 'SET_LOADING':
            return { ...state, loading: action.loading };
        case 'SET_SNACKBARDETAILS':
            return { ...state, snackBarDetails: action.snackBarDetails };
        case 'SET_ACTIVE_GROUP_LIST':
            return { ...state, activeGroupList: action.activeGroupList };
        case 'SET_TOTAL_MESSAGE_SEND':
            return { ...state, totalMessageSend: action.totalMessageSend };
        case 'SET_GROUP_LIST':
            return { ...state, groupList: action.groupList };
        case 'SET_TOTAL_PUBLIC_GROUP':
            return { ...state, totalPublicGroup: action.totalPublicGroup };
        case 'SET_TOTAL_EVENTS':
            return { ...state, totalEvents: action.totalEvents };
        case 'SET_TOTAL_BOOK_GENRE':
            return { ...state, totalBookGenre: action.totalBookGenre };
        case 'SET_PARTICIPANT_RATE':
            return { ...state, participantRate: action.participantRate };
        default:
            return state;
    }
}